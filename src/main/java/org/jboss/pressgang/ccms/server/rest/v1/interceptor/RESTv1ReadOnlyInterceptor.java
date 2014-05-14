package org.jboss.pressgang.ccms.server.rest.v1.interceptor;

import org.jboss.pressgang.ccms.model.config.ApplicationConfig;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

import javax.ws.rs.HttpMethod;
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
    public ServerResponse preProcess(final HttpRequest httpRequest, final ResourceMethod resourceMethod) throws Failure, WebApplicationException {
        if (ApplicationConfig.getInstance().getReadOnly()) {

            final Path path = resourceMethod.getMethod().getAnnotation(Path.class);
            /*
                Although this is a post method, it does not modify the database, so is allowed
             */
            if (!path.value().equals("/holdxml")) {
                if (resourceMethod.getHttpMethods().contains(HttpMethod.PUT) ||
                        resourceMethod.getHttpMethods().contains(HttpMethod.POST) ||
                        resourceMethod.getHttpMethods().contains(HttpMethod.DELETE)) {
                    return new ServerResponse(
                            "The server is readonly, and forbids all calls to POST, PUT and DELETE endpoints",
                            Response.Status.FORBIDDEN.getStatusCode(),
                            new Headers<Object>()
                    );
                }
            }
        }

        return null;
    }
}