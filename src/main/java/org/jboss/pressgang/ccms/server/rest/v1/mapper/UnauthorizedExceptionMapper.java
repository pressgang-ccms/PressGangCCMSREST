package org.jboss.pressgang.ccms.server.rest.v1.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.UnauthorizedException;

@Provider
public class UnauthorizedExceptionMapper extends BaseExceptionMapper<UnauthorizedException> implements ExceptionMapper<UnauthorizedException> {
    @Override
    public Response toResponse(final UnauthorizedException exception) {
        return buildPlainTextResponse(Response.Status.UNAUTHORIZED, exception);
    }
}
