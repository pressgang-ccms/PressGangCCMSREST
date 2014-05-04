package org.jboss.pressgang.ccms.server.rest.v1.interceptor;

import org.jboss.pressgang.ccms.model.config.ApplicationConfig;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Prevent access to PUT, POST or DELETE methods when the server is readonly.
 */
@Provider
@ServerInterceptor
public class RESTv1ReadOnlyInterceptor implements PreProcessInterceptor {

    @Override
    public ServerResponse preProcess(HttpRequest httpRequest, ResourceMethod resourceMethod) throws Failure, WebApplicationException {
        if (ApplicationConfig.getInstance().getReadOnly()) {
            return new ServerResponse(
                    "The server is readonly, and forbids all calls to POST, PUT and GET endpoints",
                    Response.Status.FORBIDDEN.getStatusCode(),
                    new Headers<Object>());
        }

        return null;
    }
}
