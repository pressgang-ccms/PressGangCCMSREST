package org.jboss.pressgang.ccms.server.rest.v1.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.ReaderException;

@Provider
public class ReaderExceptionMapper extends BaseExceptionMapper<ReaderException> implements ExceptionMapper<ReaderException> {
    @Override
    public Response toResponse(final ReaderException exception) {
        return buildPlainTextResponse(Response.Status.BAD_REQUEST, exception);
    }
}
