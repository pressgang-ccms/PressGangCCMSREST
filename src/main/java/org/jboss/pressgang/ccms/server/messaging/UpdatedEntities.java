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

package org.jboss.pressgang.ccms.server.messaging;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.IllegalStateException;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Hashtable;
import java.util.List;

import org.jboss.pressgang.ccms.filter.utils.EntityUtilities;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.config.ApplicationConfig;
import org.jboss.pressgang.ccms.model.contentspec.ContentSpec;
import org.jboss.pressgang.ccms.server.utils.ResourceProducer;
import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;

/**
 * This EJB will poll the database periodically looking for changes that can be broadcast via the message
 * queues. We do this with a periodic database query rather than intercepting changes through the REST
 * interface in order to support clustered environments where changes may be made on a different server,
 * and then replicated to this server after some undefined period of time. By using a periodic query we
 * are assured that the changes we are broadcasting are actually available on this server, and we avoid
 * having to sync broadcast messages with database replication cycles.
 *
 * This code assumes the presence of two JMS topics:
 *
 *  <jms-destinations>
 *      <jms-topic name="UpdatedTopic">
 *          <entry name="java:jboss/topics/updatedtopic"/>
 *      </jms-topic>
 *      <jms-topic name="UpdatedSpec">
 *          <entry name="java:jboss/topics/updatedspec"/>
 *      </jms-topic>
 *  </jms-destinations>
 */
@Singleton
@Startup
public class UpdatedEntities {
    private static final String EJB_REFRESH = "*/10";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.as.naming.InitialContextFactory";
    private static final String URL_PKG_PREFIXES = "org.jboss.naming:org.jnp.interfaces";
    private static final String PROVIDER_URL = "jnp://localhost:1099";
    private static final String CONNECTION_FACTORY = "/ConnectionFactory";
    /**
     * JNDI name for the JMS topic that will be notified of updated topics
     */
    private static final String TOPIC_UPDATE_QUEUE = "java:jboss/topics/updatedtopic";
    /**
     * JNDI name for the JMS topic that will be notified of updated content specs
     */
    private static final String SPEC_UPDATE_QUEUE = "java:jboss/topics/updatedspec";

    private static final String SERVER_RESTART = "SERVER_RESTART";

    /**
     * How many times to retry opening a connection and resending a message
     */
    private static final int RETRIES = 1;

    private final Hashtable<String, String> env = new Hashtable<String, String>();
    /**
     * The last highest revision that we checked for updates against
     */
    private Integer lastSpecRevision = null;
    private Integer lastTopicRevision = null;
    private Context ctx = null;
    private ConnectionFactory cf;
    private Connection connection;
    private Session session;

    @PersistenceContext(unitName = ResourceProducer.PERSISTENCE_UNIT_NAME)
    protected EntityManager entityManager;

    @Resource
    private SessionContext context;

    @Timeout
    public void onTimeout(final Timer timer) {
        final Integer thisLatestRevision = getLatestRevision();
        checkForUpdatedTopics(timer, thisLatestRevision);
        checkForUpdatedSpecs(timer, thisLatestRevision);
        createNewTimer();
    }

    protected Integer getLatestRevision() {
        return (Integer) entityManager.createQuery("SELECT MAX(id) FROM LoggingRevisionEntity").getSingleResult();
    }

    private void checkForUpdatedTopics(final Timer timer, final Integer thisLatestRevision) {

        if (lastTopicRevision != null) {
            try {
                final List<Integer> topics = EntityUtilities.getEditedEntitiesByRevision(entityManager, Topic.class, "topicId", lastTopicRevision, null);

                if (topics.size() != 0) {
                    sendMessage(TOPIC_UPDATE_QUEUE, CollectionUtilities.toSeperatedString(topics));
                }

                lastTopicRevision = thisLatestRevision + 1;

            } catch (final Exception ex) {
                // the message could not be sent. it will be retried as lastTopicRevision was not updated
                ex.printStackTrace();
            }
        } else {
            lastTopicRevision = thisLatestRevision + 1;
        }
    }

