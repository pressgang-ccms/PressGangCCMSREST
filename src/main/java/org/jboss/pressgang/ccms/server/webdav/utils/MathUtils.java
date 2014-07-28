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

/**
 * Some useful maths utility methods.
 */
public final class MathUtils {
    /**
     * @param number The number to check.
     * @return The scale of the number (i.e. the number of zeros)
     */
    public static int getScale(final int number) {
        int maxScale = Math.abs(number);

        /* find out how large is the largest (or smallest) topic id, logarithmicly speaking */
        int zeros = 0;
        maxScale = maxScale / 10;
        while (maxScale > 0) {
            maxScale = maxScale / 10;
            ++zeros;
        }

        return zeros;
    }

    /**
     * private constructor to prevent instantiation.
     */
    private MathUtils() {

    }
}
