package org.jboss.pressgang.ccms.server.async.process.task;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.pressgang.ccms.contentspec.ContentSpec;
import org.jboss.pressgang.ccms.contentspec.ITopicNode;
import org.jboss.pressgang.ccms.contentspec.structures.StringToCSNodeCollection;
import org.jboss.pressgang.ccms.contentspec.utils.CSTransformer;
import org.jboss.pressgang.ccms.contentspec.utils.EntityUtilities;
import org.jboss.pressgang.ccms.contentspec.utils.TranslationUtilities;
import org.jboss.pressgang.ccms.provider.ContentSpecProvider;
import org.jboss.pressgang.ccms.provider.DataProviderFactory;
import org.jboss.pressgang.ccms.provider.RESTProviderFactory;
import org.jboss.pressgang.ccms.provider.RESTTopicProvider;
import org.jboss.pressgang.ccms.provider.TopicProvider;
import org.jboss.pressgang.ccms.provider.TranslatedContentSpecProvider;
import org.jboss.pressgang.ccms.provider.TranslatedTopicProvider;
import org.jboss.pressgang.ccms.server.utils.ProcessUtilities;
import org.jboss.pressgang.ccms.utils.common.DocBookUtilities;
import org.jboss.pressgang.ccms.utils.common.HashUtilities;
import org.jboss.pressgang.ccms.utils.common.XMLUtilities;
import org.jboss.pressgang.ccms.utils.structures.Pair;
import org.jboss.pressgang.ccms.utils.structures.StringToNodeCollection;
import org.jboss.pressgang.ccms.wrapper.ContentSpecWrapper;
import org.jboss.pressgang.ccms.wrapper.TopicWrapper;
import org.jboss.pressgang.ccms.wrapper.TranslatedCSNodeWrapper;
import org.jboss.pressgang.ccms.wrapper.TranslatedContentSpecWrapper;
import org.jboss.pressgang.ccms.wrapper.TranslatedTopicWrapper;
import org.jboss.pressgang.ccms.zanata.NotModifiedException;
import org.jboss.pressgang.ccms.zanata.ZanataDetails;
import org.jboss.pressgang.ccms.zanata.ZanataInterface;
import org.jboss.resteasy.spi.UnauthorizedException;
import org.w3c.dom.Document;
import org.w3c.dom.Entity;
import org.zanata.common.ContentType;
import org.zanata.common.LocaleId;
import org.zanata.common.ResourceType;
import org.zanata.rest.dto.resource.Resource;
import org.zanata.rest.dto.resource.TextFlow;

public class ZanataPushTask extends ProcessRESTTask<Boolean> {
    private static final String DEFAULT_CONDITION = "default";
    private final Integer contentSpecId;
    private final boolean contentSpecOnly;
    private final String restServerUrl;
    private final ZanataDetails zanataDetails;

    public ZanataPushTask(final String restServerUrl, final Integer contentSpecId, final boolean contentSpecOnly,
            final ZanataDetails zanataDetails) {
        this.restServerUrl = restServerUrl;
        this.contentSpecId = contentSpecId;
        this.contentSpecOnly = contentSpecOnly;
        this.zanataDetails = zanataDetails;
    }

    @Override
    public void execute() {
        final RESTProviderFactory providerFactory = RESTProviderFactory.create(restServerUrl);
        // Set topics to expand their translations by default
        providerFactory.getProvider(RESTTopicProvider.class).setExpandTranslations(true);
        final ContentSpecProvider contentSpecProvider = providerFactory.getProvider(ContentSpecProvider.class);
        final ContentSpecWrapper contentSpecEntity = contentSpecProvider.getContentSpec(contentSpecId);

        // Log some basic details
        logDetails();

        // Make sure the Zanata server isn't down
        if (!ProcessUtilities.validateServerExists(zanataDetails.getServer())) {
            error("Unable to connect to the Zanata Server. Please make sure that the server is online and try again.");
            return;
        }

        // Make sure the entity was found
        if (contentSpecEntity == null) {
            error("No data was found for the specified ID!");
            return;
        }

        // Check that the content spec isn't a failed one
        if (contentSpecEntity.getFailed() != null) {
            error("The Content Specification has validation errors, please fix any errors and try again.");
            return;
        }

        // Transform the content spec
        final ContentSpec contentSpec = CSTransformer.transform(contentSpecEntity, providerFactory, false);

        // Initialise the topic wrappers in the spec topics
        initialiseSpecTopics(providerFactory, contentSpec);

        // Push the topics to Zanata
        boolean successful = pushToZanata(providerFactory, contentSpec, contentSpecEntity);
        setResult(successful);
        setSuccessful(successful);
    }

