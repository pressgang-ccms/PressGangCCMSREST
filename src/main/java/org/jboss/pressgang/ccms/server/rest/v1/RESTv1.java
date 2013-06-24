package org.jboss.pressgang.ccms.server.rest.v1;

import javax.persistence.criteria.CriteriaQuery;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.jboss.pressgang.ccms.filter.BlobConstantFieldFilter;
import org.jboss.pressgang.ccms.filter.CategoryFieldFilter;
import org.jboss.pressgang.ccms.filter.FilterFieldFilter;
import org.jboss.pressgang.ccms.filter.ImageFieldFilter;
import org.jboss.pressgang.ccms.filter.IntegerConstantFieldFilter;
import org.jboss.pressgang.ccms.filter.ProjectFieldFilter;
import org.jboss.pressgang.ccms.filter.PropertyTagCategoryFieldFilter;
import org.jboss.pressgang.ccms.filter.PropertyTagFieldFilter;
import org.jboss.pressgang.ccms.filter.RoleFieldFilter;
import org.jboss.pressgang.ccms.filter.StringConstantFieldFilter;
import org.jboss.pressgang.ccms.filter.TagFieldFilter;
import org.jboss.pressgang.ccms.filter.TopicFieldFilter;
import org.jboss.pressgang.ccms.filter.UserFieldFilter;
import org.jboss.pressgang.ccms.filter.builder.BlobConstantFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.CategoryFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.FilterFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.ImageFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.IntegerConstantFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.ProjectFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.PropertyTagCategoryFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.PropertyTagFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.RoleFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.StringConstantFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.TagFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.TopicFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.TranslatedTopicDataFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.builder.UserFilterQueryBuilder;
import org.jboss.pressgang.ccms.model.BlobConstants;
import org.jboss.pressgang.ccms.model.Category;
import org.jboss.pressgang.ccms.model.Filter;
import org.jboss.pressgang.ccms.model.ImageFile;
import org.jboss.pressgang.ccms.model.IntegerConstants;
import org.jboss.pressgang.ccms.model.LanguageImage;
import org.jboss.pressgang.ccms.model.Project;
import org.jboss.pressgang.ccms.model.PropertyTag;
import org.jboss.pressgang.ccms.model.PropertyTagCategory;
import org.jboss.pressgang.ccms.model.Role;
import org.jboss.pressgang.ccms.model.StringConstants;
import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.TranslatedTopicData;
import org.jboss.pressgang.ccms.model.User;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTBlobConstantCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTFilterCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTImageCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTIntegerConstantCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTProjectCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTPropertyCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTRoleCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTStringConstantCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTopicCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTranslatedTopicCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTUserCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.components.ComponentTopicV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTBlobConstantV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTImageV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTIntegerConstantV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTProjectV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTPropertyCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTPropertyTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTRoleV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTStringConstantV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTranslatedTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTUserV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTLogDetailsV1;
import org.jboss.pressgang.ccms.rest.v1.entities.enums.RESTXMLDoctype;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataDetails;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.rest.v1.jaxrsinterfaces.RESTBaseInterfaceV1;
import org.jboss.pressgang.ccms.rest.v1.jaxrsinterfaces.RESTInterfaceAdvancedV1;
import org.jboss.pressgang.ccms.server.rest.v1.base.BaseRESTv1;
import org.jboss.pressgang.ccms.server.utils.Constants;
import org.jboss.pressgang.ccms.server.utils.TopicUtilities;
import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;
import org.jboss.pressgang.ccms.utils.common.DocBookUtilities;
import org.jboss.pressgang.ccms.utils.common.XMLUtilities;
import org.jboss.pressgang.ccms.utils.constants.CommonConstants;
import org.jboss.resteasy.plugins.providers.atom.Feed;
import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * The PressGang REST interface implementation
 */
@Path(Constants.BASE_REST_PATH + "/1")
public class RESTv1 extends BaseRESTv1 implements RESTBaseInterfaceV1, RESTInterfaceAdvancedV1 {
    private static final Logger log = LoggerFactory.getLogger(RESTv1.class);

    @Context HttpResponse response;

    /* SYSTEM FUNCTIONS */
    @Override
    public void reIndexLuceneDatabase() {
        try {
            final Session session = (Session) entityManager.getDelegate();
            final FullTextSession fullTextSession = Search.getFullTextSession(session);
            fullTextSession.createIndexer(Topic.class).start();
        } catch (final Exception ex) {
            log.error("An error reindexing the Lucene database", ex);
        }
    }

    @Override
    public ExpandDataTrunk getJSONExpandTrunkExample() {
        final ExpandDataTrunk expand = new ExpandDataTrunk();
        final ExpandDataTrunk collection = new ExpandDataTrunk(new ExpandDataDetails("collectionname"));
        final ExpandDataTrunk subCollection = new ExpandDataTrunk(new ExpandDataDetails("subcollection"));

        collection.setBranches(CollectionUtilities.toArrayList(subCollection));
        expand.setBranches(CollectionUtilities.toArrayList(collection));

        return expand;
    }

    @Override
    public String echoXML(String xml) {
        Document doc = null;
        try {
            doc = XMLUtilities.convertStringToDocument(xml);
        } catch (Exception e) {
            throw new BadRequestException(e);
        }

        if (doc == null) {
            throw new BadRequestException("The input XML is not valid XML content");
        } else {
            return xml;
        }
    }

