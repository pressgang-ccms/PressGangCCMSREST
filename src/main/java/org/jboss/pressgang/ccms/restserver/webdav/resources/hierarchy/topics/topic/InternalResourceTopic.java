package org.jboss.pressgang.ccms.restserver.webdav.resources.hierarchy.topics.topic;

import net.java.dev.webdav.jaxrs.xml.elements.MultiStatus;
import net.java.dev.webdav.jaxrs.xml.elements.Response;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.restserver.utils.EnversUtilities;
import org.jboss.pressgang.ccms.restserver.utils.JNDIUtilities;
import org.jboss.pressgang.ccms.restserver.webdav.constants.WebDavConstants;
import org.jboss.pressgang.ccms.restserver.webdav.utils.WebDavUtils;
import org.jboss.pressgang.ccms.restserver.webdav.resources.InternalResource;
import org.jboss.pressgang.ccms.restserver.webdav.resources.MultiStatusReturnValue;
import org.jboss.pressgang.ccms.restserver.webdav.managers.DeleteManager;
import org.jboss.pressgang.ccms.restserver.webdav.managers.ResourceTypes;
import org.jboss.pressgang.ccms.restserver.webdav.resources.hierarchy.topics.topic.fields.InternalResourceTempTopicFile;
import org.jboss.pressgang.ccms.restserver.webdav.resources.hierarchy.topics.topic.fields.InternalResourceTopicContent;
import org.jboss.resteasy.spi.InternalServerErrorException;

import javax.annotation.Nullable;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the available fields and temporary files associated with a topic.
 */
public class InternalResourceTopic extends InternalResource {
    public InternalResourceTopic(@NotNull final UriInfo uriInfo, @NotNull final DeleteManager deleteManager, @Nullable final String remoteAddress, @NotNull final Integer intId) {
        super(uriInfo, deleteManager, remoteAddress, intId);
    }

    @Override
    public MultiStatusReturnValue propfind(final int depth) {

        if (getUriInfo() == null) {
            throw new IllegalStateException("Can not perform propfind without uriInfo");
        }

        if (depth == 0) {
            /* A depth of zero means we are returning information about this item only */

            return new MultiStatusReturnValue(207, new MultiStatus(getFolderProperties(getUriInfo())));
        } else {
            EntityManagerFactory entityManagerFactory = null;
            EntityManager entityManager = null;

            try {

                /* Otherwise we are retuning info on the children in this collection */
                entityManagerFactory = JNDIUtilities.lookupJBossEntityManagerFactory();
                entityManager = entityManagerFactory.createEntityManager();

                final Topic topic = entityManager.find(Topic.class, getIntId());

                final List<Response> responses = new ArrayList<Response>();

                /*
                    List the field of the topic
                 */
                if (topic != null) {
                    /* Fix the last modified date */
                    topic.setLastModifiedDate(EnversUtilities.getFixedLastModifiedDate(entityManager, topic));

                    /* Don't list the contents if it is "deleted" */
                    if (!getDeleteManager().isDeleted(ResourceTypes.TOPIC_CONTENTS, getRemoteAddress(), topic.getId())) {
                        responses.add(InternalResourceTopicContent.getProperties(getUriInfo(), topic, false));
                    }
                }

                final File dir = new File(WebDavConstants.TEMP_LOCATION);
                final String tempFileNamePrefix = InternalResourceTempTopicFile.buildTempFileName(getUriInfo().getPath());
                if (dir.exists() && dir.isDirectory()) {
                    for (final File child : dir.listFiles()) {
                        if (child.getPath().startsWith(tempFileNamePrefix) && InternalResourceTempTopicFile.exists(child)) {
                            responses.add(InternalResourceTempTopicFile.getProperties(getUriInfo(), child, false));
                        }
                    }
                }

                final MultiStatus st = new MultiStatus(responses.toArray(new Response[responses.size()]));
                return new MultiStatusReturnValue(207, st);
            } catch (NamingException e) {
                return new MultiStatusReturnValue(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
            } finally {
                if (entityManager != null) {
                    entityManager.close();
                }
            }

        }
    }
}
