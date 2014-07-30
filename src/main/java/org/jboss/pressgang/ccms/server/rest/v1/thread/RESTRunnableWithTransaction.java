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

package org.jboss.pressgang.ccms.server.rest.v1.thread;

import javax.persistence.EntityManager;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
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

        UserTransaction transaction = null;
        EntityManager em = null;

        try {
            beginRequest(dataStore);

            transaction = JNDIUtilities.lookupUserTransaction();
            em = JNDIUtilities.lookupJBossEntityManagerFactory().createEntityManager();

            // Start a Transaction
            transaction.begin();

            // Join the transaction we just started
            em.joinTransaction();

            doWork(em, transaction);

            transaction.commit();
        } catch (final Exception ex) {
            try {
                if (transaction != null) {
                    transaction.rollback();
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

    public abstract void doWork(EntityManager em, UserTransaction transaction);

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
