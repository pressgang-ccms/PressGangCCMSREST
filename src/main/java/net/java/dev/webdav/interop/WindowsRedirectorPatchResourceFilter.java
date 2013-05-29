/*
 * Copyright 2008, 2009 Daniel MANZKE
 * Copyright 2011 Markus KARG
 *
 * This file is part of webdav-interop.
 *
 * webdav-interop is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * webdav-interop is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with webdav-interop.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.java.dev.webdav.interop;

import static javax.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static net.java.dev.webdav.interop.HttpMethod.LOCK;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * This Servlet-Filter should several problems, which were found when working with Windows-Clients and
 * a WebDAV-compliant Server.
 * Microsoft needs at least a DAV-enabled response, when trying a request to the Root of your Server (http://example.com/).
 * For a successful PROPFIND request, the returned XML has to use XML-Namespaces. Due the fact that JAX-B 
 * doesn't require Namespaces, we use XSLT to transform it to a "correct" XML.
 * If you don't want to use the filter for that you can add an empty class to your JAXB-Context. This will force
 * JAXB to create namespaces. 
 * 
 * @author Daniel MANZKE (daniel.manzke@googlemail.com)
 * @author Markus KARG (mkarg@java.net)
 */
public class WindowsRedirectorPatchResourceFilter implements Filter {
	public static final String MS_AUTHOR_VIA = "MS-Author-Via";
	public static final String DAV = "DAV";
	public static final String ROOT_RESOURCE = "/";
	private static final Logger logger = Logger.getLogger(WindowsRedirectorPatchResourceFilter.class.getName());
	private ServletContext context;
	private String webdavClass;
	private Set<String> userAgents;
	private int responseWrapperBufferSize;
	
