package org.jboss.pressgang.ccms.server.async.process.task;

import java.io.StringWriter;

import org.apache.log4j.Level;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.jppf.server.protocol.JPPFTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ProcessTask extends JPPFTask {
    private Logger log;
    private String logs = null;

    @Override
    public final void run() {
        // Create the logger
        final String name = "Task";
        log = LoggerFactory.getLogger(name);

        // Add the appender to capture the output
        final WriterAppender appender = new WriterAppender();
        final StringWriter writer = new StringWriter();
        appender.setWriter(writer);
        appender.setLayout(new SimpleLayout());
        org.apache.log4j.Logger.getRootLogger().addAppender(appender);
        org.apache.log4j.Logger log4jLogger = org.apache.log4j.Logger.getLogger(name);
        log4jLogger.setLevel(Level.INFO);

        try {
            // Execute the task
            execute();
        } finally {
            // Remove the appender from the log and get the logs
            org.apache.log4j.Logger.getRootLogger().removeAppender(appender);
            logs = writer.toString();
        }
    }

    public abstract void execute();

    public String getLogs() {
        return logs;
    }

    protected Logger getLog() {
        return log;
    }
}
