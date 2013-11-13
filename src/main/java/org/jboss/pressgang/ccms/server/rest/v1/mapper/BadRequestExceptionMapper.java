package org.jboss.pressgang.ccms.server.rest.v1.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.BadRequestException;

@Provider
public class BadRequestExceptionMapper extends BaseExceptionMapper<BadRequestException> implements ExceptionMapper<BadRequestException> {
    @Override
    public Response toResponse(final BadRequestException exception) {
        return buildPlainTextResponse(Response.Status.BAD_REQUEST, exception);
    }
}
