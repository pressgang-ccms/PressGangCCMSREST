package org.jboss.pressgang.ccms.restserver.utils.topicrenderer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import net.ser1.stomp.Client;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.jboss.pressgang.ccms.docbook.messaging.DocbookRendererMessage;
import org.jboss.pressgang.ccms.docbook.messaging.TopicRendererType;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.TranslatedTopic;
import org.jboss.pressgang.ccms.model.TranslatedTopicData;
import org.jboss.pressgang.ccms.restserver.utils.Constants;
import org.jboss.pressgang.ccms.restserver.utils.JNDIUtilities;
import org.jboss.pressgang.ccms.utils.constants.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopicQueueRenderer extends BaseRenderingThread implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(TopicQueueRenderer.class);

    /**
     * The ID of the topic to rerender
     */
    private final Integer topicId;
    /**
     * The type of topic we are rendering
     */
    private final int topicType;
    /**
     * true if topics with incoming relationships should also be rerendered
     */
    private final boolean rerenderIncoming;

    protected TopicQueueRenderer(final Integer topicId, final int topicType, final boolean rerenderIncoming,
            final EntityManagerFactory entityManagerFactory, final TransactionManager transactionManager, final Transaction transaction) {
        super(entityManagerFactory, transactionManager, transaction);
        this.topicId = topicId;
        this.topicType = topicType;
        this.rerenderIncoming = rerenderIncoming;
    }

    public static TopicQueueRenderer createNewInstance(final Integer topicId, final int topicType) {
        return createNewInstance(topicId, topicType, true, null);
    }

    public static TopicQueueRenderer createNewInstance(final Integer topicId, final int topicType, final boolean rerenderIncoming) {
        return createNewInstance(topicId, topicType, rerenderIncoming, null);
    }

    public static TopicQueueRenderer createNewInstance(final Integer topicId, final int topicType, final Transaction transaction) {
        return createNewInstance(topicId, topicType, true, transaction);
    }

    public static TopicQueueRenderer createNewInstance(final Integer topicId, final int topicType, final boolean rerenderIncoming,
            final Transaction transaction) {
        try {
            final EntityManagerFactory entityManagerFactory = JNDIUtilities.lookupEntityManagerFactory();
            final TransactionManager transactionManager = JNDIUtilities.lookupTransactionManager();
            return new TopicQueueRenderer(topicId, topicType, rerenderIncoming, entityManagerFactory, transactionManager, transaction);
        } catch (final Exception ex) {
            log.error("Probably an error looking up the manager objects", ex);
        }

        return null;
    }

    @Override
    public void run() {
        /*
         * wait for the transaction associated with the entity that was just saved to finish, or fail
         */
        if (!this.waitForTransaction()) return;

        EntityManager entityManager = null;

        try {
            final ObjectMapper mapper = new ObjectMapper();

            transactionManager.begin();

            Client client = null;
            try {
                entityManager = this.entityManagerFactory.createEntityManager();
                final AuditReader reader = AuditReaderFactory.get(entityManager);

                /*
                 * Send the message to the STOMP server once the transaction has completed
                 */
                final String messageServer = System.getProperty(CommonConstants.STOMP_MESSAGE_SERVER_SYSTEM_PROPERTY);
                final String messageServerPort = System.getProperty(CommonConstants.STOMP_MESSAGE_SERVER_PORT_SYSTEM_PROPERTY);
                final String messageServerUser = System.getProperty(CommonConstants.STOMP_MESSAGE_SERVER_USER_SYSTEM_PROPERTY);
                final String messageServerPass = System.getProperty(CommonConstants.STOMP_MESSAGE_SERVER_PASS_SYSTEM_PROPERTY);
                final String messageServerQueue;
                if (this.topicType == TopicRendererType.TOPIC)
                    messageServerQueue = System.getProperty(Constants.STOMP_MESSAGE_SERVER_TOPIC_RENDER_QUEUE_SYSTEM_PROPERTY);
                else messageServerQueue = System.getProperty(Constants.STOMP_MESSAGE_SERVER_TRANSLATED_TOPIC_RENDER_QUEUE_SYSTEM_PROPERTY);

                if (messageServer != null && messageServerPort != null && messageServerUser != null && messageServerPass != null &&
                        messageServerQueue != null) {
                    client = new Client(messageServer, Integer.parseInt(messageServerPort), messageServerUser, messageServerPass);

                    if (this.topicType == TopicRendererType.TOPIC) {
                        /* get the state of the topic now */
                        final Topic initialTopic = entityManager.find(Topic.class, this.topicId);

                        if (initialTopic != null) {

                            if (initialTopic.getTopicId() != null) {
                                log.info("Requesting external rendering component transform Topic {}", initialTopic.getTopicId());
                                client.send(messageServerQueue, mapper.writeValueAsString(
                                        new DocbookRendererMessage(initialTopic.getTopicId(), TopicRendererType.TOPIC)));
                            }

                            if (rerenderIncoming) {
                                for (final Topic relatedTopic : initialTopic.getIncomingRelatedTopicsArray()) {
                                    if (relatedTopic != null && relatedTopic.getTopicId() != null) {
                                        log.info("Requesting external rendering component transform Topic {}", relatedTopic.getTopicId());
                                        client.send(messageServerQueue, mapper.writeValueAsString(
                                                new DocbookRendererMessage(relatedTopic.getTopicId(), TopicRendererType.TOPIC)));
                                    }
                                }
                            }
                        }
                    } else if (this.topicType == TopicRendererType.TRANSLATEDTOPIC) {
                        entityManager = this.entityManagerFactory.createEntityManager();
                        final TranslatedTopicData translatedTopicData = entityManager.find(TranslatedTopicData.class, this.topicId);

                        if (translatedTopicData != null) {
                            final TranslatedTopic translatedTopic = translatedTopicData.getTranslatedTopic();

                            if (translatedTopic != null && translatedTopic.getTopicId() != null) {
                                log.info("Requesting external rendering component transform TranslatedTopic {} - {}",
                                        translatedTopic.getId(), translatedTopicData.getTranslationLocale());
                                client.send(messageServerQueue,
                                        mapper.writeValueAsString(new DocbookRendererMessage(topicId, TopicRendererType.TRANSLATEDTOPIC)));

                                /*
                                 * We also need to rerender those TranslatedTopics that relate back to this one, in case the
                                 * title of this topics has been translated (which will affect the ulinks that are created for
                                 * the injection points).
                                 * 
                                 * This process is not quite as simple as just looping over a collection of related topics. For
                                 * a start, there is no guarantee that the topic with incoming relationship has been translated.
                                 * 
                                 * First, we need to find the envers topic that this TranslatedTopic refers to.
                                 */

                                final Topic topic = reader.find(Topic.class, translatedTopic.getTopicId(),
                                        translatedTopic.getTopicRevision());

                                if (topic != null) {
                                    /*
                                     * Assuming the topic exists (and there is no reason why it shouldn't), we then loop over
                                     * the incoming relationships
                                     */
                                    for (final Topic relatedTopic : topic.getIncomingRelatedTopicsArray()) {
                                        if (relatedTopic != null && relatedTopic.getTopicId() != null) {
                                            TranslatedTopicData relatedTranslatedTopicData = translatedTopicData
                                                    .getLatestRelatedTranslationDataByTopicID(
                                                    entityManager, relatedTopic.getId());

                                            /*
                                             * Check that a related TranslatedTopicData was found. If not then continue on the
                                             * the next related topic.
                                             */
                                            if (relatedTranslatedTopicData == null) continue;

                                            log.info("Requesting external rendering component transform TranslatedTopic {} - {}",
                                                    relatedTranslatedTopicData.getTranslatedTopic().getId(),
                                                    relatedTranslatedTopicData.getTranslationLocale());
                                            client.send(messageServerQueue, mapper.writeValueAsString(
                                                    new DocbookRendererMessage(relatedTranslatedTopicData.getId(),
                                                            TopicRendererType.TRANSLATEDTOPIC)));

                                        }
                                    }
                                }
                            }
                        }
                    } else if (this.topicType == TopicRendererType.CONTENTSPECIFICATON) {
                        /*
                         * TODO Add the ability to render a content specification
                         */
                    }
                }
            } catch (JsonGenerationException ex) {
                log.error("An error occurred generating the JSON for the DocbookRendererMessage", ex);
            } catch (JsonMappingException ex) {
                log.error("An error occurred mapping the DocbookRendererMessage Object to JSON", ex);
            } catch (final Exception ex) {
                log.error("Probably a STOMP messaging problem", ex);
            } finally {
                if (client != null) client.disconnect();
            }

            transactionManager.commit();

        } catch (final Exception ex) {
            log.error("Probably a problem saving a Topic entity", ex);

            try {
                transactionManager.rollback();
            } catch (final Exception ex2) {
                log.error("An error rolling back a transaction", ex2);
            }
        } finally {
            if (entityManager != null) {
                entityManager.close();
                entityManager = null;
            }
        }
    }
}