    public void checkForUpdatedSpecs(final Timer timer, final Integer thisLatestRevision) {
        if (lastSpecRevision != null) {
            try {
                final List<Integer> specs = EntityUtilities.getEditedEntitiesByRevision(entityManager, ContentSpec.class, "contentSpecId", lastSpecRevision, null);

                if (specs.size() != 0) {
                    sendMessage(SPEC_UPDATE_QUEUE, CollectionUtilities.toSeperatedString(specs));
                }

                lastSpecRevision = thisLatestRevision + 1;

            } catch (final Exception ex) {
                // the message could not be sent. it will be retried as lastSpecRevision was not updated
                ex.printStackTrace();
            }
        } else {
            lastSpecRevision = thisLatestRevision + 1;
        }
    }

    /**
     * Here we create a single action timer that reads the JMS Update Frequency value from the
     * application config and schedules the next refresh. This allows us to define the refresh
     * frequency as part of the config, and alter it at runtime.
     */
    private void createNewTimer() {
        final TimerConfig config = new TimerConfig();
        config.setPersistent(false);
        config.setInfo(ApplicationConfig.getInstance().getJmsUpdateFrequency() * 1000);

        context.getTimerService().createSingleActionTimer(
                ApplicationConfig.getInstance().getJmsUpdateFrequency() * 1000,
                config);
    }

    @PostConstruct
    private void sendStartupMessage() {
        setup();

        try {
            sendMessage(TOPIC_UPDATE_QUEUE, SERVER_RESTART);
            sendMessage(SPEC_UPDATE_QUEUE, SERVER_RESTART);
        } catch (final Exception ex) {
            // the message could not be sent.
            ex.printStackTrace();
        }
    }

    /**
     * Connect to the JMS subsystem
     */
    private void setup()  {
        try {
            env.put(Context.PROVIDER_URL, PROVIDER_URL);
            env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
            env.put(Context.URL_PKG_PREFIXES, URL_PKG_PREFIXES);
            ctx = new InitialContext(env);
            cf = (ConnectionFactory)ctx.lookup(CONNECTION_FACTORY);
            connection = cf.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.start();
        } catch (final Exception ex) {
            ex.printStackTrace();
            ctx = null;
            cf = null;
            connection = null;
            session = null;
        }

        createNewTimer();
    }

    /**
     * Close the connection to the JMS subsystem
     */
    @PreDestroy
    private void shutdown() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (final Exception ex) {

        } finally {
            connection = null;
        }
    }

    private void sendMessage(final String topicName, final String jmsMessage) throws NamingException, JMSException {
        sendMessage(topicName, jmsMessage, 0);
    }

    /**
     * Send a message to a JMS topic
     * @param topicName  The JNDI name of the JMS topic
     * @param jmsMessage The message to be sent
     * @throws NamingException
     * @throws JMSException
     */
    private void sendMessage(final String topicName, final String jmsMessage, final int retries) throws NamingException, JMSException {
        try {
            if (retries < RETRIES && ctx != null && session != null) {
                // Lookup the JMS queue
                final javax.jms.Topic topic = (javax.jms.Topic) ctx.lookup(topicName);

                // Create a JMS Message Producer to send a message on the queue
                final MessageProducer producer = session.createProducer(topic);

                // Create a Text Message and send it using the producer. The HornetQ REST server requires the use
                // of Object messages.
                final ObjectMessage message = session.createObjectMessage(jmsMessage);
                producer.send(message);
            }
        } catch (final IllegalStateException ex) {
            /*
                If the session is closed (and this can happen without calling shutdown()) we'll get
                the following error:

                javax.jms.IllegalStateException: HQ119019: Session is closed

                in this case, shutdown, startup and retry.
             */
            shutdown();
            setup();
            sendMessage(topicName, jmsMessage, retries + 1);
        }
    }
}