    protected void logDetails() {
        getLogger().info("Connecting to " + zanataDetails.getServer() + " using project \"" + zanataDetails.getProject() + "\", " +
                "version \"" + zanataDetails.getVersion() + "\"");
    }

    protected void error(final String message) {
        getLogger().error(message);
        setSuccessful(false);
    }

    protected void initialiseSpecTopics(final DataProviderFactory providerFactory, final ContentSpec contentSpec) {
        final TopicProvider topicProvider = providerFactory.getProvider(TopicProvider.class);
        // Download all the topics first
        downloadAllTopics(providerFactory, contentSpec);

        final List<ITopicNode> topicNodes = contentSpec.getAllTopicNodes();
        for (final ITopicNode topicNode : topicNodes) {
            if (topicNode.getDBId() != null && topicNode.getDBId() > 0 && topicNode.getRevision() == null) {
                topicNode.setTopic(topicProvider.getTopic(topicNode.getDBId()));
            } else if (topicNode.getDBId() != null && topicNode.getDBId() > 0 && topicNode.getRevision() != null) {
                topicNode.setTopic(topicProvider.getTopic(topicNode.getDBId(), topicNode.getRevision()));
            }
        }
    }

    /**
     * Pushes a content spec and its topics to zanata.
     *
     * @param providerFactory
     * @param contentSpec
     * @param contentSpecEntity
     * @return True if the push was successful otherwise false.
     */
    protected boolean pushToZanata(final DataProviderFactory providerFactory, final ContentSpec contentSpec,
            final ContentSpecWrapper contentSpecEntity) {
        ZanataInterface zanataInterface;
        try {
            zanataInterface = new ZanataInterface(0.2, zanataDetails);
        } catch (UnauthorizedException e) {
            getLogger().error(e.getMessage());
            return false;
        }
        final List<Entity> entities = XMLUtilities.parseEntitiesFromString(contentSpec.getEntities());
        final Map<TopicWrapper, ITopicNode> topicToTopicNode = new HashMap<TopicWrapper, ITopicNode>();
        boolean error = false;

        // Convert all the topics to DOM Documents first so we know if any are invalid
        final Map<Pair<Integer, Integer>, TopicWrapper> topics = new HashMap<Pair<Integer, Integer>, TopicWrapper>();
        final List<ITopicNode> topicNodes = contentSpec.getAllTopicNodes();
        for (final ITopicNode topicNode : topicNodes) {
            final TopicWrapper topic = (TopicWrapper) topicNode.getTopic();
            final Pair<Integer, Integer> topicId = new Pair<Integer, Integer>(topic.getId(), topic.getRevision());

            // Only process the topic if it hasn't already been added, since the same topic can exist twice
            if (!topics.containsKey(topicId)) {
                topics.put(topicId, topic);

                // Convert the XML String into a DOM object.
                Document doc = null;
                try {
                    doc = XMLUtilities.convertStringToDocument(topic.getXml());
                } catch (Exception e) {
                    // Do Nothing as we handle the error below.
                }

                if (doc == null) {
                    getLogger().error("Topic ID {}, Revision {} does not have valid XML", topic.getId(), topic.getRevision());
                    error = true;
                } else {
                    topicNode.setXMLDocument(doc);
                    topicToTopicNode.put(topic, topicNode);
                }
            }
        }

        // Return if creating the documents failed
        if (error) {
            return false;
        }

        final float total = contentSpecOnly ? 1 : (topics.size() + 1);
        float current = 0;
        final int showPercent = 5;
        int lastPercent = 0;

        getLogger().info("Pushing {} topics to zanata.", ((int) total));
        getLogger().info("Starting to push content to zanata...");

        // Upload the content specification to zanata first so we can reference the nodes when pushing topics
        final TranslatedContentSpecWrapper translatedContentSpec = pushContentSpecToZanata(providerFactory, contentSpecEntity,
                zanataInterface, entities);
        if (translatedContentSpec == null) {
            error = true;
        } else if (!contentSpecOnly) {
            // Loop through each topic and upload it to zanata
            for (final Map.Entry<TopicWrapper, ITopicNode> topicEntry : topicToTopicNode.entrySet()) {
                ++current;
                final int percent = Math.round(current / total * 100);
                if (percent - lastPercent >= showPercent) {
                    lastPercent = percent;
                    getLogger().info("\tPushing topics to zanata {}% Done", percent);
                }

                final ITopicNode topicNode = topicEntry.getValue();

                // Find the matching translated CSNode and if one can't be found then produce an error.
                final TranslatedCSNodeWrapper translatedCSNode = findTopicTranslatedCSNode(translatedContentSpec, topicNode);
                if (translatedCSNode == null) {
                    final TopicWrapper topic = (TopicWrapper) topicNode.getTopic();
                    getLogger().error("\tTopic ID {}, Revision {} failed to be created in Zanata.", topic.getId(), topic.getRevision());
                    error = true;
                } else {
                    if (!pushTopicToZanata(providerFactory, contentSpec, topicNode, translatedCSNode, zanataInterface, entities)) {
                        error = true;
                    }
                }
            }
        }

        if (error) {
            getLogger().error("Pushing content to zanata failed.");
        } else {
            getLogger().info("Content successfully pushed to Zanata for translation.");
        }

        return !error;
    }

