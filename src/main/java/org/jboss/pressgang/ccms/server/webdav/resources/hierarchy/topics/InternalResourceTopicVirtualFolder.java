package org.jboss.pressgang.ccms.server.webdav.resources.hierarchy.topics;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import net.java.dev.webdav.jaxrs.xml.elements.MultiStatus;
import net.java.dev.webdav.jaxrs.xml.elements.Response;
import org.jboss.pressgang.ccms.server.utils.JNDIUtilities;
import org.jboss.pressgang.ccms.server.webdav.managers.CompatibilityManager;
import org.jboss.pressgang.ccms.server.webdav.resources.InternalResource;
import org.jboss.pressgang.ccms.server.webdav.resources.MultiStatusReturnValue;
import org.jboss.pressgang.ccms.server.webdav.utils.MathUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a folder that can hold topics.
 */
public class InternalResourceTopicVirtualFolder extends InternalResource {
    public static final String RESOURCE_NAME = "TOPICS";
    private static final Logger LOGGER = LoggerFactory.getLogger(InternalResourceTopicVirtualFolder.class.getName());

    public InternalResourceTopicVirtualFolder(@NotNull final UriInfo uriInfo, @NotNull final CompatibilityManager compatibilityManager,
            @Nullable final String remoteAddress, @NotNull final String stringId) {
        super(uriInfo, compatibilityManager, remoteAddress, stringId);
    }

    @Override
    public MultiStatusReturnValue propfind(final String depth) {
        LOGGER.debug("ENTER InternalResourceTopicVirtualFolder.propfind() " + depth + " " + getStringId());

        if (getUriInfo() == null) {
            throw new IllegalStateException("Can not perform propfind without uriInfo");
        }

        if ("0".equals(depth)) {
            /* A depth of zero means we are returning information about this item only */
            return new MultiStatusReturnValue(207, new MultiStatus(getFolderProperties(getUriInfo())));
        } else {
            /* Otherwise we are retuning info on the children in this collection */

            EntityManagerFactory entityManagerFactory = null;
            EntityManager entityManager = null;

            try {

                final Matcher matcher = InternalResource.TOPIC_FOLDER_RE.matcher(getStringId());

                if (!matcher.matches()) {
                    throw new IllegalStateException("This regex should always match");
                }

                /* var is an optional match */
                final String var = matcher.group("var");

                entityManagerFactory = JNDIUtilities.lookupJBossEntityManagerFactory();
                entityManager = entityManagerFactory.createEntityManager();

                final Integer minId = entityManager.createQuery("SELECT MIN(topic.topicId) FROM Topic topic",
                        Integer.class).getSingleResult();
                final Integer maxId = entityManager.createQuery("SELECT MAX(topic.topicId) FROM Topic topic",
                        Integer.class).getSingleResult();

                int maxScale = Math.abs(minId) > maxId ? Math.abs(minId) : maxId;

                /* find out how large is the largest (or smallest) topic id, logarithmicaly speaking */
                final int zeros = MathUtils.getScale(maxScale);

                Integer lastPath = null;

                if (!(var == null || var.isEmpty())) {
                    final String[] varElements = var.split("/");
                    StringBuilder path = new StringBuilder();
                    for (final String varElement : varElements) {
                        path.append(varElement);
                    }
                    lastPath = Integer.parseInt(path.toString());
                }

                final int thisPathZeros = lastPath == null ? 0 : MathUtils.getScale(lastPath);

                /* we've gone too deep */
                if (thisPathZeros > zeros) {
                    return new MultiStatusReturnValue(javax.ws.rs.core.Response.Status.NOT_FOUND.getStatusCode());
                }

                /* the response collection */
                final List<Response> responses = new ArrayList<Response>();
                responses.add(getFolderProperties(getUriInfo()));

                /*
                    The only purpose of the directory /TOPICS/0 is to list TOPIC0.
                    Also don't list subdirectories when there is no way topics
                    could live in them.
                */
                if (lastPath == null || (lastPath != 0 && thisPathZeros < zeros)) {
                    for (int i = 0; i < 10; ++i) {
                        responses.add(getFolderProperties(getUriInfo(), i + ""));
                    }
                }

                /* The top level of the directory structure just lists digits, not an actual topic */
                if (lastPath != null) {
                    responses.add(getFolderProperties(getUriInfo(), "TOPIC" + lastPath.toString()));
                }

                final MultiStatus st = new MultiStatus(
                        responses.toArray(new net.java.dev.webdav.jaxrs.xml.elements.Response[responses.size()]));

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
