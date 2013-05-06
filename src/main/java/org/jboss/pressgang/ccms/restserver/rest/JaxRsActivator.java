package org.jboss.pressgang.ccms.restserver.rest;

import net.java.dev.webdav.jaxrs.xml.WebDavContextResolver;
import org.jboss.pressgang.ccms.restserver.webdav.WebDavRoot;
import org.jboss.pressgang.ccms.restserver.webdav.topics.TopicVirtualFolder;
import org.jboss.pressgang.ccms.restserver.webdav.topics.topic.WebDavTopic;
import org.jboss.pressgang.ccms.restserver.webdav.topics.topic.fields.WebDavTopicContent;

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

    private final Set<Class<?>> serviceClasses = new HashSet<Class<?>>();

    public JaxRsActivator() {
        LOGGER.info("ENTER JaxRsActivator()");
        serviceClasses.add(WebDavRoot.class);
        serviceClasses.add(TopicVirtualFolder.class);
        serviceClasses.add(WebDavTopic.class);
        serviceClasses.add(WebDavTopicContent.class);

        //ResteasyProviderFactory.getInstance().registerProvider(WebDavContextResolver.class);
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

    @Override
    public Set<Class<?>> getClasses() {
        return serviceClasses;
    }

}
