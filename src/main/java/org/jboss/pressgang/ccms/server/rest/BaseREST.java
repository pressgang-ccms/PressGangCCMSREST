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

package org.jboss.pressgang.ccms.server.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.jboss.pressgang.ccms.server.constants.Constants;

public abstract class BaseREST {
    /**
     * The Uri Information for the REST Request
     */
    @Context
    protected UriInfo uriInfo;

    /**
     * Get the URL of the root REST endpoint using UriInfo from a request.
     *
     * @return The URL of the root REST endpoint.
     */
    protected String getBaseUrl() {
        final String fullPath = uriInfo.getAbsolutePath().toString();
        final int index = fullPath.indexOf(Constants.BASE_REST_PATH);
        if (index != -1) return fullPath.substring(0, index + Constants.BASE_REST_PATH.length());

        return null;
    }

    /**
     * Get the URL of the REST endpoint from the calling request.
     *
     * @return The URL of the endpoint that was called for the request.
     */
    protected String getUrl() {
        return uriInfo.getAbsolutePath().toString();
    }

    /**
     * Get the URL of the REST endpoint from the calling request.
     *
     * @return The URL of the endpoint that was called for the request.
     */
    protected String getRequestUrl() {
        return uriInfo.getRequestUri().toString();
    }
}
