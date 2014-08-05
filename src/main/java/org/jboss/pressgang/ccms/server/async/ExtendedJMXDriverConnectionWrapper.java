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
