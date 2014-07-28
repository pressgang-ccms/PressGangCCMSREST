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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.java.dev.webdav.jaxrs.NullArgumentException;

/**
 * WebDAV error XML Element.
 * 
 * @author Markus KARG (mkarg@java.net)
 * 
 * @see <a href="http://www.webdav.org/specs/rfc4918.html#ELEMENT_error">Chapter 14.5 "error XML Element" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
 */
@XmlRootElement
public final class Error {

	@XmlAnyElement(lax = true)
	public LinkedList<Object> errors;

	@SuppressWarnings("unused")
	private Error() {
		// For unmarshalling only.
	}

	public Error(final Object error, final Object... errors) {
		if (error == null)
			throw new NullArgumentException("error");

		this.errors = new LinkedList<Object>(Collections.singletonList(error));
		this.errors.addAll(Arrays.asList(errors));
	}

	@SuppressWarnings("unchecked")
	public final List<Object> getErrors() {
		return (List<Object>) this.errors.clone();
	}

}