	@Override
	public void destroy() {
		this.templates = null;
	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		/*
		 * This filter is only able to handle HTTP, so we bypass anything else.
		 */
		if (!(servletRequest instanceof HttpServletRequest)
				|| !(servletResponse instanceof HttpServletResponse)) {
			chain.doFilter(servletRequest, servletResponse);
			return;
		}

		logger.finer("doFilter(..) - called");
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		final String agent = request.getHeader("user-agent");
		logger.fine("doFilter(..) - user-agent: " + agent+" - method: "+request.getMethod()+" uri: "+request.getRequestURI());

		/* 
		 * MiniRedir will be tested seperatly because it will be send with a complex version number and until now all of them
		 * have the OPTIONS and Namespace-"Bug"
		 */
		final boolean enableHook = (agent != null && (agent.contains("MiniRedir") || userAgents.contains(agent)));
		if (enableHook) {
			HttpMethod method;
			try {
				method = HttpMethod.valueOf(request.getMethod().toUpperCase());
			} catch (IllegalArgumentException e) {
				method = HttpMethod.UNKOWN;
			}
			logger.fine("doFilter(..) - method: " + method + " - original: "+request.getMethod());
			
			switch (method) {
			case OPTIONS:
				logger.fine("doFilter(..) - OPTIONS");

				final String uri = request.getRequestURI();
				final boolean isRoot = uri.equals(ROOT_RESOURCE);
				logger.fine("doFilter(..) - URI: " + uri + " isRoot? " + isRoot);
				chain.doFilter(request, response);
				
				if (isRoot) {
					logger.fine("doFilter(..) - procssing isRoot");
					/*
					 * For Windows interoperability the server has to return for an OPTIONS request on ROOT "/"
					 * a 204 for no content, a DAV-Header for the class which gets implemented and the Microsoft specific
					 * MS-Author-Via header
					 */
					response.setStatus(SC_OK);
					if(!response.containsHeader(DAV))
						response.addHeader(DAV, webdavClass);
				}
				
				if(!response.containsHeader(MS_AUTHOR_VIA))
					response.addHeader(MS_AUTHOR_VIA, DAV);
				break;

			case LOCK:
			case PROPFIND:
				logger.fine("doFilter(..) - PROPFIND or LOCK");
				HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(response, responseWrapperBufferSize);

				logger.finest("doFilter(..) - delegating service request");
				chain.doFilter(request, responseWrapper);
				logger.finest("doFilter(..) - get response back");

				/*
				 * Vista and Windows 7 are sending a LOCK even if the resource
				 * is not supporting locking. Unfortunately, if the LOCK is not
				 * answered with a locktoken, Vista and Windows 7 will fail.
				 */
				if (method == LOCK && responseWrapper.getStatusCode() == SC_METHOD_NOT_ALLOWED) {
					final PrintWriter out = response.getWriter();
					try {
						out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?><d:prop xmlns:d=\"DAV:\"><d:lockdiscovery><d:activelock><d:lockscope><d:shared/></d:lockscope><d:locktype><d:write/></d:locktype><d:depth>0</d:depth><d:timeout>Second-1</d:timeout><d:owner>dam</d:owner><d:locktoken><d:href>opaquelocktoken:");
						out.print(UUID.randomUUID());
						out.print("</d:href></d:locktoken></d:activelock></d:lockdiscovery></d:prop>");

						response.setHeader("Content-Type","text/xml;charset=UTF-8");
						response.setStatus(SC_OK);
					} finally {
						out.flush();
						out.close();
					}
					break;
				}

				byte[] responseMsg = responseWrapper.getByteArray();
				if(responseMsg.length > 0){
					ByteArrayInputStream sr = new ByteArrayInputStream(responseMsg);
					Source xmlSource = new StreamSource(sr);
	
					final OutputStream out = response.getOutputStream();
					try {
						final Transformer transformer = this.templates
								.newTransformer();
						StreamResult result = new StreamResult(out);
						transformer.transform(xmlSource, result);
					} catch (Exception ex) {
						context.log("Error while transforming the XML with XSLT.", ex);
						out.write(responseMsg);
					} finally {
						out.flush();
						out.close();
					}
				}
				break;

			default:
				logger.finest("doFilter(..) - delegating service request");
				chain.doFilter(request, response);
				logger.finest("doFilter(..) - get response back");

				break;
			}
		}else{
			logger.finest("doFilter(..) - delegating service request");
			chain.doFilter(request, response);
			logger.finest("doFilter(..) - get response back");
		}
	}

	/**
	 * Precompiled XSLT Style Sheet (for improved performance).
	 */
	private Templates templates;

	@Override
	public void init(FilterConfig config) throws ServletException {
		try {
			context = config.getServletContext();
			String param = config.getInitParameter("interop-xslt");
			if (param == null) {
				param = "xml/prefix.xsl";
			}
			this.templates = TransformerFactory.newInstance().newTemplates(
					new StreamSource(getClass().getClassLoader()
							.getResourceAsStream(param)));
			param = config.getInitParameter("interop-webdav-class");
			if (param == null) {
				param = "1";
			}
			this.webdavClass = param;
			
			userAgents = new HashSet<String>(Arrays.asList(
					"Microsoft Data Access Internet Publishing Provider Protocol", /* Office 2003 */
					"Microsoft Data Access Internet Publishing Provider DAV", /* Office 2003 */
					"Microsoft Data Access Internet Publishing Provider DAV 1.1", /* Office 2003 */
					"Microsoft Data Access Internet Publishing Provider Protocol Discovery", /* Office 2003 */
					"DavClnt" /* Windows 7 */
					));
			
			param = config.getInitParameter("interop-filter-buffersize");
			if(param == null){
				responseWrapperBufferSize = 368;
			}else{
				try {
					responseWrapperBufferSize = Integer.parseInt(param);
				} catch (NumberFormatException e) {
					responseWrapperBufferSize = 368;
				}
			}
		} catch (final TransformerConfigurationException e) {
			throw new ServletException(e);
		} catch (final TransformerFactoryConfigurationError e) {
			throw new ServletException(e);
		}
	}
}
