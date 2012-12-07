package org.jboss.pressgang.ccms.restserver.utils;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsArray.array;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.jboss.pressgang.ccms.docbook.messaging.TopicRendererType;
import org.jboss.pressgang.ccms.model.BlobConstants;
import org.jboss.pressgang.ccms.model.BugzillaBug;
import org.jboss.pressgang.ccms.model.Category;
import org.jboss.pressgang.ccms.model.StringConstants;
import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.model.TagToCategory;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.TopicToBugzillaBug;
import org.jboss.pressgang.ccms.model.TopicToTag;
import org.jboss.pressgang.ccms.model.TopicToTopic;
import org.jboss.pressgang.ccms.model.TopicToTopicSourceUrl;
import org.jboss.pressgang.ccms.model.TranslatedTopic;
import org.jboss.pressgang.ccms.model.TranslatedTopicData;
import org.jboss.pressgang.ccms.model.sort.CategoryNameComparator;
import org.jboss.pressgang.ccms.model.sort.TagNameComparator;
import org.jboss.pressgang.ccms.restserver.utils.topicrenderer.TopicQueueRenderer;
import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;
import org.jboss.pressgang.ccms.utils.common.DocBookUtilities;
import org.jboss.pressgang.ccms.utils.common.StringUtilities;
import org.jboss.pressgang.ccms.utils.common.XMLUtilities;
import org.jboss.pressgang.ccms.utils.common.XMLValidator;
import org.jboss.pressgang.ccms.utils.concurrency.WorkQueue;
import org.jboss.pressgang.ccms.utils.constants.CommonConstants;
import org.jboss.pressgang.ccms.utils.structures.NameIDSortMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.j2bugzilla.base.BugzillaConnector;
import com.j2bugzilla.base.ECSBug;
import com.j2bugzilla.rpc.BugSearch;
import com.j2bugzilla.rpc.GetBug;
import com.j2bugzilla.rpc.LogIn;

public class TopicUtilities extends org.jboss.pressgang.ccms.model.utils.TopicUtilities {
    private static final Logger log = LoggerFactory.getLogger(TopicUtilities.class);

    /**
     * Returns the headers for the CSV columns
     * 
     * @param entityManager
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String getCSVHeaderRow(final EntityManager entityManager) {
        final List<Category> categories = entityManager.createQuery(Category.SELECT_ALL_QUERY).getResultList();
        Collections.sort(categories, new CategoryNameComparator());

        final StringBuilder topicColumns = new StringBuilder(StringUtilities.buildString(new String[] { "Topic ID",
                "Topic Title", "Topic Text", "Topic URL" }, ","));

        for (final Category category : categories)
            topicColumns.append("," + category.getCategoryName());

        return topicColumns.toString();
    }

    /**
     * Request that a Topic's XML be rendered into HTML. This will be preformed by an external service.
     * 
     * @param topic The topic to be rendered.
     */
    public static void render(final Topic topic) {
        final String renderTopics = System.getProperty(Constants.ENABLE_RENDERING_PROPERTY);
        if (renderTopics != null && !Boolean.parseBoolean(renderTopics))
            return;

        if (topic.isTaggedWith(Constants.CONTENT_SPEC_TAG_ID))
            return;

        /*
         * Send a message to the queue that this topic, and all those that have inbound relationships, need to be rerendered. We
         * use the TopicQueueRenderer to send the topic id once the transaction has finished.
         */
        try {
            final TransactionManager transactionManager = JNDIUtilities.lookupTransactionManager();
            final Transaction transaction = transactionManager.getTransaction();
            WorkQueue.getInstance().execute(
                    TopicQueueRenderer.createNewInstance(topic.getTopicId(), TopicRendererType.TOPIC, transaction));
        } catch (final Exception ex) {
            log.error("An error occurred trying to render a topic, probably a STOMP messaging problem", ex);
        }

    }

    

