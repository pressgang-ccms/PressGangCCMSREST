package org.jboss.pressgang.ccms.server.rest;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.jboss.pressgang.ccms.rest.collections.RESTVersionDetailsCollection;
import org.jboss.pressgang.ccms.rest.jaxrsinterfaces.RESTInterface;
import org.jboss.pressgang.ccms.server.utils.Constants;

@Path("/rest")
public class REST implements RESTInterface {

    @Context
    private UriInfo uriInfo;

    @Override
    public RESTVersionDetailsCollection setVersionDetails() {
        return new VersionDetailsFactory().create(getBaseUrl());
    }

    protected String getBaseUrl() {
        final String fullPath = uriInfo.getAbsolutePath().toString();
        final int index = fullPath.indexOf(Constants.BASE_REST_PATH);
        if (index != -1) return fullPath.substring(0, index + Constants.BASE_REST_PATH.length());

        return null;
    }
}
