package org.jboss.pressgang.ccms.server.rest.v1;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.TranslatedTopic;
import org.jboss.pressgang.ccms.model.TranslatedTopicData;
import org.jboss.pressgang.ccms.model.TranslatedTopicString;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTopicSourceUrlCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTranslatedTopicCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTranslatedTopicStringCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTranslatedTopicCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTranslatedTopicStringCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTAssignedPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTranslatedTopicStringV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTranslatedTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.enums.RESTXMLDoctype;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.pressgang.ccms.utils.common.DocBookUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class TranslatedTopicV1Factory extends RESTDataObjectFactory<RESTTranslatedTopicV1, TranslatedTopicData,
        RESTTranslatedTopicCollectionV1, RESTTranslatedTopicCollectionItemV1> {
    @Inject
    protected TopicV1Factory topicFactory;
    @Inject
    protected TopicSourceUrlV1Factory topicSourceUrlFactory;
    @Inject
    protected TranslatedTopicStringV1Factory translatedTopicStringFactory;
    @Inject
    protected TagV1Factory tagFactory;
    @Inject
    protected TopicPropertyTagV1Factory topicPropertyTagFactory;

    @Override
    public RESTTranslatedTopicV1 createRESTEntityFromDBEntityInternal(final TranslatedTopicData entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTTranslatedTopicV1 retValue = new RESTTranslatedTopicV1();

        /* Set the expansion options */
        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTTranslatedTopicV1.TRANSLATEDTOPICSTRING_NAME);
        expandOptions.add(RESTTranslatedTopicV1.INCOMING_NAME);
        expandOptions.add(RESTTranslatedTopicV1.OUTGOING_NAME);
        expandOptions.add(RESTTranslatedTopicV1.ALL_LATEST_INCOMING_NAME);
        expandOptions.add(RESTTranslatedTopicV1.ALL_LATEST_OUTGOING_NAME);
        expandOptions.add(RESTTranslatedTopicV1.TAGS_NAME);
        expandOptions.add(RESTTranslatedTopicV1.SOURCE_URLS_NAME);
        expandOptions.add(RESTTranslatedTopicV1.PROPERTIES_NAME);
        expandOptions.add(RESTTranslatedTopicV1.LOG_DETAILS_NAME);

        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        // Set the simple values
        retValue.setId(entity.getTranslatedTopicDataId());
        retValue.setTranslatedTopicId(entity.getTranslatedTopic().getId());
        retValue.setTopicId(entity.getTranslatedTopic().getTopicId());
        retValue.setTopicRevision(entity.getTranslatedTopic().getTopicRevision());
        retValue.setContainsFuzzyTranslation(entity.containsFuzzyTranslation());
        retValue.setXmlDoctype(RESTXMLDoctype.getXMLDoctype(entity.getTranslatedTopic().getEnversTopic(entityManager).getXmlDoctype()));

        // Get the title from the XML or if the XML is null then use the original topics title.
        String title = DocBookUtilities.findTitle(entity.getTranslatedXml());
        if (title == null) title = entity.getTranslatedTopic().getEnversTopic(entityManager).getTopicTitle();

        // Prefix the locale to the title if its a dummy translation to show that it is missing the related translated topic
        if (entity.getId() < 0) title = "[" + entity.getTranslationLocale() + "] " + title;
        retValue.setTitle(title);

        retValue.setXml(entity.getTranslatedXml());
        retValue.setXmlErrors(entity.getTranslatedXmlErrors());
        retValue.setHtml(entity.getTranslatedXmlRendered());
        retValue.setLocale(entity.getTranslationLocale());
        retValue.setTranslationPercentage(entity.getTranslationPercentage());

        // Set the object references
        if (expandParentReferences && expand != null && expand.contains(
                RESTTranslatedTopicV1.TOPIC_NAME) && entity.getTranslatedTopic().getEnversTopic(entityManager) != null) {
            retValue.setTopic(
                    topicFactory.createRESTEntityFromDBEntity(entity.getTranslatedTopic().getEnversTopic(entityManager), baseUrl, dataType,
                            expand.get(RESTTranslatedTopicV1.TOPIC_NAME), entity.getTranslatedTopic().getTopicRevision(), true));
            retValue.getTopic().setRevision(entity.getTranslatedTopic().getTopicRevision());
        }

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTTranslatedTopicV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTDataObjectCollectionFactory.create(RESTTranslatedTopicCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // TRANSLATED STRINGS
        if (expand != null && expand.contains(RESTTranslatedTopicV1.TRANSLATEDTOPICSTRING_NAME)) {
            retValue.setTranslatedTopicStrings_OTM(
                    RESTDataObjectCollectionFactory.create(RESTTranslatedTopicStringCollectionV1.class, translatedTopicStringFactory,
                            entity.getTranslatedTopicDataStringsArray(), RESTTranslatedTopicV1.TRANSLATEDTOPICSTRING_NAME, dataType, expand,
                            baseUrl, false, entityManager));
        }

        // TAGS
        if (expand != null && expand.contains(RESTTranslatedTopicV1.TAGS_NAME)) {
            retValue.setTags(RESTDataObjectCollectionFactory.create(RESTTagCollectionV1.class, tagFactory,
                    entity.getTranslatedTopic().getEnversTopic(entityManager).getTags(), RESTv1Constants.TAGS_EXPANSION_NAME, dataType,
                    expand, baseUrl, entityManager));
        }

        // OUTGOING RELATIONSHIPS
        if (expand != null && expand.contains(RESTTranslatedTopicV1.OUTGOING_NAME)) {
            retValue.setOutgoingTranslatedRelationships(RESTDataObjectCollectionFactory.create(RESTTranslatedTopicCollectionV1.class, this,
                    entity.getOutgoingRelatedTranslatedTopicData(entityManager), RESTTranslatedTopicV1.OUTGOING_NAME, dataType, expand,
                    baseUrl, true, entityManager));
        }

        // INCOMING RELATIONSHIPS
        if (expand != null && expand.contains(RESTTranslatedTopicV1.INCOMING_NAME)) {
            retValue.setIncomingTranslatedRelationships(RESTDataObjectCollectionFactory.create(RESTTranslatedTopicCollectionV1.class, this,
                    entity.getIncomingRelatedTranslatedTopicData(entityManager), RESTTranslatedTopicV1.INCOMING_NAME, dataType, expand,
                    baseUrl, true, entityManager));
        }

        // ALL OUTGOING RELATIONSHIPS (includes dummy topics)
        if (expand != null && expand.contains(RESTTranslatedTopicV1.ALL_LATEST_OUTGOING_NAME)) {
            retValue.setOutgoingRelationships(
                    RESTDataObjectCollectionFactory.create(RESTTranslatedTopicCollectionV1.class, this,
                            entity.getOutgoingDummyFilledRelatedTranslatedTopicDatas(entityManager),
                            RESTTranslatedTopicV1.ALL_LATEST_OUTGOING_NAME, dataType, expand, baseUrl, true, entityManager));
        }

        // ALL INCOMING RELATIONSHIPS (includes dummy topics)
        if (expand != null && expand.contains(RESTTranslatedTopicV1.ALL_LATEST_INCOMING_NAME)) {
            retValue.setIncomingRelationships(
                    RESTDataObjectCollectionFactory.create(RESTTranslatedTopicCollectionV1.class, this,
                            entity.getIncomingDummyFilledRelatedTranslatedTopicDatas(entityManager),
                            RESTTranslatedTopicV1.ALL_LATEST_INCOMING_NAME, dataType, expand, baseUrl, true, entityManager));
        }

        // SOURCE URLS
        if (expand != null && expand.contains(RESTTranslatedTopicV1.SOURCE_URLS_NAME)) {
            retValue.setSourceUrls_OTM(
                    RESTDataObjectCollectionFactory.create(RESTTopicSourceUrlCollectionV1.class, topicSourceUrlFactory,
                            entity.getTranslatedTopic().getEnversTopic(entityManager).getTopicSourceUrls(),
                            RESTTranslatedTopicV1.SOURCE_URLS_NAME, dataType, expand, baseUrl, false, entityManager));
        }

        // PROPERTY TAGS
        if (expand != null && expand.contains(RESTTranslatedTopicV1.PROPERTIES_NAME)) {
            retValue.setProperties(
                    RESTDataObjectCollectionFactory.create(RESTAssignedPropertyTagCollectionV1.class, topicPropertyTagFactory,
                            entity.getTranslatedTopic().getEnversTopic(entityManager).getTopicToPropertyTagsArray(),
                            RESTTranslatedTopicV1.PROPERTIES_NAME, dataType, expand, baseUrl, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.TRANSLATEDTOPIC_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(TranslatedTopicData entity, RESTTranslatedTopicV1 dataObject) {
        /*
         * Since this factory is the rare case where two entities are combined into one. Check if it has a parent, if not then
         * check if one exists that matches otherwise create one. If one exists then update it.
         */
        TranslatedTopic translatedTopic = entity.getTranslatedTopic();
        if (translatedTopic == null) {
            try {
                final Query query = entityManager.createQuery(
                        TranslatedTopic.SELECT_ALL_QUERY + " WHERE translatedTopic.topicId=" + dataObject.getTopicId() + " AND " +
                                "translatedTopic.topicRevision=" + dataObject.getTopicRevision());
                translatedTopic = (TranslatedTopic) query.getSingleResult();
            } catch (Exception e) {
                translatedTopic = new TranslatedTopic();

                // populate the new translated topic
                if (dataObject.hasParameterSet(RESTTranslatedTopicV1.TOPICID_NAME)) translatedTopic.setTopicId(dataObject.getTopicId());
                if (dataObject.hasParameterSet(RESTTranslatedTopicV1.TOPICREVISION_NAME))
                    translatedTopic.setTopicRevision(dataObject.getTopicRevision());
            }
        }

        entity.setTranslatedTopic(translatedTopic);

        if (dataObject.hasParameterSet(RESTTranslatedTopicV1.HTML_UPDATED))
            entity.setTranslatedXmlRenderedUpdated(dataObject.getHtmlUpdated());
        if (dataObject.hasParameterSet(RESTTranslatedTopicV1.XML_ERRORS_NAME)) entity.setTranslatedXmlErrors(dataObject.getXmlErrors());
        if (dataObject.hasParameterSet(RESTTranslatedTopicV1.XML_NAME)) entity.setTranslatedXml(dataObject.getXml());
        if (dataObject.hasParameterSet(RESTTranslatedTopicV1.HTML_NAME)) entity.setTranslatedXmlRendered(dataObject.getHtml());
        if (dataObject.hasParameterSet(RESTTranslatedTopicV1.LOCALE_NAME)) entity.setTranslationLocale(dataObject.getLocale());
        if (dataObject.hasParameterSet(RESTTranslatedTopicV1.TRANSLATIONPERCENTAGE_NAME))
            entity.setTranslationPercentage(dataObject.getTranslationPercentage());

        translatedTopic.getTranslatedTopicDatas().add(entity);

        // One To Many - Add will create a child entity
        if (dataObject.hasParameterSet(
                RESTTranslatedTopicV1.TRANSLATEDTOPICSTRING_NAME) && dataObject.getTranslatedTopicStrings_OTM() != null && dataObject
                .getTranslatedTopicStrings_OTM().getItems() != null) {
            dataObject.getTranslatedTopicStrings_OTM().removeInvalidChangeItemRequests();

            // Add, Remove or Update the Translated Strings
            for (final RESTTranslatedTopicStringCollectionItemV1 restEntityItem : dataObject.getTranslatedTopicStrings_OTM().getItems()) {
                final RESTTranslatedTopicStringV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final TranslatedTopicString dbEntity = entityManager.find(TranslatedTopicString.class, restEntity.getId());
                    if (dbEntity == null) throw new BadRequestException(
                            "No TranslatedTopicString entity was found with the primary key " + restEntity.getId());

                    entity.removeTranslatedTopicString(dbEntity);
                    entityManager.remove(dbEntity);
                } else if (restEntityItem.returnIsAddItem()) {
                    final TranslatedTopicString dbEntity = translatedTopicStringFactory.createDBEntityFromRESTEntity(restEntity);
                    entity.addTranslatedTopicString(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final TranslatedTopicString dbEntity = entityManager.find(TranslatedTopicString.class, restEntity.getId());
                    if (dbEntity == null) throw new BadRequestException(
                            "No TranslatedTopicString entity was found with the primary key " + restEntity.getId());
                    if (!entity.getTranslatedTopicStrings().contains(dbEntity)) throw new BadRequestException(
                            "No TranslatedTopicString entity was found with the primary key " + restEntity.getId() + " for " +
                                    "TranslatedTopicData " + entity.getId());

                    new TranslatedTopicStringV1Factory().updateDBEntityFromRESTEntity(dbEntity, restEntity);
                }
            }
        }
    }

    @Override
    protected Class<TranslatedTopicData> getDatabaseClass() {
        return TranslatedTopicData.class;
    }
}
