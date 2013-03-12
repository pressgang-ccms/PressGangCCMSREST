package org.jboss.pressgang.ccms.restserver.rest.v1;

import javax.persistence.EntityManager;
import java.util.Date;

import org.jboss.pressgang.ccms.model.User;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTLogDetailsV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.envers.LoggingRevisionEntity;
import org.jboss.pressgang.ccms.restserver.utils.EntityUtilities;
import org.jboss.pressgang.ccms.restserver.utils.EnversUtilities;

public class LogDetailsV1Factory {

    public <T extends AuditedEntity> RESTLogDetailsV1 create(final T entity, final Number revision, final String expandName,
            final ExpandDataTrunk expand, final String dataType, final String baseUrl, final EntityManager entityManager) {
        if (expand != null && expand.get(expandName) != null) {
            final LoggingRevisionEntity revisionEntity = EnversUtilities.getRevisionEntity(entityManager, entity, revision);

            return createRESTEntityFromDBEntity(revisionEntity, expand, dataType, baseUrl, entityManager);
        } else {
            return null;
        }
    }

    public RESTLogDetailsV1 createRESTEntityFromDBEntity(final LoggingRevisionEntity revisionEntity, final ExpandDataTrunk expand,
            final String dataType, final String baseUrl, final EntityManager entityManager) {

        final RESTLogDetailsV1 retValue = new RESTLogDetailsV1();

        retValue.setMessage(revisionEntity.getLogMessage());
        retValue.setFlag(revisionEntity.getLogFlag());
        retValue.setDate(new Date(revisionEntity.getTimestamp()));

        final User user = EntityUtilities.getUserFromUsername(entityManager, revisionEntity.getUserName());
        if (user != null) {
            retValue.setUser(new UserV1Factory().createRESTEntityFromDBEntity(user, baseUrl, dataType, expand, entityManager));
        }

        return retValue;
    }
}
