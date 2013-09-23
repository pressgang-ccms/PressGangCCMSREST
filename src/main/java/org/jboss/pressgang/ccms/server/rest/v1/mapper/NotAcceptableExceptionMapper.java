package org.jboss.pressgang.ccms.server.rest.v1.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.server.rest.interceptor.VersionHeaderInterceptor;
import org.jboss.pressgang.ccms.utils.common.VersionUtilities;
import org.jboss.resteasy.spi.NotAcceptableException;

@Provider
public class NotAcceptableExceptionMapper  implements ExceptionMapper<NotAcceptableException> {
    @Override
    public Response toResponse(final NotAcceptableException exception) {
        return Response.status(Response.Status.NOT_ACCEPTABLE)
                .entity(exception.getMessage() + "\n")
                .header("Content-Type", MediaType.TEXT_PLAIN)
                .header(RESTv1Constants.X_PRESSGANG_VERSION_HEADER, VersionUtilities.getAPIVersion(VersionHeaderInterceptor.class))
                .build();
    }
}