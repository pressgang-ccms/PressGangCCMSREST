/*
  Copyright 2011-2014 Red Hat

  This file is part of PresGang CCMS.

  PresGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PresGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PresGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.java.dev.webdav.interop;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * This class wraps a real HttpServletResponse and wraps the methods which are
 * responsible for setting the status code. It also catchs the OutputStream so
 * it could not be closed and send to the client.
 * 
 * @author Daniel MANZKE (daniel.manzke@googlemail.com)
 * @author Markus KARG (mkarg@java.net)
 */
public class HttpServletResponseWrapper extends
		javax.servlet.http.HttpServletResponseWrapper {
	private final ByteArrayOutputStream output;

	public HttpServletResponseWrapper(HttpServletResponse response, int bufferSize) {
		super(response);
		output = new ByteArrayOutputStream(bufferSize);
	}

	public ServletOutputStream getOutputStream() {
		return new FilterServletStream(output);
	}

	public PrintWriter getWriter() {
		return new PrintWriter(getOutputStream(), true);
	}
	
	public byte[] getByteArray() {
		return output.toByteArray();
	}
	
	private int statusCode;
	
	/*
	 * Until this code is migrated to Servlet API 3.0 (where getStatusCode() is
	 * already implemented in javax.servlet.http.HttpServletResponseWrapper) we
	 * need to implemented this functionality on our own.
	 */
	public final int getStatusCode() {
		return this.statusCode;
	}

	@Override
	public final void sendError(final int statusCode, final String statusMessage) throws IOException {
		super.sendError(statusCode, statusMessage);
		this.statusCode = statusCode;
	}

	@Override
	public final void sendError(final int statusCode) throws IOException {
		super.sendError(statusCode);
		this.statusCode = statusCode;
	}

	@Override
	public final void setStatus(final int statusCode, final String statusMessage) {
		super.setStatus(statusCode, statusMessage);
		this.statusCode = statusCode;		
	}

	@Override
	public final void setStatus(final int statusCode) {
		super.setStatus(statusCode);
		this.statusCode = statusCode;
	}
	
}
