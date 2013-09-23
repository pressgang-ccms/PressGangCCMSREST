package org.jboss.pressgang.ccms.server.rest.v1.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.server.rest.interceptor.VersionHeaderInterceptor;
import org.jboss.pressgang.ccms.utils.common.VersionUtilities;
import org.jboss.resteasy.plugins.providers.jaxb.JAXBUnmarshalException;

@Provider
public class JAXBUnmarshalExceptionMapper  implements ExceptionMapper<JAXBUnmarshalException> {

    @Override
    public Response toResponse(final JAXBUnmarshalException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage() + "\n")
                .header("Content-Type", MediaType.TEXT_PLAIN)
                .header(RESTv1Constants.X_PRESSGANG_VERSION_HEADER, VersionUtilities.getAPIVersion(VersionHeaderInterceptor.class))
                .build();
    }
}