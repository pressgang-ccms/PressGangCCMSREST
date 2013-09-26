package org.jboss.pressgang.ccms.server.rest.v1.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.NotAcceptableException;

@Provider
public class NotAcceptableExceptionMapper extends BaseExceptionMapper<NotAcceptableException> {
    @Override
    public Response toResponse(final NotAcceptableException exception) {
        return buildPlainTextResponse(Response.Status.NOT_ACCEPTABLE, exception);
    }
}