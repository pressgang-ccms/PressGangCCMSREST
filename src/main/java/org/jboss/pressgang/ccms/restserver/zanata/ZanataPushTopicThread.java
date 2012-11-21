package org.jboss.pressgang.ccms.restserver.zanata;

import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.TransactionManager;

import org.apache.commons.codec.binary.Hex;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.jboss.pressgang.ccms.contentspec.structures.StringToCSNodeCollection;
import org.jboss.pressgang.ccms.contentspec.utils.ContentSpecUtilities;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.TranslatedTopic;
import org.jboss.pressgang.ccms.restserver.utils.Constants;
import org.jboss.pressgang.ccms.restserver.utils.JNDIUtilities;
import org.jboss.pressgang.ccms.restserver.utils.TopicUtilities;
import org.jboss.pressgang.ccms.utils.common.XMLUtilities;
import org.jboss.pressgang.ccms.utils.structures.Pair;
import org.jboss.pressgang.ccms.utils.structures.StringToNodeCollection;
import org.jboss.pressgang.ccms.zanata.ZanataInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.zanata.common.ContentType;
import org.zanata.common.LocaleId;
import org.zanata.common.ResourceType;
import org.zanata.rest.dto.resource.Resource;
import org.zanata.rest.dto.resource.TextFlow;

import com.redhat.contentspec.processor.ContentSpecParser;

/**
 * A Runnable class that will Push topics to Zanata in a background thread.
 */