    protected TranslatedCSNodeWrapper findTopicTranslatedCSNode(final TranslatedContentSpecWrapper translatedContentSpec,
            final ITopicNode topicNode) {
        final List<TranslatedCSNodeWrapper> translatedCSNodes = translatedContentSpec.getTranslatedNodes().getItems();
        for (final TranslatedCSNodeWrapper translatedCSNode : translatedCSNodes) {
            if (topicNode.getUniqueId() != null && topicNode.getUniqueId().equals(translatedCSNode.getNodeId().toString())) {
                return translatedCSNode;
            }
        }

        return null;
    }

    /**
     * @param providerFactory
     * @param contentSpec
     * @param topicNode
     * @param zanataInterface
     * @param entities
     * @return True if the topic was pushed successful otherwise false.
     */
    protected boolean pushTopicToZanata(final DataProviderFactory providerFactory, final ContentSpec contentSpec, final ITopicNode topicNode,
            final TranslatedCSNodeWrapper translatedCSNode, final ZanataInterface zanataInterface, final List<Entity> entities) {
        final TopicWrapper topic = (TopicWrapper) topicNode.getTopic();
        final Document doc = topicNode.getXMLDocument();
        boolean error = false;

        // Get the condition if the xml has any conditions
        boolean xmlHasConditions = !DocBookUtilities.getConditionNodes(doc).isEmpty();
        final String condition = xmlHasConditions ? topicNode.getConditionStatement(true) : null;

        // Process the conditions, if any exist, to remove any nodes that wouldn't be seen for the content spec.
        DocBookUtilities.processConditions(condition, doc, DEFAULT_CONDITION);

        // Remove any custom entities, since they cause massive translation issues.
        String customEntities = null;
        if (!entities.isEmpty()) {
            try {
                if (TranslationUtilities.resolveCustomTopicEntities(entities, doc)) {
                    customEntities = contentSpec.getEntities();
                }
            } catch (Exception e) {

            }
        }

        // Update the topics XML
        topic.setXml(XMLUtilities.convertNodeToString(doc.getDocumentElement(), true));

        // Create the zanata id based on whether a condition has been specified or not
        final boolean csNodeSpecificTopic = !isNullOrEmpty(condition) || !isNullOrEmpty(customEntities);
        final String zanataId = getTopicZanataId(topicNode, translatedCSNode, csNodeSpecificTopic);

        // Check if a translated topic already exists
        final boolean translatedTopicExists = EntityUtilities.getTranslatedTopicByTopicAndNodeId(providerFactory, topic.getId(),
                topic.getRevision(), csNodeSpecificTopic ? translatedCSNode.getId() : null, topic.getLocale()) != null;

        // Check if the zanata document already exists, if it does than the topic can be ignored.
        Resource zanataFile;
        try {
            zanataFile = zanataInterface.getZanataResource(zanataId);
        } catch (NotModifiedException e) {
            // Assigned zanataFile anything since the file exists
            zanataFile = new Resource();
        }
        if (zanataFile == null) {
            // Create the document to be created in Zanata
            final Resource resource = new Resource();

            resource.setContentType(ContentType.TextPlain);
            resource.setLang(LocaleId.fromJavaName(topic.getLocale()));
            resource.setName(zanataId);
            resource.setRevision(1);
            resource.setType(ResourceType.FILE);

            // Get the translatable nodes
            final List<StringToNodeCollection> translatableStrings = DocBookUtilities.getTranslatableStringsV3(doc, false);

            // Add the translatable nodes to the zanata document
            for (final StringToNodeCollection translatableStringData : translatableStrings) {
                final String translatableString = translatableStringData.getTranslationString();
                if (!translatableString.trim().isEmpty()) {
                    final TextFlow textFlow = new TextFlow();
                    textFlow.setContents(translatableString);
                    textFlow.setLang(LocaleId.fromJavaName(topic.getLocale()));
                    textFlow.setId(HashUtilities.generateMD5(translatableString));
                    textFlow.setRevision(1);

                    resource.getTextFlows().add(textFlow);
                }
            }

            try {
                // Create the document in zanata and then in PressGang if the document was successfully created in Zanata.
                if (!zanataInterface.createFile(resource)) {
                    getLogger().error("\tTopic ID {}, Revision {} failed to be created in Zanata.", topic.getId(), topic.getRevision());
                    error = true;
                } else if (!translatedTopicExists) {
                    createPressGangTranslatedTopic(providerFactory, topic, condition, customEntities, translatedCSNode);
                }
            } catch (UnauthorizedException e) {
                getLogger().error("\tTopic ID {}, Revision {} failed to be created in Zanata due to having incorrect privileges.",
                        topic.getId(), topic.getRevision());
                error = true;
            }
        } else if (!translatedTopicExists) {
            createPressGangTranslatedTopic(providerFactory, topic, condition, customEntities, translatedCSNode);
        } else {
            getLogger().warn("\tTopic ID {}, Revision {} already exists - Skipping.", topic.getId(), topic.getRevision());
        }

        return !error;
    }

