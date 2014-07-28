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

package org.jboss.pressgang.ccms.server.webdav.utils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.jetbrains.annotations.Nullable;

/**
 * Some util methods relating to the WebDav server.
 */
public final class WebDavUtils {

    /**
     * See http://stackoverflow.com/questions/12491773/why-does-request-getremoteaddr-equals127-0-0-1-when-accessing-from-a-remot
     *
     * @param req                The http request
     * @param xForwaredForHeader The X-Forwarded-For header
     * @return The client IP address
     */
    public static String getRemoteAddress(@NotNull final HttpServletRequest req, @Nullable final String xForwaredForHeader) {
        if (xForwaredForHeader != null) {
            final String[] ipAddresses = xForwaredForHeader.split(",");
            if (ipAddresses.length != 0) {
                return ipAddresses[0];
            }
        }

        return req.getRemoteAddr();
    }

    /**
     * private constructor to prevent instantiation.
     */
    private WebDavUtils() {

    }
}
