package org.jboss.pressgang.ccms.server.rest.v1.factory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;

import org.jboss.pressgang.ccms.model.User;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTLogDetailsV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.envers.LoggingRevisionEntity;
import org.jboss.pressgang.ccms.server.utils.EntityManagerWrapper;
import org.jboss.pressgang.ccms.server.utils.EntityUtilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;

@ApplicationScoped
public class LogDetailsV1Factory {
    @Inject
    protected EntityManagerWrapper entityManager;
    @Inject
    protected UserV1Factory userFactory;

    public <T extends AuditedEntity> RESTLogDetailsV1 create(final T entity, final Number revision, final String expandName,
            final ExpandDataTrunk expand, final String dataType, final String baseUrl) {
        if (expand != null && expand.get(expandName) != null) {
            final LoggingRevisionEntity revisionEntity = EnversUtilities.getRevisionEntity(entityManager, entity, revision);

            return createRESTEntityFromDBEntity(revisionEntity, expand, dataType, baseUrl);
        } else {
            return null;
        }
    }

    public RESTLogDetailsV1 createRESTEntityFromDBEntity(final LoggingRevisionEntity revisionEntity, final ExpandDataTrunk expand,
            final String dataType, final String baseUrl) {

        final RESTLogDetailsV1 retValue = new RESTLogDetailsV1();

        retValue.setMessage(revisionEntity.getLogMessage());
        retValue.setFlag(revisionEntity.getLogFlag());
        retValue.setDate(new Date(revisionEntity.getTimestamp()));

        final User user = EntityUtilities.getUserFromUsername(entityManager, revisionEntity.getUserName());
        if (user != null) {
            retValue.setUser(userFactory.createRESTEntityFromDBEntity(user, baseUrl, dataType, expand));
        }

        return retValue;
    }
}
