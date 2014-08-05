/*
  Copyright 2011-2014 Red Hat, Inc

  This file is part of PressGang CCMS.

  PressGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PressGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PressGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.server.interceptor;

import org.jboss.pressgang.ccms.model.config.ApplicationConfig;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.regex.Pattern;

/**
 * Prevent access to PUT, POST or DELETE methods when the server is readonly.
 */
@Provider
@ServerInterceptor
public class ReadOnlyInterceptor implements PreProcessInterceptor {

    /**
     * These regexs define endpoints that can be updated even when the server is readonly.
     */
    private static final Pattern[] EXEMPT_ENDPOINTS = new Pattern[] {
        Pattern.compile("^/holdxml$"),
        Pattern.compile("^/settings/.*?$"),
    };

    @Override
    public ServerResponse preProcess(final HttpRequest httpRequest, final ResourceMethod resourceMethod) throws Failure, WebApplicationException {
        if (ApplicationConfig.getInstance().getReadOnly()) {

            final Path path = resourceMethod.getMethod().getAnnotation(Path.class);
            /*
                Although this is a post method, it does not modify the database, so is allowed
             */
            boolean exempt = false;
            for(final Pattern pattern : EXEMPT_ENDPOINTS) {
                if (pattern.matcher(path.value()).matches()) {
                    exempt = true;
                }
            }

            if (!exempt) {
                if (resourceMethod.getHttpMethods().contains(HttpMethod.PUT) ||
                        resourceMethod.getHttpMethods().contains(HttpMethod.POST) ||
                        resourceMethod.getHttpMethods().contains(HttpMethod.DELETE)) {
                    return new ServerResponse(
                            "The server is readonly, and forbids all calls to POST, PUT and DELETE endpoints",
                            Response.Status.FORBIDDEN.getStatusCode(),
                            new Headers<Object>()
                    );
                }
            }
        }

        return null;
    }
}
