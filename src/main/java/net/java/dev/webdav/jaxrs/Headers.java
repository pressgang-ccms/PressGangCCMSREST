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

package net.java.dev.webdav.jaxrs;

/**
 * WebDAV Headers
 * 
 * @author Markus KARG (mkarg@java.net)
 * 
 * @see <a href="http://www.webdav.org/specs/rfc4918.html#http.headers.for.distributed.authoring">Chapter 10 "HTTP Headers for Distributed Authoring" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
 */
public interface Headers {

	/**
	 * WebDAV DAV Header
	 * 
	 * @see <a href="http://www.webdav.org/specs/rfc4918.html#HEADER_DAV">Chapter 10.1 "DAV Header" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
	 */
	public static final String DAV = "DAV";

	/**
	 * WebDAV Depth Header
	 * 
	 * @see <a href="http://www.webdav.org/specs/rfc4918.html#HEADER_Depth">Chapter 10.2 "Depth Header" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
	 */
	public static final String DEPTH = "Depth";

	/**
	 * WebDAV Depth Header Value "0"
	 * 
	 * @see <a href="http://www.webdav.org/specs/rfc4918.html#HEADER_Depth">Chapter 10.2 "Depth Header" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
	 */
	public static final String DEPTH_0 = "0";

	/**
	 * WebDAV Depth Header Value "1"
	 * 
	 * @see <a href="http://www.webdav.org/specs/rfc4918.html#HEADER_Depth">Chapter 10.2 "Depth Header" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
	 */
	public static final String DEPTH_1 = "1";

	/**
	 * WebDAV Depth Header Value "infinity"
	 * 
	 * @see <a href="http://www.webdav.org/specs/rfc4918.html#HEADER_Depth">Chapter 10.2 "Depth Header" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
	 */
	public static final String DEPTH_INFINITY = "infinity";

	/**
	 * WebDAV Destination Header
	 * 
	 * @see <a href="http://www.webdav.org/specs/rfc4918.html#HEADER_Destination">Chapter 10.3 "Destination Header" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
	 */
	public static final String DESTINATION = "Destination";

	/**
	 * WebDAV If Header
	 * 
	 * @see <a href="http://www.webdav.org/specs/rfc4918.html#HEADER_If">Chapter 10.4 "If Header" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
	 */
	public static final String IF = "If";

	/**
	 * WebDAV Lock-Token Header
	 * 
	 * @see <a href="http://www.webdav.org/specs/rfc4918.html#HEADER_Lock-Token">Chapter 10.5 "Lock-Token Header" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
	 */
	public static final String LOCK_TOKEN = "Lock-Token";

	/**
	 * WebDAV Overwrite Header
	 * 
	 * @see <a href="http://www.webdav.org/specs/rfc4918.html#HEADER_Overwrite">Chapter 10.6 "Overwrite Header" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
	 */
	public static final String OVERWRITE = "Overwrite";

	/**
	 * WebDAV Overwrite Header Value "T"
	 * 
	 * @see <a href="http://www.webdav.org/specs/rfc4918.html#HEADER_Overwrite">Chapter 10.6 "Overwrite Header" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
	 */
	public static final String OVERWRITE_TRUE = "T";

	/**
	 * WebDAV Overwrite Header Value "F"
	 * 
	 * @see <a href="http://www.webdav.org/specs/rfc4918.html#HEADER_Overwrite">Chapter 10.6 "Overwrite Header" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
	 */
	public static final String OVERWRITE_FALSE = "F";

	/**
	 * WebDAV Timeout Header
	 * 
	 * @see <a href="http://www.webdav.org/specs/rfc4918.html#HEADER_Timeout">Chapter 10.7 "Timeout Request Header" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
	 */
	public static final String TIMEOUT = "Timeout";

	/**
	 * WebDAV Timeout Header Value "Second-"
	 * 
	 * @see <a href="http://www.webdav.org/specs/rfc4918.html#HEADER_Timeout">Chapter 10.7 "Timeout Request Header" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
	 */
	public static final String TIMEOUT_SECOND = "Second-";

	/**
	 * WebDAV Timeout Header Value "Infinite"
	 * 
	 * @see <a href="http://www.webdav.org/specs/rfc4918.html#HEADER_Timeout">Chapter 10.7 "Timeout Request Header" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
	 */
	public static final String TIMEOUT_INFINITE = "Infinite";

}
