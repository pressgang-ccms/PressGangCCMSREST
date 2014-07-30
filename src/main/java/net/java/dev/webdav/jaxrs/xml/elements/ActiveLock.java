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

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import net.java.dev.webdav.jaxrs.NullArgumentException;

/**
 * WebDAV activelock XML Element.
 * 
 * @author Markus KARG (mkarg@java.net)
 * 
 * @see <a href="http://www.webdav.org/specs/rfc4918.html#ELEMENT_activelock">Chapter 14.1 "activelock XML Element" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
 */
@XmlAccessorType(FIELD)
@XmlType(propOrder = { "lockScope", "lockType", "depth", "owner", "timeOut", "lockToken", "lockRoot" })
@XmlRootElement(name = "activelock")
public final class ActiveLock {

	@XmlElement(name = "lockscope")
	private LockScope lockScope;

	@XmlElement(name = "locktype")
	private LockType lockType;

	private Depth depth;

	private Owner owner;

	@XmlElement(name = "timeout")
	private TimeOut timeOut;

	@XmlElement(name = "locktoken")
	private LockToken lockToken;

	@XmlElement(name = "lockroot")
	private LockRoot lockRoot;

	@SuppressWarnings("unused")
	private ActiveLock() {
		// For unmarshalling only;
	}

	public ActiveLock(final LockScope lockScope, final LockType lockType, final Depth depth, final Owner owner, final TimeOut timeOut,
			final LockToken lockToken, final LockRoot lockRoot) {
		if (lockScope == null)
			throw new NullArgumentException("lockScope");

		if (lockType == null)
			throw new NullArgumentException("lockType");

		if (depth == null)
			throw new NullArgumentException("depth");

		if (lockRoot == null)
			throw new NullArgumentException("lockRoot");

		this.lockScope = lockScope;
		this.lockType = lockType;
		this.depth = depth;
		this.owner = owner;
		this.timeOut = timeOut;
		this.lockToken = lockToken;
		this.lockRoot = lockRoot;
	}

	public final LockScope getLockScope() {
		return this.lockScope;
	}

	public final LockType getLockType() {
		return this.lockType;
	}

	/**
	 * @deprecated Since 1.1.1. Use {@link #getDepth()} instead.
	 */
	@Deprecated
	public final Depth depth() {
		return this.depth;
	}

	/**
	 * @since 1.1.1
	 */
	public final Depth getDepth() {
		return this.depth;
	}

	public final Owner getOwner() {
		return this.owner;
	}

	public final TimeOut getTimeOut() {
		return this.timeOut;
	}

	public final LockToken getLockToken() {
		return this.lockToken;
	}

	public final LockRoot getLockRoot() {
		return this.lockRoot;
	}

}
