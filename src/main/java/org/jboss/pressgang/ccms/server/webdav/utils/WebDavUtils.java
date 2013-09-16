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
