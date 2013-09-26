package org.jboss.pressgang.ccms.server.rest.v1.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.InternalServerErrorException;

@Provider
public class InternalServerErrorExceptionMapper extends BaseExceptionMapper<InternalServerErrorException> {
    @Override
    public Response toResponse(final InternalServerErrorException exception) {
        return buildPlainTextResponse(Response.Status.INTERNAL_SERVER_ERROR, exception);
    }
}
