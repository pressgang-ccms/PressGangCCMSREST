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

package org.jboss.pressgang.ccms.server.rest.v1.factory.base;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseEntityCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseEntityCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTUpdateCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.DatabaseOperation;
import org.jboss.pressgang.ccms.server.rest.v1.EntityCache;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.LogDetailsV1Factory;
import org.jboss.pressgang.ccms.server.utils.EntityManagerWrapper;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.InternalServerErrorException;

/**
 * Defines a factory that can create REST entity objects from JPA entities, and update JPA entities from REST entities
 *
 * @param <T> The REST object type
 * @param <U> The database object type
 * @param <V> The REST object collection type
 * @param <W> The REST object collection item type
 */
@SuppressWarnings("unchecked")
public abstract class RESTEntityFactory<T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity,
        V extends RESTBaseEntityCollectionV1<T, V, W>, W extends RESTBaseEntityCollectionItemV1<T, V, W>> {
    @Inject
    protected EntityCache entityCache;
    @Inject
    protected EntityManagerWrapper entityManager;
    @Inject
    protected LogDetailsV1Factory logDetailsFactory;

    /**
     * Create a REST Entity representation from Database Entity specified by it's Primary Key.
     *
     * @param primaryKey The id of the database entity to use as the source for the REST entity
     * @param baseUrl    The REST url that was used to access this REST entity
     * @param dataType   The type of the returned data (XML or JSON)
     * @param expand     The Object that contains details about what fields should be expanded.
     * @return A new REST entity populated with the values in a database entity
     */
    public T createRESTEntityFromDBPK(final Object primaryKey, final String baseUrl, final String dataType, final ExpandDataTrunk expand,
            final Number revision, final EntityManager entityManager) {
        final U entity = entityManager.find(getDatabaseClass(), primaryKey);

        if (entity == null) throw new BadRequestException("No entity was found with the id " + primaryKey);

        return createRESTEntityFromDBEntity(entity, baseUrl, dataType, expand, revision);
    }

    /**
     * Create a REST Entity representation from Database Entity.
     *
     * @param entity   The entity that is to be transformed into a REST Entity.
     * @param baseUrl  The REST url that was used to access this REST entity
     * @param dataType The type of the returned data (XML or JSON)
     * @param expand   The Object that contains details about what fields should be expanded.
     * @return A new REST entity populated with the values in a database entity
     */
    public T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand) {
        return createRESTEntityFromDBEntity(entity, baseUrl, dataType, expand, null, true);
    }

    /**
     * Create a REST Entity representation from Database Entity.
     *
     * @param entity   The entity that is to be transformed into a REST Entity.
     * @param baseUrl  The REST url that was used to access this REST entity
     * @param dataType The type of the returned data (XML or JSON)
     * @param expand   The Object that contains details about what fields should be expanded.
     * @param revision The revision number of the entity.
     * @return A new REST entity populated with the values in a database entity
     */
    public T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand,
            final Number revision) {
        return createRESTEntityFromDBEntity(entity, baseUrl, dataType, expand, revision, true);
    }

    /**
     * Create a REST Entity representation from Database Entity.
     *
     * @param entity                 The entity that is to be transformed into a REST Entity.
     * @param baseUrl                The REST url that was used to access this REST entity
     * @param dataType               The type of the returned data (XML or JSON)
     * @param expand                 The Object that contains details about what fields should be expanded.
     * @param revision               The revision number of the entity.
     * @param expandParentReferences If parent entities in children entities should be expanded.
     * @return A new REST entity populated with the values in a database entity
     */
    public T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand,
            final Number revision, final boolean expandParentReferences) {

        final T retValue = createRESTEntityFromDBEntityInternal(entity, baseUrl, dataType, expand, revision, expandParentReferences);

        // Set the entities revision
        final Number fixedRevision = revision == null ? EnversUtilities.getLatestRevision(entityManager,
                entity) : EnversUtilities.getClosestRevision(entityManager, entity, revision);
        retValue.setRevision(fixedRevision.intValue());

        // Add the log details
        retValue.setLogDetails(
                logDetailsFactory.createRESTEntityFromAuditedEntity(entity, fixedRevision, RESTBaseEntityV1.LOG_DETAILS_NAME, expand,
                        dataType, baseUrl));

        return retValue;
    }

    /**
     * Create a REST Entity representation from Database Entity.
     *
     * @param entity                 The entity that is to be transformed into a REST Entity.
     * @param baseUrl                The REST url that was used to access this REST entity
     * @param dataType               The type of the returned data (XML or JSON)
     * @param expand                 The Object that contains details about what fields should be expanded.
     * @param revision               The revision number of the entity.
     * @param expandParentReferences If parent entities in children entities should be expanded.
     * @return A new REST entity populated with the values in a database entity
     */
    protected abstract T createRESTEntityFromDBEntityInternal(final U entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences);

    /**
     * Creates and returns a new database entity from a REST entity
     *
     * @param restEntity
     * @return A new database entity
     */
    public U createDBEntity(T restEntity) {
        try {
            final U dbEntity = getDatabaseClass().newInstance();
            syncBaseDetails(dbEntity, restEntity);
            return dbEntity;
        } catch (InstantiationException e) {
            throw new InternalServerErrorException(e);
        } catch (IllegalAccessException e) {
            throw new InternalServerErrorException(e);
        }
    }

    public void syncDBEntityDeleteChanges(final AuditedEntity entity, final RESTBaseEntityV1<?, ?, ?> dataObject,
            final RESTChangeAction<?> action) {
        for (final RESTChangeAction<?> childAction : action.getDeleteChildActions()) {
            doDeleteChildAction((U) entity, (T) dataObject, childAction);
        }
    }

    public void syncDBEntityCreateChanges(final AuditedEntity entity, final RESTBaseEntityV1<?, ?, ?> dataObject,
            final RESTChangeAction<?> action) {
        for (final RESTChangeAction<?> childAction : action.getCreateChildActions()) {
            final AuditedEntity createdEntity = doCreateChildAction((U) entity, (T) dataObject, childAction);
            childAction.setDBEntity(createdEntity);
            childAction.getFactory().syncDBEntityCreateChanges(createdEntity, childAction.getRESTEntity(), childAction);
            entityCache.addNew(childAction.getRESTEntity(), createdEntity);
        }
    }

    public void syncDBEntityUpdateChanges(final AuditedEntity entity, final RESTBaseEntityV1<?, ?, ?> dataObject,
            final RESTChangeAction<?> action) {
        if (action.getType() == DatabaseOperation.UPDATE) {
            syncBaseDetails((U) entity, (T) dataObject);
        }

        // We'll need to sync any additional content for create changes, so do it here
        for (final RESTChangeAction<?> childAction : action.getCreateChildActions()) {
            childAction.getFactory().syncDBEntityUpdateChanges(childAction.getDBEntity(), childAction.getRESTEntity(), childAction);
        }

        for (final RESTChangeAction<?> childAction : action.getUpdateChildActions()) {
            final AuditedEntity updatedEntity = getChildEntityForAction((U) entity, (T) dataObject, childAction);
            childAction.getFactory().syncDBEntityUpdateChanges(updatedEntity, childAction.getRESTEntity(), childAction);
            entityCache.addUpdated(childAction.getRESTEntity(), updatedEntity);
        }

        // Sync the additional details now that the child actions have been performed
        syncAdditionalDetails((U) entity, (T) dataObject);
    }

    public abstract void collectChangeInformation(RESTChangeAction<T> parent, T dataObject);
    public abstract void syncBaseDetails(U entity, T dataObject);
    public void syncAdditionalDetails(U entity, T dataObject) {}
    protected void doDeleteChildAction(U entity, T dataObject, RESTChangeAction<?> action) {}

    protected AuditedEntity doCreateChildAction(U entity, T dataObject, RESTChangeAction<?> action) {
        throw new UnsupportedOperationException("No implementation exists for doCreateChildAction()");
    }

    protected AuditedEntity getChildEntityForAction(U entity, T dataObject, RESTChangeAction<?> action) {
        throw new UnsupportedOperationException("No implementation exists for getChildEntityForAction()");
    }

    protected <T extends RESTBaseEntityV1<T, V, W>,
            U extends AuditedEntity,
            V extends RESTBaseEntityCollectionV1<T, V, W>,
            W extends RESTBaseEntityCollectionItemV1<T, V, W>>
            void collectChangeInformationFromCollection(final RESTChangeAction<?> parent, final V collection,
            final RESTEntityFactory<T, U, V, W> collectionFactory) {
        collectChangeInformationFromCollection(parent, collection, collectionFactory, null);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>,
            U extends AuditedEntity,
            V extends RESTBaseEntityCollectionV1<T, V, W>,
            W extends RESTBaseEntityCollectionItemV1<T, V, W>>
    void collectChangeInformationFromCollection(final RESTChangeAction<?> parent, final V collection,
            final RESTEntityFactory<T, U, V, W> collectionFactory, final String uniqueId) {
        collection.removeInvalidChangeItemRequests();

        for (final W restEntityItem : collection.getItems()) {
            final T restEntity = restEntityItem.getItem();

            DatabaseOperation operation = null;
            if (restEntityItem.returnIsRemoveItem()) {
                operation = DatabaseOperation.DELETE;
            } else if (restEntityItem.returnIsAddItem()) {
                operation = DatabaseOperation.CREATE;
            } else if (restEntityItem instanceof RESTUpdateCollectionItemV1
                    && ((RESTUpdateCollectionItemV1) restEntityItem).returnIsUpdateItem()) {
                operation = DatabaseOperation.UPDATE;
            }

            // Create the actionable node
            final RESTChangeAction<T> childAction = new RESTChangeAction(parent, collectionFactory, restEntity, operation);
            childAction.setUniqueId(uniqueId);
            parent.addChildAction(childAction);

            if (operation != DatabaseOperation.DELETE) {
                // For deletes there is no need to go any further down the chain, as deleting the parent will delete the children
                collectionFactory.collectChangeInformation(childAction, restEntity);
            }
        }
    }

    /**
     * Get the Database class for the factory.
     *
     * @return The Database class
     */
    protected abstract Class<U> getDatabaseClass();
}
