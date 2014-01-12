package org.jboss.pressgang.ccms.server.rest.v1.factory.base;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseEntityCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseEntityCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.EntityCache;
import org.jboss.pressgang.ccms.server.rest.v1.factory.LogDetailsV1Factory;
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
public abstract class RESTEntityFactory<T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity,
        V extends RESTBaseEntityCollectionV1<T, V, W>, W extends RESTBaseEntityCollectionItemV1<T, V, W>> {
    @Inject
    protected EntityCache entityCache;
    @Inject
    protected EntityManager entityManager;
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
                logDetailsFactory.create(entity, fixedRevision, RESTBaseEntityV1.LOG_DETAILS_NAME, expand, dataType, baseUrl));

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
     * Populates the values of a database entity from a REST entity. This step should iterate over "One to Many" or "Many to Many"
     * collections and do the following:<br />
     * <br />
     * 1. Process any Remove items.<br />
     * 2. Create any "One to Many" Add items and do the First Pass on the new entities.<br />
     * 3. Do the First Pass on any Update items.<br />
     *
     * @param entity     The database entity to be synced from the REST Entity.
     * @param dataObject The REST entity object.
     */
    protected abstract void syncDBEntityWithRESTEntityFirstPass(final U entity, final T dataObject);

    /**
     * Populates the entities for new entities, that were created in the first pass. This step should iterate over "One to Many" or "Many
     * to Many"
     * collections and do the following:<br />
     * <br />
     * 1. Do the Second Pass "One to Many" Add items.<br />
     * 2. Add the "Many to Many" Add items.<br />
     * 3. Do the Second Pass on any Update items.<br />
     * 4. Set the singular entities, so new entities can be found.<br />
     *
     * @param entity     The database entity to be synced from the REST Entity.
     * @param dataObject The REST entity object.
     */
    public void syncDBEntityWithRESTEntitySecondPass(final U entity, final T dataObject) {

    }

    /**
     * Creates, populates and returns a new database entity from a REST entity
     *
     * @param dataObject The REST entity used to populate the database entity's values
     * @return A new database entity with the values supplied from the dataObject
     */
    public U createDBEntityFromRESTEntity(final T dataObject) {
        try {
            final U entity = getDatabaseClass().newInstance();
            syncDBEntityWithRESTEntityFirstPass(entity, dataObject);
            entityCache.addNew(dataObject, entity);
            return entity;
        } catch (InstantiationException e) {
            throw new InternalServerErrorException(e);
        } catch (IllegalAccessException e) {
            throw new InternalServerErrorException(e);
        }
    }

    /**
     * Updates a database entity from a REST entity
     *
     * @param entity     The database entity to be synced from the REST Entity.
     * @param dataObject The REST entity used to populate the database entity's values
     */
    public void updateDBEntityFromRESTEntity(final U entity, final T dataObject) {
        syncDBEntityWithRESTEntityFirstPass(entity, dataObject);
        entityCache.addUpdated(dataObject, entity);
    }

    /**
     * Get the Database class for the factory.
     *
     * @return The Database class
     */
    protected abstract Class<U> getDatabaseClass();
}
