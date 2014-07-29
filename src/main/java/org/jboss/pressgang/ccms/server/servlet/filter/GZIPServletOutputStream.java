/*
  Copyright 2011-2014 Red Hat

  This file is part of PressGang CCMS.

  PressGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PressGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PressGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.server.servlet.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * A wrapper class to allow using a {@link java.util.zip.GZIPOutputStream GZIPOutputStream} as a
 * {@link javax.servlet.ServletOutputStream ServletOutputStream}. <br />
 * <br />
 * See: <a href=
 * "http://stackoverflow.com/questions/4755302/which-compression-is-gzip-the-most-popular-servlet-filter-would-you-suggest/11068672
 * #11068672"
 * >which-compression-is-gzip-the-most-popular-servlet-filter-would-you-suggest</a> and <a
 * href="https://github.com/geoserver/geoserver/tree/master/src/main/src/main/java/org/geoserver/filters"
 * >https://github.com/geoserver/geoserver/</a> for details on where this implementation came from.
 */
public class GZIPServletOutputStream extends ServletOutputStream {
    /**
     * Gzipping an empty file or stream always results in a 20 byte output This is in java or elsewhere.
     * <p/>
     * On a linux system to reproduce do <code>gzip -n empty_file</code>. -n tells gzip to not include the file name. The
     * resulting file size is 20 bytes.
     * <p/>
     * Therefore 20 bytes can be used indicate that the gzip byte[] will be empty when ungzipped.
     */
    private static final int EMPTY_GZIPPED_CONTENT_SIZE = 20;

    protected ByteArrayOutputStream baos = null;
    protected GZIPOutputStream GZIPStream = null;
    protected boolean closed = false;
    protected HttpServletResponse response = null;
    protected ServletOutputStream output = null;

    public GZIPServletOutputStream(HttpServletResponse response) throws IOException {
        super();
        closed = false;
        this.response = response;
        this.output = response.getOutputStream();
        baos = new ByteArrayOutputStream();
        GZIPStream = new GZIPOutputStream(baos);
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            throw new IOException("This output stream has already been closed");
        }
        GZIPStream.finish();

        int status = response.getStatus();
        final byte[] bytes = baos.toByteArray();

        // Check if the response should be empty based on the response code
        // Also only set the gzip header and content when the response isn't empty.
        if (!shouldBodyBeZero(status) && !isBodyEmpty(bytes)) {
            final String contentLength = Integer.toString(bytes.length);

            if (response.containsHeader("Content-Length")) {
                response.setHeader("Content-Length", contentLength);
            } else {
                response.addHeader("Content-Length", contentLength);
            }

            response.addHeader("Content-Encoding", "gzip");
            output.write(bytes);
        } else {
            if (response.containsHeader("Content-Length")) {
                response.setIntHeader("Content-Length", 0);
            } else {
                response.addIntHeader("Content-Length", 0);
            }
        }
        output.flush();
        output.close();
        closed = true;
    }

    @Override
    public void flush() throws IOException {
        if (closed) {
            throw new IOException("Cannot flush a closed output stream");
        }
        GZIPStream.flush();
    }

    public void write(int b) throws IOException {
        if (closed) {
            throw new IOException("Cannot write to a closed output stream");
        }
        GZIPStream.write((byte) b);
    }

    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len) throws IOException {
        if (closed) {
            throw new IOException("Cannot write to a closed output stream");
        }
        GZIPStream.write(b, off, len);
    }

    public boolean closed() {
        return (this.closed);
    }

    public void reset() {
    }

    protected boolean isBodyEmpty(byte[] compressedOutput) {
        if (compressedOutput.length == EMPTY_GZIPPED_CONTENT_SIZE) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean shouldBodyBeZero(int responseStatus) {
        // Check for NO_CONTENT
        if (responseStatus == HttpServletResponse.SC_NO_CONTENT) {
            return true;
        }

        // Check for NOT_MODIFIED
        if (responseStatus == HttpServletResponse.SC_NOT_MODIFIED) {
            return true;
        }

        return false;
    }
}
