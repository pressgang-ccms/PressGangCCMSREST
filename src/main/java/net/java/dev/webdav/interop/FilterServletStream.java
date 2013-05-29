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

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

public class FilterServletStream extends ServletOutputStream {
	private final OutputStream stream;

	public FilterServletStream(OutputStream output) {
		stream = output;
	}

	public void write(int b) throws IOException {
		stream.write(b);
	}

	public void write(byte[] b) throws IOException {
		stream.write(b);
	}

	public void write(byte[] b, int off, int len) throws IOException {
		stream.write(b, off, len);
	}
}
