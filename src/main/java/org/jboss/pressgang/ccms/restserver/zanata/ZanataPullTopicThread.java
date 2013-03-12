package org.jboss.pressgang.ccms.restserver.zanata;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.TransactionManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.jboss.pressgang.ccms.docbook.messaging.TopicRendererType;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.TranslatedTopic;
import org.jboss.pressgang.ccms.model.TranslatedTopicData;
import org.jboss.pressgang.ccms.model.TranslatedTopicString;
import org.jboss.pressgang.ccms.restserver.utils.EntityUtilities;
import org.jboss.pressgang.ccms.restserver.utils.JNDIUtilities;
import org.jboss.pressgang.ccms.restserver.utils.topicrenderer.TopicQueueRenderer;
import org.jboss.pressgang.ccms.utils.common.XMLUtilities;
import org.jboss.pressgang.ccms.utils.concurrency.WorkQueue;
import org.jboss.pressgang.ccms.utils.constants.CommonConstants;
import org.jboss.pressgang.ccms.utils.structures.StringToNodeCollection;
import org.jboss.pressgang.ccms.zanata.ZanataInterface;
import org.jboss.pressgang.ccms.zanata.ZanataTranslation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.zanata.common.LocaleId;
import org.zanata.rest.dto.resource.Resource;
import org.zanata.rest.dto.resource.TextFlow;
import org.zanata.rest.dto.resource.TextFlowTarget;
import org.zanata.rest.dto.resource.TranslationsResource;

/**
 * A class that provides a Runnable interface to allow pulling translations from Zanata for specified topics in a background
 * Thread.
 */
