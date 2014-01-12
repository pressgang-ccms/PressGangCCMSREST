package org.jboss.pressgang.ccms.server.rest.v1.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class IllegalStateExceptionMapper extends BaseExceptionMapper<IllegalStateException> implements ExceptionMapper<IllegalStateException> {
    @Override
    public Response toResponse(final IllegalStateException exception) {
        return buildPlainTextResponse(Response.Status.INTERNAL_SERVER_ERROR, exception);
    }
}