    protected boolean createPressGangTranslatedTopic(final DataProviderFactory providerFactory, final TopicWrapper topic,
            final String condition, final String customEntities, final TranslatedCSNodeWrapper translatedCSNode) {
        final TranslatedTopicProvider translatedTopicProvider = providerFactory.getProvider(TranslatedTopicProvider.class);

        // Create the Translated Topic based on if it has a condition/custom entity or not.
        final TranslatedTopicWrapper translatedTopic;
        if (condition == null && customEntities == null) {
            translatedTopic = TranslationUtilities.createTranslatedTopic(providerFactory, topic, null, null, null);
        } else {
            translatedTopic = TranslationUtilities.createTranslatedTopic(providerFactory, topic, translatedCSNode, condition,
                    customEntities);
        }

        // Save the Translated Topic
        try {
            if (translatedTopicProvider.createTranslatedTopic(translatedTopic) == null) {
                getLogger().error("\tTopic ID {}, Revision {} failed to be created in PressGang.", topic.getId(), topic.getRevision());
                return false;
            }
        } catch (Exception e) {
            getLogger().error("\tTopic ID {}, Revision {} failed to be created in PressGang.", topic.getId(), topic.getRevision());
            return false;
        }

        return true;
    }

    /**
     * Gets the Zanata ID for a topic based on whether or not the topic has any conditional text.
     *
     * @param topicNode        The topic to create the Zanata ID for.
     * @param translatedCSNode
     * @param csNodeSpecific   If the Topic the Zanata ID is being created for is specific to the translated CS Node. That is that it
     *                         either has conditions, or custom entities.
     * @return The unique Zanata ID that can be used to create a document in Zanata.
     */
    protected String getTopicZanataId(final ITopicNode topicNode, final TranslatedCSNodeWrapper translatedCSNode, boolean csNodeSpecific) {
        final TopicWrapper topic = (TopicWrapper) topicNode.getTopic();

        // Create the zanata id based on whether a condition has been specified or not
        final String zanataId;
        if (csNodeSpecific) {
            zanataId = topic.getId() + "-" + topic.getRevision() + "-" + translatedCSNode.getId();
        } else {
            zanataId = topic.getId() + "-" + topic.getRevision();
        }

        return zanataId;
    }

