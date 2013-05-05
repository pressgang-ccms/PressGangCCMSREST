package org.jboss.pressgang.ccms.restserver.webdav;

import org.jboss.pressgang.ccms.restserver.utils.JNDIUtilities;
import org.jboss.resteasy.spi.InternalServerErrorException;

import static javax.ws.rs.core.HttpHeaders.CONTENT_LENGTH;
import static net.java.dev.webdav.jaxrs.Headers.DEPTH;
import static net.java.dev.webdav.jaxrs.Headers.DESTINATION;
import static net.java.dev.webdav.jaxrs.Headers.OVERWRITE;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
    The root of the WebDAV server.
 */
@Path("webdav")
public class WebDavRoot implements WebDavResource {
    /**
     * The Factory used to create EntityManagers
     */
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Override
    public Response get() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response put(@Context UriInfo uriInfo, InputStream entityStream, @HeaderParam(CONTENT_LENGTH) long contentLength) throws IOException, URISyntaxException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response mkcol() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response propfind(@Context UriInfo uriInfo, @HeaderParam(DEPTH) int depth, InputStream entityStream, @HeaderParam(CONTENT_LENGTH) long contentLength, @Context Providers providers, @Context HttpHeaders httpHeaders) throws URISyntaxException, IOException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response proppatch() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response copy() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response move(@Context UriInfo uriInfo, @HeaderParam(OVERWRITE) String overwriteStr, @HeaderParam(DESTINATION) String destination) throws URISyntaxException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response delete() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object findResource(@PathParam("resource") String res) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Response options() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    private EntityManager getEntityManager(boolean joinTransaction) {
        if (entityManagerFactory == null) {
            try {
                entityManagerFactory = JNDIUtilities.lookupJBossEntityManagerFactory();
            } catch (NamingException e) {
                throw new InternalServerErrorException("Could not find the EntityManagerFactory");
            }
        }

        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        if (entityManager == null) throw new InternalServerErrorException("Could not create an EntityManager");

        if (joinTransaction) {
            entityManager.joinTransaction();
        }

        return entityManager;
    }
}
