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

package org.jboss.pressgang.ccms.server.rest;

import javax.ws.rs.Path;

import org.jboss.pressgang.ccms.rest.collections.RESTVersionDetailsCollection;
import org.jboss.pressgang.ccms.rest.entities.RESTVersionDetails;
import org.jboss.pressgang.ccms.rest.v1.jaxrsinterfaces.RESTBaseInterfaceV1;
import org.jboss.pressgang.ccms.server.constants.Constants;
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
        versionDetails.setPath(baseUrl + path.value().replace(Constants.BASE_REST_PATH, ""));

        if (version.contains("SNAPSHOT")) {
            versionDetails.setState(RESTVersionDetails.DEV_STATE);
        } else {
            versionDetails.setState(RESTVersionDetails.STABLE_STATE);
        }

        return versionDetails;
    }
}
