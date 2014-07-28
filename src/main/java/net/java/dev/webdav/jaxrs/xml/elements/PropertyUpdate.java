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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import net.java.dev.webdav.jaxrs.NullArgumentException;

/**
 * WebDAV propertyupdate XML Element.
 * 
 * @author Markus KARG (mkarg@java.net)
 * 
 * @see <a href="http://www.webdav.org/specs/rfc4918.html#ELEMENT_propertyupdate">Chapter 14.19 "propertyupdate XML Element" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
 */
@XmlRootElement(name = "propertyupdate")
public final class PropertyUpdate {

	@XmlElements( { @XmlElement(name = "remove", type = Remove.class), @XmlElement(name = "set", type = Set.class) })
	private LinkedList<RemoveOrSet> removesOrSets;

	@SuppressWarnings("unused")
	private PropertyUpdate() {
		// For unmarshalling only.
	}

	public PropertyUpdate(final RemoveOrSet removeOrSet, final RemoveOrSet... removesOrSets) {
		if (removeOrSet == null)
			throw new NullArgumentException("removeOrSet");

		this.removesOrSets = new LinkedList<RemoveOrSet>(Collections.singletonList(removeOrSet));
		this.removesOrSets.addAll(Arrays.asList(removesOrSets));
	}

	@SuppressWarnings("unchecked")
	public final List<RemoveOrSet> list() {
		return (List<RemoveOrSet>) this.removesOrSets.clone();
	}

	@Override
	public final String toString() {
		final StringBuilder content = new StringBuilder();

		for (final Object o : this.removesOrSets)
			content.append(o).append(' ');

		return String.format("PropertyUpdate (%s)", content);
	}

}
