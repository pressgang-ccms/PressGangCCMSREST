package org.jboss.pressgang.ccms.server.webdav.resources.hierarchy.topics.topic.fields;

import static javax.ws.rs.core.Response.Status.OK;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.TransactionManager;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Calendar;
import java.util.Date;

import net.java.dev.webdav.jaxrs.xml.elements.HRef;
import net.java.dev.webdav.jaxrs.xml.elements.MultiStatus;
import net.java.dev.webdav.jaxrs.xml.elements.Prop;
import net.java.dev.webdav.jaxrs.xml.elements.PropStat;
import net.java.dev.webdav.jaxrs.xml.elements.Status;
import net.java.dev.webdav.jaxrs.xml.properties.DisplayName;
import net.java.dev.webdav.jaxrs.xml.properties.FixedCreationDate;
import net.java.dev.webdav.jaxrs.xml.properties.GetContentLength;
import net.java.dev.webdav.jaxrs.xml.properties.GetContentType;
import net.java.dev.webdav.jaxrs.xml.properties.GetLastModified;
import net.java.dev.webdav.jaxrs.xml.properties.LockDiscovery;
import net.java.dev.webdav.jaxrs.xml.properties.SupportedLock;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.server.utils.JNDIUtilities;
import org.jboss.pressgang.ccms.server.webdav.managers.DeleteManager;
import org.jboss.pressgang.ccms.server.webdav.managers.ResourceTypes;
import org.jboss.pressgang.ccms.server.webdav.resources.ByteArrayReturnValue;
import org.jboss.pressgang.ccms.server.webdav.resources.InternalResource;
import org.jboss.pressgang.ccms.server.webdav.resources.MultiStatusReturnValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles access to topic's XML content.
 */
