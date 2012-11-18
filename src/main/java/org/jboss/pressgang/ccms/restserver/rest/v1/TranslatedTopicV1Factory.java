package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.model.TopicSourceUrl;
import org.jboss.pressgang.ccms.model.TopicToPropertyTag;
import org.jboss.pressgang.ccms.model.TranslatedTopic;
import org.jboss.pressgang.ccms.model.TranslatedTopicData;
import org.jboss.pressgang.ccms.model.TranslatedTopicString;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTopicSourceUrlCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTranslatedTopicCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTranslatedTopicStringCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTAssignedPropertyTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTranslatedTopicCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTranslatedTopicStringCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTopicSourceUrlCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTAssignedPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicSourceUrlV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTranslatedTopicStringV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTranslatedTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTAssignedPropertyTagV1;
import org.jboss.pressgang.ccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.restserver.utils.EnversUtilities;
import org.jboss.pressgang.ccms.utils.common.DocBookUtilities;

public class TranslatedTopicV1Factory
        extends
        RESTDataObjectFactory<RESTTranslatedTopicV1, TranslatedTopicData, RESTTranslatedTopicCollectionV1, RESTTranslatedTopicCollectionItemV1> {
    public TranslatedTopicV1Factory() {
        super(TranslatedTopicData.class);
    }

    @Override
    public RESTTranslatedTopicV1 createRESTEntityFromDBEntity(final TranslatedTopicData entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";
        assert expand != null : "Parameter expand can not be null";

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

        if (revision == null)
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        /* Set the simple values */
        retValue.setId(entity.getTranslatedTopicDataId());
        retValue.setTranslatedTopicId(entity.getTranslatedTopic().getId());
        retValue.setTopicId(entity.getTranslatedTopic().getTopicId());
        retValue.setTopicRevision(entity.getTranslatedTopic().getTopicRevision());
        retValue.setContainsFuzzyTranslation(entity.containsFuzzyTranslation());

        /*
         * Get the title from the XML or if the XML is null then use the original topics title.
         */
        String title = DocBookUtilities.findTitle(entity.getTranslatedXml());
        if (title == null)
            title = entity.getTranslatedTopic().getEnversTopic(entityManager).getTopicTitle();

        /*
         * Append the locale to the title if its a dummy translation to show that it is missing the related translated topic
         */
        if (entity.getId() < 0)
            title = "[" + entity.getTranslationLocale() + "] " + title;
        retValue.setTitle(title);

        retValue.setXml(entity.getTranslatedXml());
        retValue.setXmlErrors(entity.getTranslatedXmlErrors());
        retValue.setHtml(entity.getTranslatedXmlRendered());
        retValue.setLocale(entity.getTranslationLocale());
        retValue.setTranslationPercentage(entity.getTranslationPercentage());

        /* Set the object references */
        if (expandParentReferences && expand != null && entity.getTranslatedTopic().getEnversTopic(entityManager) != null) {
            retValue.setTopic(new TopicV1Factory().createRESTEntityFromDBEntity(
                    entity.getTranslatedTopic().getEnversTopic(entityManager), baseUrl, dataType,
                    expand.get(RESTTranslatedTopicV1.TOPIC_NAME), entity.getTranslatedTopic().getTopicRevision(), true,
                    entityManager));
            retValue.getTopic().setRevision(entity.getTranslatedTopic().getTopicRevision());
        }

        // REVISIONS
        if (revision == null) {
            retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTTranslatedTopicV1, TranslatedTopicData, RESTTranslatedTopicCollectionV1, RESTTranslatedTopicCollectionItemV1>()
                    .create(RESTTranslatedTopicCollectionV1.class, new TranslatedTopicV1Factory(), entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        // TRANSLATED STRINGS
        retValue.setTranslatedTopicStrings_OTM(new RESTDataObjectCollectionFactory<RESTTranslatedTopicStringV1, TranslatedTopicString, RESTTranslatedTopicStringCollectionV1, RESTTranslatedTopicStringCollectionItemV1>()
                .create(RESTTranslatedTopicStringCollectionV1.class, new TranslatedTopicStringV1Factory(),
                        entity.getTranslatedTopicDataStringsArray(), RESTTranslatedTopicV1.TRANSLATEDTOPICSTRING_NAME,
                        dataType, expand, baseUrl, false, /* don't set the reference to this entity on the children */
                        entityManager));

        // TAGS
        retValue.setTags(new RESTDataObjectCollectionFactory<RESTTagV1, Tag, RESTTagCollectionV1, RESTTagCollectionItemV1>()
                .create(RESTTagCollectionV1.class, new TagV1Factory(), entity.getTranslatedTopic()
                        .getEnversTopic(entityManager).getTags(), RESTv1Constants.TAGS_EXPANSION_NAME, dataType, expand, baseUrl,
                        entityManager));

        // OUTGOING RELATIONSHIPS
        retValue.setOutgoingTranslatedRelationships(new RESTDataObjectCollectionFactory<RESTTranslatedTopicV1, TranslatedTopicData, RESTTranslatedTopicCollectionV1, RESTTranslatedTopicCollectionItemV1>()
                .create(RESTTranslatedTopicCollectionV1.class, new TranslatedTopicV1Factory(),
                        entity.getOutgoingRelatedTranslatedTopicData(entityManager), RESTTranslatedTopicV1.OUTGOING_NAME,
                        dataType, expand, baseUrl, true, entityManager));

        // INCOMING RELATIONSHIPS
        retValue.setIncomingTranslatedRelationships(new RESTDataObjectCollectionFactory<RESTTranslatedTopicV1, TranslatedTopicData, RESTTranslatedTopicCollectionV1, RESTTranslatedTopicCollectionItemV1>()
                .create(RESTTranslatedTopicCollectionV1.class, new TranslatedTopicV1Factory(),
                        entity.getIncomingRelatedTranslatedTopicData(entityManager), RESTTranslatedTopicV1.INCOMING_NAME,
                        dataType, expand, baseUrl, true, entityManager));

        // ALL OUTGOING RELATIONSHIPS (includes dummy topics)
        retValue.setOutgoingRelationships(new RESTDataObjectCollectionFactory<RESTTranslatedTopicV1, TranslatedTopicData, RESTTranslatedTopicCollectionV1, RESTTranslatedTopicCollectionItemV1>()
                .create(RESTTranslatedTopicCollectionV1.class, new TranslatedTopicV1Factory(),
                        entity.getOutgoingDummyFilledRelatedTranslatedTopicDatas(entityManager),
                        RESTTranslatedTopicV1.ALL_LATEST_OUTGOING_NAME, dataType, expand, baseUrl, true, entityManager));

        // ALL INCOMING RELATIONSHIPS (includes dummy topics)
        retValue.setIncomingRelationships(new RESTDataObjectCollectionFactory<RESTTranslatedTopicV1, TranslatedTopicData, RESTTranslatedTopicCollectionV1, RESTTranslatedTopicCollectionItemV1>()
                .create(RESTTranslatedTopicCollectionV1.class, new TranslatedTopicV1Factory(),
                        entity.getIncomingDummyFilledRelatedTranslatedTopicDatas(entityManager),
                        RESTTranslatedTopicV1.ALL_LATEST_INCOMING_NAME, dataType, expand, baseUrl, true, entityManager));

        // SOURCE URLS
        retValue.setSourceUrls_OTM(new RESTDataObjectCollectionFactory<RESTTopicSourceUrlV1, TopicSourceUrl, RESTTopicSourceUrlCollectionV1, RESTTopicSourceUrlCollectionItemV1>()
                .create(RESTTopicSourceUrlCollectionV1.class, new TopicSourceUrlV1Factory(), entity.getTranslatedTopic()
                        .getEnversTopic(entityManager).getTopicSourceUrls(), RESTTranslatedTopicV1.SOURCE_URLS_NAME, dataType,
                        expand, baseUrl, false, entityManager));

        // PROPERTY TAGS
        retValue.setProperties(new RESTDataObjectCollectionFactory<RESTAssignedPropertyTagV1, TopicToPropertyTag, RESTAssignedPropertyTagCollectionV1, RESTAssignedPropertyTagCollectionItemV1>()
                .create(RESTAssignedPropertyTagCollectionV1.class, new TopicPropertyTagV1Factory(), entity.getTranslatedTopic()
                        .getEnversTopic(entityManager).getTopicToPropertyTagsArray(), RESTTranslatedTopicV1.PROPERTIES_NAME,
                        dataType, expand, baseUrl, entityManager));

        retValue.setLinks(baseUrl, RESTv1Constants.TRANSLATEDTOPIC_URL_NAME, dataType, retValue.getId());
        retValue.setLogDetails(new LogDetailsV1Factory().create(entity, revision, RESTBaseEntityV1.LOG_DETAILS_NAME, expand,
                dataType, baseUrl, entityManager));

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(EntityManager entityManager, TranslatedTopicData entity,
            RESTTranslatedTopicV1 dataObject) throws InvalidParameterException {
        /*
         * Since this factory is the rare case where two entities are combined into one. Check if it has a parent, if not then
         * check if one exists that matches otherwise create one. If one exists then update it.
         */
        TranslatedTopic translatedTopic = entity.getTranslatedTopic();
        if (translatedTopic == null) {
            try {
                final Query query = entityManager.createQuery(TranslatedTopic.SELECT_ALL_QUERY
                        + " WHERE translatedTopic.topicId=" + dataObject.getTopicId() + " AND translatedTopic.topicRevision="
                        + dataObject.getTopicRevision());
                translatedTopic = (TranslatedTopic) query.getSingleResult();
            } catch (Exception e) {
                translatedTopic = new TranslatedTopic();

                /* populate the new translated topic */
                if (dataObject.hasParameterSet(RESTTranslatedTopicV1.TOPICID_NAME))
                    translatedTopic.setTopicId(dataObject.getTopicId());
                if (dataObject.hasParameterSet(RESTTranslatedTopicV1.TOPICREVISION_NAME))
                    translatedTopic.setTopicRevision(dataObject.getTopicRevision());
            }
        }

        entity.setTranslatedTopic(translatedTopic);

        if (dataObject.hasParameterSet(RESTTranslatedTopicV1.HTML_UPDATED))
            entity.setTranslatedXmlRenderedUpdated(dataObject.getHtmlUpdated());
        if (dataObject.hasParameterSet(RESTTranslatedTopicV1.XML_ERRORS_NAME))
            entity.setTranslatedXmlErrors(dataObject.getXmlErrors());
        if (dataObject.hasParameterSet(RESTTranslatedTopicV1.XML_NAME))
            entity.setTranslatedXml(dataObject.getXml());
        if (dataObject.hasParameterSet(RESTTranslatedTopicV1.HTML_NAME))
            entity.setTranslatedXmlRendered(dataObject.getHtml());
        if (dataObject.hasParameterSet(RESTTranslatedTopicV1.LOCALE_NAME))
            entity.setTranslationLocale(dataObject.getLocale());
        if (dataObject.hasParameterSet(RESTTranslatedTopicV1.TRANSLATIONPERCENTAGE_NAME))
            entity.setTranslationPercentage(dataObject.getTranslationPercentage());

        translatedTopic.getTranslatedTopicDatas().add(entity);

        /* Save the changes done to the translated topic */
        entityManager.persist(translatedTopic);

        // entityManager.persist(entity);

        /* One To Many - Add will create a child entity */
        if (dataObject.hasParameterSet(RESTTranslatedTopicV1.TRANSLATEDTOPICSTRING_NAME)
                && dataObject.getTranslatedTopicStrings_OTM() != null
                && dataObject.getTranslatedTopicStrings_OTM().getItems() != null) {
            dataObject.getTranslatedTopicStrings_OTM().removeInvalidChangeItemRequests();

            /* remove any items first */
            for (final RESTTranslatedTopicStringCollectionItemV1 restEntityItem : dataObject.getTranslatedTopicStrings_OTM()
                    .getItems()) {
                final RESTTranslatedTopicStringV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final TranslatedTopicString dbEntity = entityManager.find(TranslatedTopicString.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No TranslatedTopicString entity was found with the primary key "
                                + restEntity.getId());

                    entity.getTranslatedTopicStrings().remove(dbEntity);
                    entityManager.remove(dbEntity);
                } else if (restEntityItem.returnIsAddItem()) {
                    final TranslatedTopicString dbEntity = new TranslatedTopicString();
                    dbEntity.setTranslatedTopicData(entity);
                    new TranslatedTopicStringV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                    entity.getTranslatedTopicStrings().add(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final TranslatedTopicString dbEntity = entityManager.find(TranslatedTopicString.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No TranslatedTopicString entity was found with the primary key "
                                + restEntity.getId());

                    new TranslatedTopicStringV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                }
            }
        }
    }
}
