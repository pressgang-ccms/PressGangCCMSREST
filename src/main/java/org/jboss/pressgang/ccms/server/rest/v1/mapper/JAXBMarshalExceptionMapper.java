package org.jboss.pressgang.ccms.server.rest.v1.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.plugins.providers.jaxb.JAXBMarshalException;

@Provider
public class JAXBMarshalExceptionMapper  extends BaseExceptionMapper<JAXBMarshalException> implements ExceptionMapper<JAXBMarshalException> {

    @Override
    public Response toResponse(final JAXBMarshalException exception) {
        return buildPlainTextResponse(Response.Status.INTERNAL_SERVER_ERROR, exception);
    }
}