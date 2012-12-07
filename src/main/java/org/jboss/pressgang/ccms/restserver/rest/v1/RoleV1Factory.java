package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.model.Role;
import org.jboss.pressgang.ccms.model.RoleToRoleRelationship;
import org.jboss.pressgang.ccms.model.User;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTRoleCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTUserCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTRoleCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTUserCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTRoleV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTUserV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.restserver.utils.EnversUtilities;

/*
 * Note: Since roles and users are going to be re-done soon using Katie's OAuth Library, I've left out the ability to update the RoleToRoleRelationship information
 * and made it a static variable since we only have one definition anyways.
 * 
 * Lee
 */
public class RoleV1Factory extends RESTDataObjectFactory<RESTRoleV1, Role, RESTRoleCollectionV1, RESTRoleCollectionItemV1> {
    private static final Integer ROLE_TO_ROLE_ID = 1;

    public RoleV1Factory() {
        super(Role.class);
    }

    @Override
    public RESTRoleV1 createRESTEntityFromDBEntityInternal(final Role entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter topic can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTRoleV1 retValue = new RESTRoleV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTRoleV1.USERS_NAME);
        expandOptions.add(RESTRoleV1.CHILDROLES_NAME);
        expandOptions.add(RESTRoleV1.PARENTROLES_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null)
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        retValue.setId(entity.getRoleId());
        retValue.setName(entity.getRoleName());
        retValue.setDescription(entity.getDescription());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTRoleV1, Role, RESTRoleCollectionV1, RESTRoleCollectionItemV1>()
                    .create(RESTRoleCollectionV1.class, new RoleV1Factory(), entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType,
                            expand, baseUrl, entityManager));
        }

        // USERS
        if (expand != null && expand.contains(RESTRoleV1.USERS_NAME)) {
            retValue.setUsers(new RESTDataObjectCollectionFactory<RESTUserV1, User, RESTUserCollectionV1, RESTUserCollectionItemV1>()
                    .create(RESTUserCollectionV1.class, new UserV1Factory(), entity.getUsers(), RESTRoleV1.USERS_NAME,
                            dataType, expand, baseUrl, entityManager));
        }

        // PARENT ROLES
        if (expand != null && expand.contains(RESTRoleV1.PARENTROLES_NAME)) {
            retValue.setParentRoles(new RESTDataObjectCollectionFactory<RESTRoleV1, Role, RESTRoleCollectionV1, RESTRoleCollectionItemV1>()
                    .create(RESTRoleCollectionV1.class, new RoleV1Factory(), entity.getParentRoles(),
                            RESTRoleV1.PARENTROLES_NAME, dataType, expand, baseUrl, entityManager));
        }

        // CHILD ROLES
        if (expand != null && expand.contains(RESTRoleV1.CHILDROLES_NAME)) {
            retValue.setChildRoles(new RESTDataObjectCollectionFactory<RESTRoleV1, Role, RESTRoleCollectionV1, RESTRoleCollectionItemV1>()
                    .create(RESTRoleCollectionV1.class, new RoleV1Factory(), entity.getChildRoles(),
                            RESTRoleV1.CHILDROLES_NAME, dataType, expand, baseUrl, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.ROLE_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final Role entity, final RESTRoleV1 dataObject)
            throws InvalidParameterException {
        if (dataObject.hasParameterSet(RESTUserV1.DESCRIPTION_NAME))
            entity.setDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTUserV1.NAME_NAME))
            entity.setRoleName(dataObject.getName());

        entityManager.persist(entity);

        /* Many To Many - Add will create a mapping */
        if (dataObject.hasParameterSet(RESTRoleV1.USERS_NAME) && dataObject.getUsers() != null
                && dataObject.getUsers().getItems() != null) {
            dataObject.getUsers().removeInvalidChangeItemRequests();

            for (final RESTUserCollectionItemV1 restEntityItem : dataObject.getUsers().getItems()) {
                final RESTUserV1 restEntity = restEntityItem.getItem();

                final User dbEntity = entityManager.find(User.class, restEntity.getId());
                if (dbEntity == null)
                    throw new InvalidParameterException("No User entity was found with the primary key " + restEntity.getId());

                if (restEntityItem.returnIsAddItem()) {
                    entity.addUser(dbEntity);
                } else if (restEntityItem.returnIsRemoveItem()) {
                    entity.removeUser(dbEntity);
                }
            }
        }

        /* Many To Many - Add will create a mapping */
        if (dataObject.hasParameterSet(RESTRoleV1.PARENTROLES_NAME) && dataObject.getParentRoles() != null
                && dataObject.getParentRoles().getItems() != null) {
            dataObject.getParentRoles().removeInvalidChangeItemRequests();

            for (final RESTRoleCollectionItemV1 restEntityItem : dataObject.getParentRoles().getItems()) {
                final RESTRoleV1 restEntity = restEntityItem.getItem();

                final Role dbEntity = entityManager.find(Role.class, restEntity.getId());
                final RoleToRoleRelationship dbRelationshipEntity = entityManager.find(RoleToRoleRelationship.class,
                        ROLE_TO_ROLE_ID);

                if (dbEntity == null)
                    throw new InvalidParameterException("No Role entity was found with the primary key " + restEntity.getId());
                else if (dbRelationshipEntity == null)
                    throw new InvalidParameterException("No RoleToRoleRelationship entity was found with the primary key "
                            + ROLE_TO_ROLE_ID);

                if (restEntityItem.returnIsAddItem()) {
                    entity.addParentRole(dbEntity, dbRelationshipEntity);
                } else if (restEntityItem.returnIsRemoveItem()) {
                    entity.removeParentRole(dbEntity, dbRelationshipEntity);
                }
            }
        }

        /* Many To Many - Add will create a mapping */
        if (dataObject.hasParameterSet(RESTRoleV1.CHILDROLES_NAME) && dataObject.getChildRoles() != null
                && dataObject.getChildRoles().getItems() != null) {
            dataObject.getChildRoles().removeInvalidChangeItemRequests();

            for (final RESTRoleCollectionItemV1 restEntityItem : dataObject.getChildRoles().getItems()) {
                final RESTRoleV1 restEntity = restEntityItem.getItem();

                final Role dbEntity = entityManager.find(Role.class, restEntity.getId());
                final RoleToRoleRelationship dbRelationshipEntity = entityManager.find(RoleToRoleRelationship.class,
                        ROLE_TO_ROLE_ID);

                if (dbEntity == null)
                    throw new InvalidParameterException("No Role entity was found with the primary key " + restEntity.getId());
                else if (dbRelationshipEntity == null)
                    throw new InvalidParameterException("No RoleToRoleRelationship entity was found with the primary key "
                            + ROLE_TO_ROLE_ID);

                if (restEntityItem.returnIsAddItem()) {
                    entity.addChildRole(dbEntity, dbRelationshipEntity);
                } else if (restEntityItem.returnIsRemoveItem()) {
                    entity.removeChildRole(dbEntity, dbRelationshipEntity);
                }
            }
        }
    }
}
