/*
  Copyright 2011-2014 Red Hat, Inc

  This file is part of PressGang CCMS.

  PressGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PressGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PressGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.server.rest.v1.factory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.TranslatedTopic;
import org.jboss.pressgang.ccms.model.TranslatedTopicData;
import org.jboss.pressgang.ccms.model.TranslatedTopicString;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.model.contentspec.TranslatedCSNode;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTopicSourceUrlCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTranslatedTopicCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTranslatedTopicStringCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTranslatedTopicCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTAssignedPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTranslatedTopicStringV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTranslatedTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTranslatedCSNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.enums.RESTXMLFormat;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.pressgang.ccms.server.utils.TopicUtilities;
import org.jboss.pressgang.ccms.server.utils.TranslatedTopicUtilities;
import org.jboss.pressgang.ccms.utils.common.DocBookUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class TranslatedTopicV1Factory extends RESTEntityFactory<RESTTranslatedTopicV1, TranslatedTopicData,
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
    @Inject
    protected TranslatedCSNodeV1Factory translatedCSNodeFactory;

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
        expandOptions.add(RESTTranslatedTopicV1.TRANSLATED_CSNODE_NAME);

        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        // Set the simple values
        retValue.setId(entity.getTranslatedTopicDataId());
        retValue.setTranslatedTopicId(entity.getTranslatedTopic().getId());
        retValue.setTopicId(entity.getTranslatedTopic().getTopicId());
        retValue.setTopicRevision(entity.getTranslatedTopic().getTopicRevision());
        retValue.setContainsFuzzyTranslation(entity.containsFuzzyTranslation());
        retValue.setXmlFormat(RESTXMLFormat.getXMLFormat(entity.getTranslatedTopic().getEnversTopic(entityManager).getXmlFormat()));
        retValue.setTranslatedXMLCondition(entity.getTranslatedTopic().getTranslatedXMLCondition());
        retValue.setTranslatedAdditionalXML(entity.getTranslatedAdditionalXml());
        retValue.setCustomEntities(entity.getTranslatedTopic().getCustomEntities());

        // Get the title from the XML or if the XML is null then use the original topics title.
        final String fixedTranslatedXML = TopicUtilities.fixTopicXMLForFormat(entity.getTranslatedXml(),
                entity.getTranslatedTopic().getEnversTopic(entityManager).getXmlFormat());
        String title = DocBookUtilities.findTitle(fixedTranslatedXML);
        if (title == null) title = entity.getTranslatedTopic().getEnversTopic(entityManager).getTopicTitle();

        // Prefix the locale to the title if its a dummy translation to show that it is missing the related translated topic
        if (entity.getId() < 0) title = "[" + entity.getTranslationLocale() + "] " + title;
        retValue.setTitle(title);

        retValue.setXml(entity.getTranslatedXml());
        retValue.setXmlErrors(entity.getTranslatedXmlErrors());
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
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTTranslatedTopicCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // TRANSLATED STRINGS
        if (expand != null && expand.contains(RESTTranslatedTopicV1.TRANSLATEDTOPICSTRING_NAME)) {
            retValue.setTranslatedTopicStrings_OTM(
                    RESTEntityCollectionFactory.create(RESTTranslatedTopicStringCollectionV1.class, translatedTopicStringFactory,
                            entity.getTranslatedTopicDataStringsArray(), RESTTranslatedTopicV1.TRANSLATEDTOPICSTRING_NAME, dataType, expand,
                            baseUrl, revision, false, entityManager));
        }

        // TAGS
        if (expand != null && expand.contains(RESTTranslatedTopicV1.TAGS_NAME)) {
            retValue.setTags(RESTEntityCollectionFactory.create(RESTTagCollectionV1.class, tagFactory,
                    entity.getTranslatedTopic().getEnversTopic(entityManager).getTags(), RESTv1Constants.TAGS_EXPANSION_NAME, dataType,
                    expand, baseUrl, entity.getTranslatedTopic().getTopicRevision(), entityManager));
        }

        // OUTGOING RELATIONSHIPS
        if (expand != null && expand.contains(RESTTranslatedTopicV1.OUTGOING_NAME)) {
            retValue.setOutgoingTranslatedRelationships(RESTEntityCollectionFactory.create(RESTTranslatedTopicCollectionV1.class, this,
                    entity.getOutgoingRelatedTranslatedTopicData(entityManager), RESTTranslatedTopicV1.OUTGOING_NAME, dataType, expand,
                    baseUrl, revision, true, entityManager));
        }

        // INCOMING RELATIONSHIPS
        if (expand != null && expand.contains(RESTTranslatedTopicV1.INCOMING_NAME)) {
            retValue.setIncomingTranslatedRelationships(RESTEntityCollectionFactory.create(RESTTranslatedTopicCollectionV1.class, this,
                    entity.getIncomingRelatedTranslatedTopicData(entityManager), RESTTranslatedTopicV1.INCOMING_NAME, dataType, expand,
                    baseUrl, revision, true, entityManager));
        }

        // ALL OUTGOING RELATIONSHIPS (includes dummy topics)
        if (expand != null && expand.contains(RESTTranslatedTopicV1.ALL_LATEST_OUTGOING_NAME)) {
            retValue.setOutgoingRelationships(RESTEntityCollectionFactory.create(RESTTranslatedTopicCollectionV1.class, this,
                    entity.getOutgoingDummyFilledRelatedTranslatedTopicDatas(entityManager), RESTTranslatedTopicV1.ALL_LATEST_OUTGOING_NAME,
                    dataType, expand, baseUrl, revision, true, entityManager));
        }

        // ALL INCOMING RELATIONSHIPS (includes dummy topics)
        if (expand != null && expand.contains(RESTTranslatedTopicV1.ALL_LATEST_INCOMING_NAME)) {
            retValue.setIncomingRelationships(RESTEntityCollectionFactory.create(RESTTranslatedTopicCollectionV1.class, this,
                    entity.getIncomingDummyFilledRelatedTranslatedTopicDatas(entityManager), RESTTranslatedTopicV1.ALL_LATEST_INCOMING_NAME,
                    dataType, expand, baseUrl, revision, true, entityManager));
        }

        // SOURCE URLS
        if (expand != null && expand.contains(RESTTranslatedTopicV1.SOURCE_URLS_NAME)) {
            retValue.setSourceUrls_OTM(RESTEntityCollectionFactory.create(RESTTopicSourceUrlCollectionV1.class, topicSourceUrlFactory,
                    entity.getTranslatedTopic().getEnversTopic(entityManager).getTopicSourceUrls(), RESTTranslatedTopicV1.SOURCE_URLS_NAME,
                    dataType, expand, baseUrl, entity.getTranslatedTopic().getTopicRevision(), false, entityManager));
        }

        // PROPERTY TAGS
        if (expand != null && expand.contains(RESTTranslatedTopicV1.PROPERTIES_NAME)) {
            retValue.setProperties(RESTEntityCollectionFactory.create(RESTAssignedPropertyTagCollectionV1.class, topicPropertyTagFactory,
                    entity.getTranslatedTopic().getEnversTopic(entityManager).getPropertyTagsList(), RESTTranslatedTopicV1.PROPERTIES_NAME,
                    dataType, expand, baseUrl, entity.getTranslatedTopic().getTopicRevision(), entityManager));
        }

        // TRANSLATED CS NODE
        if (expand != null && expand.contains(
                RESTTranslatedTopicV1.TRANSLATED_CSNODE_NAME) && entity.getTranslatedTopic().getTranslatedCSNode() != null) {
            retValue.setTranslatedCSNode(
                    translatedCSNodeFactory.createRESTEntityFromDBEntity(entity.getTranslatedTopic().getTranslatedCSNode(), baseUrl,
                            dataType, expand.get(RESTTranslatedTopicV1.TRANSLATED_CSNODE_NAME), revision, true));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.TRANSLATEDTOPIC_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTTranslatedTopicV1> parent, final RESTTranslatedTopicV1 dataObject) {
        if (dataObject.getConfiguredParameters().contains(RESTTranslatedTopicV1.TRANSLATEDTOPICSTRING_NAME)
                && dataObject.getTranslatedTopicStrings_OTM() != null
                && dataObject.getTranslatedTopicStrings_OTM().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getTranslatedTopicStrings_OTM(), translatedTopicStringFactory);
        }
    }

    @Override
    public void syncBaseDetails(final TranslatedTopicData entity, final RESTTranslatedTopicV1 dataObject) {
        /*
         * Since this factory is the rare case where two entities are combined into one. Check if it has a parent, if not then
         * check if one exists that matches otherwise create one. If one exists then update it.
         */
        TranslatedTopic translatedTopic = entity.getTranslatedTopic();
        if (translatedTopic == null) {
            try {
                final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
                final CriteriaQuery<TranslatedTopic> query = builder.createQuery(TranslatedTopic.class);
                final Root<TranslatedTopic> root = query.from(TranslatedTopic.class);
                final Predicate topicIdMatches = builder.equal(root.get("topicId"), dataObject.getTopicId());
                final Predicate topicRevMatches = builder.equal(root.get("topicRevision"), dataObject.getTopicRevision());
                if (dataObject.getTranslatedCSNode() == null) {
                    final Predicate translatedCSNodeNull = builder.isNull(root.get("translatedCSNode"));
                    query.where(builder.and(topicIdMatches, topicRevMatches, translatedCSNodeNull));
                } else {
                    final Predicate translatedCSNodeMatches = builder.equal(root.get("translatedCSNode").get("translatedCSNodeId"),
                            dataObject.getTranslatedCSNode().getId());
                    query.where(builder.and(topicIdMatches, topicRevMatches, translatedCSNodeMatches));
                }
                translatedTopic = entityManager.createQuery(query).getSingleResult();
            } catch (Exception e) {
                translatedTopic = new TranslatedTopic();

                // populate the new translated topic
                if (dataObject.hasParameterSet(RESTTranslatedTopicV1.TOPICID_NAME)) translatedTopic.setTopicId(dataObject.getTopicId());
                if (dataObject.hasParameterSet(RESTTranslatedTopicV1.TOPICREVISION_NAME))
                    translatedTopic.setTopicRevision(dataObject.getTopicRevision());
                if (dataObject.hasParameterSet(RESTTranslatedTopicV1.TRANSLATED_XML_CONDITION_NAME))
                    translatedTopic.setTranslatedXMLCondition(dataObject.getTranslatedXMLCondition());
                if (dataObject.hasParameterSet(RESTTranslatedTopicV1.CUSTOM_ENTITIES_NAME))
                    translatedTopic.setCustomEntities(dataObject.getCustomEntities());

                if (dataObject.hasParameterSet(RESTTranslatedTopicV1.TRANSLATED_CSNODE_NAME)) {
                    final RESTTranslatedCSNodeV1 restEntity = dataObject.getTranslatedCSNode();
                    final TranslatedCSNode dbEntity = entityManager.find(TranslatedCSNode.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new BadRequestException("No TranslatedCSNode entity was found with the primary key " + restEntity.getId());
                    dbEntity.addTranslatedTopic(translatedTopic);
                }
            }
        }

        entity.setTranslatedTopic(translatedTopic);

        if (dataObject.hasParameterSet(RESTTranslatedTopicV1.XML_ERRORS_NAME)) entity.setTranslatedXmlErrors(dataObject.getXmlErrors());
        if (dataObject.hasParameterSet(RESTTranslatedTopicV1.XML_NAME)) entity.setTranslatedXml(dataObject.getXml());
        if (dataObject.hasParameterSet(RESTTranslatedTopicV1.LOCALE_NAME)) entity.setTranslationLocale(dataObject.getLocale());
        if (dataObject.hasParameterSet(RESTTranslatedTopicV1.TRANSLATIONPERCENTAGE_NAME))
            entity.setTranslationPercentage(dataObject.getTranslationPercentage());

        boolean additionalXMLSet = false;
        if (dataObject.hasParameterSet(RESTTranslatedTopicV1.TRANSLATED_ADDITIONAL_XML_NAME)) {
            entity.setTranslatedAdditionalXml(dataObject.getTranslatedAdditionalXML());
            additionalXMLSet = true;
        }

        /*
         * If the additional XML wasn't set and this is a new entity then try and get the previous topic's additional XML. We need to do
         * this otherwise every time one little change is made the additional xml will be lost and have to be re-added.
         */
        if (!additionalXMLSet && entity.getId() == null && entity.getTranslationLocale() != null) {
            final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            final CriteriaQuery<TranslatedTopicData> query = builder.createQuery(TranslatedTopicData.class);
            final Root<TranslatedTopicData> root = query.from(TranslatedTopicData.class);

            final Predicate localeMatches = builder.equal(root.get("translationLocale"), entity.getTranslationLocale());
            final Predicate topicIdMatches = builder.equal(root.get("translatedTopic").get("topicId"),
                    entity.getTranslatedTopic().getTopicId());
            query.where(builder.and(localeMatches, topicIdMatches));

            // Order by topic revision in descending order
            query.orderBy(builder.desc(root.get("translatedTopic").get("topicRevision")));

            final List<TranslatedTopicData> previousTranslatedTopicList = entityManager.createQuery(query).setMaxResults(1).getResultList();
            final TranslatedTopicData previousTranslatedTopic = previousTranslatedTopicList.size() > 0 ? previousTranslatedTopicList.get(
                    0) : null;
            if (previousTranslatedTopic != null) {
                entity.setTranslatedAdditionalXml(previousTranslatedTopic.getTranslatedAdditionalXml());
            }
        }

        translatedTopic.getTranslatedTopicDatas().add(entity);
    }

    @Override
    public void syncAdditionalDetails(final TranslatedTopicData entity, final RESTTranslatedTopicV1 dataObject) {
        /* This method will set the XML errors field */
        TranslatedTopicUtilities.processXML(entityManager, entity);
    }

    @Override
    protected void doDeleteChildAction(final TranslatedTopicData entity, final RESTTranslatedTopicV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        if (restEntity instanceof RESTTranslatedTopicStringV1) {
            final TranslatedTopicString dbEntity = entityManager.find(TranslatedTopicString.class, restEntity.getId());
            if (dbEntity == null) throw new BadRequestException(
                    "No TranslatedTopicString entity was found with the primary key " + restEntity.getId());

            entity.removeTranslatedString(dbEntity);
            entityManager.remove(dbEntity);
        }
    }

    @Override
    protected AuditedEntity doCreateChildAction(final TranslatedTopicData entity, final RESTTranslatedTopicV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();
        final AuditedEntity dbEntity;

        if (restEntity instanceof RESTTranslatedTopicStringV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            entity.addTranslatedString((TranslatedTopicString) dbEntity);
        } else {
            throw new IllegalArgumentException("Item is not a child of TranslatedTopicData");
        }

        return dbEntity;
    }

    @Override
    protected AuditedEntity getChildEntityForAction(final TranslatedTopicData entity, final RESTTranslatedTopicV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        final AuditedEntity dbEntity;
        if (restEntity instanceof RESTTranslatedTopicStringV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTTranslatedTopicStringV1) restEntity,
                    TranslatedTopicString.class);
            if (dbEntity == null) {
                throw new BadRequestException("No TranslatedTopicString entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getTranslatedTopicStrings().contains(dbEntity)) {
                throw new BadRequestException(
                        "No TranslatedTopicString entity was found with the primary key " + restEntity.getId() + " for TranslatedTopicData "
                                + entity.getId());
            }
        } else {
            throw new IllegalArgumentException("Item is not a child of TranslatedTopicData");
        }

        return dbEntity;
    }

    @Override
    protected Class<TranslatedTopicData> getDatabaseClass() {
        return TranslatedTopicData.class;
    }
}
