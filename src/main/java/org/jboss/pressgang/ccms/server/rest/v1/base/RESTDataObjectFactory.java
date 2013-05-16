package org.jboss.pressgang.ccms.server.rest.v1.base;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.model.utils.EnversUtilities;
import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.LogDetailsV1Factory;
import org.jboss.pressgang.ccms.utils.common.ExceptionUtilities;
import org.jboss.resteasy.spi.BadRequestException;

/**
 * Defines a factory that can create REST entity objects from JPA entities, and update JPA entities from REST entities
 * 
 * @param <T> The REST object type
 * @param <U> The database object type
 * @param <V> The REST object collection type
 * @param <W> The REST object collection item type
 */
public abstract class RESTDataObjectFactory<T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> {
    final Class<U> databaseClass;

    public RESTDataObjectFactory(final Class<U> databaseClass) {
        this.databaseClass = databaseClass;
    }

    /**
     * Create a REST Entity representation from Database Entity specified by it's Primary Key.
     * 
     * @param primaryKey The id of the database entity to use as the source for the REST entity
     * @param baseUrl The REST url that was used to access this REST entity
     * @param dataType The type of the returned data (XML or JSON)
     * @param expand The Object that contains details about what fields should be expanded.
     * @return A new REST entity populated with the values in a database entity
     */
    public T createRESTEntityFromDBPK(final Object primaryKey, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final EntityManager entityManager)
            throws InvalidParameterException {
        final U entity = entityManager.find(databaseClass, primaryKey);

        if (entity == null)
            throw new BadRequestException("No entity was found with the id " + primaryKey);

        return createRESTEntityFromDBEntity(entity, baseUrl, dataType, expand, revision, entityManager);
    }

    /**
     * Create a REST Entity representation from Database Entity.
     * 
     * @param entity The entity that is to be transformed into a REST Entity.
     * @param baseUrl The REST url that was used to access this REST entity
     * @param dataType The type of the returned data (XML or JSON)
     * @param expand The Object that contains details about what fields should be expanded.
     * @param entityManager The EntityManager object used to look up the entity and any extra information.
     * @return A new REST entity populated with the values in a database entity
     */
    public T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final EntityManager entityManager) {
        return createRESTEntityFromDBEntity(entity, baseUrl, dataType, expand, null, true, entityManager);
    }

    /**
     * Create a REST Entity representation from Database Entity.
     * 
     * @param entity The entity that is to be transformed into a REST Entity.
     * @param baseUrl The REST url that was used to access this REST entity
     * @param dataType The type of the returned data (XML or JSON)
     * @param expand The Object that contains details about what fields should be expanded.
     * @param revision The revision number of the entity.
     * @param entityManager The EntityManager object used to look up the entity and any extra information.
     * @return A new REST entity populated with the values in a database entity
     */
    public T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final EntityManager entityManager) {
        return createRESTEntityFromDBEntity(entity, baseUrl, dataType, expand, revision, true, entityManager);
    }
    
    /**
     * Create a REST Entity representation from Database Entity.
     * 
     * @param entity The entity that is to be transformed into a REST Entity.
     * @param baseUrl The REST url that was used to access this REST entity
     * @param dataType The type of the returned data (XML or JSON)
     * @param expand The Object that contains details about what fields should be expanded.
     * @param revision The revision number of the entity.
     * @param expandParentReferences If parent entities in children entities should be expanded.
     * @param entityManager The EntityManager object used to look up the entity and any extra information.
     * @return A new REST entity populated with the values in a database entity
     */
    public T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
        
        final T retValue = createRESTEntityFromDBEntityInternal(entity, baseUrl, dataType, expand, revision, expandParentReferences, entityManager);
        
        // Set the entities revision
        final Number fixedRevision = revision == null ? EnversUtilities.getLatestRevision(entityManager, entity) : revision;
        retValue.setRevision(fixedRevision.intValue());
        
        // Add the log details
        retValue.setLogDetails(new LogDetailsV1Factory().create(entity, fixedRevision, RESTBaseEntityV1.LOG_DETAILS_NAME, expand,
                dataType, baseUrl, entityManager));
        
        return retValue;
    }
    
    /**
     * Create a REST Entity representation from Database Entity.
     * 
     * @param entity The entity that is to be transformed into a REST Entity.
     * @param baseUrl The REST url that was used to access this REST entity
     * @param dataType The type of the returned data (XML or JSON)
     * @param expand The Object that contains details about what fields should be expanded.
     * @param revision The revision number of the entity.
     * @param expandParentReferences If parent entities in children entities should be expanded.
     * @param entityManager The EntityManager object used to look up the entity and any extra information.
     * @return A new REST entity populated with the values in a database entity
     */
    protected abstract T createRESTEntityFromDBEntityInternal(final U entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager);

    /**
     * Populates the values of a database entity from a REST entity
     * 
     * @param entityManager The EntityManager object used to look up the entity and any extra information.
     * @param entity The database entity to be synced from the REST Entity.
     * @param dataObject The REST entity object.
     */
    public abstract void syncDBEntityWithRESTEntity(final EntityManager entityManager, final U entity, final T dataObject)
            throws InvalidParameterException;

    /**
     * Creates, populates and returns a new database entity from a REST entity
     * 
     * @param entityManager The EntityManager object used to look up the entity and any extra information.
     * @param dataObject The REST entity used to populate the database entity's values
     * @return A new database entity with the values supplied from the dataObject
     */
    public U createDBEntityFromRESTEntity(final EntityManager entityManager, final T dataObject)
            throws InvalidParameterException {
        try {
            final U entity = databaseClass.newInstance();
            this.syncDBEntityWithRESTEntity(entityManager, entity, dataObject);
            return entity;
        } catch (final Exception ex) {
            ExceptionUtilities.handleException(ex);
        }

        return null;
    }
}
