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

package net.java.dev.webdav.jaxrs.xml.properties;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.java.dev.webdav.jaxrs.xml.elements.LockEntry;

/**
 * WebDAV supportedlock Property.
 * 
 * @author Markus KARG (mkarg@java.net)
 * 
 * @see <a href="http://www.webdav.org/specs/rfc4918.html#PROPERTY_supportedlock">Chapter 15.10 "supportedlock Property" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
 */
@XmlRootElement(name = "supportedlock")
public final class SupportedLock {

	@XmlElement(name = "lockentry")
	private LinkedList<LockEntry> lockEntries;

	public SupportedLock() {
		// Has no members.
	}

	public SupportedLock(final LockEntry... lockEntries) {
		this.lockEntries = new LinkedList<LockEntry>(Arrays.asList(lockEntries));
	}

	@SuppressWarnings("unchecked")
	public final List<LockEntry> getLockEntries() {
		return (List<LockEntry>) this.lockEntries.clone();
	}

}
