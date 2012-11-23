package org.jboss.pressgang.ccms.restserver.rest.v1.base;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.TransactionManager;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.jboss.pressgang.ccms.model.Filter;
import org.jboss.pressgang.ccms.model.User;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTopicCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTUserV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTLogDetailsV1;
import org.jboss.pressgang.ccms.rest.v1.exceptions.InternalProcessingException;
import org.jboss.pressgang.ccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.restserver.ejb.EnversLoggingBean;
import org.jboss.pressgang.ccms.restserver.filter.base.IFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.base.IFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.base.ITagFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.rest.DatabaseOperation;
import org.jboss.pressgang.ccms.restserver.utils.Constants;
import org.jboss.pressgang.ccms.restserver.utils.EntityUtilities;
import org.jboss.pressgang.ccms.restserver.utils.FilterUtilities;
import org.jboss.pressgang.ccms.restserver.utils.JNDIUtilities;
import org.jboss.resteasy.plugins.providers.atom.Content;
import org.jboss.resteasy.plugins.providers.atom.Entry;
import org.jboss.resteasy.plugins.providers.atom.Feed;
import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides the functions that retrieve, update, create and delete entities. It is expected that other classes will
 * extend BaseRESTv1 to provide expose REST functions.
 */
@RequestScoped
public class BaseRESTv1 {
    private static final Logger log = LoggerFactory.getLogger(BaseRESTv1.class);
    /** The format for dates passed and returned by the REST Interface */
    protected static final String REST_DATE_FORMAT = "dd-MMM-yyyy";

    /** An mapper to map Objects to JSON or vice-versa */
    private final ObjectMapper mapper = new ObjectMapper();
    /** The Uri Information for the REST Request */
    @Context
    private UriInfo uriInfo;
    /** The Java Bean used for logging information to Envers */
    @Inject
    private EnversLoggingBean enversLoggingBean;

    /**
     * Get the URL of the root REST endpoint using UriInfo from a request.
     * 
     * @return The URL of the root REST endpoint.
     */
    protected String getBaseUrl() {
        final String fullPath = uriInfo.getAbsolutePath().toString();
        final int index = fullPath.indexOf(Constants.BASE_REST_PATH);
        if (index != -1)
            return fullPath.substring(0, index + Constants.BASE_REST_PATH.length());

        return null;
    }

    /**
     * Get the URL of the REST endpoint from the calling request.
     * 
     * @return The URL of the endpoint that was called for the request.
     */
    protected String getUrl() {
        return uriInfo.getAbsolutePath().toString();
    }

    /**
     * Converts a Collection of Topics into an ATOM Feed.
     * 
     * @param topics The collection of topics that should be transformed into the Feed.
     * @param title The Title for the Feed.
     * @return A RESTEasy ATOM Feed Object containing the topic information.
     */
    protected Feed convertTopicsIntoFeed(final RESTTopicCollectionV1 topics, final String title) {
        try {
            final Feed feed = new Feed();

            feed.setId(new URI(this.getUrl()));
            feed.setTitle(title);
            feed.setUpdated(new Date());

            if (topics.getItems() != null) {
                for (final RESTTopicV1 topic : topics.returnItems()) {
                    final String html = topic.getHtml();

                    final Entry entry = new Entry();
                    entry.setTitle(topic.getTitle());
                    entry.setUpdated(topic.getLastModified());
                    entry.setPublished(topic.getCreated());

                    if (html != null) {
                        final Content content = new Content();
                        content.setType(MediaType.TEXT_HTML_TYPE);
                        content.setText(fixHrefs(topic.getHtml()));
                        entry.setContent(content);
                    }

                    feed.getEntries().add(entry);
                }
            }

            return feed;
        } catch (final Exception ex) {
            log.error("There was an error creating the ATOM feed", ex);
            throw new InternalServerErrorException("There was an error creating the ATOM feed");
        }
    }

    /**
     * Fix all the Links to a Topic in a HTML Page or general Link so that the URL's are Absolute, rather than Relative URL's.
     * 
     * @param input The Input data to be fixed.
     * @return The input data with all links fixed.
     */
    private String fixHrefs(final String input) {
        return input.replaceAll("Topic\\.seam", uriInfo.getBaseUri().toString() + "/Topic.seam");
    }

    /**
     * 
     * 
     * @param collectionClass
     * @param type
     * @param idProperty
     * @param dataObjectFactory
     * @param expandName
     * @param expand The expand parameters to determine what fields should be expanded.
     * @param date
     * @return
     * @throws InvalidParameterException
     */
    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V getJSONEntitiesUpdatedSince(
            final Class<V> collectionClass, final Class<U> type, final String idProperty,
            final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final String expandName, final String expand,
            final Date date) throws InvalidParameterException {
        return getEntitiesUpdatedSince(collectionClass, type, idProperty, dataObjectFactory, expandName, expand,
                RESTv1Constants.JSON_URL, date);
    }