    /**
     * Process a Topics XML and sync the topic title with the XML Title. Also reformat the XML using the formatting String
     * Constant rules.
     * 
     * @param entityManager An open EntityManager instance to lookup formatting constants.
     * @param topic The Topic to process the XML for.
     */
    private static void processXML(final EntityManager entityManager, final Topic topic) {
        if (topic.isTaggedWith(Constants.CONTENT_SPEC_TAG_ID))
            return;

        Document doc = null;
        try {
            doc = XMLUtilities.convertStringToDocument(topic.getTopicXML());
        } catch (SAXException ex) {
            // Do nothing with this as it's handled later by the validator
        } catch (Exception ex) {
            log.warn("An Error occurred transforming a XML String to a DOM Document", ex);
        }
        if (doc != null) {
            DocBookUtilities.setSectionTitle(topic.getTopicTitle(), doc);

            // Get the XML elements that require special formatting/processing
            final StringConstants xmlElementsProperties = entityManager.find(StringConstants.class,
                    CommonConstants.XML_ELEMENTS_STRING_CONSTANT_ID);

            // Load the String Constants as Properties
            final Properties prop = new Properties();
            try {
                prop.load(new StringReader(xmlElementsProperties.getConstantValue()));
            } catch (IOException ex) {
                log.error("The XML Elements Properties file couldn't be loaded as a property file", ex);
            }

            // Find the XML elements that need formating for different display rules.
            final String verbatimElementsString = prop.getProperty(CommonConstants.VERBATIM_XML_ELEMENTS_PROPERTY_KEY);
            final String inlineElementsString = prop.getProperty(CommonConstants.INLINE_XML_ELEMENTS_PROPERTY_KEY);
            final String contentsInlineElementsString = prop
                    .getProperty(CommonConstants.CONTENTS_INLINE_XML_ELEMENTS_PROPERTY_KEY);

            final ArrayList<String> verbatimElements = verbatimElementsString == null ? new ArrayList<String>()
                    : CollectionUtilities.toArrayList(verbatimElementsString.split("[\\s]*,[\\s]*"));

            final ArrayList<String> inlineElements = inlineElementsString == null ? new ArrayList<String>()
                    : CollectionUtilities.toArrayList(inlineElementsString.split("[\\s]*,[\\s]*"));

            final ArrayList<String> contentsInlineElements = contentsInlineElementsString == null ? new ArrayList<String>()
                    : CollectionUtilities.toArrayList(contentsInlineElementsString.split("[\\s]*,[\\s]*"));

            // Convert the document to a String applying the XML Formatting property rules
            topic.setTopicXML(XMLUtilities.convertNodeToString(doc, verbatimElements, inlineElements, contentsInlineElements,
                    true));
        }
    }

    /**
     * Syncs the XML with the topic details, such as setting the topic title as the title of the XML.
     * 
     * @param entityManager An open EntityManager instance to lookup formatting constants.
     * @param topic The Topic to sync the XML for.
     */
    public static void syncXML(final EntityManager entityManager, final Topic topic) {
        /* remove line breaks from the title */
        if (topic.getTopicTitle() != null)
            topic.setTopicTitle(topic.getTopicTitle().replaceAll("\n", " ").trim());
        processXML(entityManager, topic);
    }

