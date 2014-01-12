package org.jboss.pressgang.ccms.server.async;

import javax.management.ListenerNotFoundException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;

import org.jppf.management.JMXDriverConnectionWrapper;

public class ExtendedJMXDriverConnectionWrapper extends JMXDriverConnectionWrapper {
    /**
     * Initialize a local connection to the MBean server.
     */
    public ExtendedJMXDriverConnectionWrapper() {
    }

    /**
     * Initialize the connection to the remote MBean server.
     *
     * @param host the host the server is running on.
     * @param port the port used by the server.
     */
    public ExtendedJMXDriverConnectionWrapper(final String host, final int port) {
        this(host, port, false);
    }

    /**
     * Initialize the connection to the remote MBean server.
     *
     * @param host   the host the server is running on.
     * @param port   the port used by the server.
     * @param secure specifies whether the connection should be established over SSL/TLS.
     */
    public ExtendedJMXDriverConnectionWrapper(final String host, final int port, final boolean secure) {
        super(host, port, secure);
    }

    public void addConnectionNotificationListener(final NotificationListener notificationListener) {
        addConnectionNotificationListener(notificationListener, null, null);
    }

    public void addConnectionNotificationListener(final NotificationListener notificationListener, final NotificationFilter filter,
            Object handback) {
        jmxc.addConnectionNotificationListener(notificationListener, filter, handback);
    }

    public void removeConnectionNotificationListener(final NotificationListener notificationListener) throws ListenerNotFoundException {
        jmxc.removeConnectionNotificationListener(notificationListener);
    }

    @Override
    public void close() throws Exception {
        super.close();
        // Mark the connection as closed, for whatever reason JPPF doesn't do this
        connected.set(false);
        // Remove the connect thread since it's now been closed
        connectionThread.set(null);
    }
}
