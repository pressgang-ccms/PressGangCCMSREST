/*
 * Copyright 2008, 2009 Markus KARG
 *
 * This file is part of webdav-jaxrs.
 *
 * webdav-jaxrs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * webdav-jaxrs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with webdav-jaxrs.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.java.dev.webdav.jaxrs.xml.properties;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.java.dev.webdav.jaxrs.xml.elements.Collection;

/**
 * WebDAV resourcetype Property.
 * 
 * @author Markus KARG (mkarg@java.net)
 * 
 * @see <a href="http://www.webdav.org/specs/rfc4918.html#PROPERTY_resourcetype">Chapter 15.9 "resourcetype Property" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
 */
@XmlRootElement(name = "resourcetype")
public final class ResourceType {

	@XmlAnyElement(lax = true)
	private LinkedList<Object> resourceTypes;

	public static final ResourceType COLLECTION = new ResourceType(new Collection());

	/**
	 * Creates an empty (thus <em>invalid</em>) instance. Use <em>only</em> to
	 * list property name within response to &lt;propname/&gt; request. Not to
	 * be used for creation of valid instances of this property; use
	 * {@link #ResourceType(Object...)} instead.
	 */
	public ResourceType() {
		// Keeping defaults by intention.
	}

	public ResourceType(final Object... any) {
		this.resourceTypes = new LinkedList<Object>(Arrays.asList(any));
	}

	@SuppressWarnings("unchecked")
	public final List<Object> getResourceType() {
		return (List<Object>) this.resourceTypes.clone();
	}

}
