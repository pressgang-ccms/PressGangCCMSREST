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
