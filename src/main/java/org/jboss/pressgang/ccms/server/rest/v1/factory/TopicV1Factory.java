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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.BugzillaBug;
import org.jboss.pressgang.ccms.model.MinHashXOR;
import org.jboss.pressgang.ccms.model.PropertyTag;
import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.TopicSourceUrl;
import org.jboss.pressgang.ccms.model.TopicToPropertyTag;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTBugzillaBugCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTMinHashCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTopicCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTopicSourceUrlCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTranslatedTopicCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTContentSpecCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTopicCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTAssignedPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTBugzillaBugV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicSourceUrlV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.enums.RESTXMLFormat;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTAssignedPropertyTagV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.CachedEntityLoader;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.KeywordExtractor;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.pressgang.ccms.server.utils.TopicUtilities;
import org.jboss.pressgang.ccms.utils.common.DocBookUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class TopicV1Factory extends RESTEntityFactory<RESTTopicV1, Topic, RESTTopicCollectionV1, RESTTopicCollectionItemV1> {
    @Inject
    protected CachedEntityLoader cachedEntityLoader;
    @Inject
    protected TagV1Factory tagFactory;
    @Inject
    protected TopicPropertyTagV1Factory topicPropertyTagFactory;
    @Inject
    protected TopicSourceUrlV1Factory topicSourceUrlFactory;
    @Inject
    protected BugzillaBugV1Factory bugzillaBugFactory;
    @Inject
    protected TranslatedTopicV1Factory translatedTopicFactory;
    @Inject
    protected ContentSpecV1Factory contentSpecFactory;
    @Inject
    protected MinHashV1Factory minHashFactory;

    @Override
    public RESTTopicV1 createRESTEntityFromDBEntityInternal(final Topic entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTTopicV1 retValue = new RESTTopicV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTTopicV1.TAGS_NAME);
        expandOptions.add(RESTTopicV1.INCOMING_NAME);
        expandOptions.add(RESTTopicV1.OUTGOING_NAME);
        expandOptions.add(RESTTopicV1.SOURCE_URLS_NAME);
        expandOptions.add(RESTTopicV1.BUGZILLABUGS_NAME);
        expandOptions.add(RESTTopicV1.PROPERTIES_NAME);
        expandOptions.add(RESTTopicV1.LOG_DETAILS_NAME);
        expandOptions.add(RESTTopicV1.CONTENTSPECS_NAME);
        expandOptions.add(RESTTopicV1.KEYWORDS_NAME);
        expandOptions.add(RESTTopicV1.MINHASHES_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        /* Set simple properties */
        retValue.setId(entity.getTopicId());
        retValue.setTitle(entity.getTopicTitle());
        retValue.setDescription(entity.getTopicText());
        retValue.setXml(entity.getTopicXML());
        retValue.setLastModified(EnversUtilities.getFixedLastModifiedDate(entityManager, entity));
        retValue.setCreated(entity.getTopicTimeStamp());
        retValue.setLocale(entity.getTopicLocale());
        retValue.setXmlErrors(entity.getTopicXMLErrors());
        retValue.setXmlFormat(RESTXMLFormat.getXMLFormat(entity.getXmlFormat()));
        retValue.setContentHash(entity.getTopicContentHash());

        // KEYWORDS
        if (revision == null && expand != null && expand.contains(RESTTopicV1.KEYWORDS_NAME)) {
            try {
                retValue.setKeywords(new ArrayList<String>());
                retValue.getKeywords().addAll(new KeywordExtractor().findKeywordStrings(entity.getTopicSearchText()));
            } catch (final IOException ex) {
                // don't fill the collection if there was an exception
            }
        }

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTTopicV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTTopicCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // TAGS
        if (expand != null && expand.contains(RESTTopicV1.TAGS_NAME)) {
            retValue.setTags(RESTEntityCollectionFactory.create(RESTTagCollectionV1.class, tagFactory, entity.getTags(),
                    RESTv1Constants.TAGS_EXPANSION_NAME, dataType, expand, baseUrl, revision, entityManager));
        }

        // OUTGOING RELATIONSHIPS
        if (expand != null && expand.contains(RESTTopicV1.OUTGOING_NAME)) {
            retValue.setOutgoingRelationships(
                    RESTEntityCollectionFactory.create(RESTTopicCollectionV1.class, this, entity.getOutgoingRelatedTopicsArray(),
                            RESTTopicV1.OUTGOING_NAME, dataType, expand, baseUrl, revision, entityManager));
        }

        // INCOMING RELATIONSHIPS
        if (expand != null && expand.contains(RESTTopicV1.INCOMING_NAME)) {
            retValue.setIncomingRelationships(
                    RESTEntityCollectionFactory.create(RESTTopicCollectionV1.class, this, entity.getIncomingRelatedTopicsArray(),
                            RESTTopicV1.INCOMING_NAME, dataType, expand, baseUrl, revision, entityManager));
        }

        // PROPERTIES
        if (expand != null && expand.contains(RESTTopicV1.PROPERTIES_NAME)) {
            retValue.setProperties(
                    RESTEntityCollectionFactory.create(RESTAssignedPropertyTagCollectionV1.class, topicPropertyTagFactory,
                            entity.getPropertyTagsList(), RESTTopicV1.PROPERTIES_NAME, dataType, expand, baseUrl, revision, entityManager));
        }

        // SOURCE URLS
        if (expand != null && expand.contains(RESTTopicV1.SOURCE_URLS_NAME)) {
            retValue.setSourceUrls_OTM(RESTEntityCollectionFactory.create(RESTTopicSourceUrlCollectionV1.class, topicSourceUrlFactory,
                    entity.getTopicSourceUrls(), RESTTopicV1.SOURCE_URLS_NAME, dataType, expand, baseUrl, revision, false, entityManager));
        }

        // BUGZILLA BUGS
        if (expand != null && expand.contains(RESTTopicV1.BUGZILLABUGS_NAME)) {
            retValue.setBugzillaBugs_OTM(
                    RESTEntityCollectionFactory.create(RESTBugzillaBugCollectionV1.class, bugzillaBugFactory, entity.getBugzillaBugs(),
                            RESTTopicV1.BUGZILLABUGS_NAME, dataType, expand, baseUrl, revision, false, entityManager));
        }

        // TRANSLATED TOPICS
        if (expand != null && expand.contains(RESTTopicV1.TRANSLATEDTOPICS_NAME)) {
            retValue.setTranslatedTopics_OTM(
                    RESTEntityCollectionFactory.create(RESTTranslatedTopicCollectionV1.class, translatedTopicFactory,
                            entity.getTranslatedTopics(entityManager, revision), RESTTopicV1.TRANSLATEDTOPICS_NAME, dataType, expand,
                            baseUrl, false, entityManager));
        }

        // CONTENT SPECS
        if (expand != null && expand.contains(RESTTopicV1.CONTENTSPECS_NAME)) {
            retValue.setContentSpecs_OTM(RESTEntityCollectionFactory.create(RESTContentSpecCollectionV1.class, contentSpecFactory,
                    entity.getContentSpecs(entityManager), RESTTopicV1.CONTENTSPECS_NAME, dataType, expand, baseUrl, false, entityManager));
        }

        // MINHASHES
        if (expand != null && expand.contains(RESTTopicV1.MINHASHES_NAME)) {
            retValue.setMinHashes(
                    RESTEntityCollectionFactory.create(RESTMinHashCollectionV1.class, minHashFactory, entity.getMinHashesList(),
                            RESTTopicV1.MINHASHES_NAME, dataType, expand, baseUrl, false, entityManager));

        }

        retValue.setLinks(baseUrl, RESTv1Constants.TOPIC_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTTopicV1> parent, final RESTTopicV1 dataObject) {
        if (dataObject.hasParameterSet(RESTTopicV1.SOURCE_URLS_NAME)
                && dataObject.getSourceUrls_OTM() != null
                && dataObject.getSourceUrls_OTM().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getSourceUrls_OTM(), topicSourceUrlFactory);
        }

        if (dataObject.hasParameterSet(RESTTopicV1.TAGS_NAME)
                && dataObject.getTags() != null
                && dataObject.getTags().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getTags(), tagFactory);
        }

        if (dataObject.hasParameterSet(RESTTopicV1.PROPERTIES_NAME)
                && dataObject.getProperties() != null
                && dataObject.getProperties().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getProperties(), topicPropertyTagFactory);
        }

        if (dataObject.hasParameterSet(RESTTopicV1.BUGZILLABUGS_NAME)
                && dataObject.getBugzillaBugs_OTM() != null
                && dataObject.getBugzillaBugs_OTM().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getBugzillaBugs_OTM(), bugzillaBugFactory);
        }

        if (dataObject.hasParameterSet(RESTTopicV1.OUTGOING_NAME)
                && dataObject.getOutgoingRelationships() != null
                && dataObject.getOutgoingRelationships().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getOutgoingRelationships(), this, RESTTopicV1.OUTGOING_NAME);
        }

        if (dataObject.hasParameterSet(RESTTopicV1.INCOMING_NAME)
                && dataObject.getIncomingRelationships() != null
                && dataObject.getIncomingRelationships().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getIncomingRelationships(), this, RESTTopicV1.INCOMING_NAME);
        }
    }

    @Override
    public void syncBaseDetails(final Topic entity, final RESTTopicV1 dataObject) {
        /*
            The topic title can either be set specifically from the title property, or it can be inferred from
            the XML itself.

            If the property is set, that takes precedence. Otherwise the XML is extracted and the title there is
            set as the title property.
        */
        boolean titlePropertySpecificallySet = false;

        /* sync the basic properties */
        if (dataObject.hasParameterSet(RESTTopicV1.TITLE_NAME))  {
            entity.setTopicTitle(dataObject.getTitle());
            // the title was manually set, so use that
            titlePropertySpecificallySet = true;
        }
        if (dataObject.hasParameterSet(RESTTopicV1.DESCRIPTION_NAME)) entity.setTopicText(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTTopicV1.XML_NAME)) {
            entity.setTopicXML(dataObject.getXml());
            // the title was not manually set, so extract it from the XML and update the topic
            if (!titlePropertySpecificallySet && TopicUtilities.isTopicNormalTopic(entity)) {
                final String title = DocBookUtilities.findTitle(dataObject.getXml());
                if (title != null) {
                    entity.setTopicTitle(title);
                }
            }
        }
        if (dataObject.hasParameterSet(RESTTopicV1.LOCALE_NAME)) entity.setTopicLocale(dataObject.getLocale());
        if (dataObject.hasParameterSet(RESTTopicV1.FORMAT_NAME))
            entity.setXmlFormat(RESTXMLFormat.getXMLFormatId(dataObject.getXmlFormat()));
    }

    @Override
    public void syncAdditionalDetails(final Topic entity, final RESTTopicV1 dataObject) {
        // This method will set the XML errors field
        if (requiresXMLProcessing(dataObject)) {
            TopicUtilities.syncXML(entityManager, entity);
            TopicUtilities.validateXML(entityManager, entity);
        }

        // Only update the minhash if we have a change that requires it (ie a new topic, or the XML has changed)
        if (dataObject.getId() == null || requiresXMLProcessing(dataObject)) {
            // Update the minhash signature (or skip if the min hash xors have not been created.
            final List<MinHashXOR> minHashXORs = cachedEntityLoader.getXOREntities();
            if (minHashXORs.size() == org.jboss.pressgang.ccms.model.constants.Constants.NUM_MIN_HASHES - 1) {
                org.jboss.pressgang.ccms.model.utils.TopicUtilities.recalculateMinHash(entity, minHashXORs);
            }
        }
    }

    @Override
    protected void doDeleteChildAction(final Topic entity, final RESTTopicV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        if (restEntity instanceof RESTTagV1) {
            final Tag dbEntity = entityManager.find(Tag.class, restEntity.getId());
            if (dbEntity == null) throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());

            entity.removeTag(dbEntity);
        } else if (restEntity instanceof RESTAssignedPropertyTagV1) {
            final RESTAssignedPropertyTagV1 propertyTag = (RESTAssignedPropertyTagV1) restEntity;
            final TopicToPropertyTag dbEntity = entityManager.find(TopicToPropertyTag.class, propertyTag.getRelationshipId());
            if (dbEntity == null) throw new BadRequestException(
                    "No TopicToPropertyTag entity was found with the primary key " + propertyTag.getRelationshipId());

            entity.removePropertyTag(dbEntity);
        } else if (restEntity instanceof RESTTopicSourceUrlV1) {
            final TopicSourceUrl dbEntity = entityManager.find(TopicSourceUrl.class, restEntity.getId());
            if (dbEntity == null) {
                throw new BadRequestException("No TopicSourceUrl entity was found with the primary key " + restEntity.getId());
            }

            entity.removeTopicSourceUrl(dbEntity);
        } else if (restEntity instanceof RESTTopicV1) {
            final Topic dbEntity = entityManager.find(Topic.class, restEntity.getId());
            if (dbEntity == null) {
                throw new BadRequestException("No Topic entity was found with the primary key " + restEntity.getId());
            }

            if (RESTTopicV1.INCOMING_NAME.equals(action.getUniqueId())) {
                dbEntity.removeRelationshipTo(entity.getTopicId(), 1);
            } else {
                entity.removeRelationshipFrom(dbEntity.getTopicId(), 1);
            }
        } else if (restEntity instanceof RESTBugzillaBugV1) {
            final BugzillaBug dbEntity = entityManager.find(BugzillaBug.class, restEntity.getId());
            if (dbEntity == null) {
                throw new BadRequestException("No BugzillaBug entity was found with the primary key " + restEntity.getId());
            }

            entity.removeBugzillaBug(dbEntity.getId());
        }
    }

    @Override
    protected AuditedEntity doCreateChildAction(final Topic entity, final RESTTopicV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();
        final AuditedEntity dbEntity;

        if (restEntity instanceof RESTTagV1) {
            dbEntity = entityManager.find(Tag.class, restEntity.getId());
            if (dbEntity == null) {
                throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());
            }

            entity.addTag((Tag) dbEntity);
        } else if (restEntity instanceof RESTAssignedPropertyTagV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            final PropertyTag propertyTag = entityManager.find(PropertyTag.class, restEntity.getId());
            if (propertyTag == null) {
                throw new BadRequestException("No PropertyTag entity was found with the primary key " + restEntity.getId());
            }

            final TopicToPropertyTag topicToProp = (TopicToPropertyTag) dbEntity;
            topicToProp.setPropertyTag(propertyTag);
            entity.addPropertyTag(topicToProp);
        } else if (restEntity instanceof RESTTopicSourceUrlV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            entity.addTopicSourceUrl((TopicSourceUrl) dbEntity);
        } else if (restEntity instanceof RESTTopicV1) {
            dbEntity = entityManager.find(Topic.class, restEntity.getId());
            if (dbEntity == null) {
                throw new BadRequestException("No Topic entity was found with the primary key " + restEntity.getId());
            }

            if (RESTTopicV1.INCOMING_NAME.equals(action.getUniqueId())) {
                entity.addRelationshipFrom(entityManager, restEntity.getId(), 1);
            } else {
                entity.addRelationshipTo(entityManager, restEntity.getId(), 1);
            }
        } else if (restEntity instanceof RESTBugzillaBugV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            entity.addBugzillaBug((BugzillaBug) dbEntity);
        } else {
            throw new IllegalArgumentException("Item is not a child of Topic");
        }

        return dbEntity;
    }

    @Override
    protected AuditedEntity getChildEntityForAction(final Topic entity, final RESTTopicV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        final AuditedEntity dbEntity;
        if (restEntity instanceof RESTTagV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTTagV1) restEntity, Tag.class);
            if (dbEntity == null) {
                throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getTags().contains(dbEntity)) {
                throw new BadRequestException(
                        "No Tag entity was found with the primary key " + restEntity.getId() + " for Topic " + entity.getId());
            }
        } else if (restEntity instanceof RESTAssignedPropertyTagV1) {
            final RESTAssignedPropertyTagV1 assignedPropertyTag = (RESTAssignedPropertyTagV1) restEntity;
            dbEntity = entityManager.find(TopicToPropertyTag.class, assignedPropertyTag.getRelationshipId());
            if (dbEntity == null) {
                throw new BadRequestException("No TopicToPropertyTag entity was found with the primary key " + assignedPropertyTag
                        .getRelationshipId());
            } else if (!entity.getTopicToPropertyTags().contains(dbEntity)) {
                throw new BadRequestException(
                        "No TopicToPropertyTag entity was found with the primary key " + assignedPropertyTag.getRelationshipId() +
                                " for Topic " + entity.getId());
            }
        } else if (restEntity instanceof RESTTopicSourceUrlV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTTopicSourceUrlV1) restEntity, TopicSourceUrl.class);
            if (dbEntity == null) {
                throw new BadRequestException("No TopicSourceUrl entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getTopicSourceUrls().contains(dbEntity)) {
                throw new BadRequestException(
                        "No TopicSourceUrl entity was found with the primary key " + restEntity.getId() + " for Topic " + entity.getId());
            }
        } else if (restEntity instanceof RESTTopicV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTTopicV1) restEntity, Topic.class);
            if (dbEntity == null) {
                throw new BadRequestException("No Topic entity was found with the primary key " + restEntity.getId());
            } else {
                if (RESTTopicV1.INCOMING_NAME.equals(action.getUniqueId()) && !entity.getIncomingRelatedTopicsArray().contains(dbEntity)) {
                    throw new BadRequestException(
                            "No Topic entity was found with the primary key " + restEntity.getId() + " for Topic " + entity.getId());
                } else if (RESTTopicV1.OUTGOING_NAME.equals(action.getUniqueId()) && !entity.getOutgoingRelatedTopicsArray().contains
                        (dbEntity)) {
                    throw new BadRequestException(
                            "No Topic entity was found with the primary key " + restEntity.getId() + " for Topic " + entity.getId());
                }
            }
        } else if (restEntity instanceof RESTBugzillaBugV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTBugzillaBugV1) restEntity, BugzillaBug.class);
            if (dbEntity == null) {
                throw new BadRequestException("No BugzillaBug entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getTopicSourceUrls().contains(dbEntity)) {
                throw new BadRequestException(
                        "No BugzillaBug entity was found with the primary key " + restEntity.getId() + " for Topic " + entity.getId());
            }
        } else {
            throw new IllegalArgumentException("Item is not a child of Topic");
        }

        return dbEntity;
    }

    protected boolean requiresXMLProcessing(final RESTTopicV1 dataObject) {
        return dataObject.hasParameterSet(RESTTopicV1.XML_NAME)
                || dataObject.hasParameterSet(RESTTopicV1.FORMAT_NAME)
                || dataObject.hasParameterSet(RESTTopicV1.LOCALE_NAME)
                || dataObject.hasParameterSet(RESTTopicV1.TITLE_NAME)
                || dataObject.hasParameterSet(RESTTopicV1.TAGS_NAME);
    }

    @Override
    protected Class<Topic> getDatabaseClass() {
        return Topic.class;
    }
}
