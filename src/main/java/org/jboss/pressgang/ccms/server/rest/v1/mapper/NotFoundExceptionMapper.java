package org.jboss.pressgang.ccms.server.rest.v1.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.NotFoundException;

@Provider
public class NotFoundExceptionMapper extends BaseExceptionMapper<NotFoundException> implements ExceptionMapper<NotFoundException> {
    @Override
    public Response toResponse(final NotFoundException exception) {
        return buildPlainTextResponse(Response.Status.NOT_FOUND, exception);
    }
}
