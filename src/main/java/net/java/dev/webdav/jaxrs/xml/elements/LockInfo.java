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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import net.java.dev.webdav.jaxrs.NullArgumentException;

/**
 * WebDAV lockinfo XML Element.
 * 
 * @author Markus KARG (mkarg@java.net)
 * 
 * @see <a href="http://www.webdav.org/specs/rfc4918.html#ELEMENT_lockinfo">Chapter 14.11 "lockinfo XML Element" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
 */
@XmlType(propOrder = { "lockScope", "lockType", "owner" })
@XmlRootElement(name = "lockinfo")
public final class LockInfo {

	@XmlElement(name = "lockscope")
	private LockScope lockScope;

	@XmlElement(name = "locktype")
	private LockType lockType;

	@XmlElement
	private Owner owner;

	@SuppressWarnings("unused")
	private LockInfo() {
		// For unmarshalling only.
	}

	public LockInfo(final LockScope lockScope, final LockType lockType, final Owner owner) {
		if (lockScope == null)
			throw new NullArgumentException("lockScope");

		if (lockType == null)
			throw new NullArgumentException("lockType");

		this.lockScope = lockScope;
		this.lockType = lockType;
		this.owner = owner;
	}

	public final LockScope getLockScope() {
		return this.lockScope;
	}

	public final LockType getLockType() {
		return this.lockType;
	}

	public final Owner getOwner() {
		return this.owner;
	}

}
