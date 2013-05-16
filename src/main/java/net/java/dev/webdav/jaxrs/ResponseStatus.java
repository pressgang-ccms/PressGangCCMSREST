/*
 * Copyright 2008, 2009 Markus KARG
 *
 * This file is part of webdav-jaxrs.
 *
 * webdav-jaxrs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * webdav-jaxrs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with webdav-jaxrs.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.java.dev.webdav.jaxrs;

import static javax.ws.rs.core.Response.Status.Family.CLIENT_ERROR;
import static javax.ws.rs.core.Response.Status.Family.INFORMATIONAL;
import static javax.ws.rs.core.Response.Status.Family.REDIRECTION;
import static javax.ws.rs.core.Response.Status.Family.SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;

import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.core.Response.Status.Family;

/**
 * Commonly used status codes defined by WebDAV.
 * 
 * @author Markus KARG (mkarg@java.net)
 * 
 * @see <a href="http://www.webdav.org/specs/rfc4918.html#status.code.extensions.to.http11">Chapter 11 "Status Code Extensions to HTTP/1.1" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
 */
public enum ResponseStatus implements StatusType {

	/**
	 * 207 Multi-Status
	 * 
	 * @see <a href="http://www.webdav.org/specs/rfc4918.html#STATUS_207">Chapter 11.1 "207 Multi-Status" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
	 */
	MULTI_STATUS(207, "Multi-Status"),

	/**
	 * 422 Unprocessable Entity
	 * 
	 * @see <a href="http://www.webdav.org/specs/rfc4918.html#STATUS_422">Chapter 11.2 "422 Unprocessable Entity" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
	 */
	UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),

	/**
	 * 423 Locked
	 * 
	 * @see <a href="http://www.webdav.org/specs/rfc4918.html#STATUS_423">Chapter 11.3 "423 Locked" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
	 */
	LOCKED(423, "Locked"),

	/**
	 * 424 Failed Dependency
	 * 
	 * @see <a href="http://www.webdav.org/specs/rfc4918.html#STATUS_424">Chapter 11.4 "424 Failed Dependency" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
	 */
	FAILED_DEPENDENCY(424, "Failed Dependency"),

	/**
	 * 507 Insufficient Storage
	 * 
	 * @see <a href="http://www.webdav.org/specs/rfc4918.html#STATUS_507">Chapter 11.5 "507 Insufficient Storage" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
	 */
	INSUFFICIENT_STORAGE(507, "Insufficient Storage");

	private final int statusCode;

	private final String reasonPhrase;

	private ResponseStatus(final int statusCode, final String reasonPhrase) {
		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
	}

	public final int getStatusCode() {
		return this.statusCode;
	}

	public final Family getFamily() {
		switch (this.statusCode / 100) {
		case 1:
			return INFORMATIONAL;
		case 2:
			return SUCCESSFUL;
		case 3:
			return REDIRECTION;
		case 4:
			return CLIENT_ERROR;
		default:
			return SERVER_ERROR;
		}
	}

	/**
	 * @deprecated Since 1.1. Use {@link #getReasonPhrase()} instead to get the
	 *             reason phrase. Future releases will return the name of the
	 *             enum constant instead of the reason phrase (see
	 *             {@link java.lang.Enum#toString()}).
	 */
	@Deprecated
	@Override
	public final String toString() {
		return this.reasonPhrase;
	}

	@Override
	public final String getReasonPhrase() {
		return this.reasonPhrase;
	}

}
