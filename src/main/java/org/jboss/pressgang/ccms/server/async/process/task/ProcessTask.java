package org.jboss.pressgang.ccms.server.async.process.task;

import java.io.StringWriter;

import org.apache.log4j.Level;
import org.apache.log4j.NDC;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.jppf.node.protocol.AbstractTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A task that provides the ability to capture logs using a {@link WriterAppender} and {@link NDC} to only capture logs from its
 * executing thread..
 *
 * @param <T>
 */
public abstract class ProcessTask<T> extends AbstractTask<T> {
    private transient Logger log;
    private String logs = "";
    private boolean successful = true;

    @Override
    public final void run() {
        // Create the logger
        final String name = getClass().getPackage().getName() + "-" + getId();
        log = LoggerFactory.getLogger(name);

        // Add the appender to capture the output
        final StringWriter writer = new StringWriter();
        final WriterAppender appender = createAppender(writer);

        org.apache.log4j.Logger.getLogger("org.jboss.pressgang.ccms").addAppender(appender);
        org.apache.log4j.Logger log4jLogger = org.apache.log4j.Logger.getLogger(name);
        log4jLogger.setLevel(Level.INFO);

        try {
            // Execute the task
            NDC.push(getId());
            execute();
        } catch (Throwable e) {
            setThrowable(e);
            log.error("Unexpected error", e);
            setSuccessful(false);
        } finally {
            NDC.pop();
            // Remove the appender from the log and get the logs
            org.apache.log4j.Logger.getLogger("org.jboss.pressgang.ccms").removeAppender(appender);
            logs = writer.toString();
        }
        NDC.remove();
    }

    public abstract void execute();

    public String getLogs() {
        return logs;
    }

    protected Logger getLogger() {
        return log;
    }

    public boolean wasSuccessful() {
        return successful;
    }

    protected void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    private WriterAppender createAppender(final StringWriter writer) {
        final WriterAppender appender = new WriterAppender();
        appender.setWriter(writer);
        appender.setLayout(new SimpleLayout());
        appender.setThreshold(Level.INFO);

        // Add a filter that checks that the NDC of the logging event matches this threads NDC value. If it doesn't then it is a log from
        // another thread so just ignore it.
        appender.addFilter(new Filter() {
            @Override
            public int decide(final LoggingEvent event) {
                if (getId().equals(event.getNDC())) {
                    return Filter.ACCEPT;
                } else {
                    return Filter.DENY;
                }
            }
        });
        return appender;
    }
}
