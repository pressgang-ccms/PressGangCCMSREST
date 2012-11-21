package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.model.Role;
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

public class UserV1Factory extends RESTDataObjectFactory<RESTUserV1, User, RESTUserCollectionV1, RESTUserCollectionItemV1> {
    public UserV1Factory() {
        super(User.class);
    }

    @Override
    public RESTUserV1 createRESTEntityFromDBEntityInternal(final User entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter topic can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTUserV1 retValue = new RESTUserV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTUserV1.ROLES_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);

        if (revision == null)
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setId(entity.getUserId());
        retValue.setName(entity.getUserName());
        retValue.setDescription(entity.getDescription());

        // REVISIONS
        if (revision == null) {
            retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTUserV1, User, RESTUserCollectionV1, RESTUserCollectionItemV1>()
                    .create(RESTUserCollectionV1.class, new UserV1Factory(), entity, EnversUtilities.getRevisions(entityManager, entity),
                            RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
        }

        // ROLES
        retValue.setRoles(new RESTDataObjectCollectionFactory<RESTRoleV1, Role, RESTRoleCollectionV1, RESTRoleCollectionItemV1>()
                .create(RESTRoleCollectionV1.class, new RoleV1Factory(), entity.getRoles(), RESTUserV1.ROLES_NAME, dataType,
                        expand, baseUrl, entityManager));

        retValue.setLinks(baseUrl, RESTv1Constants.USER_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final User entity, final RESTUserV1 dataObject)
            throws InvalidParameterException {
        if (dataObject.hasParameterSet(RESTUserV1.DESCRIPTION_NAME))
            entity.setDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTUserV1.NAME_NAME))
            entity.setUserName(dataObject.getName());

        entityManager.persist(entity);

        if (dataObject.hasParameterSet(RESTUserV1.ROLES_NAME) && dataObject.getRoles() != null
                && dataObject.getRoles().getItems() != null) {
            dataObject.getRoles().removeInvalidChangeItemRequests();

            for (final RESTRoleCollectionItemV1 restEntityItem : dataObject.getRoles().getItems()) {
                final RESTRoleV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsRemoveItem()) {
                    final Role dbEntity = entityManager.find(Role.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No entity was found with the primary key " + restEntity.getId());

                    if (restEntityItem.returnIsAddItem()) {
                        entity.addRole(dbEntity);
                    } else if (restEntityItem.returnIsRemoveItem()) {
                        entity.removeRole(dbEntity);
                    }
                }
            }
        }
    }
}
