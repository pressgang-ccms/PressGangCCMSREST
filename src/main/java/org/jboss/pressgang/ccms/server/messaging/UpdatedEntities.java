package org.jboss.pressgang.ccms.server.messaging;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.jboss.pressgang.ccms.filter.utils.EntityUtilities;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;
import org.jboss.pressgang.ccms.utils.common.StringUtilities;
import org.joda.time.DateTime;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

/**
 * This EJB will poll the database periodically looking for changes that can be broadcast via the message
 * queues. We do this with a periodic database query rather than intercepting changes through the REST
 * interface in order to support clustered environments where changes may be made on a different server,
 * and then replicated to this server after some undefined period of time. By using a periodic query we
 * are assured that the changes we are broadcasting are actually available on this server, and we avoid
 * having to sync broadcast messages with database replication cycles.
 */
@Singleton
@Startup
public class UpdatedEntities {

    private static final long REFRESH = 10 * 1000;
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.as.naming.InitialContextFactory";
    private static final String URL_PKG_PREFIXES = "org.jboss.naming:org.jnp.interfaces";
    private static final String PROVIDER_URL = "jnp://localhost:1099";
    private static final String CONNECTION_FACTORY = "/ConnectionFactory";
    private static final String TOPIC_UPDATE_QUEUE = "java:jboss/queues/updatedtopic";
    private static final String SPEC_UPDATE_QUEUE = "java:jboss/queues/updatedspec";
    private final Hashtable<String, String> env = new Hashtable<String, String>();
    private DateTime lastTopicUpdate = null;
    private Context ctx = null;
    private ConnectionFactory cf;
    private Connection connection;
    private Session session;

    @Inject
    protected EntityManager entityManager;

    @Schedule(hour="*", minute="*", second="*/10")
    public void checkForUpdatedTopics() {
        final DateTime thisTopicUpdate = new DateTime();

        if (lastTopicUpdate == null) {
            lastTopicUpdate = thisTopicUpdate.minus(REFRESH);
        }

        try {
            final List<Integer> topics = EntityUtilities.getEditedEntities(entityManager, Topic.class, "topicId", lastTopicUpdate, null);

            sendMessage(TOPIC_UPDATE_QUEUE, CollectionUtilities.toSeperatedString(topics));
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

    @PostConstruct
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

    @PreDestroy
    private void shutdown() throws JMSException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    private void sendMessage(final String queueName, final String jmsMessage) throws NamingException, JMSException {
        if (ctx != null && session != null) {
            // Lookup the JMS queue
            final javax.jms.Topic queue = (javax.jms.Topic) ctx.lookup(queueName);

            // Create a JMS Message Producer to send a message on the queue
            final MessageProducer producer = session.createProducer(queue);

            // Create a Text Message and send it using the producer
            final TextMessage message = session.createTextMessage(jmsMessage);
            producer.send(message);
        }
    }
}
