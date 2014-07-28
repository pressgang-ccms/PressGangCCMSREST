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

package org.jboss.pressgang.ccms.server.rest.v1;

import javax.enterprise.context.RequestScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.utils.structures.Pair;

@RequestScoped
public class EntityCache {
    private static final Integer NEW_STATE = 0;
    private static final Integer UPDATED_STATE = 1;
    private final AtomicInteger counter = new AtomicInteger(-10000);

    private Map<RESTBaseEntityV1, Pair<Object, Integer>> entities = new HashMap<RESTBaseEntityV1, Pair<Object, Integer>>();

    public <U> void addNew(RESTBaseEntityV1 restEntity, U dbEntity) {
        if (restEntity.getId() == null) {
            restEntity.setId(getNextId());
        }

        entities.put(restEntity, new Pair<Object, Integer>(dbEntity, NEW_STATE));
    }

    public void addUpdated(RESTBaseEntityV1 restEntity, Object dbEntity) {
        entities.put(restEntity, new Pair<Object, Integer>(dbEntity, UPDATED_STATE));
    }

    public void remove(RESTBaseEntityV1 restEntity) {
        entities.remove(restEntity);
    }

    public <T> boolean containsRESTEntity(final RESTBaseEntityV1 restEntity) {
        return entities.containsKey(restEntity);
    }

    public Object get(final RESTBaseEntityV1 restEntity) {
        final Pair<Object, Integer> entity = entities.get(restEntity);
        return entity == null ? null : entity.getFirst();
    }

    public <U> List<U> getNewEntities(final Class<U> clazz) {
        return getEntitiesWithState(clazz, NEW_STATE);
    }

    public <U> List<U> getUpdatedEntities(final Class<U> clazz) {
        return getEntitiesWithState(clazz, UPDATED_STATE);
    }

    public <U> List<U> getEntities(final Class<U> clazz) {
        final List<U> entities = new ArrayList<U>();
        for (final Map.Entry<RESTBaseEntityV1, Pair<Object, Integer>> entry : this.entities.entrySet()) {
            if (entry.getValue().getFirst().getClass().equals(clazz)) {
                entities.add((U) entry.getValue().getFirst());
            }
        }

        return entities;
    }

    public List<Object> getAllEntities() {
        final List<Object> entities = new ArrayList<Object>();
        for (final Map.Entry<RESTBaseEntityV1, Pair<Object, Integer>> entry : this.entities.entrySet()) {
            entities.add(entry.getValue().getFirst());
        }

        return entities;
    }

    protected <U> List<U> getEntitiesWithState(final Class<U> clazz, final Integer state) {
        final List<U> entities = new ArrayList<U>();
        for (final Map.Entry<RESTBaseEntityV1, Pair<Object, Integer>> entry : this.entities.entrySet()) {
            if (entry.getValue().getSecond().equals(state) && entry.getValue().getFirst().getClass().equals(clazz)) {
                entities.add((U) entry.getValue().getFirst());
            }
        }

        return entities;
    }

    public int size() {
        return entities.size();
    }

    protected synchronized Integer getNextId() {
        Integer count = counter.get();
        if (count == Integer.MIN_VALUE) {
            counter.set(-10000);
            return -10000;
        } else {
            return counter.getAndDecrement();
        }
    }
}
