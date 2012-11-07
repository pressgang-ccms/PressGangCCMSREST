package org.jboss.pressgang.ccms.restserver.utils.topicrenderer;

import javax.persistence.EntityManagerFactory;
import javax.transaction.Status;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseRenderingThread {
    private static final Logger log = LoggerFactory.getLogger(BaseRenderingThread.class);
    /**
     * The maximum amount of time (in seconds) to wait for the entity transaction to finish
     */
    private static final int MAX_WAIT = 5;

    protected EntityManagerFactory entityManagerFactory = null;
    protected TransactionManager transactionManager = null;
    protected Transaction transaction = null;

    protected BaseRenderingThread(final EntityManagerFactory entityManagerFactory, final TransactionManager transactionManager,
            final Transaction transaction) {
        this.entityManagerFactory = entityManagerFactory;
        this.transactionManager = transactionManager;
        this.transaction = transaction;
    }

    /**
     * Wait for the transaction associated with the main entity to finish
     */
    protected boolean waitForTransaction() {
        boolean renderTopic = transaction != null ? false : true;
        try {
            if (transaction != null) {
                for (int i = 0; i < MAX_WAIT; ++i) {
                    final int status = transaction.getStatus();

                    if (status == Status.STATUS_COMMITTED) {
                        renderTopic = true;
                        break;
                    }

                    if (status == Status.STATUS_NO_TRANSACTION || status == Status.STATUS_ROLLEDBACK
                            || status == Status.STATUS_UNKNOWN) {
                        return false;
                    }

                    Thread.sleep(1000);
                }
            }
        } catch (final InterruptedException ex) {
            log.error("Thread was interrupted will waiting for a transaction", ex);
            return false;
        } catch (final Exception ex) {
            log.error("Probably a thread error", ex);
            return false;
        }

        return renderTopic;
    }
}
