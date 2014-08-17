/*
  Copyright 2011-2014 Red Hat, Inc

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
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.Role;
import org.jboss.pressgang.ccms.model.RoleToRoleRelationship;
import org.jboss.pressgang.ccms.model.User;
import org.jboss.pressgang.ccms.model.base.PressGangEntity;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTRoleCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTUserCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTRoleCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTRoleV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTUserV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseAuditedEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.InternalServerErrorException;

/*
 * Note: Since roles and users are going to be re-done soon using Katie's OAuth Library, I've left out the ability to update the
 * RoleToRoleRelationship information and made it a static variable since we only have one definition anyways.
 * 
 * Lee
 */
@ApplicationScoped
public class RoleV1Factory extends RESTEntityFactory<RESTRoleV1, Role, RESTRoleCollectionV1, RESTRoleCollectionItemV1> {
    private static final Integer ROLE_TO_ROLE_ID = 1;

    @Inject
    protected UserV1Factory userFactory;

    @Override
    public RESTRoleV1 createRESTEntityFromDBEntityInternal(final Role entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter topic can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTRoleV1 retValue = new RESTRoleV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTRoleV1.USERS_NAME);
        expandOptions.add(RESTRoleV1.CHILDROLES_NAME);
        expandOptions.add(RESTRoleV1.PARENTROLES_NAME);
        expandOptions.add(RESTBaseAuditedEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseAuditedEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        retValue.setId(entity.getRoleId());
        retValue.setName(entity.getRoleName());
        retValue.setDescription(entity.getDescription());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseAuditedEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTRoleCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseAuditedEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // USERS
        if (expand != null && expand.contains(RESTRoleV1.USERS_NAME)) {
            retValue.setUsers(
                    RESTEntityCollectionFactory.create(RESTUserCollectionV1.class, userFactory, entity.getUsers(), RESTRoleV1.USERS_NAME,
                            dataType, expand, baseUrl, revision, entityManager));
        }

        // PARENT ROLES
        if (expand != null && expand.contains(RESTRoleV1.PARENTROLES_NAME)) {
            retValue.setParentRoles(
                    RESTEntityCollectionFactory.create(RESTRoleCollectionV1.class, this, entity.getParentRoles(),
                            RESTRoleV1.PARENTROLES_NAME, dataType, expand, baseUrl, revision, entityManager));
        }

        // CHILD ROLES
        if (expand != null && expand.contains(RESTRoleV1.CHILDROLES_NAME)) {
            retValue.setChildRoles(
                    RESTEntityCollectionFactory.create(RESTRoleCollectionV1.class, this, entity.getChildRoles(), RESTRoleV1.CHILDROLES_NAME,
                            dataType, expand, baseUrl, revision, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.ROLE_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTRoleV1> parent, final RESTRoleV1 dataObject) {
        if (dataObject.hasParameterSet(RESTRoleV1.USERS_NAME)
                && dataObject.getUsers() != null
                && dataObject.getUsers().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getUsers(), userFactory);
        }

        if (dataObject.hasParameterSet(RESTRoleV1.PARENTROLES_NAME)
                && dataObject.getUsers() != null
                && dataObject.getUsers().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getUsers(), userFactory, RESTRoleV1.PARENTROLES_NAME);
        }

        if (dataObject.hasParameterSet(RESTRoleV1.CHILDROLES_NAME)
                && dataObject.getUsers() != null
                && dataObject.getUsers().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getUsers(), userFactory, RESTRoleV1.CHILDROLES_NAME);
        }
    }

