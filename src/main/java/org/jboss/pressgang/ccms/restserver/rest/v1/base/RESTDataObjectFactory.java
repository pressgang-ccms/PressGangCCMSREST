package org.jboss.pressgang.ccms.restserver.rest.v1.base;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.utils.common.ExceptionUtilities;
import org.jboss.resteasy.spi.BadRequestException;

/**
 * Defines a factory that can create REST entity objects from JPA entities, and update JPA entities from REST entities
 * 
 * @param <T> The REST object type
 * @param <U> The database object type
 * @param <V> The REST object collection type
 */
public abstract class RESTDataObjectFactory<T extends RESTBaseEntityV1<T, V, W>, U, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> {
    final Class<U> databaseClass;

    public RESTDataObjectFactory(final Class<U> databaseClass) {
        this.databaseClass = databaseClass;
    }

    /**
     * @param primaryKey The id of the database entity to use as the source for the REST entity
     * @param baseUrl The REST url that was used to access this REST entity
     * @param dataType The type of the returned data (XML or JSON)
     * @param expand The expansion JSON string, which will be converted into a ExpandDataTrunk
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
     * @return A new REST entity populated with the values in a database entity
     */
    public T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final EntityManager entityManager) {
        return createRESTEntityFromDBEntity(entity, baseUrl, dataType, expand, null, true, entityManager);
    }

    /**
     * @return A new REST entity populated with the values in a database entity
     */
    public T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final EntityManager entityManager) {
        return createRESTEntityFromDBEntity(entity, baseUrl, dataType, expand, revision, true, entityManager);
    }

    /**
     * @return A new REST entity populated with the values in a database entity
     */
    public abstract T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager);

    /**
     * Populates the values of a database entity from a REST entity
     * 
     * @param entityManager
     * @param entity The database entity
     * @param dataObject The REST entity
     */
    public abstract void syncDBEntityWithRESTEntity(final EntityManager entityManager, final U entity, final T dataObject)
            throws InvalidParameterException;

    /**
     * Creates, populates and returns a new database entity from a REST entity
     * 
     * @param entityManager
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
