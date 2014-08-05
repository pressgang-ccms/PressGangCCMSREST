/*
  Copyright 2011-2014 Red Hat, Inc

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

package net.java.dev.webdav.interop;

import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response.ResponseBuilder;
import static net.java.dev.webdav.interop.WindowsRedirectorPatchResourceFilter.*;

/**
 * This class represents a JAX-RS resource with only the OPTIONS-Method which 
 * returns WebDAV-specific headers for the root resource.
 * Why is this needed?
 * 
 * If you want to register a network folder. For example "net use X: http://mywebdavserver/filesystem".
 * Microsoft MiniRedir expects that the root resource is webdav enabled.
 * 
 * If you add this Resource, your server will be webdav enabled on the root.
 * 
 * @author Daniel MANZKE (daniel.manzke@googlemail.com)
 */
@Path(ROOT_RESOURCE)
public class WindowsRedirectorPatchResource {
	
	@OPTIONS
	public javax.ws.rs.core.Response options() {
		ResponseBuilder builder = javax.ws.rs.core.Response.noContent();
		builder.header(DAV, "1");
		/*
		 * builder.header("Allow","");
		 * OPTIONS, GET, HEAD, DELETE, PROPPATCH, COPY, MOVE, LOCK, UNLOCK, PROPFIND, PUT
		 */
		builder.header(MS_AUTHOR_VIA, DAV);
		
		return builder.build();
	}
}
