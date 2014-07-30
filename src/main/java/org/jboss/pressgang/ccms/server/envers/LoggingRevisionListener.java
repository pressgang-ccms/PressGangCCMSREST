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