    /* BLOBCONSTANTS FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPBlobConstant(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONBlobConstant(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPBlobConstantRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONBlobConstantRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    public String getJSONPBlobConstants(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONBlobConstants(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPBlobConstantsWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONBlobConstantsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTBlobConstantV1 getJSONBlobConstant(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(BlobConstants.class, blobConstantFactory, id, expand);
    }

    @Override
    public RESTBlobConstantV1 getJSONBlobConstantRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(BlobConstants.class, blobConstantFactory, id, revision, expand);
    }

    @Override
    public RESTBlobConstantCollectionV1 getJSONBlobConstants(final String expand) {
        return getJSONResources(RESTBlobConstantCollectionV1.class, BlobConstants.class, blobConstantFactory,
                RESTv1Constants.BLOBCONSTANTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTBlobConstantCollectionV1 getJSONBlobConstantsWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTBlobConstantCollectionV1.class, query.getMatrixParameters(),
                BlobConstantFilterQueryBuilder.class, new BlobConstantFieldFilter(), blobConstantFactory,
                RESTv1Constants.BLOBCONSTANTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTBlobConstantV1 updateJSONBlobConstant(final String expand, final RESTBlobConstantV1 dataObject, final String message,
            final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(BlobConstants.class, dataObject, blobConstantFactory, expand, logDetails);
    }

    @Override
    public RESTBlobConstantCollectionV1 updateJSONBlobConstants(final String expand, final RESTBlobConstantCollectionV1 dataObjects,
            final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTBlobConstantCollectionV1.class, BlobConstants.class, dataObjects, blobConstantFactory,
                RESTv1Constants.BLOBCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTBlobConstantV1 createJSONBlobConstant(final String expand, final RESTBlobConstantV1 dataObject, final String message,
            final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(BlobConstants.class, dataObject, blobConstantFactory, expand, logDetails);
    }

    @Override
    public RESTBlobConstantCollectionV1 createJSONBlobConstants(final String expand, final RESTBlobConstantCollectionV1 dataObjects,
            final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTBlobConstantCollectionV1.class, BlobConstants.class, dataObjects, blobConstantFactory,
                RESTv1Constants.BLOBCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTBlobConstantV1 deleteJSONBlobConstant(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(BlobConstants.class, blobConstantFactory, id, expand, logDetails);
    }

    @Override
    public RESTBlobConstantCollectionV1 deleteJSONBlobConstants(final PathSegment ids, final String message, final Integer flag,
            final String userId, final String expand) {
        if (ids == null) throw new BadRequestException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTBlobConstantCollectionV1.class, BlobConstants.class, blobConstantFactory, dbEntityIds,
                RESTv1Constants.BLOBCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    /* RAW FUNCTIONS */
    @Override
    public byte[] getRAWBlobConstant(@PathParam("id") Integer id) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final BlobConstants entity = getEntity(BlobConstants.class, id);

