package org.jboss.pressgang.ccms.restserver.webdav.topics;

import net.java.dev.webdav.jaxrs.methods.PROPFIND;
import static net.java.dev.webdav.jaxrs.xml.properties.ResourceType.COLLECTION;
import net.java.dev.webdav.jaxrs.xml.elements.*;
import net.java.dev.webdav.jaxrs.xml.elements.Response;
import net.java.dev.webdav.jaxrs.xml.properties.*;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.restserver.webdav.WebDavConstants;
import org.jboss.pressgang.ccms.restserver.webdav.WebDavResource;
import org.jboss.pressgang.ccms.restserver.webdav.topics.topic.WebDavTopic;
import org.jboss.pressgang.ccms.restserver.webdav.topics.topic.fields.WebDavTopicContent;
import org.jboss.pressgang.ccms.restserver.webdav.WebDavUtils;

import static javax.ws.rs.core.HttpHeaders.CONTENT_LENGTH;
import static net.java.dev.webdav.jaxrs.Headers.DEPTH;
import static javax.ws.rs.core.Response.Status.OK;

import javax.ws.rs.core.*;

import javax.persistence.EntityManager;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
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
    The virtual folder that holds all the topics.
 */
@Path("/webdav/TOPICS")
public class TopicVirtualFolder extends WebDavResource {

    private static final String RESOURCE_NAME = "TOPICS";
    private static final Logger LOGGER = Logger.getLogger(TopicVirtualFolder.class.getName());

    @Override
    @Produces(MediaType.APPLICATION_XML)
    @PROPFIND
    public javax.ws.rs.core.Response propfind(@Context final UriInfo uriInfo, @HeaderParam(DEPTH) final int depth, final InputStream entityStream,
                                              @HeaderParam(CONTENT_LENGTH) final long contentLength, @Context final Providers providers,
                                              @Context final HttpHeaders httpHeaders) throws URISyntaxException, IOException {
        try {
            LOGGER.info("ENTER TopicVirtualFolder.propfind()");

            if (depth == 0) {
                LOGGER.info("Depth == 0");
                /* A depth of zero means we are returning information about this item only */
                return javax.ws.rs.core.Response.status(207).entity(new MultiStatus(getProperties(uriInfo))).type(WebDavConstants.XML_MIME).build();
            } else {
                LOGGER.info("Depth != 0");
                /* Otherwise we are retuning info on the children in this collection */
                final EntityManager entityManager = WebDavUtils.getEntityManager(false);
                final List<Topic> topics = entityManager.createQuery("SELECT topic FROM Topic topic").getResultList();
                final List<net.java.dev.webdav.jaxrs.xml.elements.Response> responses = new ArrayList<net.java.dev.webdav.jaxrs.xml.elements.Response>();
                for (final Topic topic : topics) {
                    responses.add(WebDavTopic.getProperties(uriInfo, topic.getTopicId()));
                }

                final MultiStatus st = new MultiStatus(responses.toArray(new net.java.dev.webdav.jaxrs.xml.elements.Response[responses.size()]));

                return javax.ws.rs.core.Response.status(207).entity(st).type(WebDavConstants.XML_MIME).build();
            }

        } catch (final Exception ex) {
            LOGGER.severe(ex.toString());
            ex.printStackTrace();
            return javax.ws.rs.core.Response.status(500).build();
        }
    }

    public static Response getProperties(final UriInfo uriInfo) {
        final URI uri = uriInfo.getRequestUriBuilder().path(RESOURCE_NAME).build();
        final HRef hRef = new HRef(uri);
        final Date lastModified = new Date();
        final CreationDate creationDate = new CreationDate(lastModified);
        final GetLastModified getLastModified = new GetLastModified(lastModified);
        final Status status = new Status((javax.ws.rs.core.Response.StatusType) OK);
        final Prop prop = new Prop(creationDate, getLastModified, COLLECTION);
        final PropStat propStat = new PropStat(prop, status);

        final Response folder = new Response(hRef, null, null, null, propStat);

        return folder;
    }

}
