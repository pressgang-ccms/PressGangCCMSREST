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

package net.java.dev.webdav.jaxrs.xml.elements;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * WebDAV prop XML Element.
 * 
 * @author Markus KARG (mkarg@java.net)
 * 
 * @see <a href="http://www.webdav.org/specs/rfc4918.html#ELEMENT_prop">Chapter 14.18 "prop XML Element" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
 */
@XmlRootElement
public final class Prop {

	@XmlAnyElement(lax = true)
	private LinkedList<Object> properties;

	@SuppressWarnings("unused")
	private Prop() {
		// For unmarshalling only.
	}

	public Prop(final Object... any) {
		this.properties = new LinkedList<Object>(Arrays.asList(any));
	}

	@SuppressWarnings("unchecked")
	public final List<Object> getProperties() {
		return (List<Object>) this.properties.clone();
	}

	@Override
	public final String toString() {
		final StringBuilder content = new StringBuilder();

		for (final Object o : this.properties)
			content.append(o).append(' ');

		return String.format("Prop (%s)", content);
	}

}
