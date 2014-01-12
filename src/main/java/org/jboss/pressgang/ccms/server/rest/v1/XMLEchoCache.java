package org.jboss.pressgang.ccms.server.rest.v1;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@ApplicationScoped
public class XMLEchoCache {
    private static final long XML_ECHO_CACHE_LIFESPAN = 10;
    private final AtomicInteger counter = new AtomicInteger(0);

    private final Cache<Integer, String> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(XML_ECHO_CACHE_LIFESPAN, TimeUnit.SECONDS)
            .build();

    public String getXML(Integer id) {
        return cache.getIfPresent(id);
    }

    public Integer addXML(String xml) {
        final Integer id = getNextId();
        cache.put(id, xml);
        return id;
    }

    public void updateXML(Integer id, String xml) {
        cache.put(id, xml);
    }

    protected synchronized Integer getNextId() {
        Integer count = counter.get();
        if (count == Integer.MAX_VALUE) {
            counter.set(0);
            return 0;
        } else {
            return counter.getAndAdd(1);
        }
    }
}
