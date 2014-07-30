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

package org.jboss.pressgang.ccms.server.webdav.constants;

/**
 * Constants related to the WebDAV server.
 */
public final class WebDavConstants {
    /** The location of temporary files uploaded to the web server */
    public static final String TEMP_LOCATION = "/tmp/pressgang-webdav-temp";
    /** The length of time in seconds that a fixed resource will appear to be deleted */
    public static final int DELETE_WINDOW = 10 * 60;
    /** The length of time in seconds that a temp resource will be availble for */
    public static final int TEMP_WINDOW = 10 * 60;
    /** The header that we need to check to get the client ip */
    public static final String X_FORWARD_FOR_HEADER = "X-Forwarded-For";

    /**
     * private constructor to prevent instantiation.
     */
    private WebDavConstants() {

    }
}
