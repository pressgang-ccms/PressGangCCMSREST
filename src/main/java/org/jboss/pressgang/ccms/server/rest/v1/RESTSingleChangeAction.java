/*
 * Copyright 2011-2014 Red Hat, Inc.
 *
 * This file is part of PressGang CCMS.
 *
 * PressGang CCMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PressGang CCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with PressGang CCMS. If not, see <http://www.gnu.org/licenses/>.
 */

package org.jboss.pressgang.ccms.server.rest.v1;

import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.server.rest.DatabaseOperation;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;

public class RESTSingleChangeAction<T extends RESTBaseEntityV1<T>> extends RESTChangeAction<T> {

    public RESTSingleChangeAction(final RESTEntityFactory<T, ?, ?, ?> factory, final T restEntity) {
        this(null, factory, restEntity);
    }

    public RESTSingleChangeAction(final RESTChangeAction<?> parent, final RESTEntityFactory<T, ?, ?, ?> factory, final T restEntity) {
        super(parent, factory, restEntity, null);
    }

    @Override
    public DatabaseOperation getType() {
        if (getRESTEntity() == null) {
            return DatabaseOperation.DELETE;
        } else if (getRESTEntity().getId() == null) {
            return DatabaseOperation.CREATE;
        } else {
            return DatabaseOperation.UPDATE;
        }
    }
}
