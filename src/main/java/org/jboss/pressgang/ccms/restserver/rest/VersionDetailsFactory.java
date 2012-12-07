package org.jboss.pressgang.ccms.restserver.rest;

import javax.ws.rs.Path;

import org.jboss.pressgang.ccms.rest.collections.RESTVersionDetailsCollection;
import org.jboss.pressgang.ccms.rest.entities.RESTVersionDetails;
import org.jboss.pressgang.ccms.rest.v1.jaxrsinterfaces.RESTBaseInterfaceV1;
import org.jboss.pressgang.ccms.utils.common.VersionUtilities;

public class VersionDetailsFactory
{   
    public RESTVersionDetailsCollection create(final String baseUrl)
    {
        final RESTVersionDetailsCollection versions = new RESTVersionDetailsCollection();
        
        versions.addItem(getRESTVersionDetails(baseUrl, RESTBaseInterfaceV1.class, RESTVersionDetails.DEV_STATE));
        
        return versions;
    }
    
    protected RESTVersionDetails getRESTVersionDetails(final String baseUrl, final Class<?> interfaceClazz, final String state)
    {
        final RESTVersionDetails versionDetails = new RESTVersionDetails();
        final String version = VersionUtilities.getAPIVersion(interfaceClazz);
        final Path path = interfaceClazz.getAnnotation(Path.class);

        versionDetails.setVersion(version);
        versionDetails.setState(state);
        versionDetails.setPath(baseUrl + path.value());
        
        return versionDetails;
    }
}