        response.getOutputHeaders().putSingle("Content-Disposition", "filename=" + entity.getConstantName());
        return entity.getConstantValue();
    }

    @Override
    public byte[] getRAWBlobConstantRevision(@PathParam("id") Integer id, @PathParam("rev") Integer revision) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        final BlobConstants entity = getEntity(BlobConstants.class, id, revision);

        response.getOutputHeaders().putSingle("Content-Disposition", "filename=" + entity.getConstantName());
        return entity.getConstantValue();
    }

    /* PROJECT FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPProject(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONProject(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPProjectRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONProjectRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPProjects(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONProjects(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPProjectsWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONProjectsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTProjectV1 getJSONProject(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(Project.class, projectFactory, id, expand);
    }

    @Override
    public RESTProjectV1 getJSONProjectRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(Project.class, projectFactory, id, revision, expand);
    }

    @Override
    public RESTProjectCollectionV1 getJSONProjects(final String expand) {
        return getJSONResources(RESTProjectCollectionV1.class, Project.class, projectFactory, RESTv1Constants.PROJECTS_EXPANSION_NAME,
                expand);
    }

    @Override
    public RESTProjectCollectionV1 getJSONProjectsWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTProjectCollectionV1.class, query.getMatrixParameters(), ProjectFilterQueryBuilder.class,
                new ProjectFieldFilter(), projectFactory, RESTv1Constants.PROJECTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTProjectV1 updateJSONProject(final String expand, final RESTProjectV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(Project.class, dataObject, projectFactory, expand, logDetails);
    }

    @Override
    public RESTProjectCollectionV1 updateJSONProjects(final String expand, final RESTProjectCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTProjectCollectionV1.class, Project.class, dataObjects, projectFactory,
                RESTv1Constants.PROJECTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTProjectV1 createJSONProject(final String expand, final RESTProjectV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(Project.class, dataObject, projectFactory, expand, logDetails);
    }

    @Override
    public RESTProjectCollectionV1 createJSONProjects(final String expand, final RESTProjectCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTProjectCollectionV1.class, Project.class, dataObjects, projectFactory,
                RESTv1Constants.PROJECTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTProjectV1 deleteJSONProject(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(Project.class, projectFactory, id, expand, logDetails);
    }

    @Override
    public RESTProjectCollectionV1 deleteJSONProjects(final PathSegment ids, final String message, final Integer flag, final String userId,
            final String expand) {
        if (ids == null) throw new BadRequestException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTProjectCollectionV1.class, Project.class, projectFactory, dbEntityIds,
                RESTv1Constants.PROJECTS_EXPANSION_NAME, expand, logDetails);
    }

    /* PROPERYTAG FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPPropertyTag(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyTag(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPPropertyTagRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyTagRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPPropertyTags(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyTags(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPPropertyTagsWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyTagsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTPropertyTagV1 getJSONPropertyTag(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(PropertyTag.class, propertyTagFactory, id, expand);
    }

    @Override
    public RESTPropertyTagV1 getJSONPropertyTagRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(PropertyTag.class, propertyTagFactory, id, revision, expand);
    }

    @Override
    public RESTPropertyTagCollectionV1 getJSONPropertyTags(final String expand) {
        return getJSONResources(RESTPropertyTagCollectionV1.class, PropertyTag.class, propertyTagFactory,
                RESTv1Constants.PROPERTYTAGS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTPropertyTagCollectionV1 getJSONPropertyTagsWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTPropertyTagCollectionV1.class, query.getMatrixParameters(),
                PropertyTagFilterQueryBuilder.class, new PropertyTagFieldFilter(), propertyTagFactory,
                RESTv1Constants.PROPERTYTAGS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTPropertyTagV1 updateJSONPropertyTag(final String expand, final RESTPropertyTagV1 dataObject, final String message,
            final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(PropertyTag.class, dataObject, propertyTagFactory, expand, logDetails);
    }

    @Override
    public RESTPropertyTagCollectionV1 updateJSONPropertyTags(final String expand, final RESTPropertyTagCollectionV1 dataObjects,
            final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTPropertyTagCollectionV1.class, PropertyTag.class, dataObjects, propertyTagFactory,
                RESTv1Constants.PROPERTYTAGS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTPropertyTagV1 createJSONPropertyTag(final String expand, final RESTPropertyTagV1 dataObject, final String message,
            final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(PropertyTag.class, dataObject, propertyTagFactory, expand, logDetails);
    }

    @Override
    public RESTPropertyTagCollectionV1 createJSONPropertyTags(final String expand, final RESTPropertyTagCollectionV1 dataObjects,
            final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTPropertyTagCollectionV1.class, PropertyTag.class, dataObjects, propertyTagFactory,
                RESTv1Constants.PROPERTYTAGS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTPropertyTagV1 deleteJSONPropertyTag(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(PropertyTag.class, propertyTagFactory, id, expand, logDetails);
    }

    @Override
    public RESTPropertyTagCollectionV1 deleteJSONPropertyTags(final PathSegment ids, final String message, final Integer flag,
            final String userId, final String expand) {
        if (ids == null) throw new BadRequestException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTPropertyTagCollectionV1.class, PropertyTag.class, propertyTagFactory, dbEntityIds,
                RESTv1Constants.PROPERTYTAGS_EXPANSION_NAME, expand, logDetails);
    }

    /* PROPERYCATEGORY FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPPropertyCategory(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyCategory(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPPropertyCategoryRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyCategoryRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPPropertyCategories(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyCategories(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPPropertyCategoriesWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyCategoriesWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTPropertyCategoryV1 getJSONPropertyCategory(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(PropertyTagCategory.class, propertyCategoryFactory, id, expand);
    }

    @Override
    public RESTPropertyCategoryV1 getJSONPropertyCategoryRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(PropertyTagCategory.class, propertyCategoryFactory, id, revision, expand);
    }

    @Override
    public RESTPropertyCategoryCollectionV1 getJSONPropertyCategories(final String expand) {
        return getJSONResources(RESTPropertyCategoryCollectionV1.class, PropertyTagCategory.class, propertyCategoryFactory,
                RESTv1Constants.PROPERTY_CATEGORIES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTPropertyCategoryCollectionV1 getJSONPropertyCategoriesWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTPropertyCategoryCollectionV1.class, query.getMatrixParameters(),
                PropertyTagCategoryFilterQueryBuilder.class, new PropertyTagCategoryFieldFilter(), propertyCategoryFactory,
                RESTv1Constants.PROPERTY_CATEGORIES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTPropertyCategoryV1 updateJSONPropertyCategory(final String expand, final RESTPropertyCategoryV1 dataObject,
            final String message, final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(PropertyTagCategory.class, dataObject, propertyCategoryFactory, expand, logDetails);
    }

    @Override
    public RESTPropertyCategoryCollectionV1 updateJSONPropertyCategories(final String expand,
            final RESTPropertyCategoryCollectionV1 dataObjects, final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTPropertyCategoryCollectionV1.class, PropertyTagCategory.class, dataObjects, propertyCategoryFactory,
                RESTv1Constants.PROPERTY_CATEGORIES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTPropertyCategoryV1 createJSONPropertyCategory(final String expand, final RESTPropertyCategoryV1 dataObject,
            final String message, final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(PropertyTagCategory.class, dataObject, propertyCategoryFactory, expand, logDetails);
    }

    @Override
    public RESTPropertyCategoryCollectionV1 createJSONPropertyCategories(final String expand,
            final RESTPropertyCategoryCollectionV1 dataObjects, final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTPropertyCategoryCollectionV1.class, PropertyTagCategory.class, dataObjects, propertyCategoryFactory,
                RESTv1Constants.PROPERTY_CATEGORIES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTPropertyCategoryV1 deleteJSONPropertyCategory(final Integer id, final String message, final Integer flag,
            final String userId, final String expand) {
        if (id == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(PropertyTagCategory.class, propertyCategoryFactory, id, expand, logDetails);
    }

    @Override
    public RESTPropertyCategoryCollectionV1 deleteJSONPropertyCategories(final PathSegment ids, final String message, final Integer flag,
            final String userId, final String expand) {
        if (ids == null) throw new BadRequestException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTPropertyCategoryCollectionV1.class, PropertyTagCategory.class, propertyCategoryFactory, dbEntityIds,
                RESTv1Constants.PROPERTY_CATEGORIES_EXPANSION_NAME, expand, logDetails);
    }

    /* ROLE FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPRole(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONRole(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPRoleRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONRoleRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPRoles(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONRoles(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPRolesWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONRolesWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTRoleV1 getJSONRole(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(Role.class, roleFactory, id, expand);
    }

    @Override
    public RESTRoleV1 getJSONRoleRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(Role.class, roleFactory, id, expand);
    }

    @Override
    public RESTRoleCollectionV1 getJSONRoles(final String expand) {
        return getJSONResources(RESTRoleCollectionV1.class, Role.class, roleFactory, RESTv1Constants.ROLES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTRoleCollectionV1 getJSONRolesWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTRoleCollectionV1.class, query.getMatrixParameters(), RoleFilterQueryBuilder.class,
                new RoleFieldFilter(), roleFactory, RESTv1Constants.ROLES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTRoleV1 updateJSONRole(final String expand, final RESTRoleV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(Role.class, dataObject, roleFactory, expand, logDetails);
    }

    @Override
    public RESTRoleCollectionV1 updateJSONRoles(final String expand, final RESTRoleCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTRoleCollectionV1.class, Role.class, dataObjects, roleFactory, RESTv1Constants.ROLES_EXPANSION_NAME,
                expand, logDetails);
    }

    @Override
    public RESTRoleV1 createJSONRole(final String expand, final RESTRoleV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(Role.class, dataObject, roleFactory, expand, logDetails);
    }

    @Override
    public RESTRoleCollectionV1 createJSONRoles(final String expand, final RESTRoleCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTRoleCollectionV1.class, Role.class, dataObjects, roleFactory, RESTv1Constants.ROLES_EXPANSION_NAME,
                expand, logDetails);
    }

    @Override
    public RESTRoleV1 deleteJSONRole(final Integer id, final String message, final Integer flag, final String userId, final String expand) {
        if (id == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(Role.class, roleFactory, id, expand, logDetails);
    }

    @Override
    public RESTRoleCollectionV1 deleteJSONRoles(final PathSegment ids, final String message, final Integer flag, final String userId,
            final String expand) {
        if (ids == null) throw new BadRequestException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTRoleCollectionV1.class, Role.class, roleFactory, dbEntityIds, RESTv1Constants.ROLES_EXPANSION_NAME,
                expand, logDetails);
    }

    /* TRANSLATEDTOPIC FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPTranslatedTopic(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTranslatedTopic(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTranslatedTopicRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTranslatedTopicRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTranslatedTopics(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTranslatedTopics(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTranslatedTopicsWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTranslatedTopicsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTTranslatedTopicV1 getJSONTranslatedTopic(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(TranslatedTopicData.class, translatedTopicFactory, id, expand);
    }

    @Override
    public RESTTranslatedTopicV1 getJSONTranslatedTopicRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(TranslatedTopicData.class, translatedTopicFactory, id, revision, expand);
    }

    @Override
    public RESTTranslatedTopicCollectionV1 getJSONTranslatedTopicsWithQuery(PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTTranslatedTopicCollectionV1.class, query.getMatrixParameters(),
                TranslatedTopicDataFilterQueryBuilder.class, new TopicFieldFilter(), translatedTopicFactory,
                RESTv1Constants.TRANSLATEDTOPICS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTranslatedTopicCollectionV1 getJSONTranslatedTopics(final String expand) {
        return getJSONResources(RESTTranslatedTopicCollectionV1.class, TranslatedTopicData.class, translatedTopicFactory,
                RESTv1Constants.TRANSLATEDTOPICS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTranslatedTopicV1 updateJSONTranslatedTopic(final String expand, final RESTTranslatedTopicV1 dataObject,
            final String message, final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(TranslatedTopicData.class, dataObject, translatedTopicFactory, expand, logDetails);
    }

    @Override
    public RESTTranslatedTopicCollectionV1 updateJSONTranslatedTopics(final String expand,
            final RESTTranslatedTopicCollectionV1 dataObjects, final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTTranslatedTopicCollectionV1.class, TranslatedTopicData.class, dataObjects, translatedTopicFactory,
                RESTv1Constants.TRANSLATEDTOPICS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTTranslatedTopicV1 createJSONTranslatedTopic(final String expand, final RESTTranslatedTopicV1 dataObject,
            final String message, final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(TranslatedTopicData.class, dataObject, translatedTopicFactory, expand, logDetails);
    }

    @Override
    public RESTTranslatedTopicCollectionV1 createJSONTranslatedTopics(final String expand,
            final RESTTranslatedTopicCollectionV1 dataObjects, final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTTranslatedTopicCollectionV1.class, TranslatedTopicData.class, dataObjects, translatedTopicFactory,
                RESTv1Constants.TRANSLATEDTOPICS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTTranslatedTopicV1 deleteJSONTranslatedTopic(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(TranslatedTopicData.class, translatedTopicFactory, id, expand, logDetails);
    }

    @Override
    public RESTTranslatedTopicCollectionV1 deleteJSONTranslatedTopics(final PathSegment ids, final String message, final Integer flag,
            final String userId, final String expand) {
        if (ids == null) throw new BadRequestException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTTranslatedTopicCollectionV1.class, TranslatedTopicData.class, translatedTopicFactory, dbEntityIds,
                RESTv1Constants.TRANSLATEDTOPICS_EXPANSION_NAME, expand, logDetails);
    }

    /* STRINGCONSTANT FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPStringConstant(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONStringConstant(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPStringConstantRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONStringConstantRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPStringConstants(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONStringConstants(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPStringConstantsWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONStringConstantsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTStringConstantV1 getJSONStringConstant(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(StringConstants.class, stringConstantFactory, id, expand);
    }

    @Override
    public RESTStringConstantV1 getJSONStringConstantRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(StringConstants.class, stringConstantFactory, id, revision, expand);
    }

    @Override
    public RESTStringConstantCollectionV1 getJSONStringConstants(final String expand) {
        return getJSONResources(RESTStringConstantCollectionV1.class, StringConstants.class, stringConstantFactory,
                RESTv1Constants.STRINGCONSTANTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTStringConstantCollectionV1 getJSONStringConstantsWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTStringConstantCollectionV1.class, query.getMatrixParameters(),
                StringConstantFilterQueryBuilder.class, new StringConstantFieldFilter(), stringConstantFactory,
                RESTv1Constants.STRINGCONSTANTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTStringConstantV1 updateJSONStringConstant(final String expand, final RESTStringConstantV1 dataObject, final String message,
            final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(StringConstants.class, dataObject, stringConstantFactory, expand, logDetails);
    }

    @Override
    public RESTStringConstantCollectionV1 updateJSONStringConstants(final String expand, final RESTStringConstantCollectionV1 dataObjects,
            final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTStringConstantCollectionV1.class, StringConstants.class, dataObjects, stringConstantFactory,
                RESTv1Constants.STRINGCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTStringConstantV1 createJSONStringConstant(final String expand, final RESTStringConstantV1 dataObject, final String message,
            final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(StringConstants.class, dataObject, stringConstantFactory, expand, logDetails);
    }

    @Override
    public RESTStringConstantCollectionV1 createJSONStringConstants(final String expand, final RESTStringConstantCollectionV1 dataObjects,
            final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTStringConstantCollectionV1.class, StringConstants.class, dataObjects, stringConstantFactory,
                RESTv1Constants.STRINGCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTStringConstantV1 deleteJSONStringConstant(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(StringConstants.class, stringConstantFactory, id, expand, logDetails);
    }

    @Override
    public RESTStringConstantCollectionV1 deleteJSONStringConstants(final PathSegment ids, final String message, final Integer flag,
            final String userId, final String expand) {
        if (ids == null) throw new BadRequestException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTStringConstantCollectionV1.class, StringConstants.class, stringConstantFactory, dbEntityIds,
                RESTv1Constants.STRINGCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    /* USER FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPUser(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONUser(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPUserRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONUserRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPUsers(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONUsers(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPUsersWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONUsersWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTUserV1 getJSONUser(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(User.class, userFactory, id, expand);
    }

    @Override
    public RESTUserV1 getJSONUserRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(User.class, userFactory, id, revision, expand);
    }

    @Override
    public RESTUserCollectionV1 getJSONUsers(final String expand) {
        return getJSONResources(RESTUserCollectionV1.class, User.class, userFactory, RESTv1Constants.USERS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTUserCollectionV1 getJSONUsersWithQuery(final PathSegment query, final String expand) {
        return this.getJSONResourcesFromQuery(RESTUserCollectionV1.class, query.getMatrixParameters(), UserFilterQueryBuilder.class,
                new UserFieldFilter(), userFactory, RESTv1Constants.USERS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTUserV1 updateJSONUser(final String expand, final RESTUserV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(User.class, dataObject, userFactory, expand, logDetails);
    }

    @Override
    public RESTUserCollectionV1 updateJSONUsers(final String expand, final RESTUserCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTUserCollectionV1.class, User.class, dataObjects, userFactory, RESTv1Constants.USERS_EXPANSION_NAME,
                expand, logDetails);
    }

    @Override
    public RESTUserV1 createJSONUser(final String expand, final RESTUserV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(User.class, dataObject, userFactory, expand, logDetails);
    }

    @Override
    public RESTUserCollectionV1 createJSONUsers(final String expand, final RESTUserCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTUserCollectionV1.class, User.class, dataObjects, userFactory, RESTv1Constants.USERS_EXPANSION_NAME,
                expand, logDetails);
    }

    @Override
    public RESTUserV1 deleteJSONUser(final Integer id, final String message, final Integer flag, final String userId, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(User.class, userFactory, id, expand, logDetails);
    }

    @Override
    public RESTUserCollectionV1 deleteJSONUsers(final PathSegment ids, final String message, final Integer flag, final String userId,
            final String expand) {
        if (ids == null) throw new BadRequestException("The ids parameter can not be null");
        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTUserCollectionV1.class, User.class, userFactory, dbEntityIds, RESTv1Constants.USERS_EXPANSION_NAME,
                expand, logDetails);
    }

    /* TAG FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPTag(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTag(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTagRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTagRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTags(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTags(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTagsWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTagsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTTagV1 getJSONTag(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(Tag.class, tagFactory, id, expand);
    }

    @Override
    public RESTTagV1 getJSONTagRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(Tag.class, tagFactory, id, revision, expand);
    }

    @Override
    public RESTTagCollectionV1 getJSONTags(final String expand) {
        return getJSONResources(RESTTagCollectionV1.class, Tag.class, tagFactory, RESTv1Constants.TAGS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTagCollectionV1 getJSONTagsWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTTagCollectionV1.class, query.getMatrixParameters(), TagFilterQueryBuilder.class,
                new TagFieldFilter(), tagFactory, RESTv1Constants.TAGS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTagV1 updateJSONTag(final String expand, final RESTTagV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(Tag.class, dataObject, tagFactory, expand, logDetails);
    }

    @Override
    public RESTTagCollectionV1 updateJSONTags(final String expand, final RESTTagCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTTagCollectionV1.class, Tag.class, dataObjects, tagFactory, RESTv1Constants.TAGS_EXPANSION_NAME,
                expand, logDetails);
    }

    @Override
    public RESTTagV1 createJSONTag(final String expand, final RESTTagV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(Tag.class, dataObject, tagFactory, expand, logDetails);
    }

    @Override
    public RESTTagCollectionV1 createJSONTags(final String expand, final RESTTagCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTTagCollectionV1.class, Tag.class, dataObjects, tagFactory, RESTv1Constants.TAGS_EXPANSION_NAME,
                expand, logDetails);
    }

    @Override
    public RESTTagV1 deleteJSONTag(final Integer id, final String message, final Integer flag, final String userId, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(Tag.class, tagFactory, id, expand, logDetails);
    }

    @Override
    public RESTTagCollectionV1 deleteJSONTags(final PathSegment ids, final String message, final Integer flag, final String userId,
            final String expand) {
        if (ids == null) throw new BadRequestException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTTagCollectionV1.class, Tag.class, tagFactory, dbEntityIds, RESTv1Constants.TAGS_EXPANSION_NAME,
                expand, logDetails);
    }

    /* CATEGORY FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPCategory(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONCategory(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPCategoryRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONCategoryRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPCategories(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONCategories(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPCategoriesWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONCategoriesWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTCategoryCollectionV1 getJSONCategories(final String expand) {
        return getJSONResources(RESTCategoryCollectionV1.class, Category.class, categoryFactory, RESTv1Constants.CATEGORIES_EXPANSION_NAME,
                expand);
    }

    @Override
    public RESTCategoryV1 getJSONCategory(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(Category.class, categoryFactory, id, expand);
    }

    @Override
    public RESTCategoryV1 getJSONCategoryRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(Category.class, categoryFactory, id, revision, expand);
    }

    @Override
    public RESTCategoryCollectionV1 getJSONCategoriesWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTCategoryCollectionV1.class, query.getMatrixParameters(), CategoryFilterQueryBuilder.class,
                new CategoryFieldFilter(), categoryFactory, RESTv1Constants.CATEGORIES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTCategoryV1 updateJSONCategory(final String expand, final RESTCategoryV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(Category.class, dataObject, categoryFactory, expand, logDetails);
    }

    @Override
    public RESTCategoryCollectionV1 updateJSONCategories(final String expand, final RESTCategoryCollectionV1 dataObjects,
            final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTCategoryCollectionV1.class, Category.class, dataObjects, categoryFactory,
                RESTv1Constants.CATEGORIES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTCategoryV1 createJSONCategory(final String expand, final RESTCategoryV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(Category.class, dataObject, categoryFactory, expand, logDetails);
    }

    @Override
    public RESTCategoryCollectionV1 createJSONCategories(final String expand, final RESTCategoryCollectionV1 dataObjects,
            final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTCategoryCollectionV1.class, Category.class, dataObjects, categoryFactory,
                RESTv1Constants.CATEGORIES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTCategoryV1 deleteJSONCategory(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(Category.class, categoryFactory, id, expand, logDetails);
    }

    @Override
    public RESTCategoryCollectionV1 deleteJSONCategories(final PathSegment ids, final String message, final Integer flag,
            final String userId, final String expand) {
        if (ids == null) throw new BadRequestException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTCategoryCollectionV1.class, Category.class, categoryFactory, dbEntityIds,
                RESTv1Constants.CATEGORIES_EXPANSION_NAME, expand, logDetails);
    }

    /* IMAGE FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPImage(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONImage(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPImageRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONImageRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPImages(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONImages(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPImagesWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONImagesWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTImageV1 getJSONImage(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(ImageFile.class, imageFactory, id, expand);
    }

    @Override
    public RESTImageV1 getJSONImageRevision(final Integer id, Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(ImageFile.class, imageFactory, id, revision, expand);
    }

    @Override
    public RESTImageCollectionV1 getJSONImages(final String expand) {
        return getJSONResources(RESTImageCollectionV1.class, ImageFile.class, imageFactory, RESTv1Constants.IMAGES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTImageCollectionV1 getJSONImagesWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTImageCollectionV1.class, query.getMatrixParameters(), ImageFilterQueryBuilder.class,
                new ImageFieldFilter(), imageFactory, RESTv1Constants.IMAGES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTImageV1 updateJSONImage(final String expand, final RESTImageV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(ImageFile.class, dataObject, imageFactory, expand, logDetails);
    }

    @Override
    public RESTImageCollectionV1 updateJSONImages(final String expand, final RESTImageCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTImageCollectionV1.class, ImageFile.class, dataObjects, imageFactory,
                RESTv1Constants.IMAGES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTImageV1 createJSONImage(final String expand, final RESTImageV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(ImageFile.class, dataObject, imageFactory, expand, logDetails);
    }

    @Override
    public RESTImageCollectionV1 createJSONImages(final String expand, final RESTImageCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTImageCollectionV1.class, ImageFile.class, dataObjects, imageFactory,
                RESTv1Constants.IMAGES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTImageV1 deleteJSONImage(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(ImageFile.class, imageFactory, id, expand, logDetails);
    }

    @Override
    public RESTImageCollectionV1 deleteJSONImages(final PathSegment ids, final String message, final Integer flag, final String userId,
            final String expand) {
        if (ids == null) throw new BadRequestException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTImageCollectionV1.class, ImageFile.class, imageFactory, dbEntityIds,
                RESTv1Constants.IMAGES_EXPANSION_NAME, expand, logDetails);
    }

    /* RAW FUNCTIONS */
    @Override
    public Response getRAWImage(final Integer id, final String locale) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final ImageFile entity = getEntity(ImageFile.class, id);
        final String fixedLocale = locale == null ? CommonConstants.DEFAULT_LOCALE : locale;

        /* Try and find the locale specified first */
        for (final LanguageImage languageImage : entity.getLanguageImages()) {
            if (fixedLocale.equalsIgnoreCase(languageImage.getLocale())) {
                return Response.ok(languageImage.getImageData(), languageImage.getMimeType()).build();
            }
        }

        /* If the specified locale can't be found then use the default */
        for (final LanguageImage languageImage : entity.getLanguageImages()) {
            if (CommonConstants.DEFAULT_LOCALE.equalsIgnoreCase(languageImage.getLocale())) {
                return Response.ok(languageImage.getImageData(), languageImage.getMimeType()).build();
            }
        }

        throw new BadRequestException("No image exists for the " + fixedLocale + " locale.");
    }

    @Override
    public Response getRAWImageRevision(final Integer id, final Integer revision, final String locale) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        final ImageFile entity = getEntity(ImageFile.class, id, revision);
        final String fixedLocale = locale == null ? CommonConstants.DEFAULT_LOCALE : locale;

        /* Try and find the locale specified first */
        for (final LanguageImage languageImage : entity.getLanguageImages()) {
            if (fixedLocale.equalsIgnoreCase(languageImage.getLocale())) {
                return Response.ok(languageImage.getImageData(), languageImage.getMimeType()).build();
            }
        }

        /* If the specified locale can't be found then use the default */
        for (final LanguageImage languageImage : entity.getLanguageImages()) {
            if (CommonConstants.DEFAULT_LOCALE.equalsIgnoreCase(languageImage.getLocale())) {
                return Response.ok(languageImage.getImageData(), languageImage.getMimeType()).build();
            }
        }

        throw new BadRequestException("No image exists for the " + fixedLocale + " locale.");
    }

    @Override
    public Response getRAWImageThumbnail(final Integer id, final String locale) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final ImageFile entity = getEntity(ImageFile.class, id);
        final String fixedLocale = locale == null ? CommonConstants.DEFAULT_LOCALE : locale;

        /* Try and find the locale specified first */
        for (final LanguageImage languageImage : entity.getLanguageImages()) {
            if (fixedLocale.equalsIgnoreCase(languageImage.getLocale())) {
                return Response.ok(languageImage.getThumbnailData(), languageImage.getMimeType()).build();
            }
        }

        /* If the specified locale can't be found then use the default */
        for (final LanguageImage languageImage : entity.getLanguageImages()) {
            if (CommonConstants.DEFAULT_LOCALE.equalsIgnoreCase(languageImage.getLocale())) {
                return Response.ok(languageImage.getThumbnailData(), languageImage.getMimeType()).build();
            }
        }

        throw new BadRequestException("No image exists for the " + fixedLocale + " locale.");
    }

    /* TOPIC FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPTopicsWithQuery(PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTopicsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTopics(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTopics(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTopic(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTopic(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTopicRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTopicRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTTopicCollectionV1 getJSONTopics(final String expand) {
        return getJSONResources(RESTTopicCollectionV1.class, Topic.class, topicFactory, RESTv1Constants.TOPICS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTopicCollectionV1 getJSONTopicsWithQuery(PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTTopicCollectionV1.class, query.getMatrixParameters(), TopicFilterQueryBuilder.class,
                new TopicFieldFilter(), topicFactory, RESTv1Constants.TOPICS_EXPANSION_NAME, expand);
    }

    @Override
    public Feed getATOMTopicsWithQuery(PathSegment query, final String expand) {
        final RESTTopicCollectionV1 topics = getJSONTopicsWithQuery(query, expand);
        return convertTopicsIntoFeed(topics, "Topic Query (" + topics.getSize() + " items)");
    }

    @Override
    public RESTTopicV1 getJSONTopic(final Integer id, final String expand) {
        assert id != null : "The id parameter can not be null";

        return getJSONResource(Topic.class, topicFactory, id, expand);
    }

    @Override
    public RESTTopicV1 getJSONTopicRevision(final Integer id, final Integer revision, final String expand) {
        assert id != null : "The id parameter can not be null";
        assert revision != null : "The revision parameter can not be null";

        return getJSONResource(Topic.class, topicFactory, id, revision, expand);
    }

    @Override
    public RESTTopicV1 updateJSONTopic(final String expand, final RESTTopicV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(Topic.class, dataObject, topicFactory, expand, logDetails);
    }

    @Override
    public RESTTopicCollectionV1 updateJSONTopics(final String expand, final RESTTopicCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTTopicCollectionV1.class, Topic.class, dataObjects, topicFactory,
                RESTv1Constants.TOPICS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTTopicV1 createJSONTopic(final String expand, final RESTTopicV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(Topic.class, dataObject, topicFactory, expand, logDetails);
    }

    @Override
    public RESTTopicCollectionV1 createJSONTopics(final String expand, final RESTTopicCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTTopicCollectionV1.class, Topic.class, dataObjects, topicFactory,
                RESTv1Constants.TOPICS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTTopicV1 deleteJSONTopic(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(Topic.class, topicFactory, id, expand, logDetails);
    }

    @Override
    public RESTTopicCollectionV1 deleteJSONTopics(final PathSegment ids, final String message, final Integer flag, final String userId,
            final String expand) {
        if (ids == null) throw new BadRequestException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTTopicCollectionV1.class, Topic.class, topicFactory, dbEntityIds,
                RESTv1Constants.TOPICS_EXPANSION_NAME, expand, logDetails);
    }

    // XML TOPIC FUNCTIONS

    @Override
    public RESTTopicCollectionV1 getXMLTopics(final String expand) {
        return getXMLResources(RESTTopicCollectionV1.class, Topic.class, topicFactory, RESTv1Constants.TOPICS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTopicV1 getXMLTopic(final Integer id) {
        assert id != null : "The id parameter can not be null";

        return getXMLResource(Topic.class, topicFactory, id, null);
    }

    @Override
    public RESTTopicV1 getXMLTopicRevision(final Integer id, final Integer revision) {
        assert id != null : "The id parameter can not be null";

        return getXMLResource(Topic.class, topicFactory, id, revision, null);
    }

    @Override
    public String getXMLTopicXML(final Integer id) {
        assert id != null : "The id parameter can not be null";

        final RESTTopicV1 entity = getXMLResource(Topic.class, topicFactory, id, null);
        if (entity.getXmlDoctype() != null) {
            if (entity.getXmlDoctype() == RESTXMLDoctype.DOCBOOK_45) {
                return DocBookUtilities.addDocbook45XMLDoctype(entity.getXml(), null, "section");
            } else if (entity.getXmlDoctype() == RESTXMLDoctype.DOCBOOK_50) {
                return DocBookUtilities.addDocbook50XMLDoctype(entity.getXml(), null, "section");
            }
        }

        return entity.getXml();
    }

    @Override
    public String updateXMLTopicXML(Integer id, String xml, String message, Integer flag, String userId) {
        if (xml == null) throw new BadRequestException("The xml parameter can not be null");
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        // Create the topic data object that will be saved
        final RESTTopicV1 topic = new RESTTopicV1();
        topic.setId(id);
        topic.explicitSetXml(xml);

        RESTTopicV1 updatedTopic = updateJSONEntity(Topic.class, topic, topicFactory, "", logDetails);
        return updatedTopic == null ? null : updatedTopic.getXml();
    }

    @Override
    public String getXMLTopicRevisionXML(final Integer id, final Integer revision) {
        assert id != null : "The id parameter can not be null";
        assert revision != null : "The revision parameter can not be null";

        final RESTTopicV1 entity = getXMLResource(Topic.class, topicFactory, id, revision, null);
        if (entity.getXmlDoctype() != null) {
            if (entity.getXmlDoctype() == RESTXMLDoctype.DOCBOOK_45) {
                return DocBookUtilities.addDocbook45XMLDoctype(entity.getXml(), null, "section");
            } else if (entity.getXmlDoctype() == RESTXMLDoctype.DOCBOOK_50) {
                return DocBookUtilities.addDocbook50XMLDoctype(entity.getXml(), null, "section");
            }
        }

        return entity.getXml();
    }

    @Override
    public String getXMLTopicXMLContained(final Integer id, final String containerName) {
        assert id != null : "The id parameter can not be null";
        assert containerName != null : "The containerName parameter can not be null";

        return ComponentTopicV1.returnXMLWithNewContainer(getXMLResource(Topic.class, topicFactory, id, null), containerName);
    }

    @Override
    public String getXMLTopicXMLNoContainer(final Integer id, final Boolean includeTitle) {
        assert id != null : "The id parameter can not be null";

        final String retValue = ComponentTopicV1.returnXMLWithNoContainer(getXMLResource(Topic.class, topicFactory, id, null),
                includeTitle);
        return retValue;
    }

    // HTML TOPIC FUNCTIONS

    @Override
    public String getHTMLTopicHTML(final Integer id) {
        assert id != null : "The id parameter can not be null";

        return getXMLResource(Topic.class, topicFactory, id, null).getHtml();
    }

    @Override
    public String getHTMLTopicRevisionHTML(final Integer id, final Integer revision) {
        assert id != null : "The id parameter can not be null";

        return getXMLResource(Topic.class, topicFactory, id, revision, null).getHtml();
    }

    // CSV TOPIC FUNCTIONS

    @Override
    public String getCSVTopics() {
        final List<Topic> topicList = getAllEntities(Topic.class);

        response.getOutputHeaders().putSingle("Content-Disposition", "filename=Topics.csv");

        return TopicUtilities.getCSVForTopics(entityManager, topicList);
    }

    @Override
    public String getCSVTopicsWithQuery(@PathParam("query") PathSegment query) {
        final CriteriaQuery<Topic> criteriaQuery = getEntitiesFromQuery(query.getMatrixParameters(),
                new TopicFilterQueryBuilder(entityManager), new TopicFieldFilter());
        final List<Topic> topicList = entityManager.createQuery(criteriaQuery).getResultList();

        response.getOutputHeaders().putSingle("Content-Disposition", "filename=Topics.csv");

        return TopicUtilities.getCSVForTopics(entityManager, topicList);
    }

    // ZIP TOPIC FUNCTIONS

    @Override
    public byte[] getZIPTopics() {
        final List<Topic> topicList = getAllEntities(Topic.class);

        response.getOutputHeaders().putSingle("Content-Disposition", "filename=XML.zip");

        return TopicUtilities.getZIPTopicXMLDump(topicList);
    }

    @Override
    public byte[] getZIPTopicsWithQuery(PathSegment query) {
        final CriteriaQuery<Topic> criteriaQuery = getEntitiesFromQuery(query.getMatrixParameters(),
                new TopicFilterQueryBuilder(entityManager), new TopicFieldFilter());
        final List<Topic> topicList = entityManager.createQuery(criteriaQuery).getResultList();

        response.getOutputHeaders().putSingle("Content-Disposition", "filename=XML.zip");

        return TopicUtilities.getZIPTopicXMLDump(topicList);
    }

    /* FILTERS FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPFilter(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONFilter(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPFilterRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONFilterRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPFilters(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONFilters(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPFiltersWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONFiltersWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTFilterV1 getJSONFilter(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(Filter.class, filterFactory, id, expand);
    }

    @Override
    public RESTFilterV1 getJSONFilterRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(Filter.class, filterFactory, id, revision, expand);
    }

    @Override
    public RESTFilterCollectionV1 getJSONFilters(final String expand) {
        return getJSONResources(RESTFilterCollectionV1.class, Filter.class, filterFactory, RESTv1Constants.FILTERS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTFilterCollectionV1 getJSONFiltersWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTFilterCollectionV1.class, query.getMatrixParameters(), FilterFilterQueryBuilder.class,
                new FilterFieldFilter(), filterFactory, RESTv1Constants.FILTERS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTFilterV1 updateJSONFilter(final String expand, final RESTFilterV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(Filter.class, dataObject, filterFactory, expand, logDetails);
    }

    @Override
    public RESTFilterCollectionV1 updateJSONFilters(final String expand, final RESTFilterCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTFilterCollectionV1.class, Filter.class, dataObjects, filterFactory,
                RESTv1Constants.FILTERS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTFilterV1 createJSONFilter(final String expand, final RESTFilterV1 dataObject, final String message, final Integer flag,
            final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(Filter.class, dataObject, filterFactory, expand, logDetails);
    }

    @Override
    public RESTFilterCollectionV1 createJSONFilters(final String expand, final RESTFilterCollectionV1 dataObjects, final String message,
            final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTFilterCollectionV1.class, Filter.class, dataObjects, filterFactory,
                RESTv1Constants.FILTERS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTFilterV1 deleteJSONFilter(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(Filter.class, filterFactory, id, expand, logDetails);
    }

    @Override
    public RESTFilterCollectionV1 deleteJSONFilters(final PathSegment ids, final String message, final Integer flag, final String userId,
            final String expand) {
        if (ids == null) throw new BadRequestException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTFilterCollectionV1.class, Filter.class, filterFactory, dbEntityIds, RESTv1Constants.FILTERS_EXPANSION_NAME,
                expand, logDetails);
    }

    /* INTEGERCONSTANT FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPIntegerConstant(final Integer id, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONIntegerConstant(id, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPIntegerConstantRevision(final Integer id, final Integer revision, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONIntegerConstantRevision(id, revision, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPIntegerConstants(final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONIntegerConstants(expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPIntegerConstantsWithQuery(final PathSegment query, final String expand, final String callback) {
        if (callback == null) throw new BadRequestException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONIntegerConstantsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalServerErrorException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTIntegerConstantV1 getJSONIntegerConstant(final Integer id, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        return getJSONResource(IntegerConstants.class, integerConstantFactory, id, expand);
    }

    @Override
    public RESTIntegerConstantV1 getJSONIntegerConstantRevision(final Integer id, final Integer revision, final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");
        if (revision == null) throw new BadRequestException("The revision parameter can not be null");

        return getJSONResource(IntegerConstants.class, integerConstantFactory, id, revision, expand);
    }

    @Override
    public RESTIntegerConstantCollectionV1 getJSONIntegerConstants(final String expand) {
        return getJSONResources(RESTIntegerConstantCollectionV1.class, IntegerConstants.class, integerConstantFactory,
                RESTv1Constants.INTEGERCONSTANTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTIntegerConstantCollectionV1 getJSONIntegerConstantsWithQuery(final PathSegment query, final String expand) {
        return getJSONResourcesFromQuery(RESTIntegerConstantCollectionV1.class, query.getMatrixParameters(),
                IntegerConstantFilterQueryBuilder.class, new IntegerConstantFieldFilter(), integerConstantFactory,
                RESTv1Constants.INTEGERCONSTANTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTIntegerConstantV1 updateJSONIntegerConstant(final String expand, final RESTIntegerConstantV1 dataObject,
            final String message, final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");
        if (dataObject.getId() == null) throw new BadRequestException("The dataObject.getId() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntity(IntegerConstants.class, dataObject, integerConstantFactory, expand, logDetails);
    }

    @Override
    public RESTIntegerConstantCollectionV1 updateJSONIntegerConstants(final String expand,
            final RESTIntegerConstantCollectionV1 dataObjects, final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return updateJSONEntities(RESTIntegerConstantCollectionV1.class, IntegerConstants.class, dataObjects, integerConstantFactory,
                RESTv1Constants.INTEGERCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTIntegerConstantV1 createJSONIntegerConstant(final String expand, final RESTIntegerConstantV1 dataObject,
            final String message, final Integer flag, final String userId) {
        if (dataObject == null) throw new BadRequestException("The dataObject parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntity(IntegerConstants.class, dataObject, integerConstantFactory, expand, logDetails);
    }

    @Override
    public RESTIntegerConstantCollectionV1 createJSONIntegerConstants(final String expand,
            final RESTIntegerConstantCollectionV1 dataObjects, final String message, final Integer flag, final String userId) {
        if (dataObjects == null) throw new BadRequestException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null) throw new BadRequestException("The dataObjects.getItems() parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return createJSONEntities(RESTIntegerConstantCollectionV1.class, IntegerConstants.class, dataObjects, integerConstantFactory,
                RESTv1Constants.INTEGERCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTIntegerConstantV1 deleteJSONIntegerConstant(final Integer id, final String message, final Integer flag, final String userId,
            final String expand) {
        if (id == null) throw new BadRequestException("The id parameter can not be null");

        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntity(IntegerConstants.class, integerConstantFactory, id, expand, logDetails);
    }

    @Override
    public RESTIntegerConstantCollectionV1 deleteJSONIntegerConstants(final PathSegment ids, final String message, final Integer flag,
            final String userId, final String expand) {
        if (ids == null) throw new BadRequestException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);
        return deleteJSONEntities(RESTIntegerConstantCollectionV1.class, IntegerConstants.class, integerConstantFactory, dbEntityIds,
                RESTv1Constants.INTEGERCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }
}
