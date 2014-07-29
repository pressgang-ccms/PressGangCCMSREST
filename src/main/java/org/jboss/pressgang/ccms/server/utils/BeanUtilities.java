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

package org.jboss.pressgang.ccms.server.utils;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import java.util.Iterator;
import java.util.Set;

import org.apache.deltaspike.core.api.provider.BeanManagerProvider;

public class BeanUtilities {
    public static <T> T lookupBean(final Class<T> clazz) {
        final BeanManager beanManager = BeanManagerProvider.getInstance().getBeanManager();
        final Set<Bean<?>> beans = beanManager.getBeans(clazz);
        final Iterator<Bean<?>> iter = beans.iterator();
        if (!iter.hasNext()) {
            throw new IllegalStateException("BeanManager cannot find an instance of requested type " + clazz.getName());
        }
        final Bean<?> bean = iter.next();
        final CreationalContext<T> ctx = beanManager.createCreationalContext(
                (Bean<T>) bean);
        return (T) beanManager.getReference(bean, clazz, ctx);
    }
}
