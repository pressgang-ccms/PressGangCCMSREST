package org.jboss.pressgang.ccms.server.rest.v1.base;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.model.utils.EnversUtilities;
import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.LogDetailsV1Factory;
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
public abstract class RESTDataObjectFactory<T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity,
        V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> {
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
     *
     * @param entity        The entity that is to be transformed into a REST Entity.
     * @param baseUrl       The REST url that was used to access this REST entity
     * @param dataType      The type of the returned data (XML or JSON)
     * @param expand        The Object that contains details about what fields should be expanded.
     * @return A new REST entity populated with the values in a database entity
     */
    public T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand) {
        return createRESTEntityFromDBEntity(entity, baseUrl, dataType, expand, null, true);
    }

    /**
     * Create a REST Entity representation from Database Entity.
     *
     *
     * @param entity        The entity that is to be transformed into a REST Entity.
     * @param baseUrl       The REST url that was used to access this REST entity
     * @param dataType      The type of the returned data (XML or JSON)
     * @param expand        The Object that contains details about what fields should be expanded.
     * @param revision      The revision number of the entity.
     * @return A new REST entity populated with the values in a database entity
     */
    public T createRESTEntityFromDBEntity(final U entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand,
            final Number revision) {
        return createRESTEntityFromDBEntity(entity, baseUrl, dataType, expand, revision, true);
    }

    /**
     * Create a REST Entity representation from Database Entity.
     *
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
        final Number fixedRevision = revision == null ? EnversUtilities.getLatestRevision(entityManager, entity) : revision;
        retValue.setRevision(fixedRevision.intValue());

        // Add the log details
        retValue.setLogDetails(
                logDetailsFactory.create(entity, fixedRevision, RESTBaseEntityV1.LOG_DETAILS_NAME, expand, dataType, baseUrl));

        return retValue;
    }

    /**
     * Create a REST Entity representation from Database Entity.
     *
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
     * Populates the values of a database entity from a REST entity
     *
     * @param entity        The database entity to be synced from the REST Entity.
     * @param dataObject    The REST entity object.
     */
    public abstract void syncDBEntityWithRESTEntity(final U entity, final T dataObject);

    /**
     * Creates, populates and returns a new database entity from a REST entity
     *
     *
     * @param dataObject    The REST entity used to populate the database entity's values
     * @return A new database entity with the values supplied from the dataObject
     */
    public U createDBEntityFromRESTEntity(final T dataObject) {
        try {
            final U entity = getDatabaseClass().newInstance();
            syncDBEntityWithRESTEntity(entity, dataObject);
            return entity;
        } catch (InstantiationException e) {
            throw new InternalServerErrorException(e);
        } catch (IllegalAccessException e) {
            throw new InternalServerErrorException(e);
        }
    }

    /**
     * Get the Database class for the factory.
     *
     * @return The Database class
     */
    protected abstract Class<U> getDatabaseClass();
}
