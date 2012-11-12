package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.io.IOException;
import java.util.Set;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.Provider;

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
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTCSMetaDataCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTCSNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTContentSpecCollectionV1;
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
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSMetaDataV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTContentSpecV1;
import org.jboss.pressgang.ccms.rest.v1.exceptions.InternalProcessingException;
import org.jboss.pressgang.ccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataDetails;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.rest.v1.jaxrsinterfaces.RESTInterfaceAdvancedV1;
import org.jboss.pressgang.ccms.rest.v1.jaxrsinterfaces.RESTInterfaceV1;
import org.jboss.pressgang.ccms.restserver.entity.BlobConstants;
import org.jboss.pressgang.ccms.restserver.entity.Category;
import org.jboss.pressgang.ccms.restserver.entity.Filter;
import org.jboss.pressgang.ccms.restserver.entity.ImageFile;
import org.jboss.pressgang.ccms.restserver.entity.IntegerConstants;
import org.jboss.pressgang.ccms.restserver.entity.Project;
import org.jboss.pressgang.ccms.restserver.entity.PropertyTag;
import org.jboss.pressgang.ccms.restserver.entity.PropertyTagCategory;
import org.jboss.pressgang.ccms.restserver.entity.Role;
import org.jboss.pressgang.ccms.restserver.entity.StringConstants;
import org.jboss.pressgang.ccms.restserver.entity.Tag;
import org.jboss.pressgang.ccms.restserver.entity.Topic;
import org.jboss.pressgang.ccms.restserver.entity.TranslatedTopicData;
import org.jboss.pressgang.ccms.restserver.entity.User;
import org.jboss.pressgang.ccms.restserver.entity.contentspec.CSMetaData;
import org.jboss.pressgang.ccms.restserver.entity.contentspec.CSNode;
import org.jboss.pressgang.ccms.restserver.entity.contentspec.ContentSpec;
import org.jboss.pressgang.ccms.restserver.filter.BlobConstantFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.CategoryFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.ContentSpecFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.ContentSpecMetaDataFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.ContentSpecNodeFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.FilterFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.ImageFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.IntegerConstantFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.ProjectFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.PropertyTagCategoryFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.PropertyTagFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.RoleFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.StringConstantFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.TagFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.TopicFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.UserFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.builder.BlobConstantFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.builder.CategoryFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.builder.ContentSpecFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.builder.ContentSpecMetaDataFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.builder.ContentSpecNodeFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.builder.FilterFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.builder.ImageFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.builder.IntegerConstantFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.builder.ProjectFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.builder.PropertyTagCategoryFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.builder.PropertyTagFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.builder.RoleFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.builder.StringConstantFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.builder.TagFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.builder.TopicFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.builder.TranslatedTopicDataFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.builder.UserFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.BaseRESTv1;
import org.jboss.pressgang.ccms.restserver.utils.Constants;
import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;
import org.jboss.pressgang.ccms.utils.common.DocBookUtilities;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.plugins.providers.atom.Feed;
import org.jboss.resteasy.spi.interception.MessageBodyWriterContext;
import org.jboss.resteasy.spi.interception.MessageBodyWriterInterceptor;

/**
 * The Skynet REST interface implementation
 */