public class ZanataPullTopicThread implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ZanataPullTopicThread.class);
    private static final double ZANATA_MIN_CALL_INTERVAL = 0.2;
    private static final String XML_ENCODING = "UTF-8";

    private List<Integer> translatedTopics;
    private final ZanataInterface zanataInterface = new ZanataInterface(ZANATA_MIN_CALL_INTERVAL);

    public ZanataPullTopicThread(final List<Integer> translatedTopics) {
        this.translatedTopics = Collections.unmodifiableList(translatedTopics);
    }

    @Override
    public void run() {
        synchronized (translatedTopics) {

            final int total = translatedTopics.size();
            int current = 0;

            TransactionManager transactionManager = null;
            EntityManager entityManager = null;

            try {
                final EntityManagerFactory entityManagerFactory = JNDIUtilities.lookupEntityManagerFactory();

                // Get the TransactionManager and start a transaction.
                transactionManager = JNDIUtilities.lookupTransactionManager();
                transactionManager.begin();

                // Get an EntityManager instance
                entityManager = entityManagerFactory.createEntityManager();

                // Ensure that the Zanata Locales have been initalised
                if (zanataInterface.getZanataLocales().isEmpty()) {
                    final List<String> locales = EntityUtilities.getLocales(entityManager);
                    final List<LocaleId> localeIds = new ArrayList<LocaleId>();
                    for (final String locale : locales) {
                        localeIds.add(LocaleId.fromJavaName(locale));
                    }
                    zanataInterface.getLocaleManager().setLocales(localeIds);
                }

                final List<Integer> processedIds = new ArrayList<Integer>();

                // for each translated topic update the translated xml and translated strings
                for (final Integer topicId : this.translatedTopics) {
                    ++current;
                    final int progress = (int) ((float) current / (float) total * 100.0f);
                    log.info("Pull from Zanata Progress - Topic ID {}: {} of {} ({}%)", new Integer[]{topicId, current, total, progress});

                    /* ... and each locale */
                    final List<LocaleId> locales = zanataInterface.getZanataLocales();
                    for (final LocaleId locale : locales) {
                        /* attempt to pull down the latest translations */
                        final Integer translatedTopicDataId = processTopic(entityManager, topicId, locale.toString());
                        if (translatedTopicDataId != null) {
                            processedIds.add(translatedTopicDataId);
                        }
                    }
                }

                // Commit all the changes
                transactionManager.commit();

                // Render all the updated translated topics
                for (final Integer id : processedIds) {
                    WorkQueue.getInstance().execute(TopicQueueRenderer.createNewInstance(id, TopicRendererType.TRANSLATEDTOPIC));
                }
            } catch (final Exception ex) {
                log.error("Probably an error looking up the EntityManagerFactory or the TransactionManager", ex);

                if (transactionManager != null) {
                    try {
                        transactionManager.rollback();
                    } catch (final Exception ex2) {
                        log.error("There was an exception rolling back the transaction", ex2);
                    }
                }
            } finally {
                if (entityManager != null) entityManager.close();
            }

        }
    }

    /**
     * Process a translated topic and pull all the translation strings from Zanata.
     *
     * @param entityManager     The EntityManager instance to be used to lookup information about the stored translated topic.
     * @param translatedTopicId The ID of the Translated Topic to be processed.
     * @param locale            The locale of the translated topic to be processed.
     * @return The ID of the TranslatedTopicData that was created or updated.
     */
    @SuppressWarnings("deprecation")
    private Integer processTopic(final EntityManager entityManager, final Integer translatedTopicId, final String locale) {
        Integer retValue = null;

        final AuditReader reader = AuditReaderFactory.get(entityManager);

        // Find the original TranslatedTopic
        final TranslatedTopic topic = entityManager.find(TranslatedTopic.class, translatedTopicId);

        if (topic != null) {
            // ... find the matching historical envers topic
            final Topic historicalTopic = reader.find(Topic.class, topic.getTopicId(), topic.getTopicRevision());

            // Don't get translations for the original locale as it won't need translating
            if (historicalTopic.getTopicLocale().equals(locale)) {
                return null;
            }

            // find a translation
            final TranslationsResource translationsResource = zanataInterface.getTranslations(topic.getZanataId(),
                    LocaleId.fromJavaName(locale));
            // and find the original resource
            final Resource originalTextResource = zanataInterface.getZanataResource(topic.getZanataId());

            if (translationsResource != null && originalTextResource != null) {
                final Set<TranslatedTopicData> translatedTopicDataEntities = topic.getTranslatedTopicDatas();

                // attempt to find an existing TranslatedTopicData entity
                TranslatedTopicData translatedTopicData = null;

                for (final TranslatedTopicData myTranslatedTopicData : translatedTopicDataEntities) {
                    if (myTranslatedTopicData.getTranslationLocale().equals(locale)) {
                        translatedTopicData = myTranslatedTopicData;
                        break;
                    }
                }

                // If an existing TranslatedTopicData entity does not exist, create one
                if (translatedTopicData == null) {
                    translatedTopicData = new TranslatedTopicData();
                    translatedTopicData.setTranslatedTopic(topic);
                    translatedTopicData.setTranslationLocale(locale);
                    translatedTopicDataEntities.add(translatedTopicData);
                }

                // A mapping of the original strings to their translations
                final Map<String, ZanataTranslation> translationDetails = new HashMap<String, ZanataTranslation>();
                final Map<String, String> translations = new HashMap<String, String>();

                final List<TextFlowTarget> textFlowTargets = translationsResource.getTextFlowTargets();
                final List<TextFlow> textFlows = originalTextResource.getTextFlows();

                double wordCount = 0;
                double totalWordCount = 0;

                // map the translation to the original resource
                for (final TextFlow textFlow : textFlows) {
                    for (final TextFlowTarget textFlowTarget : textFlowTargets) {
                        if (textFlowTarget.getResId().equals(textFlow.getId())) {
                            translationDetails.put(textFlow.getContent(), new ZanataTranslation(textFlowTarget));
                            translations.put(textFlow.getContent(), textFlowTarget.getContent());
                            wordCount += textFlow.getContent().split(" ").length;
                            break;
                        }
                    }
                    totalWordCount += textFlow.getContent().split(" ").length;
                }

                /* Set the translation completion status */
                translatedTopicData.setTranslationPercentage((int) (wordCount / totalWordCount * 100.0f));

                try {
                    if (historicalTopic.isTaggedWith(CommonConstants.CONTENT_SPEC_TAG_ID)) {
                        // TODO Fix Me
                        // processContentSpec(entityManager, historicalTopic, translatedTopicData, translationDetails, translations);
                    } else {
                        processTopic(entityManager, historicalTopic, translatedTopicData, translationDetails, translations);
                    }
                    // persist the changes
                    entityManager.persist(translatedTopicData);

                    // Make a note of the TranslatedTopicData entities that have been changed, so we can render them
                    retValue = translatedTopicData.getTranslatedTopicDataId();
                } catch (Exception ex) {
                    log.error("An error occurred saving or creating the translated topic", ex);
                }
            }
        }

        return retValue;
    }

    /**
     * Processes a Translated Topic and updates or removes the translation strings in that topic to match the new values pulled
     * down from Zanata. It also updates the XML using the strings pulled from Zanata.
     *
     * @param historicalTopic    The historical topic the translation was built from.
     * @param translatedTopic    The Translated Topic to update.
     * @param translationDetails The mapping of Original Translation strings to Zanata Translation information.
     * @param translations       A direct mapping of Original strings to Translation strings.
     * @return True if anything in the translated topic changed, otherwise false.
     * @throws SAXException Thrown if the XML in the historical topic has invalid XML and can't be parsed.
     */
    @SuppressWarnings("deprecation")
    protected boolean processTopic(final EntityManager entityManager, final Topic historicalTopic,
            final TranslatedTopicData translatedTopic, final Map<String, ZanataTranslation> translationDetails,
            final Map<String, String> translations) throws SAXException {
        boolean changed = false;

        // Get a Document from the stored historical XML
        final Document xml = XMLUtilities.convertStringToDocument(historicalTopic.getTopicXML());
        if (xml != null) {

            final List<StringToNodeCollection> stringToNodeCollectionsV2 = XMLUtilities.getTranslatableStringsV2(xml, false);
            final List<StringToNodeCollection> stringToNodeCollectionsV1 = XMLUtilities.getTranslatableStringsV1(xml, false);
            final List<StringToNodeCollection> stringToNodeCollections = new ArrayList<StringToNodeCollection>();
            final List<StringToNodeCollection> tempStringToNodeCollection = new ArrayList<StringToNodeCollection>();

            // Add any StringToNode's that match the original translations
            for (final StringToNodeCollection stringToNodeCollectionV2 : stringToNodeCollectionsV2) {
                for (final String originalString : translations.keySet()) {
                    if (originalString.equals(stringToNodeCollectionV2.getTranslationString())) {
                        stringToNodeCollections.add(stringToNodeCollectionV2);
                        tempStringToNodeCollection.add(stringToNodeCollectionV2);
                    }
                }
            }

            // Add any StringToNode's that match original translations for the bugged v1 method
            for (final StringToNodeCollection stringToNodeCollectionV1 : stringToNodeCollectionsV1) {
                for (final String originalString : translations.keySet()) {
                    if (originalString.equals(stringToNodeCollectionV1.getTranslationString()) && !stringToNodeCollections.contains(
                            stringToNodeCollectionV1)) {
                        stringToNodeCollections.add(stringToNodeCollectionV1);
                        tempStringToNodeCollection.add(stringToNodeCollectionV1);
                    }
                }
            }

            // Remove or update any existing translation strings
            if (translatedTopic.getTranslatedTopicStrings() != null) {
                final List<TranslatedTopicString> removeTranslationStringList = new ArrayList<TranslatedTopicString>();

                for (final TranslatedTopicString existingString : translatedTopic.getTranslatedTopicStrings()) {
                    boolean found = false;

                    for (final StringToNodeCollection original : stringToNodeCollections) {
                        final String originalText = original.getTranslationString();

                        if (existingString.getOriginalString().equals(originalText)) {
                            found = true;
                            tempStringToNodeCollection.remove(original);

                            final ZanataTranslation translation = translationDetails.get(originalText);

                            // Check the translations still match
                            if (!translation.getTranslation().equals(existingString.getTranslatedString())) {
                                changed = true;

                                existingString.setTranslatedString(translation.getTranslation());
                            }

                            // Check if the string is still fuzzy
                            if (translation.isFuzzy() != existingString.getFuzzyTranslation()) {
                                changed = true;

                                existingString.setFuzzyTranslation(translation.isFuzzy());
                            }
                        }
                    }

                    // If the original String no longer exists then remove it (this shouldn't happen)
                    if (!found) {
                        removeTranslationStringList.add(existingString);
                    }
                }

                // Remove any translation strings that no longer exist
                for (final TranslatedTopicString translatedTopicString : removeTranslationStringList) {
                    translatedTopic.getTranslatedTopicStrings().remove(translatedTopicString);
                    entityManager.remove(translatedTopicString);
                }
            }

            // save the new strings to TranslatedTopicString entities
            for (final StringToNodeCollection original : tempStringToNodeCollection) {
                final String originalText = original.getTranslationString();
                final ZanataTranslation translation = translationDetails.get(originalText);

                if (translation != null) {
                    changed = true;

                    final TranslatedTopicString translatedTopicString = new TranslatedTopicString();
                    translatedTopicString.setOriginalString(originalText);
                    translatedTopicString.setTranslatedString(translation.getTranslation());
                    translatedTopicString.setFuzzyTranslation(translation.isFuzzy());
                    translatedTopicString.setTranslatedTopicData(translatedTopic);

                    translatedTopic.getTranslatedTopicStrings().add(translatedTopicString);
                }
            }

            // replace the translated strings, and save the result into the TranslatedTopicData entity
            if (xml != null) {
                XMLUtilities.replaceTranslatedStrings(xml, translations, stringToNodeCollections);
                translatedTopic.setTranslatedXml(XMLUtilities.convertDocumentToString(xml, XML_ENCODING));
            }
        }

        return changed;
    }

    /**
     * Processes a Translated Topic and updates or removes the translation strings in that topic to match the new values pulled
     * down from Zanata. It also updates the Content Spec using the strings pulled from Zanata.
     *
     * @param historicalTopic    The historical topic the translation was built from.
     * @param translatedTopic    The Translated Topic to update.
     * @param translationDetails The mapping of Original Translation strings to Zanata Translation information.
     * @param translations       A direct mapping of Original strings to Translation strings.
     * @return True if anything in the translated topic changed, otherwise false.
     * @throws Exception Thrown if there is an error in the Content Specification syntax.
     */
    /*protected boolean processContentSpec(final EntityManager entityManager, final Integer contentSpecId,
            final Integer contentSpecRevision,
            final TranslatedTopicData translatedTopic, final Map<String, ZanataTranslation> translationDetails,
            final Map<String, String> translations) throws Exception {
        boolean changed = false;

        // Parse the Content Spec stored in the XML Field
        final DBProviderFactory providerFactory = DBProviderFactory.create(entityManager);
        final ContentSpecWrapper contentSpecWrapper = providerFactory.getProvider(ContentSpecProvider.class).getContentSpec
                (contentSpecId, contentSpecRevision);

        // Replace the translated strings, and save the result into the TranslatedTopicData entity
        if (contentSpecWrapper != null) {
            final List<StringToCSNodeCollection> stringToNodeCollections = ContentSpecUtilities.getTranslatableStrings
            (contentSpecWrapper, false);

            // Create a temporary collection that we can freely remove items from
            final List<StringToCSNodeCollection> tempStringToNodeCollection = new ArrayList<StringToCSNodeCollection>();
            for (final StringToCSNodeCollection stringToNodeCollection : stringToNodeCollections) {
                tempStringToNodeCollection.add(stringToNodeCollection);
            }

            // Remove or update any existing translation strings
            if (translatedTopic.getTranslatedTopicStrings() != null) {
                final List<TranslatedTopicString> removeTranslationStringList = new ArrayList<TranslatedTopicString>();

                for (final TranslatedTopicString existingString : translatedTopic.getTranslatedTopicStrings()) {
                    boolean found = false;

                    for (final StringToCSNodeCollection original : tempStringToNodeCollection) {
                        final String originalText = original.getTranslationString();

                        if (existingString.getOriginalString().equals(originalText)) {
                            found = true;
                            tempStringToNodeCollection.remove(original);

                            final ZanataTranslation translation = translationDetails.get(originalText);

                            // Check the translations still match
                            if (!translation.getTranslation().equals(existingString.getTranslatedString())) {
                                changed = true;

                                existingString.setTranslatedString(translation.getTranslation());
                            }

                            // Check if the string is still fuzzy
                            if (translation.isFuzzy() != existingString.getFuzzyTranslation()) {
                                changed = true;

                                existingString.setFuzzyTranslation(translation.isFuzzy());
                            }
                        }
                    }

                    // If the original String no longer exists then remove it (this shouldn't happen)
                    if (!found) {
                        removeTranslationStringList.add(existingString);
                    }
                }

                // Remove any translation strings that no longer exist
                for (final TranslatedTopicString translatedTopicString : removeTranslationStringList) {
                    translatedTopic.getTranslatedTopicStrings().remove(translatedTopicString);
                    entityManager.remove(translatedTopicString);
                }
            }

            // save the strings to TranslatedTopicString entities
            for (final StringToCSNodeCollection original : tempStringToNodeCollection) {
                final String originalText = original.getTranslationString();
                final ZanataTranslation translation = translationDetails.get(originalText);

                if (translation != null) {
                    final TranslatedTopicString translatedTopicString = new TranslatedTopicString();
                    translatedTopicString.setOriginalString(originalText);
                    translatedTopicString.setTranslatedString(translation.getTranslation());
                    translatedTopicString.setFuzzyTranslation(translation.isFuzzy());
                    translatedTopicString.setTranslatedTopicData(translatedTopic);

                    translatedTopic.getTranslatedTopicStrings().add(translatedTopicString);
                }
            }

            ContentSpecUtilities.replaceTranslatedStrings(contentSpecWrapper, translations);
        }

        return changed;
    }*/
}
