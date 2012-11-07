package org.jboss.pressgang.ccms.restserver.rest.v1.base;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.naming.InitialContext;
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
import org.jboss.pressgang.ccms.utils.constants.CommonConstants;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.ejb.EnversLoggingBean;
import org.jboss.pressgang.ccms.restserver.entity.Filter;
import org.jboss.pressgang.ccms.restserver.entity.User;
import org.jboss.pressgang.ccms.restserver.entity.base.AuditedEntity;
import org.jboss.pressgang.ccms.restserver.filter.base.IFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.base.IFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.base.ITagFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.rest.DatabaseOperation;
import org.jboss.pressgang.ccms.restserver.utils.Constants;
import org.jboss.pressgang.ccms.restserver.utils.EntityUtilities;
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
    protected static final String REST_DATE_FORMAT = "dd-MMM-yyyy";

    public static final String TOPICS_EXPANSION_NAME = "topics";
    public static final String IMAGES_EXPANSION_NAME = "images";
    public static final String TAGS_EXPANSION_NAME = "tags";
    public static final String PROJECTS_EXPANSION_NAME = "projects";
    public static final String USERS_EXPANSION_NAME = "users";
    public static final String BLOBCONSTANTS_EXPANSION_NAME = "blobConstants";
    public static final String STRINGCONSTANTS_EXPANSION_NAME = "stringConstants";
    public static final String INTEGERCONSTANTS_EXPANSION_NAME = "integerConstants";
    public static final String PROPERTYTAGS_EXPANSION_NAME = "propertyTags";
    public static final String PROPERTY_CATEGORIES_EXPANSION_NAME = "propertyCategories";
    public static final String ROLES_EXPANSION_NAME = "roles";
    public static final String TRANSLATEDTOPICS_EXPANSION_NAME = "translatedTopics";
    public static final String TRANSLATEDTOPICSTRINGS_EXPANSION_NAME = "translatedTopicStrings";

    public static final String FILTERS_EXPANSION_NAME = "filters";
    public static final String FILTER_TAGS_EXPANSION_NAME = "filterTags";
    public static final String FILTER_CATEGORIES_EXPANSION_NAME = "filterCategories";
    public static final String FILTER_LOCALES_EXPANSION_NAME = "filterLocales";
    public static final String FILTER_FIELDS_EXPANSION_NAME = "filterFields";

    public static final String CATEGORIES_EXPANSION_NAME = "categories";

    public static final String CONTENT_SPEC_EXPANSION_NAME = "contentSpecs";
    public static final String CONTENT_SPEC_NODE_EXPANSION_NAME = "nodes";
    public static final String CONTENT_SPEC_META_DATA_EXPANSION_NAME = "metaData";

    public static final String TOPIC_URL_NAME = "topic";
    public static final String TOPICSOURCEURL_URL_NAME = "topicsourceurl";
    public static final String BUGZILLABUG_URL_NAME = "bugzillabug";
    public static final String TRANSLATEDTOPIC_URL_NAME = "translatedtopic";
    public static final String TRANSLATEDTOPICDATA_URL_NAME = "translatedtopicdata";
    public static final String TRANSLATEDTOPICSTRING_URL_NAME = "translatedtopicstring";
    public static final String PROJECT_URL_NAME = "project";
    public static final String TAG_URL_NAME = "tag";
    public static final String CATEGORY_URL_NAME = "category";
    public static final String USER_URL_NAME = "user";
    public static final String BLOBCONSTANT_URL_NAME = "blobconstant";
    public static final String STRINGCONSTANT_URL_NAME = "stringconstant";
    public static final String INTEGERCONSTANT_URL_NAME = "integerconstant";

    public static final String IMAGE_URL_NAME = "image";
    public static final String LANGUAGEIMAGE_URL_NAME = "languageimage";
    public static final String PROPERTYTAG_URL_NAME = "propertytag";
    public static final String PROPERTY_CATEGORY_URL_NAME = "propertycategory";
    public static final String ROLE_URL_NAME = "role";

    public static final String FILTER_URL_NAME = "filter";
    public static final String FILTER_TAG_URL_NAME = "filtertag";
    public static final String FILTER_LOCALE_URL_NAME = "filterlocale";
    public static final String FILTER_CATEGORY_URL_NAME = "filtercategory";
    public static final String FILTER_FIELD_URL_NAME = "filterfield";

    public static final String CONTENT_SPEC_URL_NAME = "contentspec";
    public static final String CONTENT_SPEC_NODE_URL_NAME = "contentspecnode";
    public static final String CONTENT_SPEC_META_DATA_URL_NAME = "contentspecmetadata";

    public static final String JSON_URL = "json";
    public static final String XML_URL = "json";

    private final ObjectMapper mapper = new ObjectMapper();

    @Context
    private UriInfo uriInfo;
    
    @Inject
    private EnversLoggingBean enversLoggingBean;

    protected String getBaseUrl() {
        final String fullPath = uriInfo.getAbsolutePath().toString();
        final int index = fullPath.indexOf(Constants.BASE_REST_PATH);
        if (index != -1)
            return fullPath.substring(0, index + Constants.BASE_REST_PATH.length());

        return null;
    }

    protected String getUrl() {
        return uriInfo.getAbsolutePath().toString();
    }

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

    private String fixHrefs(final String input) {
        return input.replaceAll("Topic\\.seam", CommonConstants.FULL_SERVER_URL + "/Topic.seam");
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V getJSONEntitiesUpdatedSince(
            final Class<V> collectionClass, final Class<U> type, final String idProperty,
            final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final String expandName, final String expand,
            final Date date) throws InvalidParameterException {
        return getEntitiesUpdatedSince(collectionClass, type, idProperty, dataObjectFactory, expandName, expand, JSON_URL, date);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V getEntitiesUpdatedSince(
            final Class<V> collectionClass, final Class<U> type, final String idProperty,
            final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final String expandName, final String expand,
            final String dataType, final Date date) throws InvalidParameterException {
        assert date != null : "The date parameter can not be null";

        EntityManager entityManager = null;
        TransactionManager transactionManager = null;

        try {
            final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

            final InitialContext initCtx = new InitialContext();

            final EntityManagerFactory entityManagerFactory = (EntityManagerFactory) initCtx
                    .lookup("java:jboss/EntityManagerFactory");
            if (entityManagerFactory == null)
                throw new InternalServerErrorException("Could not find the EntityManagerFactory");

            transactionManager = (TransactionManager) initCtx.lookup("java:jboss/TransactionManager");
            if (transactionManager == null)
                throw new InternalServerErrorException("Could not find the TransactionManager");

            assert transactionManager != null : "transactionManager should not be null";
            assert entityManagerFactory != null : "entityManagerFactory should not be null";

            transactionManager.begin();

            entityManager = entityManagerFactory.createEntityManager();
            if (entityManager == null)
                throw new InternalServerErrorException("Could not create an EntityManager");

            /*
             * get the list of topic ids that were edited after the selected date
             */
            final AuditReader reader = AuditReaderFactory.get(entityManager);
            final AuditQuery query = reader.createQuery().forRevisionsOfEntity(type, true, false)
                    .addOrder(AuditEntity.revisionProperty("timestamp").asc())
                    .add(AuditEntity.revisionProperty("timestamp").ge(date.getTime()))
                    .addProjection(AuditEntity.property("originalId." + idProperty).distinct());

            @SuppressWarnings("rawtypes")
            final List entityyIds = query.getResultList();

            /* now get the topics */
            final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            final CriteriaQuery<U> criteriaQuery = criteriaBuilder.createQuery(type);
            final Root<U> root = criteriaQuery.from(type);
            criteriaQuery.where(root.get(idProperty).in(entityyIds));

            final TypedQuery<U> jpaQuery = entityManager.createQuery(criteriaQuery);

            final List<U> entities = jpaQuery.getResultList();

            transactionManager.commit();

            final V retValue = new RESTDataObjectCollectionFactory<T, U, V, W>().create(collectionClass, dataObjectFactory,
                    entities, expandName, dataType, expandDataTrunk, getBaseUrl(), entityManager);

            return retValue;
        } catch (final NamingException ex) {
            throw new InternalServerErrorException("Could not find the EntityManagerFactory");
        } catch (final Exception ex) {
            log.error("Probably an issue querying Envers", ex);

            if (transactionManager != null) {
                try {
                    transactionManager.rollback();
                } catch (final Exception ex2) {
                    log.error("There was an issue rolling back the transaction", ex2);
                }
            }

            throw new InternalServerErrorException("There was an error running the query");
        } finally {
            if (entityManager != null)
                entityManager.close();
        }

    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T createEntity(
            final Class<U> type, final T restEntity, final RESTDataObjectFactory<T, U, V, W> factory, final String baseUrl,
            final String dataType, final String expand, final RESTLogDetailsV1 logDetails) throws InternalProcessingException,
            InvalidParameterException {
        return createOrUpdateEntity(type, restEntity, factory, DatabaseOperation.CREATE, baseUrl, dataType, expand, logDetails);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T updateEntity(
            final Class<U> type, final T restEntity, final RESTDataObjectFactory<T, U, V, W> factory, final String baseUrl,
            final String dataType, final String expand, final RESTLogDetailsV1 logDetails) throws InvalidParameterException {
        return createOrUpdateEntity(type, restEntity, factory, DatabaseOperation.UPDATE, baseUrl, dataType, expand, logDetails);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T deleteEntity(
            final Class<U> type, final RESTDataObjectFactory<T, U, V, W> factory, final Integer id, final String expand, final RESTLogDetailsV1 logDetails) throws InvalidParameterException {
        assert id != null : "id should not be null";

        TransactionManager transactionManager = null;
        EntityManager entityManager = null;

        try {
            final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

            final InitialContext initCtx = new InitialContext();

            final EntityManagerFactory entityManagerFactory = (EntityManagerFactory) initCtx
                    .lookup("java:jboss/EntityManagerFactory");
            if (entityManagerFactory == null)
                throw new InternalServerErrorException("Could not find the EntityManagerFactory");

            transactionManager = (TransactionManager) initCtx.lookup("java:jboss/TransactionManager");
            if (transactionManager == null)
                throw new InternalServerErrorException("Could not find the TransactionManager");

            assert transactionManager != null : "transactionManager should not be null";
            assert entityManagerFactory != null : "entityManagerFactory should not be null";

            transactionManager.begin();

            entityManager = entityManagerFactory.createEntityManager();
            if (entityManager == null)
                throw new InternalServerErrorException("Could not create an EntityManager");

            assert entityManager != null : "entityManager should not be null";

            setLogDetails(entityManager, logDetails);

            final U entity = entityManager.find(type, id);

            if (entity == null)
                throw new BadRequestException("No entity was found with the id " + id);

            entityManager.remove(entity);
            entityManager.flush();
            transactionManager.commit();

            return factory.createRESTEntityFromDBEntity(entity, this.getBaseUrl(), JSON_URL, expandDataTrunk);
        } catch (final InvalidParameterException ex) {
            log.error("There was an error looking up the entities", ex);
            throw ex;
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

    private <T extends RESTBaseEntityV1<T, V, W>, U, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T createOrUpdateEntity(
            final Class<U> type, final T restEntity, final RESTDataObjectFactory<T, U, V, W> factory,
            final DatabaseOperation operation, final String baseUrl, final String dataType, final String expand,
            final RESTLogDetailsV1 logDetails) throws InvalidParameterException {
        assert restEntity != null : "restEntity should not be null";
        assert factory != null : "factory should not be null";

        TransactionManager transactionManager = null;
        EntityManager entityManager = null;

        try {
            final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

            final InitialContext initCtx = new InitialContext();

            final EntityManagerFactory entityManagerFactory = (EntityManagerFactory) initCtx
                    .lookup("java:jboss/EntityManagerFactory");
            if (entityManagerFactory == null)
                throw new InternalServerErrorException("Could not find the EntityManagerFactory");

            transactionManager = (TransactionManager) initCtx.lookup("java:jboss/TransactionManager");
            if (transactionManager == null)
                throw new InternalServerErrorException("Could not find the TransactionManager");

            assert transactionManager != null : "transactionManager should not be null";
            assert entityManagerFactory != null : "entityManagerFactory should not be null";

            transactionManager.begin();

            entityManager = entityManagerFactory.createEntityManager();
            if (entityManager == null)
                throw new InternalServerErrorException("Could not create an EntityManager");

            assert entityManager != null : "entityManager should not be null";

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

                entity = entityManager.find(type, restEntity.getId());

                if (entity == null)
                    throw new InvalidParameterException("No entity was found with the primary key " + restEntity.getId());

                factory.syncDBEntityWithRESTEntity(entityManager, entity, restEntity);

            } else if (operation == DatabaseOperation.CREATE) {
                entity = factory.createDBEntityFromRESTEntity(entityManager, restEntity);

                if (entity == null)
                    throw new InvalidParameterException("The entity could not be created");
            }

            assert entity != null : "entity should not be null";

            entityManager.flush();
            transactionManager.commit();

            return factory.createRESTEntityFromDBEntity(entity, this.getBaseUrl(), JSON_URL, expandDataTrunk, null, true,
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

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V createEntities(
            final Class<V> collectionClass, final Class<U> type, final RESTBaseCollectionV1<T, V, W> entities,
            final RESTDataObjectFactory<T, U, V, W> factory, final String expandName, final String dataType,
            final String expand, final String baseUrl, final RESTLogDetailsV1 logDetails) throws InvalidParameterException {
        return createOrUdpateEntities(collectionClass, type, factory, entities, DatabaseOperation.CREATE, expandName, dataType,
                expand, baseUrl, logDetails);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V updateEntities(
            final Class<V> collectionClass, final Class<U> type, final RESTBaseCollectionV1<T, V, W> entities,
            final RESTDataObjectFactory<T, U, V, W> factory, final String expandName, final String dataType,
            final String expand, final String baseUrl, final RESTLogDetailsV1 logDetails) throws InvalidParameterException {
        return createOrUdpateEntities(collectionClass, type, factory, entities, DatabaseOperation.UPDATE, expandName, dataType,
                expand, baseUrl, logDetails);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V deleteEntities(
            final Class<V> collectionClass, final Class<U> type, final RESTDataObjectFactory<T, U, V, W> factory,
            final Set<String> ids, final String expandName, final String dataType, final String expand, final String baseUrl,
            final RESTLogDetailsV1 logDetails) throws InvalidParameterException, InternalProcessingException {
        assert type != null : "type should not be null";
        assert ids != null : "ids should not be null";
        assert factory != null : "factory should not be null";

        TransactionManager transactionManager = null;
        EntityManager entityManager = null;

        try {
            final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

            final InitialContext initCtx = new InitialContext();

            final EntityManagerFactory entityManagerFactory = (EntityManagerFactory) initCtx
                    .lookup("java:jboss/EntityManagerFactory");
            if (entityManagerFactory == null)
                throw new InternalServerErrorException("Could not find the EntityManagerFactory");

            transactionManager = (TransactionManager) initCtx.lookup("java:jboss/TransactionManager");
            if (transactionManager == null)
                throw new InternalServerErrorException("Could not find the TransactionManager");

            assert transactionManager != null : "transactionManager should not be null";
            assert entityManagerFactory != null : "entityManagerFactory should not be null";

            transactionManager.begin();

            entityManager = entityManagerFactory.createEntityManager();
            if (entityManager == null)
                throw new InternalServerErrorException("Could not create an EntityManager");

            assert entityManager != null : "entityManager should not be null";

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

                final U entity = entityManager.find(type, idInt);

                if (entity == null)
                    throw new InvalidParameterException("No entity was found with the primary key " + id);

                entityManager.remove(entity);
                entityManager.flush();

                retValue.add(entity);
            }

            transactionManager.commit();

            return new RESTDataObjectCollectionFactory<T, U, V, W>().create(collectionClass, factory, retValue, expandName,
                    dataType, expandDataTrunk, baseUrl, true, entityManager);
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
     * @throws InvalidParameterException
     */
    private <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V createOrUdpateEntities(
            final Class<V> collectionClass, final Class<U> type, final RESTDataObjectFactory<T, U, V, W> factory,
            final RESTBaseCollectionV1<T, V, W> entities, final DatabaseOperation operation, final String expandName,
            final String dataType, final String expand, final String baseUrl, final RESTLogDetailsV1 logDetails)
            throws InvalidParameterException {
        assert entities != null : "dataObject should not be null";
        assert factory != null : "factory should not be null";

        TransactionManager transactionManager = null;
        EntityManager entityManager = null;

        try {
            final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

            final InitialContext initCtx = new InitialContext();

            final EntityManagerFactory entityManagerFactory = (EntityManagerFactory) initCtx
                    .lookup("java:jboss/EntityManagerFactory");
            if (entityManagerFactory == null)
                throw new InternalServerErrorException("Could not find the EntityManagerFactory");

            transactionManager = (TransactionManager) initCtx.lookup("java:jboss/TransactionManager");
            if (transactionManager == null)
                throw new InternalServerErrorException("Could not find the TransactionManager");

            assert transactionManager != null : "transactionManager should not be null";
            assert entityManagerFactory != null : "entityManagerFactory should not be null";

            transactionManager.begin();

            entityManager = entityManagerFactory.createEntityManager();
            if (entityManager == null)
                throw new InternalServerErrorException("Could not create an EntityManager");

            entityManager.setFlushMode(FlushModeType.AUTO);

            assert entityManager != null : "entityManager should not be null";

            setLogDetails(entityManager, logDetails);

            final List<U> retValue = new ArrayList<U>();
            for (final T restEntity : entities.returnItems()) {

                /*
                 * The difference between creating or updating an entity is that we create a new instance of U, or find an
                 * existing instance of U.
                 */
                U entity = null;
                if (operation == DatabaseOperation.UPDATE) {
                    /*
                     * Have to have an ID for the entity we are deleting or updating
                     */
                    if (restEntity.getId() == null)
                        throw new BadRequestException("An id needs to be set for update operations");

                    entity = entityManager.find(type, restEntity.getId());

                    if (entity == null)
                        throw new BadRequestException("No entity was found with the primary key " + restEntity.getId());

                    factory.syncDBEntityWithRESTEntity(entityManager, entity, restEntity);
                } else if (operation == DatabaseOperation.CREATE) {
                    entity = factory.createDBEntityFromRESTEntity(entityManager, restEntity);

                    if (entity == null)
                        throw new BadRequestException("The entity could not be created");
                }

                assert entity != null : "entity should not be null";

                entityManager.persist(entity);
                entityManager.flush();

                retValue.add(entity);
            }

            transactionManager.commit();

            return new RESTDataObjectCollectionFactory<T, U, V, W>().create(collectionClass, factory, retValue, expandName,
                    dataType, expandDataTrunk, baseUrl, true, entityManager);
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

    protected <T> String convertObjectToJSON(final T object) throws JsonGenerationException, JsonMappingException, IOException {
        return mapper.writeValueAsString(object);
    }

    protected String wrapJsonInPadding(final String padding, final String json) {
        return padding + "(" + json + ")";
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T getJSONResource(
            final Class<U> type, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final Object id, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return getJSONResource(type, dataObjectFactory, id, null, expand);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T getJSONResource(
            final Class<U> type, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final Object id,
            final Number revision, final String expand) throws InvalidParameterException, InternalProcessingException {
        return getResource(type, dataObjectFactory, id, revision, expand, JSON_URL);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T getXMLResource(
            final Class<U> type, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final Object id, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return getXMLResource(type, dataObjectFactory, id, null, expand);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T getXMLResource(
            final Class<U> type, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final Object id,
            final Number revision, final String expand) throws InvalidParameterException, InternalProcessingException {
        return getResource(type, dataObjectFactory, id, revision, expand, XML_URL);
    }

    private <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> T getResource(
            final Class<U> type, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final Object id,
            final Number revision, final String expand, final String dataType) throws InvalidParameterException,
            InternalProcessingException {
        assert type != null : "The type parameter can not be null";
        assert id != null : "The id parameter can not be null";
        assert dataObjectFactory != null : "The dataObjectFactory parameter can not be null";

        boolean usingRevisions = revision != null;
        Number closestRevision = null;

        try {
            final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

            final InitialContext initCtx = new InitialContext();

            final EntityManagerFactory entityManagerFactory = (EntityManagerFactory) initCtx
                    .lookup("java:jboss/EntityManagerFactory");
            if (entityManagerFactory == null)
                throw new InternalServerErrorException("Could not find the EntityManagerFactory");

            final EntityManager entityManager = entityManagerFactory.createEntityManager();
            if (entityManager == null)
                throw new InternalServerErrorException("Could not create an EntityManager");

            final U entity;

            if (usingRevisions) {
                /*
                 * Looking up an Envers previous version is an expensive operation. So instead of getting a complete collection
                 * and only adding those we need to the REST collection (like we do with standard related entities in the
                 * database), when it comes to Envers we only retrieve the previous versions when they are specifically
                 * requested.
                 */

                final AuditReader reader = AuditReaderFactory.get(entityManager);

                closestRevision = (Number) reader.createQuery().forRevisionsOfEntity(type, false, true)
                        .addProjection(AuditEntity.revisionNumber().max()).add(AuditEntity.id().eq(id))
                        .add(AuditEntity.revisionNumber().le(revision)).getSingleResult();

                entity = reader.find(type, id, closestRevision);
                final Date revisionLastModified = reader.getRevisionDate(closestRevision);
                entity.setLastModifiedDate(revisionLastModified);
            } else {
                entity = entityManager.find(type, id);
            }
            if (entity == null)
                throw new BadRequestException("No entity was found with the primary key " + id);

            /* create the REST representation of the topic */
            final T restRepresentation = dataObjectFactory.createRESTEntityFromDBEntity(entity, this.getBaseUrl(), dataType,
                    expandDataTrunk, closestRevision, true, entityManager);

            /*
             * if the entities keyset relates to the revision numbers, copy that data across
             */
            if (usingRevisions && closestRevision != null) {
                restRepresentation.setRevision(closestRevision.intValue());
            }

            return restRepresentation;
        } catch (final NamingException ex) {
            throw new InternalProcessingException("Could not find the EntityManagerFactory");
        }
    }

    protected <U> U getEntity(final Class<U> type, final Object id) {
        try {
            final InitialContext initCtx = new InitialContext();

            final EntityManagerFactory entityManagerFactory = (EntityManagerFactory) initCtx
                    .lookup("java:jboss/EntityManagerFactory");
            if (entityManagerFactory == null)
                throw new InternalServerErrorException("Could not find the EntityManagerFactory");

            final EntityManager entityManager = entityManagerFactory.createEntityManager();
            if (entityManager == null)
                throw new InternalServerErrorException("Could not create an EntityManager");

            final U entity = entityManager.find(type, id);
            if (entity == null)
                throw new BadRequestException("No entity was found with the primary key " + id);

            return entity;
        } catch (final NamingException ex) {
            throw new InternalServerErrorException("Could not find the EntityManagerFactory");
        }
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V getXMLResources(
            final Class<V> collectionClass, final Class<U> type, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory,
            final String expandName, final String expand) throws InvalidParameterException, InternalProcessingException {
        return getResources(collectionClass, type, dataObjectFactory, expandName, expand, XML_URL);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V getJSONResources(
            final Class<V> collectionClass, final Class<U> type, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory,
            final String expandName, final String expand) throws InvalidParameterException, InternalProcessingException {
        return getResources(collectionClass, type, dataObjectFactory, expandName, expand, JSON_URL);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V getResources(
            final Class<V> collectionClass, final Class<U> type, final RESTDataObjectFactory<T, U, V, W> dataObjectFactory,
            final String expandName, final String expand, final String dataType) throws InvalidParameterException,
            InternalProcessingException {
        assert type != null : "The type parameter can not be null";
        assert dataObjectFactory != null : "The dataObjectFactory parameter can not be null";

        try {
            final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

            final InitialContext initCtx = new InitialContext();

            final EntityManagerFactory entityManagerFactory = (EntityManagerFactory) initCtx
                    .lookup("java:jboss/EntityManagerFactory");
            if (entityManagerFactory == null)
                throw new InternalServerErrorException("Could not find the EntityManagerFactory");

            final EntityManager entityManager = entityManagerFactory.createEntityManager();
            if (entityManager == null)
                throw new InternalServerErrorException("Could not create an EntityManager");

            final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            final CriteriaQuery<U> criteriaQuery = criteriaBuilder.createQuery(type);

            criteriaQuery.from(type);

            final TypedQuery<U> query = entityManager.createQuery(criteriaQuery);

            final List<U> result = query.getResultList();

            final V retValue = new RESTDataObjectCollectionFactory<T, U, V, W>().create(collectionClass, dataObjectFactory,
                    result, expandName, dataType, expandDataTrunk, getBaseUrl(), true, entityManager);

            return retValue;
        } catch (final NamingException ex) {
            throw new InternalProcessingException("Could not find the EntityManagerFactory");
        } catch (final Exception ex) {
            throw new InvalidParameterException("Internal processing error");
        }
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V getJSONResourcesFromQuery(
            final Class<V> collectionClass, final MultivaluedMap<String, String> queryParams,
            final Class<? extends IFilterQueryBuilder<U>> filterQueryBuilderClass, final IFieldFilter entityFieldFilter,
            final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final String expandName, final String expand)
            throws InternalProcessingException, InvalidParameterException {
        return getResourcesFromQuery(collectionClass, queryParams, filterQueryBuilderClass, entityFieldFilter, dataObjectFactory,
                expandName, expand, JSON_URL);
    }

    protected <T extends RESTBaseEntityV1<T, V, W>, U extends AuditedEntity<U>, V extends RESTBaseCollectionV1<T, V, W>, W extends RESTBaseCollectionItemV1<T, V, W>> V getResourcesFromQuery(
            final Class<V> collectionClass, final MultivaluedMap<String, String> queryParams,
            final Class<? extends IFilterQueryBuilder<U>> filterQueryBuilderClass, final IFieldFilter entityFieldFilter,
            final RESTDataObjectFactory<T, U, V, W> dataObjectFactory, final String expandName, final String expand,
            final String dataType) throws InternalProcessingException, InvalidParameterException {
        assert dataObjectFactory != null : "The dataObjectFactory parameter can not be null";
        assert uriInfo != null : "uriInfo can not be null";

        try {
            final ExpandDataTrunk expandDataTrunk = unmarshallExpand(expand);

            final InitialContext initCtx = new InitialContext();

            final EntityManagerFactory entityManagerFactory = (EntityManagerFactory) initCtx
                    .lookup("java:jboss/EntityManagerFactory");
            if (entityManagerFactory == null)
                throw new InternalProcessingException("Could not find the EntityManagerFactory");

            final EntityManager entityManager = entityManagerFactory.createEntityManager();
            if (entityManager == null)
                throw new InternalProcessingException("Could not create an EntityManager");
            
            // build up a Filter object from the URL variables
            final Filter filter;
            final IFilterQueryBuilder<U> filterQueryBuilder = filterQueryBuilderClass.getConstructor(EntityManager.class).newInstance(entityManager);
            if (filterQueryBuilder instanceof ITagFilterQueryBuilder) {
                filter = EntityUtilities.populateFilter(entityManager, queryParams, CommonFilterConstants.FILTER_ID,
                        CommonFilterConstants.MATCH_TAG, CommonFilterConstants.GROUP_TAG,
                        CommonFilterConstants.CATEORY_INTERNAL_LOGIC, CommonFilterConstants.CATEORY_EXTERNAL_LOGIC,
                        CommonFilterConstants.MATCH_LOCALE, entityFieldFilter);
            } else {
                filter = EntityUtilities.populateFilter(entityManager, queryParams, CommonFilterConstants.MATCH_LOCALE, entityFieldFilter);
            }

            final CriteriaQuery<U> query = filter.buildQuery(filterQueryBuilder);

            final List<U> result = entityManager.createQuery(query).getResultList();

            final V retValue = new RESTDataObjectCollectionFactory<T, U, V, W>().create(collectionClass, dataObjectFactory,
                    result, expandName, dataType, expandDataTrunk, getBaseUrl(), true, entityManager);

            return retValue;
        } catch (final NamingException ex) {
            throw new InternalServerErrorException("Could not find the EntityManagerFactory");
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

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

    protected RESTLogDetailsV1 generateLogDetails(final String message, final Integer flag, final Integer userId) {
        final RESTLogDetailsV1 logDetails = new RESTLogDetailsV1();

        if (message != null)
            logDetails.explicitSetFlag(flag);
        if (flag != null)
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
}
