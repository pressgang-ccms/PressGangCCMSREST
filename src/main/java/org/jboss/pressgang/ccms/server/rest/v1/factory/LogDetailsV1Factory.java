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

    public <T extends AuditedEntity> RESTLogDetailsV1 createRESTEntityFromAuditedEntity(final T entity, final Number revision,
            final String expandName, final ExpandDataTrunk expand, final String dataType, final String baseUrl) {
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
