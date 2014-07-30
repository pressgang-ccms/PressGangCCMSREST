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

package org.jboss.pressgang.ccms.server.rest.v1.factory.base;

import org.jboss.pressgang.ccms.rest.v1.elements.base.RESTBaseElementV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;

/**
 * Defines a factory that can create REST entity objects from JPA entities, and update JPA entities from REST entities
 *
 * @param <T> The REST object type
 * @param <U> The database object type
 */
public abstract class RESTElementFactory<T extends RESTBaseElementV1<T>, U> {

    /**
     * Create a REST Entity representation from an Object.
     *
     * @param object   The entity that is to be transformed into a REST Entity.
     * @param baseUrl  The REST url that was used to access this REST entity
     * @param dataType The type of the returned data (XML or JSON)
     * @param expand   The Object that contains details about what fields should be expanded.
     * @return A new REST entity populated with the values in a database entity
     */
    public abstract T createRESTEntityFromObject(final U object, final String baseUrl, final String dataType, final ExpandDataTrunk expand);

    /**
     * Populates the values of an object from a REST entity
     *
     * @param object     The database entity to be synced from the REST Entity.
     * @param dataObject The REST entity object.
     */
    public abstract void updateObjectFromRESTEntity(final U object, final T dataObject);
}
