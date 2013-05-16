package org.jboss.pressgang.ccms.server.envers;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.NamingException;
import java.util.Set;

import org.hibernate.envers.RevisionListener;
import org.jboss.pressgang.ccms.server.ejb.EnversLoggingBean;
import org.jboss.pressgang.ccms.server.utils.JNDIUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Envers Revision Listener that will add content to a Envers Revision Entity when new data is persisted. It will pull the
 * Data from the RequestScoped EnversLoggingBean.
 *
 * @author lnewson
 */
public class LoggingRevisionListener implements RevisionListener {
    private static final Logger log = LoggerFactory.getLogger(LoggingRevisionListener.class);

    /**
     * Add content to a new Envers Revision Entity.
     */
    @Override
    public void newRevision(Object o) {
        final LoggingRevisionEntity revEntity = (LoggingRevisionEntity) o;
        final EnversLoggingBean enversLoggingBean = getEnversLoggingBean();

        if (enversLoggingBean != null) {
            revEntity.setLogFlag(enversLoggingBean.getFlag());
            revEntity.setLogMessage(enversLoggingBean.getLogMessage());
            revEntity.setUserName(enversLoggingBean.getUsername());
        }
    }

    /**
     * Get the EnversLogging bean by looking it up from the Container Bean Manager.
     *
     * @return The EnversLoggingBean entity or null if it couldn't be found.
     */
    @SuppressWarnings("unchecked")
    private EnversLoggingBean getEnversLoggingBean() {
        /*
         * Note: We would normally be able to get this using CDI. However since we can't make this class managed by the
         * Container, we have to manually look it up.
         */
        try {
            final BeanManager beanManager = JNDIUtilities.lookupBeanManager();
            final Set<Bean<?>> loggingBeans = beanManager.getBeans(EnversLoggingBean.class);
            if (loggingBeans != null) {
                final Bean<?> bean = loggingBeans.iterator().next();
                final CreationalContext<EnversLoggingBean> ctx = beanManager.createCreationalContext((Bean<EnversLoggingBean>) bean);
                final EnversLoggingBean enversLoggingBean = (EnversLoggingBean) beanManager.getReference(bean, EnversLoggingBean.class,
                        ctx);
                return enversLoggingBean;
            }
        } catch (NamingException ex) {
            log.error("Error occurred while looking up the Bean Manager", ex);
        }

        return null;
    }
}
