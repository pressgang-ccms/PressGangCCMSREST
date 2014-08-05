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

import org.jboss.pressgang.ccms.model.PropertyTag;
import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.model.contentspec.CSNode;
import org.jboss.pressgang.ccms.model.contentspec.ContentSpec;
import org.jboss.pressgang.ccms.model.contentspec.ContentSpecToPropertyTag;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTProcessInformationCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTTextContentSpecCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTTranslatedContentSpecCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTTextContentSpecCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTAssignedPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTContentSpecV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTextContentSpecV1;
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
public class TextContentSpecV1Factory extends RESTEntityFactory<RESTTextContentSpecV1, ContentSpec, RESTTextContentSpecCollectionV1,
        RESTTextContentSpecCollectionItemV1> {
    @Inject
    protected ContentSpecPropertyTagV1Factory contentSpecPropertyTagFactory;
    @Inject
    protected TranslatedContentSpecV1Factory translatedContentSpecFactory;
    @Inject
    protected TagV1Factory tagFactory;
    @Inject
    protected ProcessInformationV1Factory processInformationFactory;

    @Override
    public RESTTextContentSpecV1 createRESTEntityFromDBEntityInternal(final ContentSpec entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTTextContentSpecV1 retValue = new RESTTextContentSpecV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        expandOptions.add(RESTTextContentSpecV1.PROPERTIES_NAME);
        expandOptions.add(RESTTextContentSpecV1.TAGS_NAME);
        expandOptions.add(RESTTextContentSpecV1.TEXT_NAME);
        expandOptions.add(RESTTextContentSpecV1.TRANSLATED_CONTENT_SPECS_NAME);
        expandOptions.add(RESTTextContentSpecV1.PROCESSES_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setLocale(entity.getLocale());
        retValue.setType(RESTContentSpecTypeV1.getContentSpecType(entity.getContentSpecType()));
        retValue.setLastPublished(entity.getLastPublished());
        retValue.setLastModified(EnversUtilities.getFixedLastModifiedDate(entityManager, entity));
        retValue.setErrors(entity.getErrors());
        retValue.setFailedContentSpec(ContentSpecUtilities.fixFailedContentSpec(entity));
        final CSNode titleNode = entity.getContentSpecTitle();
        retValue.setTitle(titleNode == null ? null : titleNode.getAdditionalText());
        final CSNode productNode = entity.getContentSpecProduct();
        retValue.setProduct(productNode == null ? null : productNode.getAdditionalText());
        final CSNode versionNode = entity.getContentSpecVersion();
        retValue.setVersion(versionNode == null ? null : versionNode.getAdditionalText());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTTextContentSpecCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // TEXT
        if (expand != null && expand.contains(RESTTextContentSpecV1.TEXT_NAME)) {
            final String text = ContentSpecUtilities.getContentSpecText(entity.getId(), (Integer) revision, entityManager);
            retValue.setText(text);
        }

        // PROPERTY TAGS
        if (expand != null && expand.contains(RESTContentSpecV1.PROPERTIES_NAME)) {
            retValue.setProperties(
                    RESTEntityCollectionFactory.create(RESTAssignedPropertyTagCollectionV1.class, contentSpecPropertyTagFactory,
                            entity.getPropertyTagsList(), RESTTextContentSpecV1.PROPERTIES_NAME, dataType, expand, baseUrl, revision,
                            entityManager));
        }

        // TAGS
        if (expand != null && expand.contains(RESTContentSpecV1.TAGS_NAME)) {
            retValue.setTags(RESTEntityCollectionFactory.create(RESTTagCollectionV1.class, tagFactory, entity.getTags(),
                    RESTTextContentSpecV1.TAGS_NAME, dataType, expand, baseUrl, revision, entityManager));
        }

        // TRANSLATIONS
        if (expand != null && expand.contains(RESTContentSpecV1.TRANSLATED_CONTENT_SPECS_NAME)) {
            retValue.setTranslatedContentSpecs(
                    RESTEntityCollectionFactory.create(RESTTranslatedContentSpecCollectionV1.class, translatedContentSpecFactory,
                            entity.getTranslatedContentSpecs(entityManager, revision), RESTTextContentSpecV1.TRANSLATED_CONTENT_SPECS_NAME,
                            dataType, expand, baseUrl, entityManager));
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
    public void syncBaseDetails(final ContentSpec entity, final RESTTextContentSpecV1 dataObject) {
        if (dataObject.hasParameterSet(RESTContentSpecV1.LOCALE_NAME)) entity.setLocale(dataObject.getLocale());
        if (dataObject.hasParameterSet(RESTContentSpecV1.LAST_PUBLISHED_NAME)) entity.setLastPublished(dataObject.getLastPublished());
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTTextContentSpecV1> parent,
            final RESTTextContentSpecV1 dataObject) {
        if (dataObject.hasParameterSet(RESTTextContentSpecV1.PROPERTIES_NAME)
                && dataObject.getProperties() != null
                && dataObject.getProperties().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getProperties(), contentSpecPropertyTagFactory);
        }

        if (dataObject.hasParameterSet(RESTTextContentSpecV1.TAGS_NAME)
                && dataObject.getTags() != null
                && dataObject.getTags().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getTags(), tagFactory, RESTTextContentSpecV1.TAGS_NAME);
        }
    }

    @Override
    protected void doDeleteChildAction(final ContentSpec entity, final RESTTextContentSpecV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        if (restEntity instanceof RESTTagV1) {
            final Tag dbEntity = entityManager.find(Tag.class, restEntity.getId());
            if (dbEntity == null) throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());

            entity.removeTag(dbEntity);
        } else if (restEntity instanceof RESTAssignedPropertyTagV1) {
            final RESTAssignedPropertyTagV1 propertyTag = (RESTAssignedPropertyTagV1) restEntity;
            final ContentSpecToPropertyTag dbEntity = entityManager.find(ContentSpecToPropertyTag.class, propertyTag.getRelationshipId());
            if (dbEntity == null) throw new BadRequestException(
                    "No ContentSpecToPropertyTag entity was found with the primary key " + propertyTag.getRelationshipId());

            entity.removePropertyTag(dbEntity);
        }
    }

    @Override
    protected AuditedEntity doCreateChildAction(final ContentSpec entity, final RESTTextContentSpecV1 dataObject,
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

            final ContentSpecToPropertyTag csToProp = (ContentSpecToPropertyTag) dbEntity;
            csToProp.setPropertyTag(propertyTag);
            entity.addPropertyTag(csToProp);
        } else {
            throw new IllegalArgumentException("Item is not a child of ContentSpec");
        }

        return dbEntity;
    }

    @Override
    protected AuditedEntity getChildEntityForAction(final ContentSpec entity, final RESTTextContentSpecV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        final AuditedEntity dbEntity;
        if (restEntity instanceof RESTTagV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTTagV1) restEntity, Tag.class);
            if (dbEntity == null) {
                throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getTags().contains(dbEntity)) {
                throw new BadRequestException(
                        "No Tag entity was found with the primary key " + restEntity.getId() + " for ContentSpec " + entity.getId());
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
