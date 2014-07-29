/*
  Copyright 2011-2014 Red Hat

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

package org.jboss.pressgang.ccms.server.webdav.managers;

import java.util.Calendar;

/**
 * Represents cached data used to send the saved text back to a client, even if the database text was reformatted.
 */
public class DataCache {
    private final byte[] original;
    private final byte[] database;
    private final Calendar time;

    public DataCache(final byte[] original, final byte[] database, final Calendar time) {
        this.original = original;
        this.database = database;
        this.time = time;
    }

    public byte[] getOriginal() {
        return original;
    }

    public byte[] getDatabase() {
        return database;
    }

    public Calendar getTime() {
        return time;
    }
}
