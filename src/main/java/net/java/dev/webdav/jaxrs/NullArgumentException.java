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

package net.java.dev.webdav.jaxrs;

/**
 * Thrown to indicate that a method has been passed <code>null</code> for an
 * argument which is not allowed to be <code>null<code>.
 * 
 * @author Markus KARG (mkarg@java.net)
 */
@SuppressWarnings("serial")
public final class NullArgumentException extends IllegalArgumentException {

	public NullArgumentException(final String argumentName) {
		super(String.format("Argument '%s' must not be null.", argumentName));
	}

}
