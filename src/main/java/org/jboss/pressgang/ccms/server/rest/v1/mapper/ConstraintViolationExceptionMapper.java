package org.jboss.pressgang.ccms.server.rest.v1.mapper;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.InternalServerErrorException;

@Provider
public class ConstraintViolationExceptionMapper extends BaseExceptionMapper<ConstraintViolationException> implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(final ConstraintViolationException exception) {
        return buildPlainTextResponse(Response.Status.BAD_REQUEST, exception);
    }
}