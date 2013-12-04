package org.jboss.pressgang.ccms.server.envers;

import org.hibernate.envers.RevisionListener;
import org.jboss.pressgang.ccms.server.ejb.EnversLoggingBean;
import org.jboss.pressgang.ccms.server.utils.BeanUtilities;
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
        EnversLoggingBean enversLoggingBean = null;
        try {
            enversLoggingBean = BeanUtilities.lookupBean(EnversLoggingBean.class);
        } catch (Exception e) {
            log.error("Unable to lookup the EnversLoggingBean.", e);
        }

        if (enversLoggingBean != null) {
            revEntity.setLogFlag(enversLoggingBean.getFlag());
            revEntity.setLogMessage(enversLoggingBean.getLogMessage());
            revEntity.setUserName(enversLoggingBean.getUsername());
        }
    }
}
