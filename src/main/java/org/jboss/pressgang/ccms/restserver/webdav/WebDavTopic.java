package org.jboss.pressgang.ccms.restserver.webdav;

import net.java.dev.webdav.jaxrs.xml.elements.*;
import net.java.dev.webdav.jaxrs.xml.properties.*;
import org.jboss.pressgang.ccms.model.Topic;


import javax.ws.rs.core.UriInfo;

import static javax.ws.rs.core.Response.Status.OK;

/**
    A WebDAV representation of a topic.
*/
public class WebDavTopic implements WebDavEntity {

    private final Topic topic;

    public WebDavTopic(final Topic topic) {
        this.topic = topic;
    }

    @Override
    public Response getProperties(final UriInfo uriInfo) {
        final HRef hRef = new HRef(uriInfo.getRequestUriBuilder().path(topic.getId().toString()).build());
        final CreationDate creationDate = new CreationDate(topic.getTopicTimeStamp());
        final GetLastModified getLastModified = new GetLastModified(topic.getLastModifiedDate());
        final GetContentType getContentType = new GetContentType(WebDavConstants.OCTET_STREAM_MIME);
        final GetContentLength getContentLength = new GetContentLength(topic.getTopicXML().length());
        final DisplayName displayName = new DisplayName(topic.getId().toString());
        final Prop prop = new Prop(creationDate, getLastModified, getContentType, getContentLength, displayName);
        final Status status = new Status((javax.ws.rs.core.Response.StatusType) OK);
        final PropStat propStat = new PropStat(prop, status);

        final Response davFile = new Response(hRef, null, null, null, propStat);

        return davFile;
    }
}
