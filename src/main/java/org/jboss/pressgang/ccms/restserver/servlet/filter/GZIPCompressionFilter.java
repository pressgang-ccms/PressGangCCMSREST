package org.jboss.pressgang.ccms.restserver.servlet.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Servlet Filter to Compress the response of a HTTP Request using the GZIP compression algorithm.
 * <p/>
 * It allows to you specify what MIME types should be compressed using the "mime-types" init parameter.
 * <br><br>
 * Sample web.xml config:
 * <pre>{@code<filter>
    <filter-name>compression</filter-name>
    <filter-class>org.jboss.pressgang.ccms.restserver.servlet.filter.GZIPCompressionFilter</filter-class>
    <init-param>
        <param-name>mime-types</param-name>
        <param-value>application/json, application/svg+xml, text/*</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>compression</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>}</pre>
 */
@WebFilter(urlPatterns = "/rest/*")
public class GZIPCompressionFilter implements Filter {
    private static Logger log = LoggerFactory.getLogger(GZIPCompressionFilter.class);
    private static AtomicBoolean initialised = new AtomicBoolean(false);

    private boolean enabled = false;

    private Set<Pattern> mimeTypes = new HashSet<Pattern>();

    @Override
    public void destroy() {
        if (enabled) {
            log.debug("Destroying the GZIP Compression Filter");
            enabled = false;
            initialised.set(false);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // If the filter wasn't initialised then forward the request
        if (!enabled || !(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse) || isIncluded(
                (HttpServletRequest) request)) {
            chain.doFilter(request, response);
            return;
        }

        // Check if the request will accept compression
        boolean supportCompression = false;
        Enumeration<String> e = ((HttpServletRequest) request).getHeaders("Accept-Encoding");
        while (e.hasMoreElements()) {
            final String headerName = e.nextElement();
            if (headerName.indexOf("gzip") != -1) {
                supportCompression = true;
            }
        }

        // Do the compression if it's supported otherwise continue down the chain
        if (!supportCompression) {
            chain.doFilter(request, response);
            return;
        } else {
            final GZIPResponseWrapper responseWrapper = new GZIPResponseWrapper((HttpServletResponse) response, mimeTypes);
            chain.doFilter(request, responseWrapper);
            responseWrapper.finish();
        }
    }

    /**
     * Checks if the request uri is an include. These cannot be gzipped.
     */
    private boolean isIncluded(final HttpServletRequest request) {
        final String uri = (String) request.getAttribute("javax.servlet.include.request_uri");
        final boolean includeRequest = !(uri == null);

        return includeRequest;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        /*
         * We only want one instance of the compression filter to be active at one time. As such we use a static variable to
         * determine if one has already been initialised. If it has then we leave this instance alone so it'll just continue
         * down the Filter Chain.
         */
        if (initialised.compareAndSet(false, true)) {
            log.debug("Initialising the GZIP Compression Filter");
            enabled = true;

            // Load the mime types that should be compressed. If the parameter isn't set then compress all types.
            final String compressedMimeTypes = filterConfig.getInitParameter("mime-types");
            if (compressedMimeTypes != null) {
                String[] mimeTypeNames = compressedMimeTypes.split("\\s*,\\s*");
                for (int i = 0; i < mimeTypeNames.length; i++) {
                    mimeTypes.add(Pattern.compile(mimeTypeNames[i].replace("+", "\\+").replace("*", ".*")));
                }
            } else {
                mimeTypes.add(Pattern.compile(".*"));
            }
        }
    }

}