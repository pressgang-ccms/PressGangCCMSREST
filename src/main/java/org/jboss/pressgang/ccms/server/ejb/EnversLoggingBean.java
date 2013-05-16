package org.jboss.pressgang.ccms.server.ejb;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.jboss.pressgang.ccms.server.envers.LoggingRevisionEntity;

/**
 * This java bean provides a mechanism to provide information to a 
 * 
 * @author lnewson
 *
 */
@RequestScoped
@Named("enversLoggingBean")
public class EnversLoggingBean implements Serializable {

    private static final long serialVersionUID = 7455302626872967710L;
    
    private String logMessage = null;
    private boolean minorChangeFlag = true;
    private boolean majorChangeFlag = false;
    private String username = null;

    public String getLogMessage()
    {
        return logMessage;
    }
    
    public void setLogMessage(final String message)
    {
        this.logMessage = message;
    }
    
    public void setLogMessage(final String message, final int flag)
    {
        logMessage = message;
        setFlag(flag);
    }
    
    public void addLogMessage(final String message)
    {
        if (this.logMessage == null || logMessage.isEmpty())
        {
            logMessage = message;
        }
        else
        {
            logMessage = logMessage.trim() + (logMessage.matches("(!|\\.|\\?)$") ? "\n" : ".\n") + message;
        }
    }
    
    public Integer getFlag()
    {
        int flag = 0;
        if (majorChangeFlag || minorChangeFlag)
        {
            if (minorChangeFlag)
            {
                flag |= LoggingRevisionEntity.MINOR_CHANGE_FLAG_BIT;
            }
            if (majorChangeFlag)
            {
                flag |= LoggingRevisionEntity.MAJOR_CHANGE_FLAG_BIT;
            }
            return flag;
        }
        
        return flag;
    }
    
    public void setFlag(final int flag)
    {
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
