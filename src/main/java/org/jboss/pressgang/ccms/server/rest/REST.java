package org.jboss.pressgang.ccms.server.rest;

import javax.ws.rs.Path;

import org.jboss.pressgang.ccms.rest.collections.RESTVersionDetailsCollection;
import org.jboss.pressgang.ccms.rest.jaxrsinterfaces.RESTInterface;
import org.jboss.pressgang.ccms.server.utils.Constants;

@Path(Constants.BASE_REST_PATH)
public class REST extends BaseREST implements RESTInterface {

    @Override
    public RESTVersionDetailsCollection setVersionDetails() {
        return new VersionDetailsFactory().create(getBaseUrl());
    }
}
