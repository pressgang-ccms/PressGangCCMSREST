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
