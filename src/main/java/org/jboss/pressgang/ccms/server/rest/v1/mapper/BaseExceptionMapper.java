/*
  Copyright 2011-2014 Red Hat

  This file is part of PresGang CCMS.

  PresGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PresGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PresGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.server.rest.v1.mapper;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.jaxrsinterfaces.RESTInterfaceV1;
import org.jboss.pressgang.ccms.server.rest.v1.RESTv1;
import org.jboss.pressgang.ccms.utils.common.VersionUtilities;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.interception.AcceptedByMethod;

public abstract class BaseExceptionMapper<T extends Throwable> implements ExceptionMapper<T>, AcceptedByMethod {
    @Context HttpServletResponse servletResponse;

    protected Response buildPlainTextResponse(final Response.Status status, final T exception) {
        return buildPlainTextResponse(status.getStatusCode(), exception);
    }

    protected Response buildPlainTextResponse(final int status, final T exception) {
        // Copy any headers over from the original response as resteasy creates a new response for errors.
        final Headers<Object> headers = new Headers<Object>();
        for (final String headerName : servletResponse.getHeaderNames()) {
            headers.put(headerName, new ArrayList<Object>(servletResponse.getHeaders(headerName)));
        }

        // Add the additional required headers
        headers.putSingle("Content-Type", MediaType.TEXT_PLAIN);
        headers.putSingle(RESTv1Constants.X_PRESSGANG_VERSION_HEADER, VersionUtilities.getAPIVersion(RESTInterfaceV1.class));

        // Create the response
        return new ServerResponse(exception.getMessage(), status, headers);
    }

    @Override
    public boolean accept(Class declaring, Method method) {
        // Only use this interceptor for v1 endpoints.
        return RESTv1.class.equals(declaring);
    }
}
