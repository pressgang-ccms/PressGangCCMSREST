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

package org.jboss.pressgang.ccms.server.rest;

import javax.ws.rs.Path;

import org.jboss.pressgang.ccms.rest.collections.RESTVersionDetailsCollection;
import org.jboss.pressgang.ccms.rest.jaxrsinterfaces.RESTInterface;
import org.jboss.pressgang.ccms.server.constants.Constants;

@Path(Constants.BASE_REST_PATH)
public class REST extends BaseREST implements RESTInterface {

    @Override
    public RESTVersionDetailsCollection setVersionDetails() {
        return new VersionDetailsFactory().create(getBaseUrl());
    }
}