public class InternalResourceTopicContent extends InternalResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(InternalResourceTopicContent.class.getName());

    public InternalResourceTopicContent(@NotNull final UriInfo uriInfo, @NotNull final DeleteManager deleteManager,
            @NotNull final String remoteAddress, @NotNull final Integer intId) {
        super(uriInfo, deleteManager, remoteAddress, intId);
    }

    @Override
    public int write(@NotNull final byte[] contents) {

        final String stringContents = new String(contents);

        LOGGER.debug("ENTER InternalResourceTopicContent.write() " + getIntId());
        LOGGER.debug(stringContents);

        /*
            If we are not actually saving any content, just mark the file as visible and return.
         */
        if (contents.length == 0) {
            getDeleteManager().create(ResourceTypes.TOPIC_CONTENTS, getRemoteAddress(), getIntId());
            return Response.Status.NO_CONTENT.getStatusCode();
        }

        EntityManager entityManager = null;
        EntityManagerFactory entityManagerFactory = null;
        TransactionManager transactionManager = null;

        try {
            transactionManager = JNDIUtilities.lookupJBossTransactionManager();
            transactionManager.begin();

            entityManagerFactory = JNDIUtilities.lookupJBossEntityManagerFactory();
            entityManager = entityManagerFactory.createEntityManager();

            final Topic topic = entityManager.find(Topic.class, getIntId());

            if (topic != null) {

                topic.setTopicXML(stringContents);

                entityManager.persist(topic);
                entityManager.flush();
                transactionManager.commit();

                getDeleteManager().create(ResourceTypes.TOPIC_CONTENTS, getRemoteAddress(), getIntId());

                return Response.Status.NO_CONTENT.getStatusCode();
            }

            return Response.Status.NOT_FOUND.getStatusCode();
        } catch (final Exception ex) {
            if (transactionManager != null) {
                try {
                    transactionManager.rollback();
                } catch (final Exception ex2) {
                    LOGGER.error("There was an error rolling back the transaction " + ex2.toString());
                }
            }

            return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

    }

    @Override
    public ByteArrayReturnValue get() {
        LOGGER.debug("ENTER InternalResourceTopicContent.get() " + getIntId());

        if (getDeleteManager().isDeleted(ResourceTypes.TOPIC_CONTENTS, getRemoteAddress(), getIntId())) {
            LOGGER.debug("Deletion Manager says this file is deleted");
            return new ByteArrayReturnValue(Response.Status.NOT_FOUND.getStatusCode(), null);
        }

        EntityManager entityManager = null;
        EntityManagerFactory entityManagerFactory = null;

        try {
            entityManagerFactory = JNDIUtilities.lookupJBossEntityManagerFactory();
            entityManager = entityManagerFactory.createEntityManager();

            final Topic topic = entityManager.find(Topic.class, getIntId());

            if (topic != null) {
                return new ByteArrayReturnValue(Response.Status.OK.getStatusCode(), topic.getTopicXML().getBytes());
            }

        } catch (final Exception ex) {
            return new ByteArrayReturnValue(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), null);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return new ByteArrayReturnValue(Response.Status.NOT_FOUND.getStatusCode(), null);
    }

    @Override
    public int delete() {
        LOGGER.debug("ENTER InternalResourceTopicContent.delete() " + getIntId() + " " + getRemoteAddress());

        if (getDeleteManager().isDeleted(ResourceTypes.TOPIC_CONTENTS, getRemoteAddress(), getIntId())) {
            LOGGER.debug("Deletion Manager says this file is already deleted");
            return Response.Status.NOT_FOUND.getStatusCode();
        }

        getDeleteManager().delete(ResourceTypes.TOPIC_CONTENTS, getRemoteAddress(), getIntId());

        /* pretend to be deleted */
        return Response.Status.NO_CONTENT.getStatusCode();
    }

    @Override
    public MultiStatusReturnValue propfind(final int depth) {
        if (getUriInfo() == null) {
            throw new IllegalStateException("Can not perform propfind without uriInfo");
        }

        EntityManager entityManager = null;
        EntityManagerFactory entityManagerFactory = null;

        try {
            entityManagerFactory = JNDIUtilities.lookupJBossEntityManagerFactory();
            entityManager = entityManagerFactory.createEntityManager();

            final Topic topic = entityManager.find(Topic.class, getIntId());

            if (topic != null) {
                final net.java.dev.webdav.jaxrs.xml.elements.Response response = getProperties(getDeleteManager(), getRemoteAddress(),
                        getUriInfo(), topic, true);
                final MultiStatus st = new MultiStatus(response);
                return new MultiStatusReturnValue(207, st);
            }

        } catch (final NumberFormatException ex) {
            return new MultiStatusReturnValue(Response.Status.NOT_FOUND.getStatusCode());
        } catch (NamingException e) {
            return new MultiStatusReturnValue(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return new MultiStatusReturnValue(Response.Status.NOT_FOUND.getStatusCode());
    }

    /**
     * @param uriInfo The uri that was used to access this resource
     * @param topic   The topic that this content represents
     * @param local   true if we are building the properties for the resource at the given uri, and false if we are building
     *                properties for a child resource.
     * @return
     */
    public static net.java.dev.webdav.jaxrs.xml.elements.Response getProperties(@NotNull final DeleteManager deleteManager,
            @NotNull final String remoteAddress, @NotNull final UriInfo uriInfo, @NotNull final Topic topic, final boolean local) {

        final Calendar lastCreatedDate = deleteManager.lastCreatedDate(ResourceTypes.TOPIC_CONTENTS, remoteAddress, topic.getId());
        final Calendar lastCreatedDateFixed = lastCreatedDate == null ? Calendar.getInstance() : lastCreatedDate;
        final Date lastModifiedDate = topic.getLastModifiedDate() == null ? new Date() : topic.getLastModifiedDate();
        final GetLastModified getLastModified = new GetLastModified(
                lastCreatedDateFixed.after(lastModifiedDate) ? lastCreatedDateFixed.getTime() : lastModifiedDate);

        final HRef hRef = local ? new HRef(uriInfo.getRequestUri()) : new HRef(
                uriInfo.getRequestUriBuilder().path(topic.getId() + ".xml").build());
        final FixedCreationDate creationDate = new FixedCreationDate(
                topic.getTopicTimeStamp() == null ? new Date() : topic.getTopicTimeStamp());
        final GetContentType getContentType = new GetContentType(MediaType.APPLICATION_OCTET_STREAM);
        final GetContentLength getContentLength = new GetContentLength(topic.getTopicXML() == null ? 0 : topic.getTopicXML().length());
        final DisplayName displayName = new DisplayName(topic.getId() + ".xml");
        final SupportedLock supportedLock = new SupportedLock();
        final LockDiscovery lockDiscovery = new LockDiscovery();
        final Prop prop = new Prop(creationDate, getLastModified, getContentType, getContentLength, displayName, supportedLock,
                lockDiscovery);
        final Status status = new Status((javax.ws.rs.core.Response.StatusType) OK);
        final PropStat propStat = new PropStat(prop, status);

        final net.java.dev.webdav.jaxrs.xml.elements.Response davFile = new net.java.dev.webdav.jaxrs.xml.elements.Response(hRef, null,
                null, null, propStat);

        return davFile;
    }
}
