package org.jboss.pressgang.ccms.server.rest.interceptor;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;

import org.jboss.pressgang.ccms.rest.v1.jaxrsinterfaces.RESTBaseInterfaceV1;
import org.jboss.pressgang.ccms.server.utils.Constants;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.spi.interception.AcceptedByMethod;
import org.jboss.resteasy.spi.interception.MessageBodyWriterContext;
import org.jboss.resteasy.spi.interception.MessageBodyWriterInterceptor;

@Provider
@ServerInterceptor
public class CORSInterceptor implements MessageBodyWriterInterceptor, AcceptedByMethod {

    /**
     * This method is used to allow all remote clients to access the REST interface via CORS.
     */
    @Override
    public void write(final MessageBodyWriterContext context) throws IOException {
        // Allow Cross Origin Requests for JSON requests
        if (context.getMediaType().equals(MediaType.APPLICATION_JSON_TYPE)) {
            context.getHeaders().add(RESTBaseInterfaceV1.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, Constants.CORS_ALLOW_ORIGIN_HEADER);
        }
        context.proceed();
    }

    @Override
    public boolean accept(final Class declaring, final Method method) {
        // If the CORS Header Property hasn't been set then ignore all methods
        if (Constants.CORS_ALLOW_ORIGIN_HEADER == null) {
            return false;
        }

        // Make sure the request is a REST endpoint
        if (declaring.isAnnotationPresent(Path.class)) {
            final Path path = (Path) declaring.getAnnotation(Path.class);
            if (path.value().startsWith(Constants.BASE_REST_PATH)) {
                // Make sure the request returns a JSON request
                if (method.isAnnotationPresent(Produces.class)) {
                    final Produces produces = method.getAnnotation(Produces.class);
                    for (final String value : produces.value()) {
                        if (value.contains("json")) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
