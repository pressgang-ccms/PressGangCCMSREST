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
