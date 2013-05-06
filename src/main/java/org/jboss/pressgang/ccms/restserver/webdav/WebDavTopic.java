package org.jboss.pressgang.ccms.restserver.webdav;

import net.java.dev.webdav.jaxrs.xml.elements.*;
import net.java.dev.webdav.jaxrs.xml.elements.Response;
import net.java.dev.webdav.jaxrs.xml.properties.*;
import org.jboss.pressgang.ccms.model.Topic;

import static javax.ws.rs.core.HttpHeaders.CONTENT_LENGTH;
import static net.java.dev.webdav.jaxrs.Headers.DEPTH;
import static net.java.dev.webdav.jaxrs.Headers.DESTINATION;
import static net.java.dev.webdav.jaxrs.Headers.OVERWRITE;
import static javax.ws.rs.core.Response.Status.OK;
import static net.java.dev.webdav.jaxrs.Headers.DAV;


import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Providers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.OK;
import static net.java.dev.webdav.jaxrs.Headers.DAV;

/**
    A WebDAV representation of a topic.
*/
@Path("/webdav/TOPICS")
public class WebDavTopic extends WebDavResource {

    private static final Logger LOGGER = Logger.getLogger(WebDavTopic.class.getName());

    public static Response getProperties(final UriInfo uriInfo, final Topic topic) {
        final HRef hRef = new HRef(uriInfo.getRequestUriBuilder().path(topic.getId().toString()).build());
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

    @Override
    @GET
    @Produces(WebDavConstants.OCTET_STREAM_MIME)
    public javax.ws.rs.core.Response get(@Context final UriInfo uriInfo) {
        LOGGER.info("ENTER WebDavTopic.get()");
        final Topic topic = getTopic(uriInfo);
        if (topic != null) {
            return javax.ws.rs.core.Response.ok().entity(topic.getTopicXML()).build();
        }

        return javax.ws.rs.core.Response.status(404).build();
    }

    @Override
    public javax.ws.rs.core.Response propfind(@Context UriInfo uriInfo, @HeaderParam(DEPTH) int depth, InputStream entityStream, @HeaderParam(CONTENT_LENGTH) long contentLength, @Context Providers providers, @Context HttpHeaders httpHeaders) throws URISyntaxException, IOException {
        LOGGER.info("ENTER WebDavTopic.propfind()");
        final Topic topic = getTopic(uriInfo);
        if (topic != null) {
            final Response response = getProperties(uriInfo, topic);
            final MultiStatus st = new MultiStatus(response);
            return javax.ws.rs.core.Response.status(207).entity(st).type("application/xml;charset=UTF-8").build();
        }

        return javax.ws.rs.core.Response.status(404).build();
    }

    @Path("{topicid}")
    private final Topic getTopic(final UriInfo uriInfo) {
        final String path = uriInfo.getAbsolutePath().toString();
        final int indexOfSlash =  path.lastIndexOf("/");

        if (indexOfSlash != -1) {
            final String topicIdString =  path.substring(indexOfSlash);
            try {
                final Integer topicId = Integer.parseInt(topicIdString);
                final EntityManager entityManager = WebDavUtils.getEntityManager(false);

                return entityManager.find(Topic.class, topicId);

            } catch (final NumberFormatException ex) {
                return null;
            }
        }

        return null;
    }
}
