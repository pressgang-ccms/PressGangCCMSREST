package org.jboss.pressgang.ccms.restserver.webdav;

import net.java.dev.webdav.jaxrs.methods.PROPFIND;
import static net.java.dev.webdav.jaxrs.xml.properties.ResourceType.COLLECTION;
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
import static net.java.dev.webdav.jaxrs.Headers.DAV;

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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
    The root of the WebDAV server.
 */
@Path("/webdav")
public class WebDavRoot implements WebDavResource {

    private static final Logger LOGGER = Logger.getLogger(WebDavRoot.class.getName());

    public WebDavRoot() {
        LOGGER.info("ENTER WebDavRoot()");
    }

    @Override
    public javax.ws.rs.core.Response get(@Context final UriInfo uriInfo) {
        LOGGER.info("ENTER WebDavRoot.get()");
        return javax.ws.rs.core.Response.status(404).build();
    }

    @Override
    public javax.ws.rs.core.Response put(@Context UriInfo uriInfo, InputStream entityStream, @HeaderParam(CONTENT_LENGTH) long contentLength) throws IOException, URISyntaxException {
        LOGGER.info("ENTER WebDavRoot.put()");
        return javax.ws.rs.core.Response.status(404).build();
    }

    @Override
    public javax.ws.rs.core.Response mkcol() {
        return javax.ws.rs.core.Response.status(404).build();
    }

    @Override
    @Produces("application/xml")
    @PROPFIND
    public javax.ws.rs.core.Response propfind(@Context final UriInfo uriInfo, @HeaderParam(DEPTH) final int depth,
                                              final InputStream entityStream, @HeaderParam(CONTENT_LENGTH) final long contentLength,
                                              @Context final Providers providers, @Context final HttpHeaders httpHeaders) throws URISyntaxException, IOException {
        try {
            LOGGER.info("ENTER WebDavRoot.propfind()");

            if (depth == 0) {
                /* A depth of zero means we are returning information about this item only */
                final URI uri = uriInfo.getRequestUri();
                final HRef hRef = new HRef(uri);
                final Date lastModified = new Date();
                final CreationDate creationDate = new CreationDate(lastModified);
                final GetLastModified getLastModified = new GetLastModified(lastModified);
                final Status status = new Status((StatusType) OK);
                final Prop prop = new Prop(creationDate,getLastModified, COLLECTION);
                final PropStat propStat = new PropStat(prop, status);

                final Response folder = new Response(hRef, null, null, null, propStat);

                return javax.ws.rs.core.Response.status(207).entity(new MultiStatus(folder)).type(WebDavConstants.XML_MIME).build();
            } else {
                /* Otherwise we are retuning info on the children in this collection */
                final EntityManager entityManager = WebDavUtils.getEntityManager(false);
                final List<Topic> topics = entityManager.createQuery("SELECT topic FROM Topic").getResultList();
                final List<Response> responses = new ArrayList<Response>();
                for (final Topic topic : topics) {
                    responses.add(WebDavTopic.getProperties(uriInfo, topic));
                }

                final MultiStatus st = new MultiStatus(responses.toArray(new Response[responses.size()]));

                return javax.ws.rs.core.Response.status(207).entity(st).type(WebDavConstants.XML_MIME).build();
            }

        } catch (final Exception ex) {
            return javax.ws.rs.core.Response.status(500).build();
        }
    }

    @Override
    public javax.ws.rs.core.Response proppatch() {
        LOGGER.info("ENTER WebDavRoot.proppatch()");
        return javax.ws.rs.core.Response.status(404).build();
    }

    @Override
    public javax.ws.rs.core.Response copy() {
        LOGGER.info("ENTER WebDavRoot.copy()");
        return javax.ws.rs.core.Response.status(404).build();
    }

    @Override
    public javax.ws.rs.core.Response move(@Context UriInfo uriInfo, @HeaderParam(OVERWRITE) String overwriteStr, @HeaderParam(DESTINATION) String destination) throws URISyntaxException {
        LOGGER.info("ENTER WebDavRoot.move()");
        return javax.ws.rs.core.Response.status(404).build();
    }

    @Override
    public javax.ws.rs.core.Response delete() {
        LOGGER.info("ENTER WebDavRoot.delete()");
        return javax.ws.rs.core.Response.status(404).build();
    }

    @Override
    public javax.ws.rs.core.Response options() {
        LOGGER.info("ENTER WebDavRoot.options()");
        javax.ws.rs.core.Response.ResponseBuilder builder = javax.ws.rs.core.Response.ok();
        builder.header(DAV, WEBDAV_COMPLIANCE_LEVEL);

        return builder.build();
    }



}
