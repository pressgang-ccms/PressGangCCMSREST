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

package net.java.dev.webdav.jaxrs.xml.conditions;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.java.dev.webdav.jaxrs.NullArgumentException;
import net.java.dev.webdav.jaxrs.xml.elements.HRef;

/**
 * WebDAV lock-token-submitted Precondition XML Element.
 * 
 * @author Markus KARG (mkarg@java.net)
 * 
 * @see <a href="http://www.webdav.org/specs/rfc4918.html#precondition.postcondition.xml.elements">Chapter 16 "Precondition/Postcondition XML Elements" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
 */
@XmlRootElement(name = "lock-token-submitted")
public final class LockTokenSubmitted {

	@XmlElement(name = "href")
	private LinkedList<HRef> hRefs;

	@SuppressWarnings("unused")
	private LockTokenSubmitted() {
		// For unmarshalling only.
	}

	public LockTokenSubmitted(final HRef hRef, final HRef... hRefs) {
		if (hRef == null)
			throw new NullArgumentException("hRef");

		this.hRefs = new LinkedList<HRef>(Collections.singleton(hRef));
		this.hRefs.addAll(Arrays.asList(hRefs));
	}

	@SuppressWarnings("unchecked")
	public final List<HRef> getHRefs() {
		return (List<HRef>) this.hRefs.clone();
	}

}
