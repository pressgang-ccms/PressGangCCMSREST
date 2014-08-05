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

package org.jboss.pressgang.ccms.server.rest.v1;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.pressgang.ccms.model.MinHashXOR;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.server.utils.EntityManagerWrapper;

/*
 * Note: Nothing is synchronised here as we assume that queries being executed a few times won't matter. This gives us a performance gain
  * since the data is likely to be used across many threads and having the result map synchronised is just going to bottleneck what will
  * primarily be read operations.
 */
@ApplicationScoped
public class CachedEntityLoader {
    private final Map<String, List<? extends AuditedEntity>> results = new ConcurrentHashMap<String, List<? extends AuditedEntity>>();
    @Inject
    private EntityManagerWrapper entityManager;

    public List<MinHashXOR> getXOREntities() {
        if (!results.containsKey(MinHashXOR.SELECT_ALL_QUERY)) {
            return performAndCacheQuery(MinHashXOR.SELECT_ALL_QUERY);
        } else {
            return (List<MinHashXOR>) results.get(MinHashXOR.SELECT_ALL_QUERY);
        }
    }

    public void invalidateXOREntities() {
        results.remove(MinHashXOR.SELECT_ALL_QUERY);
    }

    protected <T extends AuditedEntity> List<T> performAndCacheQuery(final String query) {
        final List<T> results = entityManager.createQuery(query).getResultList();
        this.results.put(query, results);
        return results;
    }
}
