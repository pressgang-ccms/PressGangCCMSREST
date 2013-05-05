/*
 * Copyright 2008, 2009 Daniel MANZKE
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
package org.jboss.pressgang.ccms.restserver.webdav;

import static javax.ws.rs.core.HttpHeaders.CONTENT_LENGTH;
import static net.java.dev.webdav.jaxrs.Headers.DEPTH;
import static net.java.dev.webdav.jaxrs.Headers.DESTINATION;
import static net.java.dev.webdav.jaxrs.Headers.OVERWRITE;
import static net.java.dev.webdav.jaxrs.Headers.DAV;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

import net.java.dev.webdav.jaxrs.methods.COPY;
import net.java.dev.webdav.jaxrs.methods.MKCOL;
import net.java.dev.webdav.jaxrs.methods.MOVE;
import net.java.dev.webdav.jaxrs.methods.PROPFIND;
import net.java.dev.webdav.jaxrs.methods.PROPPATCH;
import org.jboss.pressgang.ccms.restserver.utils.JNDIUtilities;
import org.jboss.resteasy.spi.InternalServerErrorException;

public interface WebDavResource {
    String WEBDAV_COMPLIANCE_LEVEL = "1";

    @GET
    @Produces("application/octet-stream")
    javax.ws.rs.core.Response get();

    @PUT
    @Consumes("application/octet-stream")
    javax.ws.rs.core.Response put(@Context final UriInfo uriInfo,
                                  final InputStream entityStream,
                                  @HeaderParam(CONTENT_LENGTH) final long contentLength)
            throws IOException, URISyntaxException;

    @MKCOL
    javax.ws.rs.core.Response mkcol();

    @Produces("application/xml")
    @PROPFIND
    javax.ws.rs.core.Response propfind(@Context final UriInfo uriInfo,
                                       @HeaderParam(DEPTH) final int depth, final InputStream entityStream, @HeaderParam(CONTENT_LENGTH) final long contentLength, @Context final Providers providers, @Context final HttpHeaders httpHeaders) throws URISyntaxException, IOException;

    @PROPPATCH
    javax.ws.rs.core.Response proppatch();

    @COPY
    javax.ws.rs.core.Response copy();

    /*
          201 (Created)	The resource was moved successfully and a new resource was created at the specified destination URI.
        204 (No Content)	The resource was moved successfully to a pre-existing destination URI.
        403 (Forbidden)	The source URI and the destination URI are the same.
        409 (Conflict)	A resource cannot be created at the destination URI until one or more intermediate collections are created.
        412 (Precondition Failed)	Either the Overwrite header is "F" and the state of the destination resource is not null, or the method was used in a Depth: 0 transaction.
        423 (Locked)	The destination resource is locked.
        502 (Bad Gateway)	The destination URI is located on a different server, which refuses to accept the resource.
     */
    @MOVE
    javax.ws.rs.core.Response move(@Context final UriInfo uriInfo, @HeaderParam(OVERWRITE) final String overwriteStr, @HeaderParam(DESTINATION) final String destination) throws URISyntaxException;

    @DELETE
    javax.ws.rs.core.Response delete();

    @Path("{resource}")
    Object findResource(@PathParam("resource") final String res);

    @OPTIONS
    javax.ws.rs.core.Response options();


}