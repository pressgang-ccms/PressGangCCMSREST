/*
  Copyright 2011-2014 Red Hat

  This file is part of PressGang CCMS.

  PressGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PressGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PressGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.server.webdav.resources.hierarchy.topics.topic;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.java.dev.webdav.jaxrs.xml.elements.MultiStatus;
import net.java.dev.webdav.jaxrs.xml.elements.Response;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.pressgang.ccms.server.utils.JNDIUtilities;
import org.jboss.pressgang.ccms.server.webdav.constants.WebDavConstants;
import org.jboss.pressgang.ccms.server.webdav.managers.CompatibilityManager;
import org.jboss.pressgang.ccms.server.webdav.managers.ResourceTypes;
import org.jboss.pressgang.ccms.server.webdav.resources.InternalResource;
import org.jboss.pressgang.ccms.server.webdav.resources.MultiStatusReturnValue;
import org.jboss.pressgang.ccms.server.webdav.resources.hierarchy.topics.topic.fields.InternalResourceTempTopicFile;
import org.jboss.pressgang.ccms.server.webdav.resources.hierarchy.topics.topic.fields.InternalResourceTopicContent;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the available fields and temporary files associated with a topic.
 */
public class InternalResourceTopic extends InternalResource {
    public InternalResourceTopic(@NotNull final UriInfo uriInfo, @NotNull final CompatibilityManager compatibilityManager,
            @Nullable final String remoteAddress, @NotNull final Integer intId) {
        super(uriInfo, compatibilityManager, remoteAddress, intId);
    }

    @Override
    public MultiStatusReturnValue propfind(final String depth) {
        if (getUriInfo() == null) {
            throw new IllegalStateException("Can not perform propfind without uriInfo");
        }

        if ("0".equals(depth)) {
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
                responses.add(getFolderProperties(getUriInfo()));

                /*
                    List the field of the topic
                 */
                if (topic != null) {
                    /* Fix the last modified date */
                    topic.setLastModifiedDate(EnversUtilities.getFixedLastModifiedDate(entityManager, topic));

                    /* Don't list the contents if it is "deleted" */
                    if (!getCompatibilityManager().isDeleted(ResourceTypes.TOPIC_CONTENTS, getRemoteAddress(), topic.getId())) {
                        responses.add(
                                InternalResourceTopicContent.getProperties(getCompatibilityManager(), getRemoteAddress(), getUriInfo(), topic,
                                        false));
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