    /**
     * Get an Alphabetical Sorted list of Categories to Tags for a Topic. The sort is performed based on the Category Sort value
     * first and then Name.
     * 
     * @param topic The topic to get the tags from.
     * @return A Sorted Tree map of categories to tags.
     */
    private static TreeMap<NameIDSortMap, ArrayList<Tag>> getCategoriesMappedToTags(final Topic topic) {
        final TreeMap<NameIDSortMap, ArrayList<Tag>> tags = new TreeMap<NameIDSortMap, ArrayList<Tag>>();

        final Set<TopicToTag> topicToTags = topic.getTopicToTags();
        for (final TopicToTag topicToTag : topicToTags) {
            final Tag tag = topicToTag.getTag();
            final Set<TagToCategory> tagToCategories = tag.getTagToCategories();

            if (tagToCategories.size() == 0) {
                final NameIDSortMap categoryDetails = new NameIDSortMap("Uncatagorised", -1, 0);

                if (!tags.containsKey(categoryDetails))
                    tags.put(categoryDetails, new ArrayList<Tag>());

                tags.get(categoryDetails).add(tag);
            } else {
                for (final TagToCategory category : tagToCategories) {
                    final NameIDSortMap categoryDetails = new NameIDSortMap(category.getCategory().getCategoryName(), category
                            .getCategory().getCategoryId(), category.getCategory().getCategorySort() == null ? 0 : category
                            .getCategory().getCategorySort());

                    if (!tags.containsKey(categoryDetails))
                        tags.put(categoryDetails, new ArrayList<Tag>());

                    tags.get(categoryDetails).add(tag);
                }
            }
        }

        return tags;
    }

    /**
     * Get a Comma Separated list of all the tags attached to a topic, grouped by category.
     * 
     * @param topic The topic to get the tags from.
     * @return A CSV tag list for the topic.
     */
    public static String getCommaSeparatedTagList(final Topic topic) {
        final TreeMap<NameIDSortMap, ArrayList<Tag>> tags = getCategoriesMappedToTags(topic);

        String tagsList = "";
        for (final NameIDSortMap key : tags.keySet()) {
            // sort alphabetically
            Collections.sort(tags.get(key), new TagNameComparator());

            if (tagsList.length() != 0)
                tagsList += " ";

            tagsList += key.getName() + ": ";

            String thisTagList = "";

            for (final Tag tag : tags.get(key)) {
                if (thisTagList.length() != 0)
                    thisTagList += ", ";

                thisTagList += tag.getTagName();
            }

            tagsList += thisTagList + " ";
        }

        return tagsList;
    }

    /**
     * Get a CSV Row representation of a topic.
     * 
     * @param entityManager An open EntityManager instance to lookup data.
     * @param topic The topic to transform into the CSV row.
     * @return A Comma Separated Value list containing information about the topic.
     */
    @SuppressWarnings("unchecked")
    public static String getCSVRow(final EntityManager entityManager, final Topic topic) {
        // get a space separated list of source URLs
        String sourceUrls = "";
        for (final TopicToTopicSourceUrl url : topic.getTopicToTopicSourceUrls()) {
            if (sourceUrls.length() != 0)
                sourceUrls += " ";
            sourceUrls += url.getTopicSourceUrl().getSourceUrl();
        }

        String topicColumns = StringUtilities.cleanTextForCSV(topic.getTopicId().toString()) + ","
                + StringUtilities.cleanTextForCSV(topic.getTopicTitle()) + ","
                + StringUtilities.cleanTextForCSV(topic.getTopicText()) + "," + StringUtilities.cleanTextForCSV(sourceUrls);

        final List<Category> categories = entityManager.createQuery(Category.SELECT_ALL_QUERY).getResultList();
        Collections.sort(categories, new CategoryNameComparator());

        for (final Category category : categories) {
            final List<TopicToTag> matchingTags = filter(
                    having(on(TopicToTag.class).getTag().getTagToCategories().toArray(),
                            array(org.hamcrest.Matchers.hasProperty("category", equalTo(category)))), topic.getTopicToTags());

            String tags = "";
            for (final TopicToTag topicToTag : matchingTags) {
                final Tag tag = topicToTag.getTag();
                if (tags.length() != 0)
                    tags += ", ";
                tags += tag.getTagId() + ": " + tag.getTagName();
            }

            topicColumns += "," + StringUtilities.cleanTextForCSV(tags);
        }

        return topicColumns;
    }

