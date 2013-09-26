package org.jboss.pressgang.ccms.server.rest.v1.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.MethodNotAllowedException;

@Provider
public class MethodNotAllowedExceptionMapper extends BaseExceptionMapper<MethodNotAllowedException> {
    @Override
    public Response toResponse(final MethodNotAllowedException exception) {
        return buildPlainTextResponse(405, exception);
    }
}
