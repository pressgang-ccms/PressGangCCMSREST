package org.jboss.pressgang.ccms.server.rest.v1.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.WriterException;

@Provider
public class WriterExceptionMapper extends BaseExceptionMapper<WriterException> implements ExceptionMapper<WriterException> {
    @Override
    public Response toResponse(final WriterException exception) {
        return buildPlainTextResponse(Response.Status.INTERNAL_SERVER_ERROR, exception);
    }
}
