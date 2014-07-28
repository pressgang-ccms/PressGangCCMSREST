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

package net.java.dev.webdav.jaxrs.xml.elements;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * WebDAV multistatus XML Element.
 * 
 * @author Markus KARG (mkarg@java.net)
 * 
 * @see <a href="http://www.webdav.org/specs/rfc4918.html#ELEMENT_multistatus">Chapter 14.16 "multistatus XML Element" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
 */
@XmlType(propOrder = { "responses", "responseDescription" })
@XmlRootElement(name = "multistatus")
public final class MultiStatus {

	@XmlElement(name = "response")
	private LinkedList<Response> responses;

	@XmlElement(name = "responsedescription")
	private ResponseDescription responseDescription;

	public MultiStatus() {
		this.responses = new LinkedList<Response>();
	}

	public MultiStatus(final ResponseDescription responseDescription, final Response... responses) {
		this.responses = new LinkedList<Response>(Arrays.asList(responses));
		this.responseDescription = responseDescription;
	}

	public MultiStatus(final Response... responses) {
		this(null, responses);
	}

	public MultiStatus(final ResponseDescription responseDescription) {
		this(responseDescription, (Response) null);
	}

	@SuppressWarnings("unchecked")
	public final List<Response> getResponses() {
		return (List<Response>) this.responses.clone();
	}

	public final ResponseDescription getResponseDescription() {
		return this.responseDescription;
	}

}