    /**
     * Generates a HTML formatted and categorized list of the tags that are associated with this topic
     * 
     * @return A HTML String to display in a table
     */
    public static String getTagsList(final Topic topic, final boolean brLineBreak) {
        // define the line breaks for html and for tooltips
        final String lineBreak = brLineBreak ? "<br/>" : " \n";
        final String boldStart = brLineBreak ? "<b>" : "";
        final String boldEnd = brLineBreak ? "</b>" : "";

        final TreeMap<NameIDSortMap, ArrayList<Tag>> tags = getCategoriesMappedToTags(topic);

        String tagsList = "";
        for (final NameIDSortMap key : tags.keySet()) {
            // sort alphabetically
            Collections.sort(tags.get(key), new TagNameComparator());

            if (tagsList.length() != 0)
                tagsList += lineBreak;

            tagsList += boldStart + key.getName() + boldEnd + ": ";

            String thisTagList = "";

            for (final Tag tag : tags.get(key)) {
                if (thisTagList.length() != 0)
                    thisTagList += ", ";

                thisTagList += "<span title=\"Tag ID: " + tag.getTagId() + " &#13;Tag Description: " + tag.getTagDescription()
                        + "\">" + tag.getTagName() + "</span>";
            }

            tagsList += thisTagList + " ";
        }

        return tagsList;
    }

    public static String getRelatedTopicsList(final Topic topic) {
        String topicList = "";
        for (final TopicToTopic topicToTopic : topic.getParentTopicToTopics()) {
            final Topic relatedTopic = topicToTopic.getRelatedTopic();

            if (topicList.length() != 0)
                topicList += ", ";

            topicList += "<span title=\"" + relatedTopic.getTopicTitle() + " \n" + getCommaSeparatedTagList(relatedTopic)
                    + "\">" + topicToTopic.getRelatedTopic().getTopicId() + "</span>";
        }
        return topicList;
    }

    /**
     * Validate the XML, and save any errors
     * 
     * @param entityManager The EntityManager
     * @param blobConstantId The BlobConstants ID that is the DTD to validate against
     */
    public static void validateXML(final EntityManager entityManager, final Topic topic, final int blobConstantId) {
        if (topic.isTaggedWith(Constants.CONTENT_SPEC_TAG_ID))
            return;

        if (entityManager == null)
            throw new IllegalArgumentException("entityManager cannot be null");
        if (blobConstantId < 0)
            throw new IllegalArgumentException("blobConstantId must be positive");

        final BlobConstants dtd = entityManager.find(BlobConstants.class, blobConstantId);

        if (dtd == null)
            throw new IllegalArgumentException("blobConstantId must be a valid BlobConstants entity id");

        final XMLValidator validator = new XMLValidator();
        if (validator.validateTopicXML(topic.getTopicXML(), dtd.getConstantName(), dtd.getConstantValue()) == null) {
            topic.setTopicXMLErrors(validator.getErrorText());
        } else {
            topic.setTopicXMLErrors(null);
        }
    }

    public static TranslatedTopic createTranslatedTopic(final EntityManager entityManager, final Integer topicId,
            final Number revision) {
        /*
         * Search to see if a translated topic already exists for the current revision
         */
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<TranslatedTopic> criteriaQuery = criteriaBuilder.createQuery(TranslatedTopic.class);
        final Root<TranslatedTopic> root = criteriaQuery.from(TranslatedTopic.class);
        final Predicate whereTopicId = criteriaBuilder.equal(root.get("topicId"), topicId);
        final Predicate whereTopicRevision = criteriaBuilder.equal(root.get("topicRevision"), revision);
        criteriaQuery.where(criteriaBuilder.and(whereTopicId, whereTopicRevision));

        final TypedQuery<TranslatedTopic> query = entityManager.createQuery(criteriaQuery);
        final List<TranslatedTopic> result = query.getResultList();

        if (result.size() == 0) {
            final TranslatedTopic translatedTopic = new TranslatedTopic();
            translatedTopic.setTopicId(topicId);
            translatedTopic.setTopicRevision((Integer) revision);

            /*
             * Create a TranslationTopicData entity that represents the original locale
             */
            final Topic revisionTopic = entityManager.find(Topic.class, topicId);
            if (revisionTopic != null) {
                final String locale = revisionTopic.getTopicLocale();

                final TranslatedTopicData translatedTopicData = new TranslatedTopicData();
                translatedTopicData.setTranslatedTopic(translatedTopic);
                translatedTopicData.setTranslatedXml(revisionTopic.getTopicXML());
                translatedTopicData.setTranslationLocale(locale == null ? CommonConstants.DEFAULT_LOCALE : locale);
                translatedTopicData.setTranslationPercentage(100);
                translatedTopic.getTranslatedTopicDatas().add(translatedTopicData);
            }

            return translatedTopic;
        }

        return null;
    }

