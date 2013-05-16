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

package net.java.dev.webdav.jaxrs.xml.elements;

import static javax.xml.bind.annotation.XmlAccessType.FIELD;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import net.java.dev.webdav.jaxrs.NullArgumentException;

/**
 * WebDAV response XML Element.
 * 
 * @author Markus KARG (mkarg@java.net)
 * 
 * @see <a href="http://www.webdav.org/specs/rfc4918.html#ELEMENT_response">Chapter 14.24 "response XML Element" of RFC 4918 "HTTP Extensions for Web Distributed Authoring and Versioning (WebDAV)"</a>
 */
@XmlAccessorType(FIELD)
@XmlType(propOrder = { "hRefs", "status", "propStats", "error", "responseDescription", "location" })
@XmlRootElement
public final class Response {

	@XmlElement(name = "href")
	private LinkedList<HRef> hRefs;

	private Status status;

	@XmlElement(name = "propstat")
	private LinkedList<PropStat> propStats;

	private Error error;

	@XmlElement(name = "responsedescription")
	private ResponseDescription responseDescription;

	private Location location;

	@SuppressWarnings("unused")
	private Response() {
		// For unmarshalling only.
	}

	private Response(final HRef hRef, final Error error, final ResponseDescription responseDescription, final Location location) {
		if (hRef == null)
			throw new NullArgumentException("hRef");

		this.hRefs = new LinkedList<HRef>(Collections.singletonList(hRef));
		this.error = error;
		this.responseDescription = responseDescription;
		this.location = location;
	}

	public Response(final HRef hRef, final Error error, final ResponseDescription responseDescription, final Location location, final PropStat propStat,
			final PropStat... propStats) {
		this(hRef, error, responseDescription, location);

		if (propStat == null)
			throw new NullArgumentException("propStat");

		this.propStats = new LinkedList<PropStat>(Collections.singletonList(propStat));
		this.propStats.addAll(Arrays.asList(propStats));
	}

	/**
	 * @since 1.1.1
	 */
	public Response(final HRef hRef, final Error error, final ResponseDescription responseDescription, final Location location, final Collection<PropStat> propStats) {
		this(hRef, error, responseDescription, location);

		if (propStats == null || !propStats.iterator().hasNext())
			throw new NullArgumentException("propStat");

		this.propStats = new LinkedList<PropStat>(propStats);
	}
	
	public Response(final Status status, final Error error, final ResponseDescription responseDescription, final Location location, final HRef hRef,
			final HRef... hRefs) {
		this(hRef, error, responseDescription, location);

		if (status == null)
			throw new NullArgumentException("status");

		this.status = status;
		this.hRefs.addAll(Arrays.asList(hRefs));
	}

	@SuppressWarnings("unchecked")
	public final List<HRef> getHRefs() {
		return (List<HRef>) this.hRefs.clone();
	}

	public final Status getStatus() {
		return this.status;
	}

	public final Error getError() {
		return this.error;
	}

	public final ResponseDescription getResponseDescription() {
		return this.responseDescription;
	}

	public final Location getLocation() {
		return this.location;
	}

	@SuppressWarnings("unchecked")
	public final List<PropStat> getPropStats() {
		return (List<PropStat>) this.propStats.clone();
	}

}
