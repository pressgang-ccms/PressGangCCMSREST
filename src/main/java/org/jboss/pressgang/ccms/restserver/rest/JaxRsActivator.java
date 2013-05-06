package org.jboss.pressgang.ccms.restserver.rest;

import net.java.dev.webdav.jaxrs.xml.WebDavContextResolver;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.xml.bind.JAXBException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * A class extending {@link javax.ws.rs.core.Application} and annotated with @ApplicationPath is the Java EE 6
 * "no XML" approach to activating JAX-RS.
 * <p/>
 * <p>
 * Resources are served relative to the servlet path specified in the {@link javax.ws.rs.ApplicationPath}
 * annotation.
 * </p>
 */
@ApplicationPath("/rest")
public class JaxRsActivator extends Application {
    private static final Logger LOGGER = Logger.getLogger(JaxRsActivator.class.getName());

    public JaxRsActivator() {
        LOGGER.info("ENTER JaxRsActivator()");
    }

    @Override
    public Set<Object> getSingletons() {
        try {
            return new HashSet<Object>(Arrays.asList(new WebDavContextResolver()));
        } catch (final JAXBException e) {
            LOGGER.severe(e.toString());
            return null;
        }
    }

}
