/*
  Copyright 2011-2014 Red Hat

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

package org.jboss.pressgang.ccms.server.rest.v1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.server.rest.DatabaseOperation;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;

public class RESTChangeAction<T extends RESTBaseEntityV1<T, ?, ?>> {
    private final RESTChangeAction<?> parent;
    private final RESTEntityFactory factory;
    private final T restEntity;
    private AuditedEntity dbEntity;
    private final DatabaseOperation type;
    private List<RESTChangeAction<?>> deleteChildren = new ArrayList<RESTChangeAction<?>>();
    private List<RESTChangeAction<?>> createChildren = new ArrayList<RESTChangeAction<?>>();
    private List<RESTChangeAction<?>> updateChildren = new ArrayList<RESTChangeAction<?>>();
    private String uniqueId;

    public RESTChangeAction(final RESTEntityFactory<T, ?, ?, ?> factory, final T restEntity, final DatabaseOperation type) {
        this(null, factory, restEntity, type);
    }

    public RESTChangeAction(final RESTChangeAction<?> parent, final RESTEntityFactory<T, ?, ?, ?> factory, final T restEntity,
            final DatabaseOperation type) {
        this.parent = parent;
        this.factory = factory;
        this.restEntity = restEntity;
        this.type = type;
    }

    public RESTChangeAction<?> getParent() {
        return parent;
    }

    public void addChildAction(final RESTChangeAction<?> actionNode) {
        switch (actionNode.getType()) {
            case DELETE:
                deleteChildren.add(actionNode);
                break;
            case CREATE:
                createChildren.add(actionNode);
                break;
            case UPDATE:
                updateChildren.add(actionNode);
                break;
        }
    }

    public List<RESTChangeAction<?>> getDeleteChildActions() {
        return Collections.unmodifiableList(deleteChildren);
    }

    public List<RESTChangeAction<?>> getCreateChildActions() {
        return Collections.unmodifiableList(createChildren);
    }

    public List<RESTChangeAction<?>> getUpdateChildActions() {
        return Collections.unmodifiableList(updateChildren);
    }

    public RESTEntityFactory getFactory() {
        return factory;
    }

    public T getRESTEntity() {
        return restEntity;
    }

    public DatabaseOperation getType() {
        return type;
    }

    public AuditedEntity getDBEntity() {
        return dbEntity;
    }

    public void setDBEntity(AuditedEntity dbEntity) {
        this.dbEntity = dbEntity;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
