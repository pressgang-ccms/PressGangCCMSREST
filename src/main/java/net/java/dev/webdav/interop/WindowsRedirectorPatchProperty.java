/*
 * Copyright 2008, 2009 Daniel MANZKE
 * Copyright 2011 Markus KARG
 *
 * This file is part of webdav-interop.
 *
 * webdav-interop is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * webdav-interop is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with webdav-interop.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.java.dev.webdav.interop;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * This class represents a Mock-Property. Due the fact that it has no namespace, it causes JAXB 
 * to generate the WebDAV-XML with namespaces. Without this property, JAXB would generate XML which
 * has no namespaces and the Microsoft MiniRedir would reject the response as a non valid answer.
 * 
 * @author Daniel MANZKE (daniel.manzke@googlemail.com)
 */
@XmlRootElement
public class WindowsRedirectorPatchProperty {
	// EMPTY
}