    /**
     * 
     * @param collectionClass
     * @param type
     * @param idProperty
     * @param dataObjectFactory
     * @param expandName
     * @param expand The expand parameters to determine what fields should be expanded.
     * @param dataType
     * @param date
     * @return
     * @throws InvalidParameterException
     */
    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V getEntitiesUpdatedSince(
            final Class<V> collectionClass, final Class<U> type, final String idProperty,
            final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final String expandName, final String expand,
            final String dataType, final Date date) throws InvalidParameterException {
        assert date != null : "The date parameter can not be null";

        EntityManager entityManager = null;

        try {
            // Unmarshall the expand string into the ExpandDataTrunk object.
            final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

            // Get an EntityManager instance
            entityManager = getEntityManager();

            // Get the list of entity ids that were edited after the selected date
            final AuditReader reader = AuditReaderFactory.get(entityManager);
            final AuditQuery query = reader.createQuery().forRevisionsOfEntity(type, true, false)
                    .addOrder(AuditEntity.revisionProperty("timestamp").asc())
                    .add(AuditEntity.revisionProperty("timestamp").ge(date.getTime()))
                    .addProjection(AuditEntity.property("originalId." + idProperty).distinct());

            @SuppressWarnings("rawtypes")
            final List entityIds = query.getResultList();

            // Create the query to get the actual entities using the ids from the previous query
            final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            final CriteriaQuery<U> criteriaQuery = criteriaBuilder.createQuery(type);
            final Root<U> root = criteriaQuery.from(type);
            criteriaQuery.where(root.get(idProperty).in(entityIds));

            // Get the Entities from the database.
            final TypedQuery<U> jpaQuery = entityManager.createQuery(criteriaQuery);
            final List<U> entities = jpaQuery.getResultList();

            // Create and initialise the Collection using the specified REST Object Factory
            final V retValue = new RESTDataObjectCollectionFactory<T, U, V, W>().create(collectionClass, dataObjectFactory,
                    entities, expandName, dataType, expandDataTrunk, getBaseUrl(), entityManager);

            return retValue;
        } catch (final Exception ex) {
            log.error("Probably an issue querying Envers", ex);
            throw new InternalServerErrorException("There was an error running the query");
        } finally {
            if (entityManager != null)
                entityManager.close();
        }

    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T deleteJSONEntity(
            final Class<U> type, final RESTDataObjectFactory<T, U, V, W> factory, final Integer id, final String expand,
            final RESTLogDetailsV1 logDetails) throws InvalidParameterException {
        return deleteEntity(type, factory, id, RESTv1Constants.JSON_URL, expand, logDetails);
    }

    /**
     * Delete a Entity from the database specified by the entities Primary Key.
     * 
     * @param type The Database Entity type to be deleted.
     * @param factory The REST Object Factory to be used to generate a REST Entity return.
     * @param id The ID of the Database Entity to be deleted.
     * @param dataType The data type for the returned REST Entity response. (XML or JSON)
     * @param expand The expand parameters to determine what fields should be expanded.
     * @param logDetails The details about the changes that need to be logged.
     * @return
     * @throws InvalidParameterException
     */
    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T deleteEntity(
            final Class<U> type, final RESTDataObjectFactory<T, U, V, W> factory, final Integer id, final String dataType,
            final String expand, final RESTLogDetailsV1 logDetails) throws InvalidParameterException {
        assert id != null : "id should not be null";

        TransactionManager transactionManager = null;
        EntityManager entityManager = null;

        try {
            // Unmarshall the expand string into the ExpandDataTrunk object.
            final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

            // Get the TransactionManager and start a Transaction
            transactionManager = JNDIUtilities.lookupTransactionManager();
            transactionManager.begin();

            // Get an EntityManager instance
            entityManager = getEntityManager();

            // Store the log details into the Logging Java Bean
            setLogDetails(entityManager, logDetails);

            // Find the specified entity and make sure that it exists
            final U entity = entityManager.find(type, id);
            if (entity == null)
                throw new BadRequestException("No entity was found with the id " + id);

            // Remove the entity from the persistence context
            entityManager.remove(entity);

            // Flush and commit the changes to the database
            entityManager.flush();
            transactionManager.commit();

            return factory.createRESTEntityFromDBEntity(entity, getBaseUrl(), dataType, expandDataTrunk, entityManager);
        } catch (final InvalidParameterException ex) {
            log.error("There was an error looking up the entities", ex);
            throw ex;
        } catch (final Failure ex) {
            log.error("There was an error looking up the required manager objects", ex);
            throw ex;
        } catch (final Exception ex) {
            log.error("Probably an error saving the entity", ex);

            if (transactionManager != null) {
                try {
                    transactionManager.rollback();
                } catch (final Exception ex2) {
                    log.error("There was an error rolling back the transaction", ex2);
                }
            }

            throw new InternalServerErrorException("There was an error saving the entity");
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T createJSONEntity(
            final Class<U> type, final T restEntity, final RESTDataObjectFactory<T, U, V, W> factory, final String expand,
            final RESTLogDetailsV1 logDetails) throws InternalProcessingException, InvalidParameterException {
        return createEntity(type, restEntity, factory, RESTv1Constants.JSON_URL, expand, logDetails);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T updateJSONEntity(
            final Class<U> type, final T restEntity, final RESTDataObjectFactory<T, U, V, W> factory, final String expand,
            final RESTLogDetailsV1 logDetails) throws InvalidParameterException {
        return updateEntity(type, restEntity, factory, RESTv1Constants.JSON_URL, expand, logDetails);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T createEntity(
            final Class<U> type, final T restEntity, final RESTDataObjectFactory<T, U, V, W> factory, final String dataType,
            final String expand, final RESTLogDetailsV1 logDetails) throws InternalProcessingException,
            InvalidParameterException {
        return createOrUpdateEntity(type, restEntity, factory, DatabaseOperation.CREATE, dataType, expand, logDetails);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T updateEntity(
            final Class<U> type, final T restEntity, final RESTDataObjectFactory<T, U, V, W> factory, final String dataType,
            final String expand, final RESTLogDetailsV1 logDetails) throws InvalidParameterException {
        return createOrUpdateEntity(type, restEntity, factory, DatabaseOperation.UPDATE, dataType, expand, logDetails);
    }

    /**
     * Create or update a Database Entity using the data given from a REST Entity.
     * 
     * @param type The Database entity type.
     * @param restEntity The REST Entity to create/update the database with.
     * @param factory The type of REST Object Factory to use to generate the returned entity.
     * @param operation The Database Operation type (CREATE or UPDATE).
     * @param dataType
     * @param expand The expand parameters to determine what fields should be expanded.
     * @param logDetails The details about the changes that need to be logged.
     * @return The updated/created REST Entity representation of the database entity.
     * @throws InvalidParameterException
     */
    private <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T createOrUpdateEntity(
            final Class<U> type, final T restEntity, final RESTDataObjectFactory<T, U, V, W> factory,
            final DatabaseOperation operation, final String dataType, final String expand, final RESTLogDetailsV1 logDetails)
            throws InvalidParameterException {
        assert restEntity != null : "restEntity should not be null";
        assert factory != null : "factory should not be null";

        TransactionManager transactionManager = null;
        EntityManager entityManager = null;

        try {
            // Unmarshall the expand string into the ExpandDataTrunk object.
            final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

            // Get the TransactionManager and start a Transaction
            transactionManager = JNDIUtilities.lookupTransactionManager();
            transactionManager.begin();

            // Get an EntityManager instance
            entityManager = getEntityManager();
            entityManager.setFlushMode(FlushModeType.AUTO);

            // Store the log details into the Logging Java Bean
            setLogDetails(entityManager, logDetails);

            /*
             * The difference between creating or updating an entity is that we create a new instance of U, or find an existing
             * instance of U.
             */
            U entity = null;
            if (operation == DatabaseOperation.UPDATE) {
                /* Have to have an ID for the entity we are deleting or updating */
                if (restEntity.getId() == null)
                    throw new InvalidParameterException("An id needs to be set for update operations");

                // Find the entity and check that it actually exists
                entity = entityManager.find(type, restEntity.getId());
                if (entity == null)
                    throw new InvalidParameterException("No entity was found with the primary key " + restEntity.getId());

                // Sync the changes from the REST Entity to the Database.
                factory.syncDBEntityWithRESTEntity(entityManager, entity, restEntity);

            } else if (operation == DatabaseOperation.CREATE) {
                // Create a new Database Entity using the REST Entity.
                entity = factory.createDBEntityFromRESTEntity(entityManager, restEntity);

                // Check that a entity was able to be successfully created.
                if (entity == null)
                    throw new InvalidParameterException("The entity could not be created");
            }

            assert entity != null : "entity should not be null";

            entityManager.flush();
            transactionManager.commit();

            return factory.createRESTEntityFromDBEntity(entity, this.getBaseUrl(), dataType, expandDataTrunk, null, true,
                    entityManager);
        } catch (final InvalidParameterException ex) {
            log.error("There was an error looking up the entities", ex);

            if (transactionManager != null) {
                try {
                    transactionManager.rollback();
                } catch (final Exception ex2) {
                    log.error("There was an error rolling back the transaction", ex2);
                }
            }

            throw ex;
        } catch (final Failure ex) {
            log.error("There was an error looking up the required manager objects", ex);
            throw ex;
        } catch (final Exception ex) {
            log.error("Probably an error saving the entity", ex);

            if (transactionManager != null) {
                try {
                    transactionManager.rollback();
                } catch (final Exception ex2) {
                    log.error("There was an error rolling back the transaction", ex2);
                }
            }

            throw new InternalServerErrorException("There was an error saving the entity");
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V createJSONEntities(
            final Class<V> collectionClass, final Class<U> type, final RESTBaseCollectionV1<T, V, W> entities,
            final RESTDataObjectFactory<T, U, V, W> factory, final String expandName, final String expand,
            final RESTLogDetailsV1 logDetails) throws InvalidParameterException {
        return createEntities(collectionClass, type, entities, factory, expandName, RESTv1Constants.JSON_URL, expand,
                logDetails);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V updateJSONEntities(
            final Class<V> collectionClass, final Class<U> type, final RESTBaseCollectionV1<T, V, W> entities,
            final RESTDataObjectFactory<T, U, V, W> factory, final String expandName, final String expand,
            final RESTLogDetailsV1 logDetails) throws InvalidParameterException {
        return updateEntities(collectionClass, type, entities, factory, expandName, RESTv1Constants.JSON_URL, expand,
                logDetails);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V createEntities(
            final Class<V> collectionClass, final Class<U> type, final RESTBaseCollectionV1<T, V, W> entities,
            final RESTDataObjectFactory<T, U, V, W> factory, final String expandName, final String dataType,
            final String expand, final RESTLogDetailsV1 logDetails) throws InvalidParameterException {
        return createOrUdpateEntities(collectionClass, type, factory, entities, DatabaseOperation.CREATE, expandName, dataType,
                expand, logDetails);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V updateEntities(
            final Class<V> collectionClass, final Class<U> type, final RESTBaseCollectionV1<T, V, W> entities,
            final RESTDataObjectFactory<T, U, V, W> factory, final String expandName, final String dataType,
            final String expand, final RESTLogDetailsV1 logDetails) throws InvalidParameterException {
        return createOrUdpateEntities(collectionClass, type, factory, entities, DatabaseOperation.UPDATE, expandName, dataType,
                expand, logDetails);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V deleteJSONEntities(
            final Class<V> collectionClass, final Class<U> type, final RESTDataObjectFactory<T, U, V, W> factory,
            final Set<String> ids, final String expandName, final String expand, final RESTLogDetailsV1 logDetails)
            throws InvalidParameterException, InternalProcessingException {
        return deleteEntities(collectionClass, type, factory, ids, expandName, RESTv1Constants.JSON_URL, expand, logDetails);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V deleteEntities(
            final Class<V> collectionClass, final Class<U> type, final RESTDataObjectFactory<T, U, V, W> factory,
            final Set<String> ids, final String expandName, final String dataType, final String expand,
            final RESTLogDetailsV1 logDetails) throws InvalidParameterException, InternalProcessingException {
        assert type != null : "type should not be null";
        assert ids != null : "ids should not be null";
        assert factory != null : "factory should not be null";

        TransactionManager transactionManager = null;
        EntityManager entityManager = null;

        try {
            // Unmarshall the expand string into the ExpandDataTrunk object.
            final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

            // Get the TransactionManager and start a Transaction
            transactionManager = JNDIUtilities.lookupTransactionManager();
            transactionManager.begin();

            // Get an EntityManager instance
            entityManager = getEntityManager();
            entityManager.setFlushMode(FlushModeType.AUTO);

            // Store the log details into the Logging Java Bean
            setLogDetails(entityManager, logDetails);

            final List<U> retValue = new ArrayList<U>();
            for (final String id : ids) {
                /*
                 * The ids are passed as strings into a PathSegment. We need to change these into Integers
                 */
                Integer idInt = null;
                try {
                    idInt = Integer.parseInt(id);
                } catch (final Exception ex) {
                    throw new InvalidParameterException("The id " + id + " was not a valid Integer");
                }

                // Get the entity from the database and check it exists.
                final U entity = entityManager.find(type, idInt);
                if (entity == null)
                    throw new InvalidParameterException("No entity was found with the primary key " + id);

                // Delete the entity from the persistence context
                entityManager.remove(entity);

                retValue.add(entity);
            }

            // Flush the changes and commit the changes
            entityManager.flush();
            transactionManager.commit();

            return new RESTDataObjectCollectionFactory<T, U, V, W>().create(collectionClass, factory, retValue, expandName,
                    dataType, expandDataTrunk, getBaseUrl(), true, entityManager);
        } catch (final InvalidParameterException ex) {
            log.error("There was an error looking up the database entities", ex);

            if (transactionManager != null) {
                try {
                    transactionManager.rollback();
                } catch (final Exception ex2) {
                    log.error("There was an error rolling back the transaction", ex2);
                }
            }

            throw ex;
        } catch (final Exception ex) {
            log.error("Probably an error saving the entity", ex);

            if (transactionManager != null) {
                try {
                    transactionManager.rollback();
                } catch (final Exception ex2) {
                    log.error("There was an error rolling back the transaction", ex2);
                }
            }

            throw new InternalServerErrorException("There was an error saving the entity");
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    /**
     * Takes a collection of REST entities, updates or creates the corresponding database entities, and returns those database
     * entities in a collection
     * 
     * @param collectionClass The Class of the collection that should be returned.
     * @param type
     * @param factory
     * @param entities
     * @param operation
     * @param expandName
     * @param dataType
     * @param expand The expand parameters to determine what fields should be expanded.
     * @param logDetails The details about the changes that need to be logged.
     * @return
     * @throws InvalidParameterException
     */
    private <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V createOrUdpateEntities(
            final Class<V> collectionClass, final Class<U> type, final RESTDataObjectFactory<T, U, V, W> factory,
            final RESTBaseCollectionV1<T, V, W> entities, final DatabaseOperation operation, final String expandName,
            final String dataType, final String expand, final RESTLogDetailsV1 logDetails) throws InvalidParameterException {
        assert entities != null : "dataObject should not be null";
        assert factory != null : "factory should not be null";

        TransactionManager transactionManager = null;
        EntityManager entityManager = null;

        try {
            // Unmarshall the expand string into the ExpandDataTrunk object.
            final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

            // Get the TransactionManager and start a Transaction
            transactionManager = JNDIUtilities.lookupTransactionManager();
            transactionManager.begin();

            // Get an EntityManager instance
            entityManager = getEntityManager();
            entityManager.setFlushMode(FlushModeType.AUTO);

            // Store the log details into the Logging Java Bean
            setLogDetails(entityManager, logDetails);

            final List<U> retValue = new ArrayList<U>();
            for (final T restEntity : entities.returnItems()) {

                /*
                 * The difference between creating or updating an entity is that we create a new instance of U, or find an
                 * existing instance of U.
                 */
                U entity = null;
                if (operation == DatabaseOperation.UPDATE) {
                    // Have to have an ID for the entity we are deleting or updating
                    if (restEntity.getId() == null)
                        throw new BadRequestException("An id needs to be set for update operations");

                    // Load the entity from the database and verify it exists
                    entity = entityManager.find(type, restEntity.getId());
                    if (entity == null)
                        throw new BadRequestException("No entity was found with the primary key " + restEntity.getId());

                    // Sync the database entity with the REST Entity
                    factory.syncDBEntityWithRESTEntity(entityManager, entity, restEntity);
                } else if (operation == DatabaseOperation.CREATE) {
                    // Create a Database Entity using the information from the REST Entity.
                    entity = factory.createDBEntityFromRESTEntity(entityManager, restEntity);

                    // Check that the entity was successfully created
                    if (entity == null)
                        throw new BadRequestException("The entity could not be created");
                }

                // Save the created/updated entity
                entityManager.persist(entity);

                retValue.add(entity);
            }

            // Flush and commit the changes to the database.
            entityManager.flush();
            transactionManager.commit();

            return new RESTDataObjectCollectionFactory<T, U, V, W>().create(collectionClass, factory, retValue, expandName,
                    dataType, expandDataTrunk, getBaseUrl(), true, entityManager);
        } catch (final Failure ex) {
            log.error("There was an error looking up the required manager objects", ex);
            throw ex;
        } catch (final Exception ex) {
            log.error("Probably an error saving the entity", ex);

            try {
                transactionManager.rollback();
            } catch (final Exception ex2) {
                log.error("There was an error rolling back the transaction", ex2);
            }

            throw new InternalServerErrorException("There was an error saving the entity");
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    /**
     * Convert a POJO Object to JSON.
     * 
     * @param object The Object to be Converted to JSON.
     * @return The JSON representation of the Object.
     * @throws JsonGenerationException Thrown if there is an error generating the JSON for the Object.
     * @throws JsonMappingException Thrown if the Object can't be mapped to a JSON output.
     * @throws IOException
     */
    protected <T> String convertObjectToJSON(final T object) throws JsonGenerationException, JsonMappingException, IOException {
        return mapper.writeValueAsString(object);
    }

    /**
     * Wrap JSON content in a callback function that can be used for JSONP responses.
     * 
     * @param padding The Callback Function.
     * @param json The json to be wrapped in the callback function.
     * @return The JSON wrapped in the callback function.
     */
    protected String wrapJsonInPadding(final String padding, final String json) {
        return padding + "(" + json + ")";
    }

    /**
     * This method is just a wrapper for the {@link #getResource(Class, RESTDataObjectFactory, Object, Number, String, String)
     * getResource()} method that will specify that the response dataType will be JSON.
     * 
     * @param type The matching Database Entity type for the REST Entity.
     * @param dataObjectFactory The REST Object Factory to generate the REST Entity.
     * @param id The ID of the database entity to generate the REST Entity for.
     * @param expand The expand parameters to determine what fields should be expanded.
     * @return The REST Entity containing the information from the database entity.
     * @throws InvalidParameterException Thrown if any of the parsed parameters are invalid.
     * @throws InternalProcessingException Thrown if an error occurs during processing the request.
     */
    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T getJSONResource(
            final Class<U> type, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final Object id, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return getJSONResource(type, dataObjectFactory, id, null, expand);
    }

    /**
     * This method is just a wrapper for the {@link #getResource(Class, RESTDataObjectFactory, Object, Number, String, String)
     * getResource()} method that will specify that the response dataType will be JSON.
     * 
     * @param type The matching Database Entity type for the REST Entity.
     * @param dataObjectFactory The REST Object Factory to generate the REST Entity.
     * @param id The ID of the database entity to generate the REST Entity for.
     * @param revision The Revision of the entity to use to get the database entity.
     * @param expand The expand parameters to determine what fields should be expanded.
     * @return The REST Entity containing the information from the database entity.
     * @throws InvalidParameterException Thrown if any of the parsed parameters are invalid.
     * @throws InternalProcessingException Thrown if an error occurs during processing the request.
     */
    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T getJSONResource(
            final Class<U> type, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final Object id,
            final Number revision, final String expand) throws InvalidParameterException, InternalProcessingException {
        return getResource(type, dataObjectFactory, id, revision, expand, RESTv1Constants.JSON_URL);
    }

    /**
     * This method is just a wrapper for the {@link #getResource(Class, RESTDataObjectFactory, Object, Number, String, String)
     * getResource()} method that will specify that the response dataType will be XML.
     * 
     * @param type The matching Database Entity type for the REST Entity.
     * @param dataObjectFactory The REST Object Factory to generate the REST Entity.
     * @param id The ID of the database entity to generate the REST Entity for.
     * @param expand The expand parameters to determine what fields should be expanded.
     * @return The REST Entity containing the information from the database entity.
     * @throws InvalidParameterException Thrown if any of the parsed parameters are invalid.
     * @throws InternalProcessingException Thrown if an error occurs during processing the request.
     */
    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T getXMLResource(
            final Class<U> type, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final Object id, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return getXMLResource(type, dataObjectFactory, id, null, expand);
    }

    /**
     * This method is just a wrapper for the {@link #getResource(Class, RESTDataObjectFactory, Object, Number, String, String)
     * getResource()} method that will specify that the response dataType will be XML.
     * 
     * @param type The matching Database Entity type for the REST Entity.
     * @param dataObjectFactory The REST Object Factory to generate the REST Entity.
     * @param id The ID of the database entity to generate the REST Entity for.
     * @param revision The Revision of the entity to use to get the database entity.
     * @param expand The expand parameters to determine what fields should be expanded.
     * @return The REST Entity containing the information from the database entity.
     * @throws InvalidParameterException Thrown if any of the parsed parameters are invalid.
     * @throws InternalProcessingException Thrown if an error occurs during processing the request.
     */
    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T getXMLResource(
            final Class<U> type, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final Object id,
            final Number revision, final String expand) throws InvalidParameterException, InternalProcessingException {
        return getResource(type, dataObjectFactory, id, revision, expand, RESTv1Constants.XML_URL);
    }

    /**
     * Generate the a REST Entity using an Entity resource from the database.
     * 
     * @param type The matching Database Entity type for the REST Entity.
     * @param dataObjectFactory The REST Object Factory to generate the REST Entity.
     * @param id The ID of the database entity to generate the REST Entity for.
     * @param revision The Revision of the entity to use to get the database entity.
     * @param expand The expand parameters to determine what fields should be expanded.
     * @param dataType The output data type. eg JSON or XML.
     * @return The REST Entity containing the information from the database entity.
     * @throws InvalidParameterException Thrown if any of the parsed parameters are invalid.
     * @throws InternalProcessingException Thrown if an error occurs during processing the request.
     */
    private <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T getResource(
            final Class<U> type, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final Object id,
            final Number revision, final String expand, final String dataType) throws InvalidParameterException,
            InternalProcessingException {
        assert type != null : "The type parameter can not be null";
        assert id != null : "The id parameter can not be null";
        assert dataObjectFactory != null : "The dataObjectFactory parameter can not be null";

        boolean usingRevisions = revision != null;
        Number closestRevision = null;

        EntityManager entityManager = null;

        try {
            // Unmarshall the expand string into the ExpandDataTrunk object.
            final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

            // Get an EntityManager instance
            entityManager = getEntityManager();

            /*
             * Load the Entity from the Database. If we aren't getting revision information then we can just use a normal
             * lookup. If we are getting a revision entity then we need to first do a Envers query to find the closest relevant
             * revision and then get that instance of the Entity.
             */
            final U entity;

            if (usingRevisions) {
                // Find the closest revision that is less then the revision specified.
                final AuditReader reader = AuditReaderFactory.get(entityManager);
                closestRevision = (Number) reader.createQuery().forRevisionsOfEntity(type, false, true)
                        .addProjection(AuditEntity.revisionNumber().max()).add(AuditEntity.id().eq(id))
                        .add(AuditEntity.revisionNumber().le(revision)).getSingleResult();

                // Get the Revision Entity using an envers lookup.
                entity = reader.find(type, id, closestRevision);
                
                if (entity == null)
                    throw new BadRequestException("No entity was found with the primary key " + id);

                // Set the entities last modified date to the information assoicated with the revision.
                final Date revisionLastModified = reader.getRevisionDate(closestRevision);
                entity.setLastModifiedDate(revisionLastModified);
            } else {
                entity = entityManager.find(type, id);
            }
            
            if (entity == null)
                throw new BadRequestException("No entity was found with the primary key " + id);

            // Create the REST representation of the topic
            final T restRepresentation = dataObjectFactory.createRESTEntityFromDBEntity(entity, this.getBaseUrl(), dataType,
                    expandDataTrunk, closestRevision, true, entityManager);

            return restRepresentation;
        } catch (final InternalProcessingException ex) {
            throw ex;
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    protected <U> U getEntity(final Class<U> type, final Object id) throws InternalProcessingException {
        final EntityManager entityManager = getEntityManager();

        final U entity = entityManager.find(type, id);
        if (entity == null)
            throw new BadRequestException("No entity was found with the primary key " + id);

        return entity;
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V getXMLResources(
            final Class<V> collectionClass, final Class<U> type, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory,
            final String expandName, final String expand) throws InvalidParameterException, InternalProcessingException {
        return getResources(collectionClass, type, dataObjectFactory, expandName, expand, RESTv1Constants.XML_URL);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V getJSONResources(
            final Class<V> collectionClass, final Class<U> type, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory,
            final String expandName, final String expand) throws InvalidParameterException, InternalProcessingException {
        return getResources(collectionClass, type, dataObjectFactory, expandName, expand, RESTv1Constants.JSON_URL);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V getResources(
            final Class<V> collectionClass, final Class<U> type, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory,
            final String expandName, final String expand, final String dataType) throws InvalidParameterException,
            InternalProcessingException {
        assert type != null : "The type parameter can not be null";
        assert dataObjectFactory != null : "The dataObjectFactory parameter can not be null";

        EntityManager entityManager = null;

        try {
            // Unmarshall the expand string into the ExpandDataTrunk object.
            final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

            // Get an EntityManager instance
            entityManager = getEntityManager();

            // Create the select all query
            final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            final CriteriaQuery<U> criteriaQuery = criteriaBuilder.createQuery(type);
            criteriaQuery.from(type);

            /*
             * TODO This really should be constrained to the data that we need using the setMaxResults() and setFirstResult()
             * method. It should also only be executed if the expand is specified.
             */
            // Execute the query and retrieve the results from the database
            final TypedQuery<U> query = entityManager.createQuery(criteriaQuery);
            final List<U> result = query.getResultList();

            // Create and initialise the Collection using the specified REST Object Factory
            final V retValue = new RESTDataObjectCollectionFactory<T, U, V, W>().create(collectionClass, dataObjectFactory,
                    result, expandName, dataType, expandDataTrunk, getBaseUrl(), true, entityManager);

            return retValue;
        } catch (final InternalProcessingException ex) {
            throw ex;
        } catch (final Exception ex) {
            throw new InvalidParameterException("Internal processing error");
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    /**
     * Get a set of entity resources that will be JSON Encoded using a URL Query Parameter Map.
     * 
     * @param collectionClass The Class of the collection that should be returned.
     * @param queryParams The map of URL Query Parameters to use when searching.
     * @param filterQueryBuilderClass The Class of the query builder to be used.
     * @param entityFieldFilter A custom Field filter to be used by the Filter Query Builder.
     * @param dataObjectFactory The Collection Factory object to be used to generate the contents of the collection.
     * @param expandName The name that should be used to expand the collection.
     * @param expand The Expand Object that contains details about what should be expanded.
     * @return A Collection of Entities represented as the passed collectionClass.
     * @throws InternalProcessingException
     * @throws InvalidParameterException
     */
    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V getJSONResourcesFromQuery(
            final Class<V> collectionClass, final MultivaluedMap<String, String> queryParams,
            final Class<? extends IFilterQueryBuilder<U>> filterQueryBuilderClass, final IFieldFilter entityFieldFilter,
            final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final String expandName, final String expand)
            throws InternalProcessingException, InvalidParameterException {
        return getResourcesFromQuery(collectionClass, queryParams, filterQueryBuilderClass, entityFieldFilter,
                dataObjectFactory, expandName, expand, RESTv1Constants.JSON_URL);
    }

    /**
     * Get a set of entity resources using a URL Query Parameter Map.
     * 
     * @param collectionClass The Class of the collection that should be returned.
     * @param queryParams The map of URL Query Parameters to use when searching.
     * @param filterQueryBuilderClass The Class of the query builder to be used.
     * @param entityFieldFilter A custom Field filter to be used by the Filter Query Builder.
     * @param dataObjectFactory The Collection Factory object to be used to generate the contents of the collection.
     * @param expandName The name that should be used to expand the collection.
     * @param expand The Expand Object that contains details about what should be expanded.
     * @param dataType The MIME data type that should be returned and used for entity URL links.
     * @return A Collection of Entities represented as the passed collectionClass.
     * @throws InternalProcessingException
     * @throws InvalidParameterException
     */
    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V getResourcesFromQuery(
            final Class<V> collectionClass, final MultivaluedMap<String, String> queryParams,
            final Class<? extends IFilterQueryBuilder<U>> filterQueryBuilderClass, final IFieldFilter entityFieldFilter,
            final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final String expandName, final String expand,
            final String dataType) throws InternalProcessingException, InvalidParameterException {
        assert dataObjectFactory != null : "The dataObjectFactory parameter can not be null";
        assert uriInfo != null : "uriInfo can not be null";

        EntityManager entityManager = null;

        try {
            // Unmarshall the expand string into the ExpandDataTrunk object.
            final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

            // Get an EntityManager instance
            entityManager = getEntityManager();

            // build up a Filter object from the URL variables
            final Filter filter;
            final IFilterQueryBuilder<U> filterQueryBuilder = filterQueryBuilderClass.getConstructor(EntityManager.class)
                    .newInstance(entityManager);
            if (filterQueryBuilder instanceof ITagFilterQueryBuilder) {
                filter = EntityUtilities.populateFilter(entityManager, queryParams, CommonFilterConstants.FILTER_ID,
                        CommonFilterConstants.MATCH_TAG, CommonFilterConstants.GROUP_TAG,
                        CommonFilterConstants.CATEORY_INTERNAL_LOGIC, CommonFilterConstants.CATEORY_EXTERNAL_LOGIC,
                        CommonFilterConstants.MATCH_LOCALE, entityFieldFilter);
            } else {
                filter = EntityUtilities.populateFilter(entityManager, queryParams, CommonFilterConstants.MATCH_LOCALE,
                        entityFieldFilter);
            }

            // Build the query to be used to get the resources
            final CriteriaQuery<U> query = FilterUtilities.buildQuery(filter, filterQueryBuilder);

            /*
             * TODO This really should be constrained to the data that we need using the setMaxResults() and setFirstResult()
             * method. It should also only be executed if the expand is specified.
             */
            // Retrieve the results using the generated query.
            final List<U> result = entityManager.createQuery(query).getResultList();

            // Create the Collection Class and populate it with data using the query result data
            final V retValue = new RESTDataObjectCollectionFactory<T, U, V, W>().create(collectionClass, dataObjectFactory,
                    result, expandName, dataType, expandDataTrunk, getBaseUrl(), true, entityManager);

            return retValue;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        } finally {
            if (entityManager != null)
                entityManager.close();
        }
    }

    /**
     * Set the log details for the current request. This will use the injected EnversLoggingBean object to set the logging
     * details for Envers.
     * 
     * @param entityManager An EntityManager Object that can be used to look up database entities.
     * @param dataObject The LogDetails object that contains the details to be associted with in the log.
     */
    private void setLogDetails(final EntityManager entityManager, final RESTLogDetailsV1 dataObject) {
        if (dataObject == null)
            return;

        if (dataObject.hasParameterSet(RESTLogDetailsV1.MESSAGE_NAME))
            enversLoggingBean.addLogMessage(dataObject.getMessage());
        if (dataObject.hasParameterSet(RESTLogDetailsV1.FLAG_NAME))
            enversLoggingBean.setFlag(dataObject.getFlag());
        if (dataObject.hasParameterSet(RESTLogDetailsV1.USERNAME_NAME)) {
            if (dataObject.getUser() != null && dataObject.getUser().getId() != null) {
                final User user = entityManager.find(User.class, dataObject.getUser().getId());

                if (user == null)
                    throw new BadRequestException("No user entity was found with the primary key "
                            + dataObject.getUser().getId());

                enversLoggingBean.setUsername(user.getUserName());
            }
        }
    }

    /**
     * Generate a RESTLogDetails object from a set of URL passed parameters.
     * 
     * @param message The message to be associated with the log.
     * @param flag The Message Flags. (ie Minor or Major change).
     * @param userId The ID of the user who has made the changes.
     * @return A pre-populated RESTLogDetailsV1 object.
     */
    protected RESTLogDetailsV1 generateLogDetails(final String message, final Integer flag, final Integer userId) {
        final RESTLogDetailsV1 logDetails = new RESTLogDetailsV1();

        if (flag != null)
            logDetails.explicitSetFlag(flag);
        if (message != null)
            logDetails.explicitSetMessage(message);
        if (userId != null) {
            final RESTUserV1 user = new RESTUserV1();
            user.setId(userId);
            logDetails.explicitSetUser(user);
        }

        return logDetails;
    }

    /**
     * Convert a String Expand representation into an ExpandDataTrunk object.
     * 
     * @param expand The String representation for the expand.
     * @return An ExpandDataTrunk object containing the converted data.
     * @throws InvalidParameterException Thrown if the expand string can't be parsed as JSON or mapped to the ExpandDataTrunk
     *         class.
     */
    protected ExpandDataTrunk unmarshallExpand(final String expand) throws InvalidParameterException {
        try {
            /*
             * convert the expand string from JSON to an instance of ExpandDataTrunk
             */
            ExpandDataTrunk expandDataTrunk = new ExpandDataTrunk();
            if (expand != null && !expand.trim().isEmpty()) {
                expandDataTrunk = mapper.readValue(expand, ExpandDataTrunk.class);
            }

            return expandDataTrunk;
        } catch (final JsonParseException ex) {
            throw new InvalidParameterException("Could not convert expand data from JSON to an instance of ExpandDataTrunk");
        } catch (final JsonMappingException ex) {
            throw new InvalidParameterException("Could not convert expand data from JSON to an instance of ExpandDataTrunk");
        } catch (final IOException ex) {
            throw new InvalidParameterException("Could not convert expand data from JSON to an instance of ExpandDataTrunk");
        }
    }

    /**
     * Get an EntityManager instance from the EntityManagerFactory. If the Factory hasn't been looked up yet, then perform the
     * lookup as well.
     * 
     * Note: This method won't join any active Transactions.
     * 
     * @return An intialised EntityManager object.
     * @throws InternalProcessingException
     */
    protected EntityManager getEntityManager() throws InternalProcessingException {
        return getEntityManager(false);
    }

    /**
     * Get an EntityManager instance from the EntityManagerFactory. If the Factory hasn't been looked up yet, then perform the
     * lookup as well. *
     * 
     * @param joinTransaction Whether or not the EntityManager should attempt to join any active Transactions.
     * @return An intialised EntityManager object.
     * @throws InternalProcessingException
     */
    protected EntityManager getEntityManager(boolean joinTransaction) throws InternalProcessingException {
        EntityManagerFactory entityManagerFactory = null;
        try {
            entityManagerFactory = JNDIUtilities.lookupEntityManagerFactory();
        } catch (NamingException e) {
            throw new InternalProcessingException("Could not find the EntityManagerFactory");
        }

        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        if (entityManager == null)
            throw new InternalProcessingException("Could not create an EntityManager");

        if (joinTransaction) {
            entityManager.joinTransaction();
        }

        return entityManager;
    }
}
