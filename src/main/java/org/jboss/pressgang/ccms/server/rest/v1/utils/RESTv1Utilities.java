package org.jboss.pressgang.ccms.server.rest.v1.utils;

import javax.persistence.EntityManager;
import java.util.Map;

import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;

public class RESTv1Utilities {

    public static <U extends AuditedEntity, T extends RESTBaseEntityV1<T, ?, ?>> U findEntity(final EntityManager entityManager,
            final Map<RESTBaseEntityV1<?, ?, ?>, AuditedEntity> newEntityCache, final T restEntity, final Class<U> databaseClass) {
        if (restEntity.getId() == null) {
            // If the id is null than there is no possible way a matching entity could be found, so return null
            return null;
        } else if (restEntity.getId() < 0 && newEntityCache.containsKey(restEntity)) {
            // If the id is less than zero than it is a new entity, so get the matching entity from the cache
            return (U) newEntityCache.get(restEntity);
        } else {
            // At this stage the id will be positive, so get the entity from the Persistence Context
            return entityManager.find(databaseClass, restEntity.getId());
        }
    }
}
