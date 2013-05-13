package org.jboss.pressgang.ccms.restserver.webdav.utils;

import org.jboss.pressgang.ccms.restserver.utils.JNDIUtilities;
import org.jboss.resteasy.spi.InternalServerErrorException;

import javax.annotation.Nullable;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

public class WebDavUtils {

    /**
     * See http://stackoverflow.com/questions/12491773/why-does-request-getremoteaddr-equals127-0-0-1-when-accessing-from-a-remot
     * @param req The http request
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

    private WebDavUtils() {

    }
}
