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

package org.jboss.pressgang.ccms.server.utils;

import static com.google.common.base.Strings.isNullOrEmpty;

import javax.persistence.EntityManager;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.jboss.pressgang.ccms.model.User;
import org.jboss.pressgang.ccms.provider.DBProviderFactory;
import org.jboss.pressgang.ccms.provider.listener.LogMessageListener;
import org.jboss.pressgang.ccms.server.ejb.EnversLoggingBean;
import org.jboss.pressgang.ccms.wrapper.LogMessageWrapper;

public class ProviderUtilities {

    public static DBProviderFactory getDBProviderFactory(final EntityManager entityManager, final UserTransaction transactionManager) {
        return getDBProviderFactory(entityManager, transactionManager, null);
    }

    public static DBProviderFactory getDBProviderFactory(final EntityManager entityManager, final UserTransaction transactionManager,
            final EnversLoggingBean loggingBean) {
        final DBProviderFactory providerFactory = DBProviderFactory.create(entityManager, transactionManager);
        if (loggingBean != null) {
            final DBLogMessageListener listener = new DBLogMessageListener(entityManager, loggingBean);
            providerFactory.registerListener(listener);
        }
        return providerFactory;
    }

    public static DBProviderFactory getDBProviderFactory(final EntityManager entityManager) {
        return getDBProviderFactory(entityManager, (EnversLoggingBean) null);
    }

    public static DBProviderFactory getDBProviderFactory(final EntityManager entityManager, final EnversLoggingBean loggingBean) {
        final DBProviderFactory providerFactory = DBProviderFactory.create(entityManager);
        if (loggingBean != null) {
            final DBLogMessageListener listener = new DBLogMessageListener(entityManager, loggingBean);
            providerFactory.registerListener(listener);
        }
        return providerFactory;
    }
}

class DBLogMessageListener implements LogMessageListener {
    private final EntityManager entityManager;
    private final EnversLoggingBean loggingBean;

    public DBLogMessageListener(final EntityManager entityManager, final EnversLoggingBean loggingBean) {
        this.entityManager = entityManager;
        this.loggingBean = loggingBean;
    }

    @Override
    public void handleLogMessage(LogMessageWrapper logMessage) {
        if (!isNullOrEmpty(logMessage.getMessage())) {
            loggingBean.addLogMessage(logMessage.getMessage());
        }
        if (logMessage.getFlags() != null) {
            loggingBean.setFlag(logMessage.getFlags());
        }
        if (!isNullOrEmpty(logMessage.getUser())) {
            if (logMessage.getUser().matches("^\\d+$")) {
                final User user = entityManager.find(User.class, Integer.parseInt(logMessage.getUser()));
                loggingBean.setUsername(user.getUserName());
            } else {
                loggingBean.setUsername(logMessage.getUser());
            }
        }
    }
}
