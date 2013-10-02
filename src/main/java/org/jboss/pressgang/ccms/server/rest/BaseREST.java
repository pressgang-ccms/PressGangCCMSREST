package org.jboss.pressgang.ccms.server.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

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
}