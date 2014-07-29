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

package org.jboss.pressgang.ccms.server.envers;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

/**
 * A Custom Envers Revision Entity class to hold extra details about revision changes. The extra data for this class is: Log
 * Message, Username and Logging Flags.
 *
 * @author lnewson
 */
@Entity
@RevisionEntity(LoggingRevisionListener.class)
@Table(name = "REVINFO")
public class LoggingRevisionEntity implements Serializable {

    private static final long serialVersionUID = -3219457394110160090L;
    public static final byte MINOR_CHANGE_FLAG_BIT = 0x01;
    public static final byte MAJOR_CHANGE_FLAG_BIT = 0x02;

    protected Integer id;
    protected Long timestamp;
    protected String logMessage;
    protected Integer logFlag;
    protected String username;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @RevisionNumber
    @Column(name = "REV")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @RevisionTimestamp
    @Column(name = "REVTSTMP")
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Column(name = "Message", columnDefinition = "TEXT")
    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    @Column(name = "Flag", columnDefinition = "TINYINT")
    public Integer getLogFlag() {
        return logFlag;
    }

    public void setLogFlag(Integer logFlag) {
        this.logFlag = logFlag;
    }

    @Column(name = "UserName", length = 255)
    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoggingRevisionEntity)) return false;

        LoggingRevisionEntity that = (LoggingRevisionEntity) o;

        if (!id.equals(that.id)) return false;
        if (!timestamp.equals(that.timestamp)) return false;
        if (logMessage != null ? !logMessage.equals(that.logMessage) : that.logMessage != null) return false;
        if (logFlag != null ? !logFlag.equals(that.logFlag) : that.logFlag != null) return false;
        if (username != null && !username.equals(that.username)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (logMessage != null ? logMessage.hashCode() : 0);
        result = 31 * result + (logFlag != null ? logFlag.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
