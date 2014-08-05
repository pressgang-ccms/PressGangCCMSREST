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

package org.jboss.pressgang.ccms.server.webdav.resources;

import org.jetbrains.annotations.Nullable;

/**
 * A wrapper that contains the HTTP return code, and the returned byte array.
 */
public final class ByteArrayReturnValue {
    private final int statusCode;
    @Nullable
    private final byte[] value;

    public ByteArrayReturnValue(final int statusCode, @Nullable final byte[] value) {
        this.statusCode = statusCode;
        this.value = value;
    }

    public ByteArrayReturnValue(final int statusCode) {
        this.statusCode = statusCode;
        this.value = null;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Nullable
    public byte[] getValue() {
        return value;
    }
}
