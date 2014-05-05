package org.jboss.pressgang.ccms.server.messaging;

import org.jboss.pressgang.ccms.filter.utils.EntityUtilities;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.contentspec.ContentSpec;
import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;
import org.joda.time.DateTime;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import java.util.Hashtable;
import java.util.List;

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
    /**
     * Milliseconds between database queries
     */
    private static final long REFRESH = 10 * 1000;
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
    private final Hashtable<String, String> env = new Hashtable<String, String>();
    private DateTime lastTopicUpdate = null;
    private DateTime specTopicUpdate = null;
    private Context ctx = null;
    private ConnectionFactory cf;
    private Connection connection;
    private Session session;

    @Inject
    protected EntityManager entityManager;

    @Schedule(hour="*", minute="*", second=EJB_REFRESH)
    public void checkForUpdatedTopics() {
        final DateTime thisTopicUpdate = new DateTime();

        if (lastTopicUpdate == null) {
            lastTopicUpdate = thisTopicUpdate.minus(REFRESH);
        }

        try {
            final List<Integer> topics = EntityUtilities.getEditedEntities(entityManager, Topic.class, "topicId", lastTopicUpdate, null);

            if (topics.size() != 0) {
                sendMessage(TOPIC_UPDATE_QUEUE, CollectionUtilities.toSeperatedString(topics));
            }

            /*
                There is a possibility that a topic will be found twice if it is edited in the time it takes
                to set thisTopicUpdate and execute the query. This is a pretty small window though.
            */
            lastTopicUpdate = thisTopicUpdate;

        } catch (final Exception ex) {
            // the message could not be sent. it will be retried as lastTopicUpdate was not updated
            ex.printStackTrace();
        }
    }

    @Schedule(hour="*", minute="*", second=EJB_REFRESH)
    public void checkForUpdatedSpecs() {
        final DateTime thisTopicUpdate = new DateTime();

        if (specTopicUpdate == null) {
            specTopicUpdate = thisTopicUpdate.minus(REFRESH);
        }

        try {
            final List<Integer> specs = EntityUtilities.getEditedEntities(entityManager, ContentSpec.class, "contentSpecId", specTopicUpdate, null);

            if (specs.size() != 0) {
                sendMessage(SPEC_UPDATE_QUEUE, CollectionUtilities.toSeperatedString(specs));
            }

            /*
                There is a possibility that a topic will be found twice if it is edited in the time it takes
                to set thisTopicUpdate and execute the query. This is a pretty small window though.
            */
            specTopicUpdate = thisTopicUpdate;

        } catch (final Exception ex) {
            // the message could not be sent. it will be retried as lastTopicUpdate was not updated
            ex.printStackTrace();
        }
    }

    @PostConstruct
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
            ctx = null;
            cf = null;
            connection = null;
            session = null;
        }
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

    /**
     * Send a message to a JMS topic
     * @param topicName  The JNDI name of the JMS topic
     * @param jmsMessage The message to be sent
     * @throws NamingException
     * @throws JMSException
     */
    private void sendMessage(final String topicName, final String jmsMessage) throws NamingException, JMSException {
        if (ctx != null && session != null) {
            // Lookup the JMS queue
            final javax.jms.Topic topic = (javax.jms.Topic) ctx.lookup(topicName);

            // Create a JMS Message Producer to send a message on the queue
            final MessageProducer producer = session.createProducer(topic);

            // Create a Text Message and send it using the producer. The HornetQ REST server requires the use
            // of Object messages.
            final ObjectMessage message = session.createObjectMessage(jmsMessage);
            producer.send(message);
        }
    }
}
