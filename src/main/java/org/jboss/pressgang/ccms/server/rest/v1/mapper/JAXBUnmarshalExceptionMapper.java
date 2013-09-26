package org.jboss.pressgang.ccms.server.rest.v1.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.plugins.providers.jaxb.JAXBUnmarshalException;

@Provider
public class JAXBUnmarshalExceptionMapper extends BaseExceptionMapper<JAXBUnmarshalException> {

    @Override
    public Response toResponse(final JAXBUnmarshalException exception) {
        return buildPlainTextResponse(Response.Status.BAD_REQUEST, exception);
    }
}