@Provider
@ServerInterceptor
@Path("/1")
public class RESTv1 extends BaseRESTv1 implements RESTInterfaceV1, RESTInterfaceAdvancedV1, MessageBodyWriterInterceptor {
    /**
     * This method is used to allow all remote clients to access the REST interface via CORS.
     */
    @Override
    public void write(final MessageBodyWriterContext context) throws IOException, WebApplicationException {
        /* allow all origins for simple CORS requests */
        context.getHeaders().add(RESTInterfaceV1.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        context.proceed();
    }

    /**
     * This method will match any preflight CORS request, and can be used as a central location to manage cross origin requests.
     * 
     * Since the browser restrictions on cross site requests are a very insecure way to prevent cross site access (you can just
     * setup a proxy), this method simply accepts all CORS requests.
     * 
     * @param requestMethod The Access-Control-Request-Method header
     * @param requestHeaders The Access-Control-Request-Headers header
     * @return A HTTP response that indicates that all CORS requests are valid.
     */
    @OPTIONS
    @Path("/{path:.*}")
    public Response handleCORSRequest(@HeaderParam(RESTInterfaceV1.ACCESS_CONTROL_REQUEST_METHOD) final String requestMethod,
            @HeaderParam(RESTInterfaceV1.ACCESS_CONTROL_REQUEST_HEADERS) final String requestHeaders) {
        final ResponseBuilder retValue = Response.ok();

        if (requestHeaders != null)
            retValue.header(RESTInterfaceV1.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders);

        if (requestMethod != null)
            retValue.header(RESTInterfaceV1.ACCESS_CONTROL_ALLOW_METHODS, requestMethod);

        retValue.header(RESTInterfaceV1.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");

        return retValue.build();
    }

    /* SYSTEM FUNCTIONS */
    @Override
    public void setRerenderTopic(final Boolean enabled) {
        System.setProperty(Constants.ENABLE_RENDERING_PROPERTY, enabled == null ? null : enabled.toString());
    }

    @Override
    public ExpandDataTrunk getJSONExpandTrunkExample() throws InvalidParameterException, InternalProcessingException {
        final ExpandDataTrunk expand = new ExpandDataTrunk();
        final ExpandDataTrunk collection = new ExpandDataTrunk(new ExpandDataDetails("collectionname"));
        final ExpandDataTrunk subCollection = new ExpandDataTrunk(new ExpandDataDetails("subcollection"));

        collection.setBranches(CollectionUtilities.toArrayList(subCollection));
        expand.setBranches(CollectionUtilities.toArrayList(collection));

        return expand;
    }

    /* BLOBCONSTANTS FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPBlobConstant(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONBlobConstant(id, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    public String getJSONPBlobConstants(final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONBlobConstants(expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPBlobConstantsWithQuery(final PathSegment query, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONBlobConstantsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPBlobConstant(final String expand, final RESTBlobConstantV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPBlobConstant(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String updateJSONPBlobConstant(final String expand, final RESTBlobConstantV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONBlobConstant(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPBlobConstants(final String expand, final RESTBlobConstantCollectionV1 dataObjects,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return updateJSONPBlobConstants(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String updateJSONPBlobConstants(final String expand, final RESTBlobConstantCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONBlobConstants(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPBlobConstant(final String expand, final RESTBlobConstantV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPBlobConstant(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String createJSONPBlobConstant(final String expand, final RESTBlobConstantV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONBlobConstant(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPBlobConstants(final String expand, final RESTBlobConstantCollectionV1 dataObjects,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return createJSONPBlobConstants(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String createJSONPBlobConstants(final String expand, final RESTBlobConstantCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONBlobConstants(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPBlobConstant(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPBlobConstant(id, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPBlobConstant(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONBlobConstant(id, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPBlobConstants(final PathSegment ids, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPBlobConstants(ids, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPBlobConstants(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONBlobConstants(ids, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTBlobConstantV1 getJSONBlobConstant(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        return getJSONResource(BlobConstants.class, new BlobConstantV1Factory(), id, expand);
    }

    @Override
    public RESTBlobConstantCollectionV1 getJSONBlobConstants(final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return getJSONResources(RESTBlobConstantCollectionV1.class, BlobConstants.class, new BlobConstantV1Factory(),
                RESTv1Constants.BLOBCONSTANTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTBlobConstantCollectionV1 getJSONBlobConstantsWithQuery(final PathSegment query, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return getJSONResourcesFromQuery(RESTBlobConstantCollectionV1.class, query.getMatrixParameters(),
                BlobConstantFilterQueryBuilder.class, new BlobConstantFieldFilter(), new BlobConstantV1Factory(),
                RESTv1Constants.BLOBCONSTANTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTBlobConstantV1 updateJSONBlobConstant(final String expand, final RESTBlobConstantV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONBlobConstant(expand, dataObject, null, null, null);
    }

    @Override
    public RESTBlobConstantV1 updateJSONBlobConstant(final String expand, final RESTBlobConstantV1 dataObject,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        if (dataObject.getId() == null)
            throw new InvalidParameterException("The dataObject.getId() parameter can not be null");

        final BlobConstantV1Factory factory = new BlobConstantV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntity(BlobConstants.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTBlobConstantCollectionV1 updateJSONBlobConstants(final String expand,
            final RESTBlobConstantCollectionV1 dataObjects) throws InvalidParameterException, InternalProcessingException {
        return updateJSONBlobConstants(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTBlobConstantCollectionV1 updateJSONBlobConstants(final String expand,
            final RESTBlobConstantCollectionV1 dataObjects, final String message, final Integer flag, final Integer userId)
            throws InvalidParameterException, InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");
        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final BlobConstantV1Factory factory = new BlobConstantV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntities(RESTBlobConstantCollectionV1.class, BlobConstants.class, dataObjects, factory,
                RESTv1Constants.BLOBCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTBlobConstantV1 createJSONBlobConstant(final String expand, final RESTBlobConstantV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONBlobConstant(expand, dataObject, null, null, null);
    }

    @Override
    public RESTBlobConstantV1 createJSONBlobConstant(final String expand, final RESTBlobConstantV1 dataObject,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final BlobConstantV1Factory factory = new BlobConstantV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntity(BlobConstants.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTBlobConstantCollectionV1 createJSONBlobConstants(final String expand,
            final RESTBlobConstantCollectionV1 dataObjects) throws InvalidParameterException, InternalProcessingException {
        return createJSONBlobConstants(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTBlobConstantCollectionV1 createJSONBlobConstants(final String expand,
            final RESTBlobConstantCollectionV1 dataObjects, final String message, final Integer flag, final Integer userId)
            throws InvalidParameterException, InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final BlobConstantV1Factory factory = new BlobConstantV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntities(RESTBlobConstantCollectionV1.class, BlobConstants.class, dataObjects, factory,
                RESTv1Constants.BLOBCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTBlobConstantV1 deleteJSONBlobConstant(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return deleteJSONBlobConstant(id, null, null, null, expand);
    }

    @Override
    public RESTBlobConstantV1 deleteJSONBlobConstant(final Integer id, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final BlobConstantV1Factory factory = new BlobConstantV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntity(BlobConstants.class, factory, id, expand, logDetails);
    }

    @Override
    public RESTBlobConstantCollectionV1 deleteJSONBlobConstants(final PathSegment ids, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONBlobConstants(ids, null, null, null, expand);
    }

    @Override
    public RESTBlobConstantCollectionV1 deleteJSONBlobConstants(final PathSegment ids, final String message,
            final Integer flag, final Integer userId, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        if (ids == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final BlobConstantV1Factory factory = new BlobConstantV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntities(RESTBlobConstantCollectionV1.class, BlobConstants.class, factory, dbEntityIds,
                RESTv1Constants.BLOBCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    /* PROJECT FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPProject(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONProject(id, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPProjects(final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONProjects(expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPProjectsWithQuery(final PathSegment query, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONProjectsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPProject(final String expand, final RESTProjectV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPProject(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String updateJSONPProject(final String expand, final RESTProjectV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONProject(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPProjects(final String expand, final RESTProjectCollectionV1 dataObjects, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPProjects(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String updateJSONPProjects(final String expand, final RESTProjectCollectionV1 dataObjects, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONProjects(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPProject(final String expand, final RESTProjectV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPProject(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String createJSONPProject(final String expand, final RESTProjectV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONProject(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPProjects(final String expand, final RESTProjectCollectionV1 dataObjects, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPProjects(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String createJSONPProjects(final String expand, final RESTProjectCollectionV1 dataObjects, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONProjects(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPProject(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPProject(id, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPProject(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONProject(id, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPProjects(final PathSegment ids, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPProjects(ids, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPProjects(final PathSegment ids, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONProjects(ids, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTProjectV1 getJSONProject(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        return getJSONResource(Project.class, new ProjectV1Factory(), id, expand);
    }

    @Override
    public RESTProjectCollectionV1 getJSONProjects(final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return getJSONResources(RESTProjectCollectionV1.class, Project.class, new ProjectV1Factory(),
                RESTv1Constants.PROJECTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTProjectCollectionV1 getJSONProjectsWithQuery(final PathSegment query, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return getJSONResourcesFromQuery(RESTProjectCollectionV1.class, query.getMatrixParameters(),
                ProjectFilterQueryBuilder.class, new ProjectFieldFilter(), new ProjectV1Factory(),
                RESTv1Constants.PROJECTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTProjectV1 updateJSONProject(final String expand, final RESTProjectV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONProject(expand, dataObject, null, null, null);
    }

    @Override
    public RESTProjectV1 updateJSONProject(final String expand, final RESTProjectV1 dataObject, final String message,
            final Integer flag, final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        if (dataObject.getId() == null)
            throw new InvalidParameterException("The dataObject.getId() parameter can not be null");

        final ProjectV1Factory factory = new ProjectV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntity(Project.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTProjectCollectionV1 updateJSONProjects(final String expand, final RESTProjectCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONProjects(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTProjectCollectionV1 updateJSONProjects(final String expand, final RESTProjectCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final ProjectV1Factory factory = new ProjectV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntities(RESTProjectCollectionV1.class, Project.class, dataObjects, factory,
                RESTv1Constants.PROJECTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTProjectV1 createJSONProject(final String expand, final RESTProjectV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONProject(expand, dataObject, null, null, null);
    }

    @Override
    public RESTProjectV1 createJSONProject(final String expand, final RESTProjectV1 dataObject, final String message,
            final Integer flag, final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final ProjectV1Factory factory = new ProjectV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntity(Project.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTProjectCollectionV1 createJSONProjects(final String expand, final RESTProjectCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONProjects(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTProjectCollectionV1 createJSONProjects(final String expand, final RESTProjectCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final ProjectV1Factory factory = new ProjectV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntities(RESTProjectCollectionV1.class, Project.class, dataObjects, factory,
                RESTv1Constants.PROJECTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTProjectV1 deleteJSONProject(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return deleteJSONProject(id, null, null, null, expand);
    }

    @Override
    public RESTProjectV1 deleteJSONProject(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand) throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final ProjectV1Factory factory = new ProjectV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntity(Project.class, factory, id, expand, logDetails);
    }

    @Override
    public RESTProjectCollectionV1 deleteJSONProjects(final PathSegment ids, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONProjects(ids, null, null, null, expand);
    }

    @Override
    public RESTProjectCollectionV1 deleteJSONProjects(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (ids == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final ProjectV1Factory factory = new ProjectV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntities(RESTProjectCollectionV1.class, Project.class, factory, dbEntityIds,
                RESTv1Constants.PROJECTS_EXPANSION_NAME, expand, logDetails);
    }

    /* PROPERYTAG FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPPropertyTag(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyTag(id, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPPropertyTags(final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyTags(expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPPropertyTagsWithQuery(final PathSegment query, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyTagsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPPropertyTag(final String expand, final RESTPropertyTagV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPPropertyTag(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String updateJSONPPropertyTag(final String expand, final RESTPropertyTagV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONPropertyTag(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPPropertyTags(final String expand, final RESTPropertyTagCollectionV1 dataObjects,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return updateJSONPPropertyTags(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String updateJSONPPropertyTags(final String expand, final RESTPropertyTagCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONPropertyTags(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPPropertyTag(final String expand, final RESTPropertyTagV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPPropertyTag(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String createJSONPPropertyTag(final String expand, final RESTPropertyTagV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONPropertyTag(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPPropertyTags(final String expand, final RESTPropertyTagCollectionV1 dataObjects,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return createJSONPPropertyTags(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String createJSONPPropertyTags(final String expand, final RESTPropertyTagCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONPropertyTags(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPPropertyTag(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPPropertyTag(id, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPPropertyTag(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONPropertyTag(id, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPPropertyTags(final PathSegment ids, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPPropertyTags(ids, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPPropertyTags(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONPropertyTags(ids, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTPropertyTagV1 getJSONPropertyTag(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        return getJSONResource(PropertyTag.class, new PropertyTagV1Factory(), id, expand);
    }

    @Override
    public RESTPropertyTagCollectionV1 getJSONPropertyTags(final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return getJSONResources(RESTPropertyTagCollectionV1.class, PropertyTag.class, new PropertyTagV1Factory(),
                RESTv1Constants.PROPERTYTAGS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTPropertyTagCollectionV1 getJSONPropertyTagsWithQuery(final PathSegment query, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return this.getJSONResourcesFromQuery(RESTPropertyTagCollectionV1.class, query.getMatrixParameters(),
                PropertyTagFilterQueryBuilder.class, new PropertyTagFieldFilter(), new PropertyTagV1Factory(),
                RESTv1Constants.PROPERTYTAGS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTPropertyTagV1 updateJSONPropertyTag(final String expand, final RESTPropertyTagV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPropertyTag(expand, dataObject, null, null, null);
    }

    @Override
    public RESTPropertyTagV1 updateJSONPropertyTag(final String expand, final RESTPropertyTagV1 dataObject,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        if (dataObject.getId() == null)
            throw new InvalidParameterException("The dataObject.getId() parameter can not be null");

        final PropertyTagV1Factory factory = new PropertyTagV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntity(PropertyTag.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTPropertyTagCollectionV1 updateJSONPropertyTags(final String expand, final RESTPropertyTagCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPropertyTags(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTPropertyTagCollectionV1 updateJSONPropertyTags(final String expand,
            final RESTPropertyTagCollectionV1 dataObjects, final String message, final Integer flag, final Integer userId)
            throws InvalidParameterException, InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final PropertyTagV1Factory factory = new PropertyTagV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntities(RESTPropertyTagCollectionV1.class, PropertyTag.class, dataObjects, factory,
                RESTv1Constants.PROPERTYTAGS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTPropertyTagV1 createJSONPropertyTag(final String expand, final RESTPropertyTagV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPropertyTag(expand, dataObject, null, null, null);
    }

    @Override
    public RESTPropertyTagV1 createJSONPropertyTag(final String expand, final RESTPropertyTagV1 dataObject,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final PropertyTagV1Factory factory = new PropertyTagV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntity(PropertyTag.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTPropertyTagCollectionV1 createJSONPropertyTags(final String expand, final RESTPropertyTagCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPropertyTags(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTPropertyTagCollectionV1 createJSONPropertyTags(final String expand,
            final RESTPropertyTagCollectionV1 dataObjects, final String message, final Integer flag, final Integer userId)
            throws InvalidParameterException, InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final PropertyTagV1Factory factory = new PropertyTagV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntities(RESTPropertyTagCollectionV1.class, PropertyTag.class, dataObjects, factory,
                RESTv1Constants.PROPERTYTAGS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTPropertyTagV1 deleteJSONPropertyTag(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return deleteJSONPropertyTag(id, null, null, null, expand);
    }

    @Override
    public RESTPropertyTagV1 deleteJSONPropertyTag(final Integer id, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final PropertyTagV1Factory factory = new PropertyTagV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntity(PropertyTag.class, factory, id, expand, logDetails);
    }

    @Override
    public RESTPropertyTagCollectionV1 deleteJSONPropertyTags(final PathSegment ids, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPropertyTags(ids, null, null, null, expand);
    }

    @Override
    public RESTPropertyTagCollectionV1 deleteJSONPropertyTags(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (ids == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();

        final PropertyTagV1Factory factory = new PropertyTagV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntities(RESTPropertyTagCollectionV1.class, PropertyTag.class, factory, dbEntityIds,
                RESTv1Constants.PROPERTYTAGS_EXPANSION_NAME, expand, logDetails);
    }

    /* PROPERYCATEGORY FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPPropertyCategory(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyCategory(id, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPPropertyCategories(final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyCategories(expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPPropertyCategoriesWithQuery(final PathSegment query, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONPropertyCategoriesWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPPropertyCategory(final String expand, final RESTPropertyCategoryV1 dataObject,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return updateJSONPPropertyCategory(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String updateJSONPPropertyCategory(final String expand, final RESTPropertyCategoryV1 dataObject,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONPropertyCategory(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPPropertyCategories(final String expand, final RESTPropertyCategoryCollectionV1 dataObjects,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return updateJSONPPropertyCategories(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String updateJSONPPropertyCategories(final String expand, final RESTPropertyCategoryCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONPropertyCategories(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPPropertyCategory(final String expand, final RESTPropertyCategoryV1 dataObject,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return createJSONPPropertyCategory(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String createJSONPPropertyCategory(final String expand, final RESTPropertyCategoryV1 dataObject,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONPropertyCategory(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPPropertyCategories(final String expand, final RESTPropertyCategoryCollectionV1 dataObjects,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return createJSONPPropertyCategories(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String createJSONPPropertyCategories(final String expand, final RESTPropertyCategoryCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONPropertyCategories(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPPropertyCategory(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPPropertyCategory(id, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPPropertyCategory(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(deleteJSONPropertyCategory(id, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPPropertyCategories(final PathSegment ids, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPPropertyCategories(ids, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPPropertyCategories(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(deleteJSONPropertyCategories(ids, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTPropertyCategoryV1 getJSONPropertyCategory(final Integer id, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        return getJSONResource(PropertyTagCategory.class, new PropertyCategoryV1Factory(), id, expand);
    }

    @Override
    public RESTPropertyCategoryCollectionV1 getJSONPropertyCategories(final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return getJSONResources(RESTPropertyCategoryCollectionV1.class, PropertyTagCategory.class,
                new PropertyCategoryV1Factory(), RESTv1Constants.PROPERTY_CATEGORIES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTPropertyCategoryCollectionV1 getJSONPropertyCategoriesWithQuery(final PathSegment query, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return this.getJSONResourcesFromQuery(RESTPropertyCategoryCollectionV1.class, query.getMatrixParameters(),
                PropertyTagCategoryFilterQueryBuilder.class, new PropertyTagCategoryFieldFilter(),
                new PropertyCategoryV1Factory(), RESTv1Constants.PROPERTY_CATEGORIES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTPropertyCategoryV1 updateJSONPropertyCategory(final String expand, final RESTPropertyCategoryV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPropertyCategory(expand, dataObject, null, null, null);
    }

    @Override
    public RESTPropertyCategoryV1 updateJSONPropertyCategory(final String expand, final RESTPropertyCategoryV1 dataObject,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        if (dataObject.getId() == null)
            throw new InvalidParameterException("The dataObject.getId() parameter can not be null");

        final PropertyCategoryV1Factory factory = new PropertyCategoryV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntity(PropertyTagCategory.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTPropertyCategoryCollectionV1 updateJSONPropertyCategories(final String expand,
            final RESTPropertyCategoryCollectionV1 dataObjects) throws InvalidParameterException, InternalProcessingException {
        return updateJSONPropertyCategories(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTPropertyCategoryCollectionV1 updateJSONPropertyCategories(final String expand,
            final RESTPropertyCategoryCollectionV1 dataObjects, final String message, final Integer flag, final Integer userId)
            throws InvalidParameterException, InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final PropertyCategoryV1Factory factory = new PropertyCategoryV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntities(RESTPropertyCategoryCollectionV1.class, PropertyTagCategory.class, dataObjects, factory,
                RESTv1Constants.PROPERTY_CATEGORIES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTPropertyCategoryV1 createJSONPropertyCategory(final String expand, final RESTPropertyCategoryV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPropertyCategory(expand, dataObject, null, null, null);
    }

    @Override
    public RESTPropertyCategoryV1 createJSONPropertyCategory(final String expand, final RESTPropertyCategoryV1 dataObject,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final PropertyCategoryV1Factory factory = new PropertyCategoryV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntity(PropertyTagCategory.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTPropertyCategoryCollectionV1 createJSONPropertyCategories(final String expand,
            final RESTPropertyCategoryCollectionV1 dataObjects) throws InvalidParameterException, InternalProcessingException {
        return createJSONPropertyCategories(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTPropertyCategoryCollectionV1 createJSONPropertyCategories(final String expand,
            final RESTPropertyCategoryCollectionV1 dataObjects, final String message, final Integer flag, final Integer userId)
            throws InvalidParameterException, InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final PropertyCategoryV1Factory factory = new PropertyCategoryV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntities(RESTPropertyCategoryCollectionV1.class, PropertyTagCategory.class, dataObjects, factory,
                RESTv1Constants.PROPERTY_CATEGORIES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTPropertyCategoryV1 deleteJSONPropertyCategory(final Integer id, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPropertyCategory(id, null, null, null, expand);
    }

    @Override
    public RESTPropertyCategoryV1 deleteJSONPropertyCategory(final Integer id, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final PropertyCategoryV1Factory factory = new PropertyCategoryV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntity(PropertyTagCategory.class, factory, id, expand, logDetails);
    }

    @Override
    public RESTPropertyCategoryCollectionV1 deleteJSONPropertyCategories(final PathSegment ids, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPropertyCategories(ids, null, null, null, expand);
    }

    @Override
    public RESTPropertyCategoryCollectionV1 deleteJSONPropertyCategories(final PathSegment ids, final String message,
            final Integer flag, final Integer userId, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        if (ids == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();

        final PropertyCategoryV1Factory factory = new PropertyCategoryV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntities(RESTPropertyCategoryCollectionV1.class, PropertyTagCategory.class, factory, dbEntityIds,
                RESTv1Constants.PROPERTY_CATEGORIES_EXPANSION_NAME, expand, logDetails);
    }

    /* ROLE FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPRole(final Integer id, final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONRole(id, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPRoles(final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONRoles(expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPRolesWithQuery(final PathSegment query, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONRolesWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPRole(final String expand, final RESTRoleV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPRole(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String updateJSONPRole(final String expand, final RESTRoleV1 dataObject, final String message, final Integer flag,
            final Integer userId, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(updateJSONRole(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPRoles(final String expand, final RESTRoleCollectionV1 dataObjects, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPRoles(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String updateJSONPRoles(final String expand, final RESTRoleCollectionV1 dataObjects, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(updateJSONRoles(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPRole(final String expand, final RESTRoleV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPRole(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String createJSONPRole(final String expand, final RESTRoleV1 dataObject, final String message, final Integer flag,
            final Integer userId, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(createJSONRole(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPRoles(final String expand, final RESTRoleCollectionV1 dataObjects, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPRoles(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String createJSONPRoles(final String expand, final RESTRoleCollectionV1 dataObjects, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(createJSONRoles(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPRole(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPRole(id, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPRole(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONRole(id, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPRoles(final PathSegment ids, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPRoles(ids, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPRoles(final PathSegment ids, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONRoles(ids, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTRoleV1 getJSONRole(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        return getJSONResource(Role.class, new RoleV1Factory(), id, expand);
    }

    @Override
    public RESTRoleCollectionV1 getJSONRoles(final String expand) throws InvalidParameterException, InternalProcessingException {
        return getJSONResources(RESTRoleCollectionV1.class, Role.class, new RoleV1Factory(),
                RESTv1Constants.ROLES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTRoleCollectionV1 getJSONRolesWithQuery(final PathSegment query, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return getJSONResourcesFromQuery(RESTRoleCollectionV1.class, query.getMatrixParameters(), RoleFilterQueryBuilder.class,
                new RoleFieldFilter(), new RoleV1Factory(), RESTv1Constants.ROLES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTRoleV1 updateJSONRole(final String expand, final RESTRoleV1 dataObject) throws InvalidParameterException,
            InternalProcessingException {
        return updateJSONRole(expand, dataObject, null, null, null);
    }

    @Override
    public RESTRoleV1 updateJSONRole(final String expand, final RESTRoleV1 dataObject, final String message,
            final Integer flag, final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        if (dataObject.getId() == null)
            throw new InvalidParameterException("The dataObject.getId() parameter can not be null");

        final RoleV1Factory factory = new RoleV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntity(Role.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTRoleCollectionV1 updateJSONRoles(final String expand, final RESTRoleCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONRoles(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTRoleCollectionV1 updateJSONRoles(final String expand, final RESTRoleCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final RoleV1Factory factory = new RoleV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntities(RESTRoleCollectionV1.class, Role.class, dataObjects, factory,
                RESTv1Constants.ROLES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTRoleV1 createJSONRole(final String expand, final RESTRoleV1 dataObject) throws InvalidParameterException,
            InternalProcessingException {
        return createJSONRole(expand, dataObject, null, null, null);
    }

    @Override
    public RESTRoleV1 createJSONRole(final String expand, final RESTRoleV1 dataObject, final String message,
            final Integer flag, final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final RoleV1Factory factory = new RoleV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntity(Role.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTRoleCollectionV1 createJSONRoles(final String expand, final RESTRoleCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONRoles(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTRoleCollectionV1 createJSONRoles(final String expand, final RESTRoleCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final RoleV1Factory factory = new RoleV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntities(RESTRoleCollectionV1.class, Role.class, dataObjects, factory,
                RESTv1Constants.ROLES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTRoleV1 deleteJSONRole(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return deleteJSONRole(id, null, null, null, expand);
    }

    @Override
    public RESTRoleV1 deleteJSONRole(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand) throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final RoleV1Factory factory = new RoleV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntity(Role.class, factory, id, expand, logDetails);
    }

    @Override
    public RESTRoleCollectionV1 deleteJSONRoles(final PathSegment ids, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return deleteJSONRoles(ids, null, null, null, expand);
    }

    @Override
    public RESTRoleCollectionV1 deleteJSONRoles(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (ids == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();

        final RoleV1Factory factory = new RoleV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntities(RESTRoleCollectionV1.class, Role.class, factory, dbEntityIds,
                RESTv1Constants.ROLES_EXPANSION_NAME, expand, logDetails);
    }

    /* TRANSLATEDTOPIC FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPTranslatedTopic(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTranslatedTopic(id, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTranslatedTopics(final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTranslatedTopics(expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTranslatedTopicsWithQuery(final PathSegment query, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTranslatedTopicsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPTranslatedTopic(final String expand, final RESTTranslatedTopicV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPTranslatedTopic(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String updateJSONPTranslatedTopic(final String expand, final RESTTranslatedTopicV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONTranslatedTopic(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPTranslatedTopics(final String expand, final RESTTranslatedTopicCollectionV1 dataObjects,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return updateJSONPTranslatedTopics(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String updateJSONPTranslatedTopics(final String expand, final RESTTranslatedTopicCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONTranslatedTopics(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPTranslatedTopic(final String expand, final RESTTranslatedTopicV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONTranslatedTopic(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPTranslatedTopic(final String expand, final RESTTranslatedTopicV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPTranslatedTopic(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String createJSONPTranslatedTopics(final String expand, final RESTTranslatedTopicCollectionV1 dataObjects,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return createJSONPTranslatedTopics(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String createJSONPTranslatedTopics(final String expand, final RESTTranslatedTopicCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONTranslatedTopics(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPTranslatedTopic(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPTranslatedTopic(id, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPTranslatedTopic(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(deleteJSONTranslatedTopic(id, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPTranslatedTopics(final PathSegment ids, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPTranslatedTopics(ids, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPTranslatedTopics(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(deleteJSONTranslatedTopics(ids, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTTranslatedTopicV1 getJSONTranslatedTopic(final Integer id, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        return getJSONResource(TranslatedTopicData.class, new TranslatedTopicV1Factory(), id, expand);
    }

    @Override
    public RESTTranslatedTopicCollectionV1 getJSONTranslatedTopicsWithQuery(PathSegment query, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return getJSONResourcesFromQuery(RESTTranslatedTopicCollectionV1.class, query.getMatrixParameters(),
                TranslatedTopicDataFilterQueryBuilder.class, new TopicFieldFilter(), new TranslatedTopicV1Factory(),
                RESTv1Constants.TRANSLATEDTOPICS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTranslatedTopicCollectionV1 getJSONTranslatedTopics(final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return getJSONResources(RESTTranslatedTopicCollectionV1.class, TranslatedTopicData.class,
                new TranslatedTopicV1Factory(), RESTv1Constants.TRANSLATEDTOPICS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTranslatedTopicV1 updateJSONTranslatedTopic(final String expand, final RESTTranslatedTopicV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONTranslatedTopic(expand, dataObject, null, null, null);
    }

    @Override
    public RESTTranslatedTopicV1 updateJSONTranslatedTopic(final String expand, final RESTTranslatedTopicV1 dataObject,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        if (dataObject.getId() == null)
            throw new InvalidParameterException("The dataObject.getId() parameter can not be null");

        final TranslatedTopicV1Factory factory = new TranslatedTopicV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntity(TranslatedTopicData.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTTranslatedTopicCollectionV1 updateJSONTranslatedTopics(final String expand,
            final RESTTranslatedTopicCollectionV1 dataObjects) throws InvalidParameterException, InternalProcessingException {
        return updateJSONTranslatedTopics(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTTranslatedTopicCollectionV1 updateJSONTranslatedTopics(final String expand,
            final RESTTranslatedTopicCollectionV1 dataObjects, final String message, final Integer flag, final Integer userId)
            throws InvalidParameterException, InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final TranslatedTopicV1Factory factory = new TranslatedTopicV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntities(RESTTranslatedTopicCollectionV1.class, TranslatedTopicData.class, dataObjects, factory,
                RESTv1Constants.TRANSLATEDTOPICS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTTranslatedTopicV1 createJSONTranslatedTopic(final String expand, final RESTTranslatedTopicV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONTranslatedTopic(expand, dataObject, null, null, null);
    }

    @Override
    public RESTTranslatedTopicV1 createJSONTranslatedTopic(final String expand, final RESTTranslatedTopicV1 dataObject,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final TranslatedTopicV1Factory factory = new TranslatedTopicV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntity(TranslatedTopicData.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTTranslatedTopicCollectionV1 createJSONTranslatedTopics(final String expand,
            final RESTTranslatedTopicCollectionV1 dataObjects) throws InvalidParameterException, InternalProcessingException {
        return createJSONTranslatedTopics(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTTranslatedTopicCollectionV1 createJSONTranslatedTopics(final String expand,
            final RESTTranslatedTopicCollectionV1 dataObjects, final String message, final Integer flag, final Integer userId)
            throws InvalidParameterException, InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final TranslatedTopicV1Factory factory = new TranslatedTopicV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntities(RESTTranslatedTopicCollectionV1.class, TranslatedTopicData.class, dataObjects, factory,
                RESTv1Constants.TRANSLATEDTOPICS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTTranslatedTopicV1 deleteJSONTranslatedTopic(final Integer id, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONTranslatedTopic(id, null, null, null, expand);
    }

    @Override
    public RESTTranslatedTopicV1 deleteJSONTranslatedTopic(final Integer id, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final TranslatedTopicV1Factory factory = new TranslatedTopicV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntity(TranslatedTopicData.class, factory, id, expand, logDetails);
    }

    @Override
    public RESTTranslatedTopicCollectionV1 deleteJSONTranslatedTopics(final PathSegment ids, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONTranslatedTopics(ids, null, null, null, expand);
    }

    @Override
    public RESTTranslatedTopicCollectionV1 deleteJSONTranslatedTopics(final PathSegment ids, final String message,
            final Integer flag, final Integer userId, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        if (ids == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();

        final TranslatedTopicV1Factory factory = new TranslatedTopicV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntities(RESTTranslatedTopicCollectionV1.class, TranslatedTopicData.class, factory, dbEntityIds,
                RESTv1Constants.TRANSLATEDTOPICS_EXPANSION_NAME, expand, logDetails);
    }

    /* STRINGCONSTANT FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPStringConstant(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONStringConstant(id, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPStringConstants(final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONStringConstants(expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPStringConstantsWithQuery(final PathSegment query, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONStringConstantsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPStringConstant(final String expand, final RESTStringConstantV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPStringConstant(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String updateJSONPStringConstant(final String expand, final RESTStringConstantV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONStringConstant(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPStringConstants(final String expand, final RESTStringConstantCollectionV1 dataObjects,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return updateJSONPStringConstants(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String updateJSONPStringConstants(final String expand, final RESTStringConstantCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONStringConstants(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPStringConstant(final String expand, final RESTStringConstantV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPStringConstant(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String createJSONPStringConstant(final String expand, final RESTStringConstantV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONStringConstant(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPStringConstants(final String expand, final RESTStringConstantCollectionV1 dataObjects,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return createJSONPStringConstants(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String createJSONPStringConstants(final String expand, final RESTStringConstantCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONStringConstants(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPStringConstant(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPStringConstant(id, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPStringConstant(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONStringConstant(id, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPStringConstants(final PathSegment ids, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPStringConstants(ids, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPStringConstants(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(deleteJSONStringConstants(ids, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTStringConstantV1 getJSONStringConstant(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        return getJSONResource(StringConstants.class, new StringConstantV1Factory(), id, expand);
    }

    @Override
    public RESTStringConstantCollectionV1 getJSONStringConstants(final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return getJSONResources(RESTStringConstantCollectionV1.class, StringConstants.class, new StringConstantV1Factory(),
                RESTv1Constants.STRINGCONSTANTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTStringConstantCollectionV1 getJSONStringConstantsWithQuery(final PathSegment query, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return getJSONResourcesFromQuery(RESTStringConstantCollectionV1.class, query.getMatrixParameters(),
                StringConstantFilterQueryBuilder.class, new StringConstantFieldFilter(), new StringConstantV1Factory(),
                RESTv1Constants.STRINGCONSTANTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTStringConstantV1 updateJSONStringConstant(final String expand, final RESTStringConstantV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONStringConstant(expand, dataObject, null, null, null);
    }

    @Override
    public RESTStringConstantV1 updateJSONStringConstant(final String expand, final RESTStringConstantV1 dataObject,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        if (dataObject.getId() == null)
            throw new InvalidParameterException("The dataObject.getId() parameter can not be null");

        final StringConstantV1Factory factory = new StringConstantV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntity(StringConstants.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTStringConstantCollectionV1 updateJSONStringConstants(final String expand,
            final RESTStringConstantCollectionV1 dataObjects) throws InvalidParameterException, InternalProcessingException {
        return updateJSONStringConstants(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTStringConstantCollectionV1 updateJSONStringConstants(final String expand,
            final RESTStringConstantCollectionV1 dataObjects, final String message, final Integer flag, final Integer userId)
            throws InvalidParameterException, InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final StringConstantV1Factory factory = new StringConstantV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntities(RESTStringConstantCollectionV1.class, StringConstants.class, dataObjects, factory,
                RESTv1Constants.STRINGCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTStringConstantV1 createJSONStringConstant(final String expand, final RESTStringConstantV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONStringConstant(expand, dataObject, null, null, null);
    }

    @Override
    public RESTStringConstantV1 createJSONStringConstant(final String expand, final RESTStringConstantV1 dataObject,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final StringConstantV1Factory factory = new StringConstantV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntity(StringConstants.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTStringConstantCollectionV1 createJSONStringConstants(final String expand,
            final RESTStringConstantCollectionV1 dataObjects) throws InvalidParameterException, InternalProcessingException {
        return createJSONStringConstants(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTStringConstantCollectionV1 createJSONStringConstants(final String expand,
            final RESTStringConstantCollectionV1 dataObjects, final String message, final Integer flag, final Integer userId)
            throws InvalidParameterException, InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final StringConstantV1Factory factory = new StringConstantV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntities(RESTStringConstantCollectionV1.class, StringConstants.class, dataObjects, factory,
                RESTv1Constants.STRINGCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTStringConstantV1 deleteJSONStringConstant(final Integer id, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONStringConstant(id, null, null, null, expand);
    }

    @Override
    public RESTStringConstantV1 deleteJSONStringConstant(final Integer id, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        final StringConstantV1Factory factory = new StringConstantV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntity(StringConstants.class, factory, id, expand, logDetails);
    }

    @Override
    public RESTStringConstantCollectionV1 deleteJSONStringConstants(final PathSegment ids, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONStringConstants(ids, null, null, null, expand);
    }

    @Override
    public RESTStringConstantCollectionV1 deleteJSONStringConstants(final PathSegment ids, final String message,
            final Integer flag, final Integer userId, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        if (ids == null)
            throw new InvalidParameterException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final StringConstantV1Factory factory = new StringConstantV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntities(RESTStringConstantCollectionV1.class, StringConstants.class, factory, dbEntityIds,
                RESTv1Constants.STRINGCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    /* USER FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPUser(final Integer id, final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONUser(id, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPUsers(final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONUsers(expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPUsersWithQuery(final PathSegment query, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONUsersWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPUser(final String expand, final RESTUserV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPUser(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String updateJSONPUser(final String expand, final RESTUserV1 dataObject, final String message, final Integer flag,
            final Integer userId, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(updateJSONUser(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPUsers(final String expand, final RESTUserCollectionV1 dataObjects, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPUsers(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String updateJSONPUsers(final String expand, final RESTUserCollectionV1 dataObjects, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(updateJSONUsers(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPUser(final String expand, final RESTUserV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPUser(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String createJSONPUser(final String expand, final RESTUserV1 dataObject, final String message, final Integer flag,
            final Integer userId, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(createJSONUser(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPUsers(final String expand, final RESTUserCollectionV1 dataObjects, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPUsers(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String createJSONPUsers(final String expand, final RESTUserCollectionV1 dataObjects, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(createJSONUsers(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPUser(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPUser(id, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPUser(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONUser(id, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPUsers(final PathSegment ids, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPUsers(ids, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPUsers(final PathSegment ids, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONUsers(ids, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTUserV1 getJSONUser(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        return getJSONResource(User.class, new UserV1Factory(), id, expand);
    }

    @Override
    public RESTUserCollectionV1 getJSONUsers(final String expand) throws InvalidParameterException, InternalProcessingException {
        return getJSONResources(RESTUserCollectionV1.class, User.class, new UserV1Factory(),
                RESTv1Constants.USERS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTUserCollectionV1 getJSONUsersWithQuery(final PathSegment query, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return this.getJSONResourcesFromQuery(RESTUserCollectionV1.class, query.getMatrixParameters(),
                UserFilterQueryBuilder.class, new UserFieldFilter(), new UserV1Factory(), RESTv1Constants.USERS_EXPANSION_NAME,
                expand);
    }

    @Override
    public RESTUserV1 updateJSONUser(final String expand, final RESTUserV1 dataObject) throws InvalidParameterException,
            InternalProcessingException {
        return updateJSONUser(expand, dataObject, null, null, null);
    }

    @Override
    public RESTUserV1 updateJSONUser(final String expand, final RESTUserV1 dataObject, final String message,
            final Integer flag, final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        if (dataObject.getId() == null)
            throw new InvalidParameterException("The dataObject.getId() parameter can not be null");

        final UserV1Factory factory = new UserV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntity(User.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTUserCollectionV1 updateJSONUsers(final String expand, final RESTUserCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONUsers(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTUserCollectionV1 updateJSONUsers(final String expand, final RESTUserCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final UserV1Factory factory = new UserV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntities(RESTUserCollectionV1.class, User.class, dataObjects, factory,
                RESTv1Constants.USERS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTUserV1 createJSONUser(final String expand, final RESTUserV1 dataObject) throws InvalidParameterException,
            InternalProcessingException {
        return createJSONUser(expand, dataObject, null, null, null);
    }

    @Override
    public RESTUserV1 createJSONUser(final String expand, final RESTUserV1 dataObject, final String message,
            final Integer flag, final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final UserV1Factory factory = new UserV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntity(User.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTUserCollectionV1 createJSONUsers(final String expand, final RESTUserCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONUsers(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTUserCollectionV1 createJSONUsers(final String expand, final RESTUserCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final UserV1Factory factory = new UserV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntities(RESTUserCollectionV1.class, User.class, dataObjects, factory,
                RESTv1Constants.USERS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTUserV1 deleteJSONUser(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return deleteJSONUser(id, null, null, null, expand);
    }

    @Override
    public RESTUserV1 deleteJSONUser(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand) throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        final UserV1Factory factory = new UserV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntity(User.class, factory, id, expand, logDetails);
    }

    @Override
    public RESTUserCollectionV1 deleteJSONUsers(final PathSegment ids, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return deleteJSONUsers(ids, null, null, null, expand);
    }

    @Override
    public RESTUserCollectionV1 deleteJSONUsers(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (ids == null)
            throw new InvalidParameterException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();

        final UserV1Factory factory = new UserV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntities(RESTUserCollectionV1.class, User.class, factory, dbEntityIds,
                RESTv1Constants.USERS_EXPANSION_NAME, expand, logDetails);
    }

    /* TAG FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPTag(final Integer id, final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTag(id, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTags(final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTags(expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTagsWithQuery(final PathSegment query, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTagsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPTag(final String expand, final RESTTagV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPTag(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String updateJSONPTag(final String expand, final RESTTagV1 dataObject, final String message, final Integer flag,
            final Integer userId, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(updateJSONTag(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPTags(final String expand, final RESTTagCollectionV1 dataObjects, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPTags(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String updateJSONPTags(final String expand, final RESTTagCollectionV1 dataObjects, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(updateJSONTags(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPTag(final String expand, final RESTTagV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPTag(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String createJSONPTag(final String expand, final RESTTagV1 dataObject, final String message, final Integer flag,
            final Integer userId, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(createJSONTag(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPTags(final String expand, final RESTTagCollectionV1 dataObjects, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPTags(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String createJSONPTags(final String expand, final RESTTagCollectionV1 dataObjects, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(createJSONTags(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPTag(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPTag(id, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPTag(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONTag(id, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPTags(final PathSegment ids, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPTags(ids, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPTags(final PathSegment ids, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONTags(ids, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTTagV1 getJSONTag(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        return getJSONResource(Tag.class, new TagV1Factory(), id, expand);
    }

    @Override
    public RESTTagCollectionV1 getJSONTags(final String expand) throws InvalidParameterException, InternalProcessingException {
        return getJSONResources(RESTTagCollectionV1.class, Tag.class, new TagV1Factory(), RESTv1Constants.TAGS_EXPANSION_NAME,
                expand);
    }

    @Override
    public RESTTagCollectionV1 getJSONTagsWithQuery(final PathSegment query, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return getJSONResourcesFromQuery(RESTTagCollectionV1.class, query.getMatrixParameters(), TagFilterQueryBuilder.class,
                new TagFieldFilter(), new TagV1Factory(), RESTv1Constants.TAGS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTagV1 updateJSONTag(final String expand, final RESTTagV1 dataObject) throws InvalidParameterException,
            InternalProcessingException {
        return updateJSONTag(expand, dataObject, null, null, null);
    }

    @Override
    public RESTTagV1 updateJSONTag(final String expand, final RESTTagV1 dataObject, final String message, final Integer flag,
            final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        if (dataObject.getId() == null)
            throw new InvalidParameterException("The dataObject.getId() parameter can not be null");

        final TagV1Factory factory = new TagV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntity(Tag.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTTagCollectionV1 updateJSONTags(final String expand, final RESTTagCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONTags(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTTagCollectionV1 updateJSONTags(final String expand, final RESTTagCollectionV1 dataObjects, final String message,
            final Integer flag, final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final TagV1Factory factory = new TagV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntities(RESTTagCollectionV1.class, Tag.class, dataObjects, factory,
                RESTv1Constants.TAGS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTTagV1 createJSONTag(final String expand, final RESTTagV1 dataObject) throws InvalidParameterException,
            InternalProcessingException {
        return createJSONTag(expand, dataObject, null, null, null);
    }

    @Override
    public RESTTagV1 createJSONTag(final String expand, final RESTTagV1 dataObject, final String message, final Integer flag,
            final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final TagV1Factory factory = new TagV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntity(Tag.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTTagCollectionV1 createJSONTags(final String expand, final RESTTagCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONTags(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTTagCollectionV1 createJSONTags(final String expand, final RESTTagCollectionV1 dataObjects, final String message,
            final Integer flag, final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final TagV1Factory factory = new TagV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntities(RESTTagCollectionV1.class, Tag.class, dataObjects, factory,
                RESTv1Constants.TAGS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTTagV1 deleteJSONTag(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return deleteJSONTag(id, null, null, null, expand);
    }

    @Override
    public RESTTagV1 deleteJSONTag(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand) throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        final TagV1Factory factory = new TagV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntity(Tag.class, factory, id, expand, logDetails);
    }

    @Override
    public RESTTagCollectionV1 deleteJSONTags(final PathSegment ids, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return deleteJSONTags(ids, null, null, null, expand);
    }

    @Override
    public RESTTagCollectionV1 deleteJSONTags(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (ids == null)
            throw new InvalidParameterException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final TagV1Factory factory = new TagV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntities(RESTTagCollectionV1.class, Tag.class, factory, dbEntityIds,
                RESTv1Constants.TAGS_EXPANSION_NAME, expand, logDetails);
    }

    /* CATEGORY FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPCategory(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONCategory(id, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPCategories(final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONCategories(expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPCategoriesWithQuery(final PathSegment query, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONCategoriesWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPCategory(final String expand, final RESTCategoryV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPCategory(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String updateJSONPCategory(final String expand, final RESTCategoryV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONCategory(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPCategories(final String expand, final RESTCategoryCollectionV1 dataObjects, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPCategories(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String updateJSONPCategories(final String expand, final RESTCategoryCollectionV1 dataObjects, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONCategories(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPCategory(final String expand, final RESTCategoryV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPCategory(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String createJSONPCategory(final String expand, final RESTCategoryV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONCategory(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPCategories(final String expand, final RESTCategoryCollectionV1 dataObjects, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPCategories(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String createJSONPCategories(final String expand, final RESTCategoryCollectionV1 dataObjects, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONCategories(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPCategory(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPCategory(id, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPCategory(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONCategory(id, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPCategories(final PathSegment ids, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPCategories(ids, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPCategories(final PathSegment ids, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONCategories(ids, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTCategoryCollectionV1 getJSONCategories(final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return getJSONResources(RESTCategoryCollectionV1.class, Category.class, new CategoryV1Factory(),
                RESTv1Constants.CATEGORIES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTCategoryV1 getJSONCategory(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        return getJSONResource(Category.class, new CategoryV1Factory(), id, expand);
    }

    @Override
    public RESTCategoryCollectionV1 getJSONCategoriesWithQuery(final PathSegment query, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return getJSONResourcesFromQuery(RESTCategoryCollectionV1.class, query.getMatrixParameters(),
                CategoryFilterQueryBuilder.class, new CategoryFieldFilter(), new CategoryV1Factory(),
                RESTv1Constants.CATEGORIES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTCategoryV1 updateJSONCategory(final String expand, final RESTCategoryV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONCategory(expand, dataObject, null, null, null);
    }

    @Override
    public RESTCategoryV1 updateJSONCategory(final String expand, final RESTCategoryV1 dataObject, final String message,
            final Integer flag, final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        if (dataObject.getId() == null)
            throw new InvalidParameterException("The dataObject.getId() parameter can not be null");

        final CategoryV1Factory factory = new CategoryV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntity(Category.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTCategoryCollectionV1 updateJSONCategories(final String expand, final RESTCategoryCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONCategories(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTCategoryCollectionV1 updateJSONCategories(final String expand, final RESTCategoryCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final CategoryV1Factory factory = new CategoryV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntities(RESTCategoryCollectionV1.class, Category.class, dataObjects, factory,
                RESTv1Constants.CATEGORIES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTCategoryV1 createJSONCategory(final String expand, final RESTCategoryV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONCategory(expand, dataObject, null, null, null);
    }

    @Override
    public RESTCategoryV1 createJSONCategory(final String expand, final RESTCategoryV1 dataObject, final String message,
            final Integer flag, final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final CategoryV1Factory factory = new CategoryV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntity(Category.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTCategoryCollectionV1 createJSONCategories(final String expand, final RESTCategoryCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONCategories(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTCategoryCollectionV1 createJSONCategories(final String expand, final RESTCategoryCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final CategoryV1Factory factory = new CategoryV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntities(RESTCategoryCollectionV1.class, Category.class, dataObjects, factory,
                RESTv1Constants.CATEGORIES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTCategoryV1 deleteJSONCategory(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return deleteJSONCategory(id, null, null, null, expand);
    }

    @Override
    public RESTCategoryV1 deleteJSONCategory(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand) throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        final CategoryV1Factory factory = new CategoryV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntity(Category.class, factory, id, expand, logDetails);
    }

    @Override
    public RESTCategoryCollectionV1 deleteJSONCategories(final PathSegment ids, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONCategories(ids, null, null, null, expand);
    }

    @Override
    public RESTCategoryCollectionV1 deleteJSONCategories(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (ids == null)
            throw new InvalidParameterException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();

        final CategoryV1Factory factory = new CategoryV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntities(RESTCategoryCollectionV1.class, Category.class, factory, dbEntityIds,
                RESTv1Constants.CATEGORIES_EXPANSION_NAME, expand, logDetails);
    }

    /* IMAGE FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPImage(final Integer id, final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONImage(id, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPImages(final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONImages(expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPImagesWithQuery(final PathSegment query, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONImagesWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPImage(final String expand, final RESTImageV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPImage(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String updateJSONPImage(final String expand, final RESTImageV1 dataObject, final String message, final Integer flag,
            final Integer userId, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(updateJSONImage(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPImages(final String expand, final RESTImageCollectionV1 dataObjects, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPImages(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String updateJSONPImages(final String expand, final RESTImageCollectionV1 dataObjects, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONImages(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPImage(final String expand, final RESTImageV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPImage(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String createJSONPImage(final String expand, final RESTImageV1 dataObject, final String message, final Integer flag,
            final Integer userId, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(createJSONImage(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPImages(final String expand, final RESTImageCollectionV1 dataObjects, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPImages(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String createJSONPImages(final String expand, final RESTImageCollectionV1 dataObjects, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONImages(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPImage(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPImage(id, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPImage(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONImage(id, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPImages(final PathSegment ids, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPImages(ids, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPImages(final PathSegment ids, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONImages(ids, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTImageV1 getJSONImage(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        return getJSONResource(ImageFile.class, new ImageV1Factory(), id, expand);
    }

    @Override
    public RESTImageV1 getJSONImageRevision(final Integer id, Integer revision, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        if (revision == null)
            throw new InvalidParameterException("The revision parameter can not be null");

        return getJSONResource(ImageFile.class, new ImageV1Factory(), id, revision, expand);
    }

    @Override
    public RESTImageCollectionV1 getJSONImages(final String expand) throws InvalidParameterException,
            InternalProcessingException {
        /*
         * Construct a collection with the given expansion name. The user will have to expand the collection to get the details
         * of the items in it.
         */
        return getJSONResources(RESTImageCollectionV1.class, ImageFile.class, new ImageV1Factory(),
                RESTv1Constants.IMAGES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTImageCollectionV1 getJSONImagesWithQuery(final PathSegment query, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return this.getJSONResourcesFromQuery(RESTImageCollectionV1.class, query.getMatrixParameters(),
                ImageFilterQueryBuilder.class, new ImageFieldFilter(), new ImageV1Factory(),
                RESTv1Constants.IMAGES_EXPANSION_NAME, expand);
    }

    @Override
    public RESTImageV1 updateJSONImage(final String expand, final RESTImageV1 dataObject) throws InvalidParameterException,
            InternalProcessingException {
        return updateJSONImage(expand, dataObject, null, null, null);
    }

    @Override
    public RESTImageV1 updateJSONImage(final String expand, final RESTImageV1 dataObject, final String message,
            final Integer flag, final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        if (dataObject.getId() == null)
            throw new InvalidParameterException("The dataObject.getId() parameter can not be null");

        final ImageV1Factory factory = new ImageV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntity(ImageFile.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTImageCollectionV1 updateJSONImages(final String expand, final RESTImageCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONImages(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTImageCollectionV1 updateJSONImages(final String expand, final RESTImageCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final ImageV1Factory factory = new ImageV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntities(RESTImageCollectionV1.class, ImageFile.class, dataObjects, factory,
                RESTv1Constants.IMAGES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTImageV1 createJSONImage(final String expand, final RESTImageV1 dataObject) throws InvalidParameterException,
            InternalProcessingException {
        return createJSONImage(expand, dataObject, null, null, null);
    }

    @Override
    public RESTImageV1 createJSONImage(final String expand, final RESTImageV1 dataObject, final String message,
            final Integer flag, final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final ImageV1Factory factory = new ImageV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntity(ImageFile.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTImageCollectionV1 createJSONImages(final String expand, final RESTImageCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONImages(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTImageCollectionV1 createJSONImages(final String expand, final RESTImageCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final ImageV1Factory factory = new ImageV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntities(RESTImageCollectionV1.class, ImageFile.class, dataObjects, factory,
                RESTv1Constants.IMAGES_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTImageV1 deleteJSONImage(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return deleteJSONImage(id, null, null, null, expand);
    }

    @Override
    public RESTImageV1 deleteJSONImage(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand) throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        final ImageV1Factory factory = new ImageV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntity(ImageFile.class, factory, id, expand, logDetails);
    }

    @Override
    public RESTImageCollectionV1 deleteJSONImages(final PathSegment ids, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return deleteJSONImages(ids, null, null, null, expand);
    }

    @Override
    public RESTImageCollectionV1 deleteJSONImages(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (ids == null)
            throw new InvalidParameterException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final ImageV1Factory factory = new ImageV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntities(RESTImageCollectionV1.class, ImageFile.class, factory, dbEntityIds,
                RESTv1Constants.IMAGES_EXPANSION_NAME, expand, logDetails);
    }

    /* TOPIC FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPTopicsWithQuery(PathSegment query, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTopicsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTopics(final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTopics(expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPTopic(final Integer id, final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONTopic(id, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPTopic(final String expand, final RESTTopicV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPTopic(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String updateJSONPTopic(final String expand, final RESTTopicV1 dataObject, final String message, final Integer flag,
            final Integer userId, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(updateJSONTopic(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPTopics(final String expand, final RESTTopicCollectionV1 dataObjects, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPTopics(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String updateJSONPTopics(final String expand, final RESTTopicCollectionV1 dataObjects, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONTopics(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPTopic(final String expand, final RESTTopicV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPTopic(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String createJSONPTopic(final String expand, final RESTTopicV1 dataObject, final String message, final Integer flag,
            final Integer userId, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(createJSONTopic(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPTopics(final String expand, final RESTTopicCollectionV1 dataObjects, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPTopics(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String createJSONPTopics(final String expand, final RESTTopicCollectionV1 dataObjects, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONTopics(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPTopic(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPTopic(id, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPTopic(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONTopic(id, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPTopics(final PathSegment ids, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPTopics(ids, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPTopics(final PathSegment ids, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONTopics(ids, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTTopicCollectionV1 getJSONTopics(final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return getJSONResources(RESTTopicCollectionV1.class, Topic.class, new TopicV1Factory(),
                RESTv1Constants.TOPICS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTopicCollectionV1 getJSONTopicsWithQuery(PathSegment query, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return getJSONResourcesFromQuery(RESTTopicCollectionV1.class, query.getMatrixParameters(),
                TopicFilterQueryBuilder.class, new TopicFieldFilter(), new TopicV1Factory(),
                RESTv1Constants.TOPICS_EXPANSION_NAME, expand);
    }

    @Override
    public Feed getATOMTopicsWithQuery(PathSegment query, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        final RESTTopicCollectionV1 topics = getJSONTopicsWithQuery(query, expand);
        return this.convertTopicsIntoFeed(topics, "Topic Query (" + topics.getSize() + " items)");
    }

    @Override
    public RESTTopicCollectionV1 getXMLTopics(final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return getXMLResources(RESTTopicCollectionV1.class, Topic.class, new TopicV1Factory(),
                RESTv1Constants.TOPICS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTTopicV1 getJSONTopic(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        assert id != null : "The id parameter can not be null";

        return getJSONResource(Topic.class, new TopicV1Factory(), id, expand);
    }

    @Override
    public RESTTopicV1 getJSONTopicRevision(final Integer id, final Integer revision, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        assert id != null : "The id parameter can not be null";
        assert revision != null : "The revision parameter can not be null";

        return getJSONResource(Topic.class, new TopicV1Factory(), id, revision, expand);
    }

    @Override
    public RESTTopicV1 getXMLTopic(final Integer id) throws InvalidParameterException, InternalProcessingException {
        assert id != null : "The id parameter can not be null";

        return getXMLResource(Topic.class, new TopicV1Factory(), id, null);
    }

    @Override
    public RESTTopicV1 getXMLTopicRevision(final Integer id, final Integer revision) throws InvalidParameterException,
            InternalProcessingException {
        assert id != null : "The id parameter can not be null";

        return getXMLResource(Topic.class, new TopicV1Factory(), id, revision, null);
    }

    @Override
    public String getXMLTopicXML(final Integer id, final String doctype) throws InvalidParameterException,
            InternalProcessingException {
        assert id != null : "The id parameter can not be null";

        if (doctype != null) {
            if (doctype.equalsIgnoreCase(RESTv1Constants.DOCBOOK_45_DOCTYPE_NAME)) {
                return DocBookUtilities.addDocbook45XMLDoctype(getXMLResource(Topic.class, new TopicV1Factory(), id, null)
                        .getXml(), null, "section");
            } else if (doctype.equalsIgnoreCase(RESTv1Constants.DOCBOOK_50_DOCTYPE_NAME)) {
                return DocBookUtilities.addDocbook50XMLDoctype(getXMLResource(Topic.class, new TopicV1Factory(), id, null)
                        .getXml(), null, "section");
            }
        }

        return getXMLResource(Topic.class, new TopicV1Factory(), id, null).getXml();
    }

    @Override
    public String getXMLTopicRevisionXML(final Integer id, final Integer revision, final String doctype)
            throws InvalidParameterException, InternalProcessingException {
        assert id != null : "The id parameter can not be null";

        if (doctype != null) {
            if (doctype.equalsIgnoreCase(RESTv1Constants.DOCBOOK_45_DOCTYPE_NAME)) {
                return DocBookUtilities.addDocbook45XMLDoctype(
                        getXMLResource(Topic.class, new TopicV1Factory(), id, revision, null).getXml(), null, "section");
            } else if (doctype.equalsIgnoreCase(RESTv1Constants.DOCBOOK_50_DOCTYPE_NAME)) {
                return DocBookUtilities.addDocbook50XMLDoctype(
                        getXMLResource(Topic.class, new TopicV1Factory(), id, revision, null).getXml(), null, "section");
            }
        }

        return getXMLResource(Topic.class, new TopicV1Factory(), id, revision, null).getXml();
    }

    @Override
    public String getXMLTopicXMLContained(final Integer id, final String containerName) throws InvalidParameterException,
            InternalProcessingException {
        assert id != null : "The id parameter can not be null";
        assert containerName != null : "The containerName parameter can not be null";

        return ComponentTopicV1.returnXMLWithNewContainer(getXMLResource(Topic.class, new TopicV1Factory(), id, null),
                containerName);
    }

    @Override
    public String getXMLTopicXMLNoContainer(final Integer id, final Boolean includeTitle) throws InvalidParameterException,
            InternalProcessingException {
        assert id != null : "The id parameter can not be null";

        final String retValue = ComponentTopicV1.returnXMLWithNoContainer(
                getXMLResource(Topic.class, new TopicV1Factory(), id, null), includeTitle);
        return retValue;
    }

    @Override
    public String getHTMLTopicHTML(final Integer id) throws InvalidParameterException, InternalProcessingException {
        assert id != null : "The id parameter can not be null";

        return getXMLResource(Topic.class, new TopicV1Factory(), id, null).getHtml();
    }

    @Override
    public String getHTMLTopicRevisionHTML(final Integer id, final Integer revision) throws InvalidParameterException,
            InternalProcessingException {
        assert id != null : "The id parameter can not be null";

        return getXMLResource(Topic.class, new TopicV1Factory(), id, revision, null).getHtml();
    }

    @Override
    public RESTTopicV1 updateJSONTopic(final String expand, final RESTTopicV1 dataObject) throws InvalidParameterException,
            InternalProcessingException {
        return updateJSONTopic(expand, dataObject, null, null, null);
    }

    @Override
    public RESTTopicV1 updateJSONTopic(final String expand, final RESTTopicV1 dataObject, final String message,
            final Integer flag, final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        if (dataObject.getId() == null)
            throw new InvalidParameterException("The dataObject.getId() parameter can not be null");

        final TopicV1Factory factory = new TopicV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntity(Topic.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTTopicCollectionV1 updateJSONTopics(final String expand, final RESTTopicCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONTopics(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTTopicCollectionV1 updateJSONTopics(final String expand, final RESTTopicCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final TopicV1Factory factory = new TopicV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntities(RESTTopicCollectionV1.class, Topic.class, dataObjects, factory,
                RESTv1Constants.TOPICS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTTopicV1 createJSONTopic(final String expand, final RESTTopicV1 dataObject) throws InvalidParameterException,
            InternalProcessingException {
        return createJSONTopic(expand, dataObject, null, null, null);
    }

    @Override
    public RESTTopicV1 createJSONTopic(final String expand, final RESTTopicV1 dataObject, final String message,
            final Integer flag, final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final TopicV1Factory factory = new TopicV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntity(Topic.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTTopicCollectionV1 createJSONTopics(final String expand, final RESTTopicCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONTopics(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTTopicCollectionV1 createJSONTopics(final String expand, final RESTTopicCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final TopicV1Factory factory = new TopicV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntities(RESTTopicCollectionV1.class, Topic.class, dataObjects, factory,
                RESTv1Constants.TOPICS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTTopicV1 deleteJSONTopic(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return deleteJSONTopic(id, null, null, null, expand);
    }

    @Override
    public RESTTopicV1 deleteJSONTopic(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand) throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        final TopicV1Factory factory = new TopicV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntity(Topic.class, factory, id, expand, logDetails);
    }

    @Override
    public RESTTopicCollectionV1 deleteJSONTopics(final PathSegment ids, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return deleteJSONTopics(ids, null, null, null, expand);
    }

    @Override
    public RESTTopicCollectionV1 deleteJSONTopics(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (ids == null)
            throw new InvalidParameterException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();

        final TopicV1Factory factory = new TopicV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntities(RESTTopicCollectionV1.class, Topic.class, factory, dbEntityIds,
                RESTv1Constants.TOPICS_EXPANSION_NAME, expand, logDetails);
    }

    /* FILTERS FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPFilter(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONFilter(id, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPFilters(final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONFilters(expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPFiltersWithQuery(final PathSegment query, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONFiltersWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPFilter(final String expand, final RESTFilterV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPFilter(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String updateJSONPFilter(final String expand, final RESTFilterV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(updateJSONFilter(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPFilters(final String expand, final RESTFilterCollectionV1 dataObjects, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPFilters(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String updateJSONPFilters(final String expand, final RESTFilterCollectionV1 dataObjects, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONFilters(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPFilter(final String expand, final RESTFilterV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPFilter(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String createJSONPFilter(final String expand, final RESTFilterV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(createJSONFilter(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPFilters(final String expand, final RESTFilterCollectionV1 dataObjects, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPFilters(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String createJSONPFilters(final String expand, final RESTFilterCollectionV1 dataObjects, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONFilters(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPFilter(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPFilter(id, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPFilter(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONFilter(id, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPFilters(final PathSegment ids, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPFilters(ids, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPFilters(final PathSegment ids, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONFilters(ids, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTFilterV1 getJSONFilter(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        return getJSONResource(Filter.class, new FilterV1Factory(), id, expand);
    }

    @Override
    public RESTFilterV1 getJSONFilterRevision(final Integer id, final Integer revision, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        if (revision == null)
            throw new InvalidParameterException("The revision parameter can not be null");

        return getJSONResource(Filter.class, new FilterV1Factory(), id, revision, expand);
    }

    @Override
    public RESTFilterCollectionV1 getJSONFilters(final String expand) throws InvalidParameterException,
            InternalProcessingException {
        /*
         * Construct a collection with the given expansion name. The user will have to expand the collection to get the details
         * of the items in it.
         */
        return getJSONResources(RESTFilterCollectionV1.class, Filter.class, new FilterV1Factory(),
                RESTv1Constants.FILTERS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTFilterCollectionV1 getJSONFiltersWithQuery(final PathSegment query, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return getJSONResourcesFromQuery(RESTFilterCollectionV1.class, query.getMatrixParameters(),
                FilterFilterQueryBuilder.class, new FilterFieldFilter(), new FilterV1Factory(),
                RESTv1Constants.FILTERS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTFilterV1 updateJSONFilter(final String expand, final RESTFilterV1 dataObject) throws InvalidParameterException,
            InternalProcessingException {
        return updateJSONFilter(expand, dataObject, null, null, null);
    }

    @Override
    public RESTFilterV1 updateJSONFilter(final String expand, final RESTFilterV1 dataObject, final String message,
            final Integer flag, final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        if (dataObject.getId() == null)
            throw new InvalidParameterException("The dataObject.getId() parameter can not be null");

        final FilterV1Factory factory = new FilterV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntity(Filter.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTFilterCollectionV1 updateJSONFilters(final String expand, final RESTFilterCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONFilters(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTFilterCollectionV1 updateJSONFilters(final String expand, final RESTFilterCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final FilterV1Factory factory = new FilterV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntities(RESTFilterCollectionV1.class, Filter.class, dataObjects, factory,
                RESTv1Constants.FILTERS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTFilterV1 createJSONFilter(final String expand, final RESTFilterV1 dataObject) throws InvalidParameterException,
            InternalProcessingException {
        return createJSONFilter(expand, dataObject, null, null, null);
    }

    @Override
    public RESTFilterV1 createJSONFilter(final String expand, final RESTFilterV1 dataObject, final String message,
            final Integer flag, final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final FilterV1Factory factory = new FilterV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntity(Filter.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTFilterCollectionV1 createJSONFilters(final String expand, final RESTFilterCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONFilters(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTFilterCollectionV1 createJSONFilters(final String expand, final RESTFilterCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final FilterV1Factory factory = new FilterV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntities(RESTFilterCollectionV1.class, Filter.class, dataObjects, factory,
                RESTv1Constants.FILTERS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTFilterV1 deleteJSONFilter(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return deleteJSONFilter(id, null, null, null, expand);
    }

    @Override
    public RESTFilterV1 deleteJSONFilter(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand) throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        final FilterV1Factory factory = new FilterV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntity(Filter.class, factory, id, expand, logDetails);
    }

    @Override
    public RESTFilterCollectionV1 deleteJSONFilters(final PathSegment ids, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONFilters(ids, null, null, null, expand);
    }

    @Override
    public RESTFilterCollectionV1 deleteJSONFilters(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (ids == null)
            throw new InvalidParameterException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final FilterV1Factory factory = new FilterV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntities(RESTFilterCollectionV1.class, Filter.class, factory, dbEntityIds,
                RESTv1Constants.FILTERS_EXPANSION_NAME, expand, logDetails);
    }

    /* INTEGERCONSTANT FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPIntegerConstant(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONIntegerConstant(id, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPIntegerConstants(final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONIntegerConstants(expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPIntegerConstantsWithQuery(final PathSegment query, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONIntegerConstantsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPIntegerConstant(final String expand, final RESTIntegerConstantV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPIntegerConstant(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String updateJSONPIntegerConstant(final String expand, final RESTIntegerConstantV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONIntegerConstant(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPIntegerConstants(final String expand, final RESTIntegerConstantCollectionV1 dataObjects,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return updateJSONPIntegerConstants(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String updateJSONPIntegerConstants(final String expand, final RESTIntegerConstantCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONIntegerConstants(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPIntegerConstant(final String expand, final RESTIntegerConstantV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPIntegerConstant(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String createJSONPIntegerConstant(final String expand, final RESTIntegerConstantV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONIntegerConstant(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPIntegerConstants(final String expand, final RESTIntegerConstantCollectionV1 dataObjects,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return createJSONPIntegerConstants(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String createJSONPIntegerConstants(final String expand, final RESTIntegerConstantCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONIntegerConstants(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPIntegerConstant(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPIntegerConstant(id, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPIntegerConstant(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(deleteJSONIntegerConstant(id, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPIntegerConstants(final PathSegment ids, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPIntegerConstants(ids, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPIntegerConstants(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(deleteJSONIntegerConstants(ids, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTIntegerConstantV1 getJSONIntegerConstant(final Integer id, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        return getJSONResource(IntegerConstants.class, new IntegerConstantV1Factory(), id, expand);
    }

    @Override
    public RESTIntegerConstantCollectionV1 getJSONIntegerConstants(final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return getJSONResources(RESTIntegerConstantCollectionV1.class, IntegerConstants.class, new IntegerConstantV1Factory(),
                RESTv1Constants.INTEGERCONSTANTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTIntegerConstantCollectionV1 getJSONIntegerConstantsWithQuery(final PathSegment query, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return getJSONResourcesFromQuery(RESTIntegerConstantCollectionV1.class, query.getMatrixParameters(),
                IntegerConstantFilterQueryBuilder.class, new IntegerConstantFieldFilter(), new IntegerConstantV1Factory(),
                RESTv1Constants.INTEGERCONSTANTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTIntegerConstantV1 updateJSONIntegerConstant(final String expand, final RESTIntegerConstantV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONIntegerConstant(expand, dataObject, null, null, null);
    }

    @Override
    public RESTIntegerConstantV1 updateJSONIntegerConstant(final String expand, final RESTIntegerConstantV1 dataObject,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        if (dataObject.getId() == null)
            throw new InvalidParameterException("The dataObject.getId() parameter can not be null");

        final IntegerConstantV1Factory factory = new IntegerConstantV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntity(IntegerConstants.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTIntegerConstantCollectionV1 updateJSONIntegerConstants(final String expand,
            final RESTIntegerConstantCollectionV1 dataObjects) throws InvalidParameterException, InternalProcessingException {
        return updateJSONIntegerConstants(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTIntegerConstantCollectionV1 updateJSONIntegerConstants(final String expand,
            final RESTIntegerConstantCollectionV1 dataObjects, final String message, final Integer flag, final Integer userId)
            throws InvalidParameterException, InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final IntegerConstantV1Factory factory = new IntegerConstantV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntities(RESTIntegerConstantCollectionV1.class, IntegerConstants.class, dataObjects, factory,
                RESTv1Constants.INTEGERCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTIntegerConstantV1 createJSONIntegerConstant(final String expand, final RESTIntegerConstantV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONIntegerConstant(expand, dataObject, null, null, null);
    }

    @Override
    public RESTIntegerConstantV1 createJSONIntegerConstant(final String expand, final RESTIntegerConstantV1 dataObject,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final IntegerConstantV1Factory factory = new IntegerConstantV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntity(IntegerConstants.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTIntegerConstantCollectionV1 createJSONIntegerConstants(final String expand,
            final RESTIntegerConstantCollectionV1 dataObjects) throws InvalidParameterException, InternalProcessingException {
        return createJSONIntegerConstants(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTIntegerConstantCollectionV1 createJSONIntegerConstants(final String expand,
            final RESTIntegerConstantCollectionV1 dataObjects, final String message, final Integer flag, final Integer userId)
            throws InvalidParameterException, InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final IntegerConstantV1Factory factory = new IntegerConstantV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntities(RESTIntegerConstantCollectionV1.class, IntegerConstants.class, dataObjects, factory,
                RESTv1Constants.INTEGERCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTIntegerConstantV1 deleteJSONIntegerConstant(final Integer id, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONIntegerConstant(id, null, null, null, expand);
    }

    @Override
    public RESTIntegerConstantV1 deleteJSONIntegerConstant(final Integer id, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        final IntegerConstantV1Factory factory = new IntegerConstantV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntity(IntegerConstants.class, factory, id, expand, logDetails);
    }

    @Override
    public RESTIntegerConstantCollectionV1 deleteJSONIntegerConstants(final PathSegment ids, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONIntegerConstants(ids, null, null, null, expand);
    }

    @Override
    public RESTIntegerConstantCollectionV1 deleteJSONIntegerConstants(final PathSegment ids, final String message,
            final Integer flag, final Integer userId, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        if (ids == null)
            throw new InvalidParameterException("The ids parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();
        final IntegerConstantV1Factory factory = new IntegerConstantV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntities(RESTIntegerConstantCollectionV1.class, IntegerConstants.class, factory, dbEntityIds,
                RESTv1Constants.INTEGERCONSTANTS_EXPANSION_NAME, expand, logDetails);
    }

    /* CONTENT SPEC FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPContentSpec(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONContentSpec(id, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPContentSpecs(final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONContentSpecs(expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPContentSpecsWithQuery(final PathSegment query, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONContentSpecsWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPContentSpec(final String expand, final RESTContentSpecV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPContentSpec(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String updateJSONPContentSpec(final String expand, final RESTContentSpecV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONContentSpec(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPContentSpecs(final String expand, final RESTContentSpecCollectionV1 dataObjects,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return updateJSONPContentSpecs(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String updateJSONPContentSpecs(final String expand, final RESTContentSpecCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONContentSpecs(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPContentSpec(final String expand, final RESTContentSpecV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPContentSpec(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String createJSONPContentSpec(final String expand, final RESTContentSpecV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONContentSpec(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPContentSpecs(final String expand, final RESTContentSpecCollectionV1 dataObjects,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return createJSONPContentSpecs(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String createJSONPContentSpecs(final String expand, final RESTContentSpecCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONContentSpecs(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPContentSpec(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPContentSpec(id, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPContentSpec(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONContentSpec(id, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPContentSpecs(final PathSegment ids, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPContentSpecs(ids, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPContentSpecs(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(deleteJSONContentSpecs(ids, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTContentSpecV1 getJSONContentSpec(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        return getJSONResource(ContentSpec.class, new ContentSpecV1Factory(), id, expand);
    }

    @Override
    public RESTContentSpecCollectionV1 getJSONContentSpecs(final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return getJSONResources(RESTContentSpecCollectionV1.class, ContentSpec.class, new ContentSpecV1Factory(),
                RESTv1Constants.INTEGERCONSTANTS_EXPANSION_NAME, expand);
    }

    @Override
    public RESTContentSpecCollectionV1 getJSONContentSpecsWithQuery(final PathSegment query, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return getJSONResourcesFromQuery(RESTContentSpecCollectionV1.class, query.getMatrixParameters(),
                ContentSpecFilterQueryBuilder.class, new ContentSpecFieldFilter(), new ContentSpecV1Factory(),
                RESTv1Constants.CONTENT_SPEC_EXPANSION_NAME, expand);
    }

    @Override
    public RESTContentSpecV1 updateJSONContentSpec(final String expand, final RESTContentSpecV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONContentSpec(expand, dataObject, null, null, null);
    }

    @Override
    public RESTContentSpecV1 updateJSONContentSpec(final String expand, final RESTContentSpecV1 dataObject,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        if (dataObject.getId() == null)
            throw new InvalidParameterException("The dataObject.getId() parameter can not be null");

        final ContentSpecV1Factory factory = new ContentSpecV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntity(ContentSpec.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTContentSpecCollectionV1 updateJSONContentSpecs(final String expand, final RESTContentSpecCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONContentSpecs(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTContentSpecCollectionV1 updateJSONContentSpecs(final String expand,
            final RESTContentSpecCollectionV1 dataObjects, final String message, final Integer flag, final Integer userId)
            throws InvalidParameterException, InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final ContentSpecV1Factory factory = new ContentSpecV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntities(RESTContentSpecCollectionV1.class, ContentSpec.class, dataObjects, factory,
                RESTv1Constants.CONTENT_SPEC_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTContentSpecV1 createJSONContentSpec(final String expand, final RESTContentSpecV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONContentSpec(expand, dataObject, null, null, null);
    }

    @Override
    public RESTContentSpecV1 createJSONContentSpec(final String expand, final RESTContentSpecV1 dataObject,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final ContentSpecV1Factory factory = new ContentSpecV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntity(ContentSpec.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTContentSpecCollectionV1 createJSONContentSpecs(final String expand, final RESTContentSpecCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONContentSpecs(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTContentSpecCollectionV1 createJSONContentSpecs(final String expand,
            final RESTContentSpecCollectionV1 dataObjects, final String message, final Integer flag, final Integer userId)
            throws InvalidParameterException, InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final ContentSpecV1Factory factory = new ContentSpecV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntities(RESTContentSpecCollectionV1.class, ContentSpec.class, dataObjects, factory,
                RESTv1Constants.CONTENT_SPEC_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTContentSpecV1 deleteJSONContentSpec(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return deleteJSONContentSpec(id, null, null, null, expand);
    }

    @Override
    public RESTContentSpecV1 deleteJSONContentSpec(final Integer id, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final ContentSpecV1Factory factory = new ContentSpecV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntity(ContentSpec.class, factory, id, expand, logDetails);
    }

    @Override
    public RESTContentSpecCollectionV1 deleteJSONContentSpecs(final PathSegment ids, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONContentSpecs(ids, null, null, null, expand);
    }

    @Override
    public RESTContentSpecCollectionV1 deleteJSONContentSpecs(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (ids == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();

        final ContentSpecV1Factory factory = new ContentSpecV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntities(RESTContentSpecCollectionV1.class, ContentSpec.class, factory, dbEntityIds,
                RESTv1Constants.CONTENT_SPEC_EXPANSION_NAME, expand, logDetails);
    }

    /* CONTENT SPEC NODE FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPContentSpecNode(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONContentSpecNode(id, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPContentSpecNodes(final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONContentSpecNodes(expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPContentSpecNodesWithQuery(final PathSegment query, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONContentSpecNodesWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPContentSpecNode(final String expand, final RESTCSNodeV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPContentSpecNode(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String updateJSONPContentSpecNode(final String expand, final RESTCSNodeV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONContentSpecNode(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPContentSpecNodes(final String expand, final RESTCSNodeCollectionV1 dataObjects,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return updateJSONPContentSpecNodes(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String updateJSONPContentSpecNodes(final String expand, final RESTCSNodeCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONContentSpecNodes(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPContentSpecNode(final String expand, final RESTCSNodeV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPContentSpecNode(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String createJSONPContentSpecNode(final String expand, final RESTCSNodeV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONContentSpecNode(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPContentSpecNodes(final String expand, final RESTCSNodeCollectionV1 dataObjects,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return createJSONPContentSpecNodes(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String createJSONPContentSpecNodes(final String expand, final RESTCSNodeCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONContentSpecNodes(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPContentSpecNode(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPContentSpecNode(id, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPContentSpecNode(final Integer id, final String message, final Integer flag, final Integer userId,
            final String expand, final String callback) throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(deleteJSONContentSpecNode(id, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPContentSpecNodes(final PathSegment ids, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPContentSpecNodes(ids, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPContentSpecNodes(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(deleteJSONContentSpecNodes(ids, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTCSNodeV1 getJSONContentSpecNode(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        return getJSONResource(CSNode.class, new CSNodeV1Factory(), id, expand);
    }

    @Override
    public RESTCSNodeCollectionV1 getJSONContentSpecNodes(final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return getJSONResources(RESTCSNodeCollectionV1.class, CSNode.class, new CSNodeV1Factory(),
                RESTv1Constants.CONTENT_SPEC_NODE_EXPANSION_NAME, expand);
    }

    @Override
    public RESTCSNodeCollectionV1 getJSONContentSpecNodesWithQuery(final PathSegment query, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return getJSONResourcesFromQuery(RESTCSNodeCollectionV1.class, query.getMatrixParameters(),
                ContentSpecNodeFilterQueryBuilder.class, new ContentSpecNodeFieldFilter(), new CSNodeV1Factory(),
                RESTv1Constants.CONTENT_SPEC_NODE_EXPANSION_NAME, expand);
    }

    @Override
    public RESTCSNodeV1 updateJSONContentSpecNode(final String expand, final RESTCSNodeV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONContentSpecNode(expand, dataObject, null, null, null);
    }

    @Override
    public RESTCSNodeV1 updateJSONContentSpecNode(final String expand, final RESTCSNodeV1 dataObject, final String message,
            final Integer flag, final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        if (dataObject.getId() == null)
            throw new InvalidParameterException("The dataObject.getId() parameter can not be null");

        final CSNodeV1Factory factory = new CSNodeV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntity(CSNode.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTCSNodeCollectionV1 updateJSONContentSpecNodes(final String expand, final RESTCSNodeCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONContentSpecNodes(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTCSNodeCollectionV1 updateJSONContentSpecNodes(final String expand, final RESTCSNodeCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final CSNodeV1Factory factory = new CSNodeV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntities(RESTCSNodeCollectionV1.class, CSNode.class, dataObjects, factory,
                RESTv1Constants.CONTENT_SPEC_NODE_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTCSNodeV1 createJSONContentSpecNode(final String expand, final RESTCSNodeV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONContentSpecNode(expand, dataObject, null, null, null);
    }

    @Override
    public RESTCSNodeV1 createJSONContentSpecNode(final String expand, final RESTCSNodeV1 dataObject, final String message,
            final Integer flag, final Integer userId) throws InvalidParameterException, InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final CSNodeV1Factory factory = new CSNodeV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntity(CSNode.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTCSNodeCollectionV1 createJSONContentSpecNodes(final String expand, final RESTCSNodeCollectionV1 dataObjects)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONContentSpecNodes(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTCSNodeCollectionV1 createJSONContentSpecNodes(final String expand, final RESTCSNodeCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final CSNodeV1Factory factory = new CSNodeV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntities(RESTCSNodeCollectionV1.class, CSNode.class, dataObjects, factory,
                RESTv1Constants.CONTENT_SPEC_NODE_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTCSNodeV1 deleteJSONContentSpecNode(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return deleteJSONContentSpecNode(id, null, null, null, expand);
    }

    @Override
    public RESTCSNodeV1 deleteJSONContentSpecNode(final Integer id, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final CSNodeV1Factory factory = new CSNodeV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntity(CSNode.class, factory, id, expand, logDetails);
    }

    @Override
    public RESTCSNodeCollectionV1 deleteJSONContentSpecNodes(final PathSegment ids, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONContentSpecNodes(ids, null, null, null, expand);
    }

    @Override
    public RESTCSNodeCollectionV1 deleteJSONContentSpecNodes(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (ids == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();

        final CSNodeV1Factory factory = new CSNodeV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntities(RESTCSNodeCollectionV1.class, CSNode.class, factory, dbEntityIds,
                RESTv1Constants.CONTENT_SPEC_NODE_EXPANSION_NAME, expand, logDetails);
    }

    /* CONTENT SPEC NODE FUNCTIONS */
    /* JSONP FUNCTIONS */
    @Override
    public String getJSONPContentSpecMetaData(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONContentSpecMetaData(id, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPContentSpecMetaDatas(final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONContentSpecMetaDatas(expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String getJSONPContentSpecMetaDatasWithQuery(final PathSegment query, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback, convertObjectToJSON(getJSONContentSpecMetaDatasWithQuery(query, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPContentSpecMetaData(final String expand, final RESTCSMetaDataV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONPContentSpecMetaData(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String updateJSONPContentSpecMetaData(final String expand, final RESTCSMetaDataV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONContentSpecMetaData(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String updateJSONPContentSpecMetaDatas(final String expand, final RESTCSMetaDataCollectionV1 dataObjects,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return updateJSONPContentSpecMetaDatas(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String updateJSONPContentSpecMetaDatas(final String expand, final RESTCSMetaDataCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(updateJSONContentSpecMetaDatas(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPContentSpecMetaData(final String expand, final RESTCSMetaDataV1 dataObject, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONPContentSpecMetaData(expand, dataObject, null, null, null, callback);
    }

    @Override
    public String createJSONPContentSpecMetaData(final String expand, final RESTCSMetaDataV1 dataObject, final String message,
            final Integer flag, final Integer userId, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONContentSpecMetaData(expand, dataObject, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String createJSONPContentSpecMetaDatas(final String expand, final RESTCSMetaDataCollectionV1 dataObjects,
            final String callback) throws InvalidParameterException, InternalProcessingException {
        return createJSONPContentSpecMetaDatas(expand, dataObjects, null, null, null, callback);
    }

    @Override
    public String createJSONPContentSpecMetaDatas(final String expand, final RESTCSMetaDataCollectionV1 dataObjects,
            final String message, final Integer flag, final Integer userId, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(createJSONContentSpecMetaDatas(expand, dataObjects, message, flag, userId)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPContentSpecMetaData(final Integer id, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPContentSpecMetaData(id, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPContentSpecMetaData(final Integer id, final String message, final Integer flag,
            final Integer userId, final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(deleteJSONContentSpecMetaData(id, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    @Override
    public String deleteJSONPContentSpecMetaDatas(final PathSegment ids, final String expand, final String callback)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONPContentSpecMetaDatas(ids, null, null, null, expand, callback);
    }

    @Override
    public String deleteJSONPContentSpecMetaDatas(final PathSegment ids, final String message, final Integer flag,
            final Integer userId, final String expand, final String callback) throws InvalidParameterException,
            InternalProcessingException {
        if (callback == null)
            throw new InvalidParameterException("The callback parameter can not be null");

        try {
            return wrapJsonInPadding(callback,
                    convertObjectToJSON(deleteJSONContentSpecMetaDatas(ids, message, flag, userId, expand)));
        } catch (final Exception ex) {
            throw new InternalProcessingException("Could not marshall return value into JSON");
        }
    }

    /* JSON FUNCTIONS */
    @Override
    public RESTCSMetaDataV1 getJSONContentSpecMetaData(final Integer id, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The id parameter can not be null");

        return getJSONResource(CSMetaData.class, new CSMetaDataV1Factory(), id, expand);
    }

    @Override
    public RESTCSMetaDataCollectionV1 getJSONContentSpecMetaDatas(final String expand) throws InvalidParameterException,
            InternalProcessingException {
        return getJSONResources(RESTCSMetaDataCollectionV1.class, CSMetaData.class, new CSMetaDataV1Factory(),
                RESTv1Constants.CONTENT_SPEC_META_DATA_EXPANSION_NAME, expand);
    }

    @Override
    public RESTCSMetaDataCollectionV1 getJSONContentSpecMetaDatasWithQuery(final PathSegment query, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return getJSONResourcesFromQuery(RESTCSMetaDataCollectionV1.class, query.getMatrixParameters(),
                ContentSpecMetaDataFilterQueryBuilder.class, new ContentSpecMetaDataFieldFilter(), new CSMetaDataV1Factory(),
                RESTv1Constants.CONTENT_SPEC_META_DATA_EXPANSION_NAME, expand);
    }

    @Override
    public RESTCSMetaDataV1 updateJSONContentSpecMetaData(final String expand, final RESTCSMetaDataV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return updateJSONContentSpecMetaData(expand, dataObject, null, null, null);
    }

    @Override
    public RESTCSMetaDataV1 updateJSONContentSpecMetaData(final String expand, final RESTCSMetaDataV1 dataObject,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        if (dataObject.getId() == null)
            throw new InvalidParameterException("The dataObject.getId() parameter can not be null");

        final CSMetaDataV1Factory factory = new CSMetaDataV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntity(CSMetaData.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTCSMetaDataCollectionV1 updateJSONContentSpecMetaDatas(final String expand,
            final RESTCSMetaDataCollectionV1 dataObjects) throws InvalidParameterException, InternalProcessingException {
        return updateJSONContentSpecMetaDatas(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTCSMetaDataCollectionV1 updateJSONContentSpecMetaDatas(final String expand,
            final RESTCSMetaDataCollectionV1 dataObjects, final String message, final Integer flag, final Integer userId)
            throws InvalidParameterException, InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final CSMetaDataV1Factory factory = new CSMetaDataV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return updateJSONEntities(RESTCSMetaDataCollectionV1.class, CSMetaData.class, dataObjects, factory,
                RESTv1Constants.CONTENT_SPEC_META_DATA_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTCSMetaDataV1 createJSONContentSpecMetaData(final String expand, final RESTCSMetaDataV1 dataObject)
            throws InvalidParameterException, InternalProcessingException {
        return createJSONContentSpecMetaData(expand, dataObject, null, null, null);
    }

    @Override
    public RESTCSMetaDataV1 createJSONContentSpecMetaData(final String expand, final RESTCSMetaDataV1 dataObject,
            final String message, final Integer flag, final Integer userId) throws InvalidParameterException,
            InternalProcessingException {
        if (dataObject == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final CSMetaDataV1Factory factory = new CSMetaDataV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntity(CSMetaData.class, dataObject, factory, expand, logDetails);
    }

    @Override
    public RESTCSMetaDataCollectionV1 createJSONContentSpecMetaDatas(final String expand,
            final RESTCSMetaDataCollectionV1 dataObjects) throws InvalidParameterException, InternalProcessingException {
        return createJSONContentSpecMetaDatas(expand, dataObjects, null, null, null);
    }

    @Override
    public RESTCSMetaDataCollectionV1 createJSONContentSpecMetaDatas(final String expand,
            final RESTCSMetaDataCollectionV1 dataObjects, final String message, final Integer flag, final Integer userId)
            throws InvalidParameterException, InternalProcessingException {
        if (dataObjects == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        if (dataObjects.getItems() == null)
            throw new InvalidParameterException("The dataObjects.getItems() parameter can not be null");

        final CSMetaDataV1Factory factory = new CSMetaDataV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return createJSONEntities(RESTCSMetaDataCollectionV1.class, CSMetaData.class, dataObjects, factory,
                RESTv1Constants.CONTENT_SPEC_META_DATA_EXPANSION_NAME, expand, logDetails);
    }

    @Override
    public RESTCSMetaDataV1 deleteJSONContentSpecMetaData(final Integer id, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONContentSpecMetaData(id, null, null, null, expand);
    }

    @Override
    public RESTCSMetaDataV1 deleteJSONContentSpecMetaData(final Integer id, final String message, final Integer flag,
            final Integer userId, final String expand) throws InvalidParameterException, InternalProcessingException {
        if (id == null)
            throw new InvalidParameterException("The dataObject parameter can not be null");

        final CSMetaDataV1Factory factory = new CSMetaDataV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntity(CSMetaData.class, factory, id, expand, logDetails);
    }

    @Override
    public RESTCSMetaDataCollectionV1 deleteJSONContentSpecMetaDatas(final PathSegment ids, final String expand)
            throws InvalidParameterException, InternalProcessingException {
        return deleteJSONContentSpecMetaDatas(ids, null, null, null, expand);
    }

    @Override
    public RESTCSMetaDataCollectionV1 deleteJSONContentSpecMetaDatas(final PathSegment ids, final String message,
            final Integer flag, final Integer userId, final String expand) throws InvalidParameterException,
            InternalProcessingException {
        if (ids == null)
            throw new InvalidParameterException("The dataObjects parameter can not be null");

        final Set<String> dbEntityIds = ids.getMatrixParameters().keySet();

        final CSMetaDataV1Factory factory = new CSMetaDataV1Factory();
        final RESTLogDetailsV1 logDetails = generateLogDetails(message, flag, userId);

        return deleteJSONEntities(RESTCSMetaDataCollectionV1.class, CSMetaData.class, factory, dbEntityIds,
                RESTv1Constants.CONTENT_SPEC_META_DATA_EXPANSION_NAME, expand, logDetails);
    }
}
