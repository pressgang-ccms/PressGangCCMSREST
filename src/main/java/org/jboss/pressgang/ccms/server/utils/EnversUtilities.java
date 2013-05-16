package org.jboss.pressgang.ccms.server.utils;

import javax.persistence.EntityManager;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.server.envers.LoggingRevisionEntity;

public class EnversUtilities extends org.jboss.pressgang.ccms.model.utils.EnversUtilities {

    public static <T extends AuditedEntity> LoggingRevisionEntity getRevisionEntity(final EntityManager entityManager, final T entity,
            final Number revision) {
        final AuditReader reader = AuditReaderFactory.get(entityManager);
        if (revision == null) {
            return reader.findRevision(LoggingRevisionEntity.class, getLatestRevision(entityManager, entity));
        } else {
            return reader.findRevision(LoggingRevisionEntity.class, revision);
        }
    }

    public static <T extends AuditedEntity> String getLogMessage(final EntityManager entityManager, final T entity, final Number revision) {
        final LoggingRevisionEntity revEntity = getRevisionEntity(entityManager, entity, revision);
        return revEntity == null ? null : revEntity.getLogMessage();
    }

    public static <T extends AuditedEntity> Integer getLogFlag(final EntityManager entityManager, final T entity, final Number revision) {
        final LoggingRevisionEntity revEntity = getRevisionEntity(entityManager, entity, revision);
        return revEntity == null ? null : revEntity.getLogFlag();
    }

    public static <T extends AuditedEntity> String getLogUsername(final EntityManager entityManager, final T entity,
            final Number revision) {
        final LoggingRevisionEntity revEntity = getRevisionEntity(entityManager, entity, revision);
        return revEntity == null ? null : revEntity.getUserName();
    }
}