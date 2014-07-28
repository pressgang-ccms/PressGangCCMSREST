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

package org.jboss.pressgang.ccms.server.rest.v1.interceptor;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

import org.apache.http.HttpStatus;
import org.jboss.pressgang.ccms.server.rest.v1.RESTv1;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.interception.PostProcessInterceptor;

@Provider
@ServerInterceptor
public class RESTv1CreateResponseInterceptor implements PostProcessInterceptor {
    @Override
    public void postProcess(final ServerResponse response) {
        if (RESTv1.class.equals(response.getResourceClass()) && HttpStatus.SC_OK == response.getStatus()) {
            final Path path = response.getResourceMethod().getAnnotation(Path.class);
            if (path.value().matches("^/\\w+?/create/.*")) {
                response.setStatus(HttpStatus.SC_CREATED);
            }
        }
    }
}
