package org.jboss.pressgang.ccms.server.rest.v1.thread;

import javax.persistence.EntityManager;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import java.util.HashMap;
import java.util.Map;

import org.jboss.pressgang.ccms.server.utils.BeanUtilities;
import org.jboss.pressgang.ccms.server.utils.JNDIUtilities;
import org.jboss.weld.context.bound.BoundRequestContext;

public abstract class RESTRunnableWithTransaction implements Runnable {
    public void run() {
        /*
         * Since the envers logging we use relies on being associated with a managed context we need to
         * simulate this in the runnable. This can be achieved by associating the thread with a
         * RequestContext, see: http://docs.jboss.org/weld/reference/latest/en-US/html/contexts.html
         */
        Map<String, Object> dataStore = new HashMap<String, Object>();

        TransactionManager transactionManager = null;
        EntityManager em = null;

        try {
            beginRequest(dataStore);

            transactionManager = JNDIUtilities.lookupJBossTransactionManager();
            em = JNDIUtilities.lookupJBossEntityManagerFactory().createEntityManager();

            // Start a Transaction
            transactionManager.begin();

            // Join the transaction we just started
            em.joinTransaction();

            doWork(em, transactionManager);

            transactionManager.commit();
        } catch (final Exception ex) {
            try {
                if (transactionManager != null) {
                    transactionManager.rollback();
                }
            } catch (final SystemException ex2) {
                // nothing to do here
            }
        } finally {
            if (em != null) {
                em.close();
            }
            endRequest(dataStore);
            dataStore = null;
        }
    }

    public abstract void doWork(EntityManager em, TransactionManager transactionManager);

    private void beginRequest(final Map<String, Object> dataStore) {
        final BoundRequestContext requestContext = getRequestContext();
        requestContext.associate(dataStore);
        requestContext.activate();
    }

    private void endRequest(final Map<String, Object> dataStore) {
        final BoundRequestContext requestContext = getRequestContext();
        try {
            requestContext.invalidate();
            requestContext.deactivate();
        } finally {
            requestContext.dissociate(dataStore);
        }
    }

    private BoundRequestContext getRequestContext() {
        return BeanUtilities.lookupBean(BoundRequestContext.class);
    }
}