    @Override
    public void syncBaseDetails(final Role entity, final RESTRoleV1 dataObject) {
        if (dataObject.hasParameterSet(RESTUserV1.DESCRIPTION_NAME)) entity.setDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTUserV1.NAME_NAME)) entity.setRoleName(dataObject.getName());
    }

    @Override
    protected void doDeleteChildAction(final Role entity, final RESTRoleV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();

        if (restEntity instanceof RESTUserV1) {
            final User dbEntity = entityManager.find(User.class, restEntity.getId());
            if (dbEntity == null) throw new BadRequestException(
                    "No User entity was found with the primary key " + restEntity.getId());

            entity.removeUser(dbEntity);
        } else if (restEntity instanceof RESTRoleV1) {
            final Role dbEntity = entityManager.find(Role.class, restEntity.getId());
            final RoleToRoleRelationship dbRelationshipEntity = entityManager.find(RoleToRoleRelationship.class, ROLE_TO_ROLE_ID);
            if (dbEntity == null) {
                throw new BadRequestException(
                        "No Role entity was found with the primary key " + restEntity.getId());
            } else if (dbRelationshipEntity == null) {
                throw new InternalServerErrorException("No RoleToRoleRelationship entity was found with the primary key "
                        + ROLE_TO_ROLE_ID);
            }

            if (RESTRoleV1.PARENTROLES_NAME.equals(action.getUniqueId())) {
                entity.removeParentRole(dbEntity, dbRelationshipEntity);
            } else {
                entity.removeChildRole(dbEntity, dbRelationshipEntity);
            }
        }
    }

    @Override
    protected PressGangEntity doCreateChildAction(final Role entity, final RESTRoleV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();
        final PressGangEntity dbEntity;

        if (restEntity instanceof RESTUserV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTUserV1) restEntity, User.class);
            if (dbEntity == null) {
                throw new BadRequestException("No User entity was found with the primary key " + restEntity.getId());
            }

            entity.addUser((User) dbEntity);
        } else if (restEntity instanceof RESTRoleV1) {
            dbEntity = entityManager.find(Role.class, restEntity.getId());
            final RoleToRoleRelationship dbRelationshipEntity = entityManager.find(RoleToRoleRelationship.class, ROLE_TO_ROLE_ID);
            if (dbEntity == null) {
                throw new BadRequestException("No Role entity was found with the primary key " + restEntity.getId());
            } else if (dbRelationshipEntity == null) {
                throw new InternalServerErrorException("No RoleToRoleRelationship entity was found with the primary key "
                        + ROLE_TO_ROLE_ID);
            }

            if (RESTRoleV1.PARENTROLES_NAME.equals(action.getUniqueId())) {
                entity.addParentRole((Role) dbEntity, dbRelationshipEntity);
            } else {
                entity.addChildRole((Role) dbEntity, dbRelationshipEntity);
            }
        } else {
            throw new IllegalArgumentException("Item is not a child of Role");
        }

        return dbEntity;
    }

    @Override
    protected PressGangEntity getChildEntityForAction(final Role entity, final RESTRoleV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();

        final PressGangEntity dbEntity;
        if (restEntity instanceof RESTUserV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTUserV1) restEntity, User.class);
            if (dbEntity == null) {
                throw new BadRequestException("No User entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getUsers().contains(dbEntity)) {
                throw new BadRequestException(
                        "No User entity was found with the primary key " + restEntity.getId() + " for Role " + entity.getId());
            }
        } else if (restEntity instanceof RESTRoleV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTRoleV1) restEntity, Role.class);
            if (dbEntity == null) {
                throw new BadRequestException("No Role entity was found with the primary key " + restEntity.getId());
            } else {
                if (RESTRoleV1.CHILDROLES_NAME.equals(action.getUniqueId()) && !entity.getChildRoles().contains(dbEntity)) {
                    throw new BadRequestException(
                            "No Role entity was found with the primary key " + restEntity.getId() + " for Role " + entity.getId());
                } else if (RESTRoleV1.PARENTROLES_NAME.equals(action.getUniqueId()) && !entity.getParentRoles().contains(dbEntity)) {
                    throw new BadRequestException(
                            "No Role entity was found with the primary key " + restEntity.getId() + " for Role " + entity.getId());
                }
            }
        } else {
            throw new IllegalArgumentException("Item is not a child of Role");
        }

        return dbEntity;
    }

    @Override
    protected Class<Role> getDatabaseClass() {
        return Role.class;
    }
}