    /**
     * Update the Bugzilla Bugs for a Topic by connecting and then searching for bugs from bugzilla.
     * 
     * @param entityManager An open EntityManager instance.
     * @param topic The topic to be updated.
     */
    public static void updateBugzillaBugs(final EntityManager entityManager, final Topic topic) {
        final String username = System.getProperty(CommonConstants.BUGZILLA_USERNAME_PROPERTY);
        final String password = System.getProperty(CommonConstants.BUGZILLA_PASSWORD_PROPERTY);
        final String url = System.getProperty(CommonConstants.BUGZILLA_URL_PROPERTY);

        // return if the system properties have not been setup properly
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty() || url == null
                || url.trim().isEmpty())
            return;

        final LogIn login = new LogIn(username, password);
        final BugzillaConnector connector = new BugzillaConnector();
        final BugSearch<ECSBug> search = new BugSearch<ECSBug>(ECSBug.class);

        // Create a query that will return all bugs whose Build ID matches what is pre-populated by PressGang
        search.addQueryParam("f1", "cf_build_id");
        search.addQueryParam("o1", "regexp");
        search.addQueryParam("v1", topic.getTopicId() + CommonConstants.BUGZILLA_BUILD_ID_RE);
        search.addQueryParam("query_format", "advanced");

        try {
            connector.connectTo("https://" + url + "/xmlrpc.cgi");
            connector.executeMethod(login);
            connector.executeMethod(search);

            /* clear the mappings */
            while (topic.getTopicToBugzillaBugs().size() != 0) {
                final TopicToBugzillaBug map = topic.getTopicToBugzillaBugs().iterator().next();
                map.getBugzillaBug().getTopicToBugzillaBugs().remove(map);
                topic.getTopicToBugzillaBugs().remove(map);
            }

            /* For each bug, get the bug details and comments */
            for (ECSBug bug : search.getSearchResults()) {
                int id = bug.getID();

                final GetBug<ECSBug> getBug = new GetBug<ECSBug>(ECSBug.class, id);
                connector.executeMethod(getBug);
                final ECSBug bugDetails = getBug.getBug();

                final BugzillaBug bugzillaBug = new BugzillaBug();
                bugzillaBug.setBugzillaBugBugzillaId(bugDetails.getID());
                bugzillaBug.setBugzillaBugOpen(bugDetails.getIsOpen());
                bugzillaBug.setBugzillaBugSummary(bugDetails.getSummary());

                entityManager.persist(bugzillaBug);

                final TopicToBugzillaBug mapping = new TopicToBugzillaBug();
                mapping.setBugzillaBug(bugzillaBug);
                mapping.setTopic(topic);

                topic.getTopicToBugzillaBugs().add(mapping);
            }

        } catch (final Exception ex) {
            log.error("Probably an error searching Bugzilla", ex);
        }
    }
    
    public static String getXMLDoctypeString(final Topic topic) {
        if (topic.getXmlDoctype() == null) {
            return "None";
        } else if (topic.getXmlDoctype() == org.jboss.pressgang.ccms.model.constants.Constants.DOCBOOK_45) {
            return "Docbook 4.5";
        } else if (topic.getXmlDoctype() == org.jboss.pressgang.ccms.model.constants.Constants.DOCBOOK_50) {
            return "Docbook 5.0";
        }
        
        return "Unknown";
    }
}
