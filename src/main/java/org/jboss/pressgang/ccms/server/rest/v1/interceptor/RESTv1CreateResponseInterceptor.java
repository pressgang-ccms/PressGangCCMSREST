package org.jboss.pressgang.ccms.server.rest.v1.interceptor;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

import org.apache.http.HttpStatus;
import org.jboss.pressgang.ccms.server.rest.v1.RESTv1;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.interception.PostProcessInterceptor;

@Provider
@ServerInterceptor
public class RESTv1CreateResponseInterceptor implements PostProcessInterceptor {
    @Override
    public void postProcess(final ServerResponse response) {
        if (RESTv1.class.equals(response.getResourceClass()) && HttpStatus.SC_OK == response.getStatus()) {
            final Path path = response.getResourceMethod().getAnnotation(Path.class);
            if (path.value().matches("^/\\w+?/create/.*")) {
                response.setStatus(HttpStatus.SC_CREATED);
            }
        }
    }
}
