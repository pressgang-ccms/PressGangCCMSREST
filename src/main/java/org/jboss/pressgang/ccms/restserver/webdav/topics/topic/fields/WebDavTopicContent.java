package org.jboss.pressgang.ccms.restserver.webdav.topics.topic.fields;

import net.java.dev.webdav.jaxrs.xml.elements.*;
import net.java.dev.webdav.jaxrs.xml.elements.Response;
import net.java.dev.webdav.jaxrs.xml.properties.*;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.restserver.webdav.WebDavConstants;
import org.jboss.pressgang.ccms.restserver.webdav.WebDavResource;
import org.jboss.pressgang.ccms.restserver.webdav.WebDavUtils;

import static javax.ws.rs.core.HttpHeaders.CONTENT_LENGTH;
import static net.java.dev.webdav.jaxrs.Headers.DEPTH;
import static javax.ws.rs.core.Response.Status.OK;


import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Providers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.logging.Logger;

/**
 A WebDAV representation of a topic.
 */
@Path("{var:.*}/{topicId: \\d+}/CONTENT")
public class WebDavTopicContent extends WebDavResource {

    private static final Logger LOGGER = Logger.getLogger(WebDavTopicContent.class.getName());

    @PathParam("topicId") int topicId;

    @GET
    @Produces(WebDavConstants.OCTET_STREAM_MIME)
    public javax.ws.rs.core.Response get(@PathParam("topicId") final int topicId) {
        LOGGER.info("ENTER WebDavTopic.get()");

        try {
            final EntityManager entityManager = WebDavUtils.getEntityManager(false);

            final Topic topic = entityManager.find(Topic.class, topicId);

            if (topic != null) {
                return javax.ws.rs.core.Response.ok().entity(topic.getTopicXML()).build();
            }

        } catch (final NumberFormatException ex) {
            return javax.ws.rs.core.Response.status(404).build();
        }

        return javax.ws.rs.core.Response.status(404).build();
    }

    @Override
    public javax.ws.rs.core.Response propfind(@Context UriInfo uriInfo, @HeaderParam(DEPTH) int depth, InputStream entityStream, @HeaderParam(CONTENT_LENGTH) long contentLength, @Context Providers providers, @Context HttpHeaders httpHeaders) throws URISyntaxException, IOException {
        LOGGER.info("ENTER WebDavTopic.propfind()");

        try {
            final EntityManager entityManager = WebDavUtils.getEntityManager(false);

            final Topic topic = entityManager.find(Topic.class, topicId);

            if (topic != null) {
                final Response response = getProperties(uriInfo, topic);
                final MultiStatus st = new MultiStatus(response);
                return javax.ws.rs.core.Response.status(207).entity(st).type("application/xml;charset=UTF-8").build();
            }

        } catch (final NumberFormatException ex) {
            return javax.ws.rs.core.Response.status(404).build();
        }

        return javax.ws.rs.core.Response.status(404).build();
    }

    public static Response getProperties(final UriInfo uriInfo, final Topic topic) {
        final HRef hRef = new HRef(uriInfo.getRequestUriBuilder().path("CONTENT").build());
        final CreationDate creationDate = new CreationDate(topic.getTopicTimeStamp() == null ? new Date() : topic.getTopicTimeStamp());
        final GetLastModified getLastModified = new GetLastModified(topic.getLastModifiedDate() == null ? new Date() : topic.getLastModifiedDate());
        final GetContentType getContentType = new GetContentType(WebDavConstants.OCTET_STREAM_MIME);
        final GetContentLength getContentLength = new GetContentLength(topic.getTopicXML() == null ? 0 : topic.getTopicXML().length());
        final DisplayName displayName = new DisplayName(topic.getId().toString());
        final Prop prop = new Prop(creationDate, getLastModified, getContentType, getContentLength, displayName);
        final Status status = new Status((javax.ws.rs.core.Response.StatusType) OK);
        final PropStat propStat = new PropStat(prop, status);

        final Response davFile = new Response(hRef, null, null, null, propStat);

        return davFile;
    }
}
