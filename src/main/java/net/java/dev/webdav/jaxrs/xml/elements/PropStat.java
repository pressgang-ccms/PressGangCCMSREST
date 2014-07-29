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

package net.java.dev.webdav.jaxrs.xml.elements;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import net.java.dev.webdav.jaxrs.NullArgumentException;

/**
 * WebDAV propstat XML Element.
 * 
 * @author Markus KARG (mkarg@java.net)
 * 
 * @see <a href="http://www.webdav.org/specs/rfc4918.html#ELEMENT_propstat">Chapter 14.22 "propstat XML Element" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
 */
@XmlType(propOrder = { "prop", "status", "error", "responseDescription" })
@XmlRootElement(name = "propstat")
public final class PropStat {

	@XmlElement
	private Prop prop;

	@XmlElement
	private Status status;

	@XmlElement
	private Error error;

	@XmlElement(name = "responsedescription")
	private ResponseDescription responseDescription;

	@SuppressWarnings("unused")
	private PropStat() {
		// For unmarshalling only.
	}

	public PropStat(final Prop prop, final Status status, final Error error, final ResponseDescription responseDescription) {
		if (prop == null)
			throw new NullArgumentException("prop");

		if (status == null)
			throw new NullArgumentException("status");

		this.prop = prop;
		this.status = status;
		this.error = error;
		this.responseDescription = responseDescription;
	}

	public PropStat(final Prop prop, final Status status) {
		this(prop, status, null, null);
	}

	public PropStat(final Prop prop, final Status status, final Error error) {
		this(prop, status, error, null);
	}

	public PropStat(final Prop prop, final Status status, final ResponseDescription responseDescription) {
		this(prop, status, null, responseDescription);
	}

	public final Prop getProp() {
		return this.prop;
	}

	public final Status getStatus() {
		return this.status;
	}

	public final Error getError() {
		return this.error;
	}

	public final ResponseDescription getResponseDescription() {
		return this.responseDescription;
	}

}
