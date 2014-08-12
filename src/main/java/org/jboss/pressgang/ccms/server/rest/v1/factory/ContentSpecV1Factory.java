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
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.Locale;
import org.jboss.pressgang.ccms.model.PropertyTag;
import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.model.contentspec.CSNode;
import org.jboss.pressgang.ccms.model.contentspec.ContentSpec;
import org.jboss.pressgang.ccms.model.contentspec.ContentSpecToPropertyTag;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTProcessInformationCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTopicCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTCSNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTContentSpecCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTTranslatedContentSpecCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTContentSpecCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTAssignedPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTLocaleV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTContentSpecV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.enums.RESTContentSpecTypeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTAssignedPropertyTagV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTElementCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.ContentSpecUtilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class ContentSpecV1Factory extends RESTEntityFactory<RESTContentSpecV1, ContentSpec, RESTContentSpecCollectionV1,
        RESTContentSpecCollectionItemV1> {
    @Inject
    protected LocaleV1Factory localeFactory;
    @Inject
    protected CSNodeV1Factory csNodeFactory;
    @Inject
    protected ContentSpecPropertyTagV1Factory contentSpecPropertyTagFactory;
    @Inject
    protected TranslatedContentSpecV1Factory translatedContentSpecFactory;
    @Inject
    protected TagV1Factory tagFactory;
    @Inject
    protected TopicV1Factory topicFactory;
    @Inject
    protected ProcessInformationV1Factory processInformationFactory;

    @Override
    public RESTContentSpecV1 createRESTEntityFromDBEntityInternal(final ContentSpec entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTContentSpecV1 retValue = new RESTContentSpecV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        expandOptions.add(RESTContentSpecV1.ALL_CHILDREN_NAME);
        expandOptions.add(RESTContentSpecV1.CHILDREN_NAME);
        expandOptions.add(RESTContentSpecV1.PROPERTIES_NAME);
        expandOptions.add(RESTContentSpecV1.BOOK_TAGS_NAME);
        expandOptions.add(RESTContentSpecV1.TAGS_NAME);
        expandOptions.add(RESTContentSpecV1.TOPICS_NAME);
        expandOptions.add(RESTContentSpecV1.TRANSLATED_CONTENT_SPECS_NAME);
        expandOptions.add(RESTContentSpecV1.PROCESSES_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setCondition(entity.getCondition());
        retValue.setType(RESTContentSpecTypeV1.getContentSpecType(entity.getContentSpecType()));
        retValue.setLastPublished(entity.getLastPublished());
        retValue.setLastModified(EnversUtilities.getFixedLastModifiedDate(entityManager, entity));
        retValue.setErrors(entity.getErrors());
        retValue.setFailedContentSpec(ContentSpecUtilities.fixFailedContentSpec(entity));

        // LOCALE
        if (entity.getLocale() != null) {
            final ExpandDataTrunk childExpand = expand == null ? null: expand.get(RESTContentSpecV1.LOCALE_NAME);
            retValue.setLocale(localeFactory.createRESTEntityFromDBEntity(entity.getLocale(), baseUrl, dataType, childExpand, revision));
        }

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTContentSpecCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // ALL CHILDREN NODES
        if (expand != null && expand.contains(RESTContentSpecV1.ALL_CHILDREN_NAME)) {
            retValue.setAllChildren(RESTEntityCollectionFactory.create(RESTCSNodeCollectionV1.class, csNodeFactory,
                    new ArrayList<CSNode>(entity.getCSNodes()), RESTContentSpecV1.ALL_CHILDREN_NAME, dataType, expand, baseUrl, revision,
                    expandParentReferences, entityManager));
        }

        // CHILDREN NODES
        if (expand != null && expand.contains(RESTContentSpecV1.CHILDREN_NAME)) {
            retValue.setChildren_OTM(
                    RESTEntityCollectionFactory.create(RESTCSNodeCollectionV1.class, csNodeFactory, entity.getChildrenList(),
                            RESTContentSpecV1.CHILDREN_NAME, dataType, expand, baseUrl, revision, expandParentReferences, entityManager));
        }

        // PROPERTY TAGS
        if (expand != null && expand.contains(RESTContentSpecV1.PROPERTIES_NAME)) {
            retValue.setProperties(
                    RESTEntityCollectionFactory.create(RESTAssignedPropertyTagCollectionV1.class, contentSpecPropertyTagFactory,
                            entity.getPropertyTagsList(), RESTContentSpecV1.PROPERTIES_NAME, dataType, expand, baseUrl, revision,
                            entityManager));
        }

        // BOOK TAGS
        if (expand != null && expand.contains(RESTContentSpecV1.BOOK_TAGS_NAME)) {
            retValue.setBookTags(RESTEntityCollectionFactory.create(RESTTagCollectionV1.class, tagFactory, entity.getBookTags(),
                    RESTContentSpecV1.BOOK_TAGS_NAME, dataType, expand, baseUrl, revision, entityManager));
        }

        // TAGS
        if (expand != null && expand.contains(RESTContentSpecV1.TAGS_NAME)) {
            retValue.setTags(
                    RESTEntityCollectionFactory.create(RESTTagCollectionV1.class, tagFactory, entity.getTags(), RESTContentSpecV1.TAGS_NAME,
                            dataType, expand, baseUrl, revision, entityManager));
        }

        // TRANSLATIONS
        if (expand != null && expand.contains(RESTContentSpecV1.TRANSLATED_CONTENT_SPECS_NAME)) {
            retValue.setTranslatedContentSpecs(
                    RESTEntityCollectionFactory.create(RESTTranslatedContentSpecCollectionV1.class, translatedContentSpecFactory,
                            entity.getTranslatedContentSpecs(entityManager, revision), RESTContentSpecV1.TRANSLATED_CONTENT_SPECS_NAME,
                            dataType, expand, baseUrl, entityManager));
        }

        // TOPICS
        if (expand != null && expand.contains(RESTContentSpecV1.TOPICS_NAME)) {
            retValue.setTopics(
                    RESTEntityCollectionFactory.create(RESTTopicCollectionV1.class, topicFactory, entity.getTopics(entityManager),
                            RESTContentSpecV1.TOPICS_NAME, dataType, expand, baseUrl, entityManager));
        }

        // PROCESSES
        if (expand != null && expand.contains(RESTContentSpecV1.PROCESSES_NAME)) {
            retValue.setProcesses(
                    RESTElementCollectionFactory.create(RESTProcessInformationCollectionV1.class, processInformationFactory,
                            entity.getProcesses(), RESTContentSpecV1.PROCESSES_NAME, dataType, expand, baseUrl));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.CONTENT_SPEC_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void syncBaseDetails(final ContentSpec entity, final RESTContentSpecV1 dataObject) {
        if (dataObject.hasParameterSet(RESTContentSpecV1.CONDITION_NAME)) entity.setCondition(dataObject.getCondition());
        if (dataObject.hasParameterSet(RESTContentSpecV1.LAST_PUBLISHED_NAME)) entity.setLastPublished(dataObject.getLastPublished());
        if (dataObject.hasParameterSet(RESTContentSpecV1.TYPE_NAME))
            entity.setContentSpecType(RESTContentSpecTypeV1.getContentSpecTypeId(dataObject.getType()));

        // Remove any error content
        entity.setErrors(null);
        entity.setFailedContentSpec(null);
    }

    @Override
    public void syncAdditionalDetails(final ContentSpec entity, final RESTContentSpecV1 dataObject) {
        // Set the Locale
        if (dataObject.hasParameterSet(RESTContentSpecV1.LOCALE_NAME)) {
            final RESTLocaleV1 restEntity = dataObject.getLocale();

            if (restEntity != null) {
                final Locale dbEntity;
                if (restEntity.getId() != null) {
                    dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, restEntity, Locale.class);
                } else {
                    dbEntity = localeFactory.createDBEntity(restEntity);
                }

                if (dbEntity == null)
                    throw new BadRequestException("No Locale entity was found with the primary key " + restEntity.getId());

                localeFactory.syncBaseDetails(dbEntity, restEntity);

                entity.setLocale(dbEntity);
            } else {
                entity.setLocale(null);
            }
        }
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTContentSpecV1> parent,
            final RESTContentSpecV1 dataObject) {
        if (dataObject.hasParameterSet(RESTContentSpecV1.CHILDREN_NAME)
                && dataObject.getChildren_OTM() != null
                && dataObject.getChildren_OTM().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getChildren_OTM(), csNodeFactory);
        }

        if (dataObject.hasParameterSet(RESTContentSpecV1.PROPERTIES_NAME)
                && dataObject.getProperties() != null
                && dataObject.getProperties().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getProperties(), contentSpecPropertyTagFactory);
        }

        if (dataObject.hasParameterSet(RESTContentSpecV1.BOOK_TAGS_NAME)
                && dataObject.getBookTags() != null
                && dataObject.getBookTags().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getBookTags(), tagFactory, RESTContentSpecV1.BOOK_TAGS_NAME);
        }

        if (dataObject.hasParameterSet(RESTContentSpecV1.TAGS_NAME)
                && dataObject.getTags() != null
                && dataObject.getTags().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getTags(), tagFactory, RESTContentSpecV1.TAGS_NAME);
        }
    }

    @Override
    protected void doDeleteChildAction(final ContentSpec entity, final RESTContentSpecV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        if (restEntity instanceof RESTTagV1) {
            final Tag dbEntity = entityManager.find(Tag.class, restEntity.getId());
            if (dbEntity == null) throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());

            if (RESTContentSpecV1.BOOK_TAGS_NAME.equals(action.getUniqueId())) {
                entity.removeBookTag(dbEntity);
            } else {
                entity.removeTag(dbEntity);
            }
        } else if (restEntity instanceof RESTAssignedPropertyTagV1) {
            final RESTAssignedPropertyTagV1 propertyTag = (RESTAssignedPropertyTagV1) restEntity;
            final ContentSpecToPropertyTag dbEntity = entityManager.find(ContentSpecToPropertyTag.class, propertyTag.getRelationshipId());
            if (dbEntity == null) throw new BadRequestException(
                    "No ContentSpecToPropertyTag entity was found with the primary key " + propertyTag.getRelationshipId());

            entity.removePropertyTag(dbEntity);
        } else if (restEntity instanceof RESTCSNodeV1) {
            final CSNode dbEntity = entityManager.find(CSNode.class, restEntity.getId());
            if (dbEntity == null)
                throw new BadRequestException("No CSNode entity was found with the primary key " + restEntity.getId());

            entity.removeChild(dbEntity);
            entityManager.remove(dbEntity);
        }
    }

    @Override
    protected AuditedEntity doCreateChildAction(final ContentSpec entity, final RESTContentSpecV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();
        final AuditedEntity dbEntity;

        if (restEntity instanceof RESTTagV1) {
            dbEntity = entityManager.find(Tag.class, restEntity.getId());
            if (dbEntity == null) {
                throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());
            }

            if (RESTContentSpecV1.BOOK_TAGS_NAME.equals(action.getUniqueId())) {
                entity.addBookTag((Tag) dbEntity);
            } else {
                entity.addTag((Tag) dbEntity);
            }
        } else if (restEntity instanceof RESTAssignedPropertyTagV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            final PropertyTag propertyTag = entityManager.find(PropertyTag.class, restEntity.getId());
            if (propertyTag == null) {
                throw new BadRequestException("No PropertyTag entity was found with the primary key " + restEntity.getId());
            }

            final ContentSpecToPropertyTag csToProp = (ContentSpecToPropertyTag) dbEntity;
            csToProp.setPropertyTag(propertyTag);
            entity.addPropertyTag(csToProp);
        } else if (restEntity instanceof RESTCSNodeV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            final CSNode csNodeEntity = (CSNode) dbEntity;
            csNodeEntity.setParent(null);
            entity.addChild(csNodeEntity);
        } else {
            throw new IllegalArgumentException("Item is not a child of ContentSpec");
        }

        return dbEntity;
    }

    @Override
    protected AuditedEntity getChildEntityForAction(final ContentSpec entity, final RESTContentSpecV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        final AuditedEntity dbEntity;
        if (restEntity instanceof RESTTagV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTTagV1) restEntity, Tag.class);
            if (dbEntity == null) {
                throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());
            } else {
                if (RESTContentSpecV1.BOOK_TAGS_NAME.equals(action.getUniqueId()) && !entity.getBookTags().contains(dbEntity)) {
                    throw new BadRequestException(
                            "No Tag entity was found with the primary key " + restEntity.getId() + " for ContentSpec " + entity.getId());
                } else if (RESTContentSpecV1.TAGS_NAME.equals(action.getUniqueId()) && !entity.getTags().contains(dbEntity)) {
                    throw new BadRequestException(
                            "No Tag entity was found with the primary key " + restEntity.getId() + " for ContentSpec " + entity.getId());
                }
            }
        } else if (restEntity instanceof RESTAssignedPropertyTagV1) {
            final RESTAssignedPropertyTagV1 assignedPropertyTag = (RESTAssignedPropertyTagV1) restEntity;
            dbEntity = entityManager.find(ContentSpecToPropertyTag.class, assignedPropertyTag.getRelationshipId());
            if (dbEntity == null) {
                throw new BadRequestException("No ContentSpecToPropertyTag entity was found with the primary key " + assignedPropertyTag
                        .getRelationshipId());
            } else if (!entity.getContentSpecToPropertyTags().contains(dbEntity)) {
                throw new BadRequestException(
                        "No ContentSpecToPropertyTag entity was found with the primary key " + assignedPropertyTag.getRelationshipId() +
                                " for ContentSpec " + entity.getId());
            }
        } else if (restEntity instanceof RESTCSNodeV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTCSNodeV1) restEntity, CSNode.class);
            if (dbEntity == null) {
                throw new BadRequestException("No CSNode entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getCSNodes().contains(dbEntity)) {
                throw new BadRequestException(
                        "No CSNode entity was found with the primary key " + restEntity.getId() + " for ContentSpec " + entity.getId());
            }
        } else {
            throw new IllegalArgumentException("Item is not a child of ContentSpec");
        }

        return dbEntity;
    }

    @Override
    protected Class<ContentSpec> getDatabaseClass() {
        return ContentSpec.class;
    }
}
