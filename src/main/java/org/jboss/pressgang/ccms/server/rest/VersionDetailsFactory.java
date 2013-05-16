package org.jboss.pressgang.ccms.server.rest;

import javax.ws.rs.Path;

import org.jboss.pressgang.ccms.rest.collections.RESTVersionDetailsCollection;
import org.jboss.pressgang.ccms.rest.entities.RESTVersionDetails;
import org.jboss.pressgang.ccms.rest.v1.jaxrsinterfaces.RESTBaseInterfaceV1;
import org.jboss.pressgang.ccms.utils.common.VersionUtilities;

public class VersionDetailsFactory {
    public RESTVersionDetailsCollection create(final String baseUrl) {
        final RESTVersionDetailsCollection versions = new RESTVersionDetailsCollection();

        versions.addItem(getRESTVersionDetails(baseUrl, RESTBaseInterfaceV1.class));

        return versions;
    }

    protected RESTVersionDetails getRESTVersionDetails(final String baseUrl, final Class<?> interfaceClazz) {
        final RESTVersionDetails versionDetails = new RESTVersionDetails();
        final String version = VersionUtilities.getAPIVersion(interfaceClazz);
        final String build = VersionUtilities.getAPIBuildTimestamp(interfaceClazz);
        final Path path = interfaceClazz.getAnnotation(Path.class);

        versionDetails.setVersion(version);
        versionDetails.setBuild(build);
        versionDetails.setPath(baseUrl + path.value());

        if (version.contains("SNAPSHOT")) {
            versionDetails.setState(RESTVersionDetails.DEV_STATE);
        } else {
            versionDetails.setState(RESTVersionDetails.STABLE_STATE);
        }

        return versionDetails;
    }
}