// TODO We really should have a way to retrieve or save details about what topics succeeded or failed.
public class ZanataPushTopicThread implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ZanataPushTopicThread.class);
    private static final double ZANATA_MIN_CALL_INTERVAL = 0.2;

    private final ZanataInterface zanataInterface = new ZanataInterface(ZANATA_MIN_CALL_INTERVAL);
    private List<Pair<Integer, Integer>> topics;
    private boolean overwrite = false;

    public ZanataPushTopicThread(final List<Pair<Integer, Integer>> topics, final boolean overwrite) {
        /* get a read only copy of the list */
        this.topics = Collections.unmodifiableList(topics);

        this.overwrite = overwrite;
    }

    @Override
    public void run() {
        /*
         * Only one thread should be created to use the list of topics, but we synchronize anyway just in case.
         */
        synchronized (topics) {
            TransactionManager transactionManager = null;
            EntityManager entityManager = null;

            try {
                final EntityManagerFactory entityManagerFactory = JNDIUtilities.lookupEntityManagerFactory();

                // Get the TransactionManager and start a transaction.
                transactionManager = JNDIUtilities.lookupTransactionManager();
                transactionManager.begin();

                // Get an EntityManager instance and Envers audit reader.
                entityManager = entityManagerFactory.createEntityManager();
                final AuditReader reader = AuditReaderFactory.get(entityManager);

                final int total = topics.size();
                int current = 0;

                for (final Pair<Integer, Integer> topicDetails : topics) {
                    ++current;
                    final int progress = (int) ((float) current / (float) total * 100.0f);
                    log.info("Push To Zanata Progress - Topic ID {}: {} of {} ({}%)", new Integer[] { topicDetails.getFirst(),
                            current, total, progress });

                    final Topic topic = reader.find(Topic.class, topicDetails.getFirst(), topicDetails.getSecond());

                    if (topic != null) {
                        final Number topicRevision = topicDetails.getSecond();
                        final String zanataId = topic.getTopicId() + "-" + topicRevision;

                        /*
                         * deleting existing resources is useful for debugging, but not for production
                         */
                        final boolean zanataFileExists = zanataInterface.getZanataResource(zanataId) != null;

                        if (zanataFileExists) {
                            if (overwrite) {
                                log.info("Topic ID {} revision {} already exists - Deleting.", topic.getTopicId(),
                                        topicRevision);
                                zanataInterface.deleteResource(zanataId);
                            } else {
                                log.info("Topic ID {} revision {} already exists - Skipping.", topic.getTopicId(),
                                        topicRevision);
                                continue;
                            }
                        }

                        /* Content Specs are parsed differently then XML */
                        if (topic.isTaggedWith(Constants.CONTENT_SPEC_TAG_ID)) {
                            // TODO the URL shouldn't be statically coded
                            final ContentSpecParser parser = new ContentSpecParser("http://localhost:8080/TopicIndex/");

                            try {
                                if (parser.parse(topic.getTopicXML())) {

                                    // Create a Zanata Resource Object and populate it with the base data
                                    final Resource resource = new Resource();

                                    resource.setContentType(ContentType.TextPlain);
                                    resource.setLang(LocaleId.fromJavaName(topic.getTopicLocale()));
                                    resource.setName(zanataId);
                                    resource.setRevision(1);
                                    resource.setType(ResourceType.FILE);

                                    // Get the translation strings from the Content Spec.
                                    final List<StringToCSNodeCollection> translatableStrings = ContentSpecUtilities
                                            .getTranslatableStrings(parser.getContentSpec(), false);

                                    // Populate the Zanata Resource with the translation strings
                                    for (final StringToCSNodeCollection translatableStringData : translatableStrings) {
                                        final String translatableString = translatableStringData.getTranslationString();
                                        if (!translatableString.trim().isEmpty()) {
                                            final TextFlow textFlow = new TextFlow();
                                            textFlow.setContents(translatableString);
                                            textFlow.setLang(LocaleId.fromJavaName(topic.getTopicLocale()));
                                            textFlow.setId(createId(translatableString));
                                            textFlow.setRevision(1);

                                            resource.getTextFlows().add(textFlow);
                                        }
                                    }

                                    // Create the file in Zanata
                                    if (zanataInterface.createFile(resource)) {
                                        // Create a translation in TopicIndex
                                        final TranslatedTopic translatedTopic = TopicUtilities.createTranslatedTopic(
                                                entityManager, topic.getTopicId(), topicDetails.getSecond());
                                        if (translatedTopic != null) {
                                            /* Persist the new Translated Topic */
                                            entityManager.persist(translatedTopic);
                                        }
                                    } else {
                                        log.error("Failed to create the Document in Zanata");
                                    }
                                } else {
                                    log.info("Content Spec ID {} revision {} does not have valid syntax - Skipping.",
                                            topic.getTopicId(), topicRevision);
                                }

                            } catch (Exception ex) {
                                log.error("Probably an error parsing the Content Specification", ex);
                            }
                        } else {
                            try {
                                final Document doc = XMLUtilities.convertStringToDocument(topic.getTopicXML());

                                /* the historical object may have invalid xml */
                                if (doc != null) {

                                    // Create a Zanata Resource Object and populate it with the base data
                                    final Resource resource = new Resource();

                                    resource.setContentType(ContentType.TextPlain);
                                    resource.setLang(LocaleId.fromJavaName(topic.getTopicLocale()));
                                    resource.setName(zanataId);
                                    resource.setRevision(1);
                                    resource.setType(ResourceType.FILE);

                                    // Get the Translation Strings from the XML
                                    final List<StringToNodeCollection> translatableStrings = XMLUtilities
                                            .getTranslatableStringsV2(doc, false);

                                    // Populate the Zanata Resource with the translation strings
                                    for (final StringToNodeCollection translatableStringData : translatableStrings) {
                                        final String translatableString = translatableStringData.getTranslationString();
                                        if (!translatableString.trim().isEmpty()) {
                                            final TextFlow textFlow = new TextFlow();
                                            textFlow.setContents(translatableString);
                                            textFlow.setLang(LocaleId.fromJavaName(topic.getTopicLocale()));
                                            textFlow.setId(createId(translatableString));
                                            textFlow.setRevision(1);

                                            resource.getTextFlows().add(textFlow);
                                        }
                                    }

                                    // Create the file in Zanata
                                    if (zanataInterface.createFile(resource)) {
                                        // Create a translation in TopicIndex
                                        final TranslatedTopic translatedTopic = TopicUtilities.createTranslatedTopic(
                                                entityManager, topic.getTopicId(), topicDetails.getSecond());
                                        if (translatedTopic != null) {
                                            /* Persist the new Translated Topic */
                                            entityManager.persist(translatedTopic);
                                        }
                                    } else {
                                        log.error("Failed to create the Document in Zanata");
                                    }
                                } else {
                                    log.info("Topic ID {} revision {} does not have valid XML - Skipping.", topic.getTopicId(),
                                            topicRevision);
                                }
                            } catch (final Exception ex) {
                                log.error("Probably an error saving the Topic entity or creating the DOM Document", ex);
                            }
                        }
                    }
                }

                // Save the changes.
                transactionManager.commit();
            } catch (final Exception ex) {
                log.error("Probably an error retrieveing the Topic entity", ex);

                if (transactionManager != null) {
                    try {
                        transactionManager.rollback();
                    } catch (final Exception ex2) {
                        log.error("There was an issue rolling back the transaction", ex);
                    }
                }
            } finally {
                if (entityManager != null)
                    entityManager.close();
            }
        }
    }

    private static String createId(final String text) {
        return createId(text, null);
    }

    private static String createId(final String text, final String postFix) {
        if (postFix != null) {
            final String sep = "\u0000";
            final String hashBase = text + sep + postFix;
            return generateHash(hashBase);
        } else {
            return generateHash(text);
        }
    }

    private static String generateHash(final String key) {
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            return new String(Hex.encodeHex(md5.digest(key.getBytes("UTF-8"))));
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

}
