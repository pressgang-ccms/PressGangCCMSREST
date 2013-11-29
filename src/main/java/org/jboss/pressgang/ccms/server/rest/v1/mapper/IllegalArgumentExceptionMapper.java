package org.jboss.pressgang.ccms.server.rest.v1.mapper;

import org.jboss.resteasy.spi.BadRequestException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Wraps the IllegalArgumentException exception.
 */
@Provider
public class IllegalArgumentExceptionMapper extends BaseExceptionMapper<IllegalArgumentException> implements ExceptionMapper<IllegalArgumentException> {
    @Override
    public Response toResponse(final IllegalArgumentException exception) {
        return buildPlainTextResponse(Response.Status.BAD_REQUEST, exception);
    }
}