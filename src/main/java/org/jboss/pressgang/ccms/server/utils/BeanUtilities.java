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
