package org.jboss.pressgang.ccms.restserver.webdav;


import net.java.dev.webdav.jaxrs.xml.elements.Response;

import javax.ws.rs.core.UriInfo;

/**
    The base of all WebDAV entities
 */
public interface WebDavEntity {
    Response getProperties(final UriInfo uriInfo);
}
