package org.jboss.pressgang.ccms.restserver.webdav;

import net.java.dev.webdav.jaxrs.methods.PROPFIND;
import net.java.dev.webdav.jaxrs.xml.elements.*;
import net.java.dev.webdav.jaxrs.xml.elements.Response;
import net.java.dev.webdav.jaxrs.xml.properties.*;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.restserver.utils.JNDIUtilities;
import org.jboss.resteasy.spi.InternalServerErrorException;

import static javax.ws.rs.core.HttpHeaders.CONTENT_LENGTH;
import static net.java.dev.webdav.jaxrs.Headers.DEPTH;
import static net.java.dev.webdav.jaxrs.Headers.DESTINATION;
import static net.java.dev.webdav.jaxrs.Headers.OVERWRITE;
import static javax.ws.rs.core.Response.Status.OK;

import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.StatusType;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Providers;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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
    public javax.ws.rs.core.Response get() {
        return null;
    }

    @Override
    public javax.ws.rs.core.Response put(@Context UriInfo uriInfo, InputStream entityStream, @HeaderParam(CONTENT_LENGTH) long contentLength) throws IOException, URISyntaxException {
        return null;
    }

    @Override
    public javax.ws.rs.core.Response mkcol() {
        return null;
    }

    @Override
    @Produces("application/xml")
    @PROPFIND
    public javax.ws.rs.core.Response propfind(@Context UriInfo uriInfo, @HeaderParam(DEPTH) int depth, InputStream entityStream, @HeaderParam(CONTENT_LENGTH) long contentLength, @Context Providers providers, @Context HttpHeaders httpHeaders) throws URISyntaxException, IOException {
        try {

            final EntityManager entityManager = getEntityManager(false);
            final List<Topic> topics = entityManager.createQuery("SELECT topic FROM Topic").getResultList();
            final List<Response> responses = new ArrayList<Response>();
            for (final Topic topic : topics) {

                final HRef hRef = new HRef(uriInfo.getRequestUriBuilder().path(topic.getId().toString()).build());
                final CreationDate creationDate = new CreationDate(topic.getTopicTimeStamp());
                final GetLastModified getLastModified = new GetLastModified(topic.getLastModifiedDate());
                final GetContentType getContentType = new GetContentType("application/octet-stream");
                final GetContentLength getContentLength = new GetContentLength(topic.getTopicXML().length());
                final DisplayName displayName = new DisplayName(topic.getId().toString());
                final Prop prop = new Prop(creationDate, getLastModified, getContentType, getContentLength, displayName);
                final Status status = new Status((StatusType) OK);
                final PropStat propStat = new PropStat(prop, status);

                final Response davFile = new Response(hRef, null, null, null, propStat);

                responses.add(davFile);
            }

            final MultiStatus st = new MultiStatus(responses.toArray(new Response[responses.size()]));

            return javax.ws.rs.core.Response.status(207).entity(st).type("application/xml;charset=UTF-8").build();

        } catch (final Exception ex) {
            return javax.ws.rs.core.Response.status(500).build();
        }
    }

    @Override
    public javax.ws.rs.core.Response proppatch() {
        return null;
    }

    @Override
    public javax.ws.rs.core.Response copy() {
        return null;
    }

    @Override
    public javax.ws.rs.core.Response move(@Context UriInfo uriInfo, @HeaderParam(OVERWRITE) String overwriteStr, @HeaderParam(DESTINATION) String destination) throws URISyntaxException {
        return null;
    }

    @Override
    public javax.ws.rs.core.Response delete() {
        return null;
    }

    @Override
    public Object findResource(@PathParam("resource") String res) {
        return null;
    }

    @Override
    public javax.ws.rs.core.Response options() {
        javax.ws.rs.core.Response.ResponseBuilder builder = javax.ws.rs.core.Response.ok();
        builder.header(DAV, WEBDAV_COMPLIANCE_LEVEL);

        return builder.build();
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
