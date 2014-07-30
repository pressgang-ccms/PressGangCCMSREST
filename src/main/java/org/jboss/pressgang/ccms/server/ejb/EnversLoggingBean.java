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

package org.jboss.pressgang.ccms.server.ejb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;

import org.jboss.pressgang.ccms.server.envers.LoggingRevisionEntity;

/**
 * This java bean provides a mechanism to provide information to a EnversListener
 *
 * @author lnewson
 */
@RequestScoped
@Named("enversLoggingBean")
public class EnversLoggingBean implements Serializable {
    private static final long serialVersionUID = 7455302626872967710L;

    private String logMessage = null;
    private boolean minorChangeFlag = true;
    private boolean majorChangeFlag = false;
    private String username = null;

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(final String logMessage) {
        this.logMessage = logMessage;
    }

    public void setLogMessage(final String message, final int flag) {
        logMessage = message;
        setFlag(flag);
    }

    public void addLogMessage(final String message) {
        if (logMessage == null || logMessage.isEmpty()) {
            logMessage = message;
        } else {
            logMessage = logMessage.trim() + (logMessage.matches("(!|\\.|\\?)$") ? "\n" : ".\n") + message;
        }
    }

    public Integer getFlag() {
        int flag = 0;
        if (majorChangeFlag || minorChangeFlag) {
            if (minorChangeFlag) {
                flag |= LoggingRevisionEntity.MINOR_CHANGE_FLAG_BIT;
            }
            if (majorChangeFlag) {
                flag |= LoggingRevisionEntity.MAJOR_CHANGE_FLAG_BIT;
            }
            return flag;
        }

        return flag;
    }

    public void setFlag(final int flag) {
        minorChangeFlag = (LoggingRevisionEntity.MINOR_CHANGE_FLAG_BIT & flag) == LoggingRevisionEntity.MINOR_CHANGE_FLAG_BIT;
        majorChangeFlag = (LoggingRevisionEntity.MAJOR_CHANGE_FLAG_BIT & flag) == LoggingRevisionEntity.MAJOR_CHANGE_FLAG_BIT;
    }

    public boolean getMinorChangeFlag() {
        return minorChangeFlag;
    }

    public void setMinorChangeFlag(final boolean minorChangeFlag) {
        this.minorChangeFlag = minorChangeFlag;
    }

    public boolean getMajorChangeFlag() {
        return majorChangeFlag;
    }

    public void setMajorChangeFlag(final boolean majorChangeFlag) {
        this.majorChangeFlag = majorChangeFlag;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
