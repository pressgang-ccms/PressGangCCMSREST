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

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * WebDAV lockscope XML Element.
 * 
 * @author Markus KARG (mkarg@java.net)
 * 
 * @see <a href="http://www.webdav.org/specs/rfc4918.html#ELEMENT_lockscope">Chapter 14.13 "lockscope XML Element" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
 */
@XmlAccessorType(FIELD)
@XmlType(propOrder = { "exclusive", "shared" })
@XmlJavaTypeAdapter(LockScope.LockScopeAdapter.class)
@XmlRootElement(name = "lockscope")
public final class LockScope {

	public static final LockScope SHARED = new LockScope(Shared.SINGLETON, null);

	public static final LockScope EXCLUSIVE = new LockScope(null, Exclusive.SINGLETON);

	private Shared shared;

	private Exclusive exclusive;

	// Singleton
	private LockScope() {
		// For unmarshalling only.
	}

	// Enum
	private LockScope(final Shared shared, final Exclusive exclusive) {
		this.shared = shared;
		this.exclusive = exclusive;
	}

	/*
	 * XmlAdapter is intentionally not directly implemented by surrounding class
	 * to prevent third party code to call it's methods: Unfortunately
	 * XmlAdapter enforces public visibility of all it's e.
	 * 
	 * This inner class cannot be public since Sun's compiler doesn't allow
	 * that, while Eclipse's compiler actually does.
	 */
	protected static final class LockScopeAdapter extends XmlAdapter<LockScope, LockScope> {

		/**
		 * For internal use only. Do not call this from client code.
		 * 
		 * @since 1.1.1
		 */
		@Override
		public final LockScope marshal(final LockScope value) throws Exception {
			return value;
		}

		/**
		 * For internal use only. Do not call this from client code.
		 * 
		 * @since 1.1.1
		 */
		@SuppressWarnings("synthetic-access")
		@Override
		public final LockScope unmarshal(final LockScope value) throws Exception {
			if (value == null)
				return null;

			if (value.exclusive != null)
				return EXCLUSIVE;

			if (value.shared != null)
				return SHARED;

			return value;
		}
	}
}