    /**
     * @param providerFactory
     * @param contentSpecEntity
     * @param zanataInterface
     * @param entities
     * @return
     */
    protected TranslatedContentSpecWrapper pushContentSpecToZanata(final DataProviderFactory providerFactory,
            final ContentSpecWrapper contentSpecEntity, final ZanataInterface zanataInterface, final List<Entity> entities) {
        final String zanataId = "CS" + contentSpecEntity.getId() + "-" + contentSpecEntity.getRevision();
        Resource zanataFile;
        try {
            zanataFile = zanataInterface.getZanataResource(zanataId);
        } catch (NotModifiedException e) {
            // Assigned zanataFile anything since the file exists
            zanataFile = new Resource();
        }
        TranslatedContentSpecWrapper translatedContentSpec = EntityUtilities.getTranslatedContentSpecById(providerFactory,
                contentSpecEntity.getId(), contentSpecEntity.getRevision());

        // Resolve any custom entities that might exist
        TranslationUtilities.resolveCustomContentSpecEntities(entities, contentSpecEntity);

        if (zanataFile == null) {
            final Resource resource = new Resource();

            resource.setContentType(ContentType.TextPlain);
            resource.setLang(LocaleId.fromJavaName(contentSpecEntity.getLocale()));
            resource.setName(zanataId);
            resource.setRevision(1);
            resource.setType(ResourceType.FILE);

            final List<StringToCSNodeCollection> translatableStrings = TranslationUtilities.getTranslatableStrings(contentSpecEntity,
                    false);

            for (final StringToCSNodeCollection translatableStringData : translatableStrings) {
                final String translatableString = translatableStringData.getTranslationString();
                if (!translatableString.trim().isEmpty()) {
                    final TextFlow textFlow = new TextFlow();
                    textFlow.setContents(translatableString);
                    textFlow.setLang(LocaleId.fromJavaName(contentSpecEntity.getLocale()));
                    textFlow.setId(HashUtilities.generateMD5(translatableString));
                    textFlow.setRevision(1);

                    resource.getTextFlows().add(textFlow);
                }
            }

            try {
                // Create the document in Zanata
                if (!zanataInterface.createFile(resource)) {
                    getLogger().error("\tContent Spec ID {}, Revision {} failed to be created in Zanata.", contentSpecEntity.getId(),
                            contentSpecEntity.getRevision());
                    return null;
                } else if (translatedContentSpec == null) {
                    return createPressGangTranslatedContentSpec(providerFactory, contentSpecEntity);
                }
            } catch (UnauthorizedException e) {
                getLogger().error("\tContent Spec ID {}, Revision {} failed to be created in Zanata due to having incorrect privileges.",
                        contentSpecEntity.getId(), contentSpecEntity.getRevision());
                return null;
            }
        } else if (translatedContentSpec == null) {
            return createPressGangTranslatedContentSpec(providerFactory, contentSpecEntity);
        } else {
            getLogger().warn("\tContent Spec ID {}, Revision {} already exists - Skipping.", contentSpecEntity.getId(),
                    contentSpecEntity.getRevision());
        }

        return translatedContentSpec;
    }

    protected TranslatedContentSpecWrapper createPressGangTranslatedContentSpec(final DataProviderFactory providerFactory,
            final ContentSpecWrapper contentSpecEntity) {
        final TranslatedContentSpecProvider translatedContentSpecProvider = providerFactory.getProvider(
                TranslatedContentSpecProvider.class);

        // Create the Translated Content Spec and it's nodes
        final TranslatedContentSpecWrapper newTranslatedContentSpec = TranslationUtilities.createTranslatedContentSpec(providerFactory,
                contentSpecEntity);
        try {
            // Save the translated content spec
            final TranslatedContentSpecWrapper translatedContentSpec = translatedContentSpecProvider.createTranslatedContentSpec(
                    newTranslatedContentSpec);
            if (translatedContentSpec == null) {
                getLogger().error("\tContent Spec ID {}, Revision {} failed to be created in PressGang.", contentSpecEntity.getId(),
                        contentSpecEntity.getRevision());
                return null;
            } else {
                return translatedContentSpec;
            }
        } catch (Exception e) {
            getLogger().error("\tContent Spec ID {}, Revision {} failed to be created in PressGang.", contentSpecEntity.getId(),
                    contentSpecEntity.getRevision());
            return null;
        }
    }
}
