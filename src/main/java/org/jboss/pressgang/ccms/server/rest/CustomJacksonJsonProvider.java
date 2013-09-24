package org.jboss.pressgang.ccms.server.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.DeserializationConfig;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;

@Provider
@Consumes({MediaType.APPLICATION_JSON, "application/*+json", "text/json"})
@Produces({MediaType.APPLICATION_JSON, "application/*+json", "text/json"})
public class CustomJacksonJsonProvider extends ResteasyJacksonProvider {

    public CustomJacksonJsonProvider() {
        configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
