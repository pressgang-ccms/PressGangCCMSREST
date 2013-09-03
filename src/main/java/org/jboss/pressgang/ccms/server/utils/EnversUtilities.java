package org.jboss.pressgang.ccms.server.utils;

import javax.persistence.EntityManager;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
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

    public static <T extends AuditedEntity> Number getClosestRevision(final EntityManager entityManager, final T entity,
            final Number revision) {
        return getClosestRevision(entityManager, entity.getClass(), entity.getId(), revision);
    }

    public static <T extends AuditedEntity> Number getClosestRevision(final EntityManager entityManager, final Class<T> entityClass,
            final Integer id, final Number revision) {
        final AuditReader reader = AuditReaderFactory.get(entityManager);
        return getClosestRevision(reader, entityClass, id, revision);
    }

    public static <T extends AuditedEntity> Number getClosestRevision(final AuditReader reader, final Class<T> entityClass,
            final Integer id, final Number revision) {
        // Find the closest revision that is less than or equal to the revision specified.
        final Number closestRevision = (Number) reader.createQuery().forRevisionsOfEntity(entityClass, false, true).addProjection(
                AuditEntity.revisionNumber().max()).add(AuditEntity.id().eq(id)).add(
                AuditEntity.revisionNumber().le(revision)).getSingleResult();
        return closestRevision;
    }
}