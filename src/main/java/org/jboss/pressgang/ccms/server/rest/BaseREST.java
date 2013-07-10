package org.jboss.pressgang.ccms.server.rest;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.server.utils.Constants;

public abstract class BaseREST {
    /**
     * The Uri Information for the REST Request
     */
    @Context
    protected UriInfo uriInfo;

    /**
     * Get the URL of the root REST endpoint using UriInfo from a request.
     *
     * @return The URL of the root REST endpoint.
     */
    protected String getBaseUrl() {
        final String fullPath = uriInfo.getAbsolutePath().toString();
        final int index = fullPath.indexOf(Constants.BASE_REST_PATH);
        if (index != -1) return fullPath.substring(0, index + Constants.BASE_REST_PATH.length());

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
     * Get the URL of the REST endpoint from the calling request.
     *
     * @return The URL of the endpoint that was called for the request.
     */
    protected String getRequestUrl() {
        return uriInfo.getRequestUri().toString();
    }

    /**
     * This method will match any preflight CORS request, and can be used as a central location to manage cross origin requests.
     * <p/>
     * Note: The browser restrictions on cross site requests are a very insecure way to prevent cross site access (you can just
     * setup a proxy).
     *
     * @param requestMethod  The Access-Control-Request-Method header
     * @param requestHeaders The Access-Control-Request-Headers header
     * @return A HTTP response that indicates CORS requests are valid.
     */
    @OPTIONS
    @Path("/{path:.*}")
    public Response handleCORSRequest(@HeaderParam(RESTv1Constants.ACCESS_CONTROL_REQUEST_METHOD) final String requestMethod,
            @HeaderParam(RESTv1Constants.ACCESS_CONTROL_REQUEST_HEADERS) final String requestHeaders) {
        final Response.ResponseBuilder retValue = Response.ok();

        if (Constants.CORS_ALLOW_ORIGIN_HEADER != null) {
            if (requestHeaders != null) retValue.header(RESTv1Constants.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders);

            if (requestMethod != null) retValue.header(RESTv1Constants.ACCESS_CONTROL_ALLOW_METHODS, requestMethod);

            retValue.header(RESTv1Constants.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, Constants.CORS_ALLOW_ORIGIN_HEADER);

            /**
             * Headers that do not fall under the category of simple response headers have to be
             * specifically allowed via Access-Control-Expose-Headers.
             * http://www.w3.org/TR/cors/#simple-response-header
             */
            retValue.header("Access-Control-Expose-Headers", Constants.X_PRESSGANG_VERSION_HEADER);
        }

        return retValue.build();
    }
}