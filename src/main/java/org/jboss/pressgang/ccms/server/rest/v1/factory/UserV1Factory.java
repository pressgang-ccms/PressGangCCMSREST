/*
  Copyright 2011-2014 Red Hat

  This file is part of PresGang CCMS.

  PresGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PresGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PresGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.server.rest.v1.factory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.Role;
import org.jboss.pressgang.ccms.model.User;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTRoleCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTUserCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTRoleCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTUserCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTRoleV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTUserV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class UserV1Factory extends RESTEntityFactory<RESTUserV1, User, RESTUserCollectionV1, RESTUserCollectionItemV1> {
    @Inject
    protected RoleV1Factory roleFactory;

    @Override
    public RESTUserV1 createRESTEntityFromDBEntityInternal(final User entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTUserV1 retValue = new RESTUserV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTUserV1.ROLES_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);

        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setId(entity.getUserId());
        retValue.setName(entity.getUserName());
        retValue.setDescription(entity.getDescription());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTTopicV1.REVISIONS_NAME)) {
            retValue.setRevisions(
                    RESTEntityCollectionFactory.create(RESTUserCollectionV1.class, this, entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        // ROLES
        if (expand != null && expand.contains(RESTUserV1.ROLES_NAME)) {
            retValue.setRoles(
                    RESTEntityCollectionFactory.create(RESTRoleCollectionV1.class, roleFactory, entity.getRoles(), RESTUserV1.ROLES_NAME,
                            dataType, expand, baseUrl, revision, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.USER_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntityFirstPass(final User entity, final RESTUserV1 dataObject) {
        if (dataObject.hasParameterSet(RESTUserV1.DESCRIPTION_NAME))
            entity.setDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTUserV1.NAME_NAME))
            entity.setUserName(dataObject.getName());
    }

    @Override
    public void syncDBEntityWithRESTEntitySecondPass(User entity, RESTUserV1 dataObject) {
        // Many to Many
        if (dataObject.hasParameterSet(
                RESTUserV1.ROLES_NAME) && dataObject.getRoles() != null && dataObject.getRoles().getItems() != null) {
            dataObject.getRoles().removeInvalidChangeItemRequests();

            for (final RESTRoleCollectionItemV1 restEntityItem : dataObject.getRoles().getItems()) {
                final RESTRoleV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsRemoveItem()) {
                    final Role dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, restEntity, Role.class);
                    if (dbEntity == null)
                        throw new BadRequestException("No entity was found with the primary key " + restEntity.getId());

                    if (restEntityItem.returnIsAddItem()) {
                        entity.addRole(dbEntity);
                    } else if (restEntityItem.returnIsRemoveItem()) {
                        entity.removeRole(dbEntity);
                    }
                }
            }
        }
    }

    @Override
    protected Class<User> getDatabaseClass() {
        return User.class;
    }
}
