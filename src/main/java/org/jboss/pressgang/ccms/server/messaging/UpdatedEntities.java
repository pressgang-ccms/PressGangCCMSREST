package org.jboss.pressgang.ccms.server.messaging;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.jboss.pressgang.ccms.filter.utils.EntityUtilities;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.config.ApplicationConfig;
import org.jboss.pressgang.ccms.model.contentspec.ContentSpec;
import org.jboss.pressgang.ccms.server.utils.ResourceProducer;
import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;
import org.joda.time.DateTime;
import org.jppf.classloader.ResourceProvider;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.jms.*;
import javax.jms.IllegalStateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.NamedNativeQuery;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import javax.ejb.Timer;

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

    private static final String MAX_REV_QUERY = "SELECT MAX( REV ) FROM Skynet.REVINFO";

    /**
     * How many times to retry opening a connection and resending a message
     */
    private static final int RETRIES = 1;

    private final Hashtable<String, String> env = new Hashtable<String, String>();
    /**
     * The last highest revision that we checked for updates against
     */
    private Integer lastLatestRevision = null;
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
        final AuditReader reader = AuditReaderFactory.get(entityManager);
        final Integer thisLatestRevision = (Integer)entityManager.createNativeQuery(MAX_REV_QUERY).getSingleResult();

        checkForUpdatedTopics(timer);
        checkForUpdatedSpecs(timer);
        triggerNextTimeout(timer);

        lastLatestRevision = thisLatestRevision + 1;
    }

    private void checkForUpdatedTopics(final Timer timer) {

        if (lastLatestRevision != null) {
            try {
                final List<Integer> topics = EntityUtilities.getEditedEntitiesByRevision(entityManager, Topic.class, "topicId", lastLatestRevision, null);

                if (topics.size() != 0) {
                    sendMessage(TOPIC_UPDATE_QUEUE, CollectionUtilities.toSeperatedString(topics));
                }

            } catch (final Exception ex) {
                // the message could not be sent. it will be retried as lastTopicUpdate was not updated
                ex.printStackTrace();
            }
        }
    }

    public void checkForUpdatedSpecs(final Timer timer) {
        if (lastLatestRevision != null) {
            try {
                final List<Integer> specs = EntityUtilities.getEditedEntitiesByRevision(entityManager, ContentSpec.class, "contentSpecId", lastLatestRevision, null);

                if (specs.size() != 0) {
                    sendMessage(SPEC_UPDATE_QUEUE, CollectionUtilities.toSeperatedString(specs));
                }

            } catch (final Exception ex) {
                // the message could not be sent. it will be retried as lastTopicUpdate was not updated
                ex.printStackTrace();
            }
        }
    }

    public void triggerNextTimeout(final Timer timer) {
        createNewTimer();
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
