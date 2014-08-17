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

import org.jboss.pressgang.ccms.model.TranslatedTopicData;
import org.jboss.pressgang.ccms.model.base.PressGangEntity;
import org.jboss.pressgang.ccms.model.contentspec.TranslatedCSNode;
import org.jboss.pressgang.ccms.model.contentspec.TranslatedCSNodeString;
import org.jboss.pressgang.ccms.model.contentspec.TranslatedContentSpec;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTranslatedTopicCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTTranslatedCSNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTTranslatedCSNodeStringCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTTranslatedCSNodeCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTranslatedTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseAuditedEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTranslatedCSNodeStringV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTranslatedCSNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTranslatedContentSpecV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class TranslatedCSNodeV1Factory extends RESTEntityFactory<RESTTranslatedCSNodeV1, TranslatedCSNode,
        RESTTranslatedCSNodeCollectionV1, RESTTranslatedCSNodeCollectionItemV1> {
    @Inject
    protected CSNodeV1Factory csNodeFactory;
    @Inject
    protected TranslatedContentSpecV1Factory translatedContentSpecFactory;
    @Inject
    protected TranslatedCSNodeStringV1Factory translatedCSNodeStringFactory;
    @Inject
    protected TranslatedTopicV1Factory translatedTopicFactory;

    @Override
    protected RESTTranslatedCSNodeV1 createRESTEntityFromDBEntityInternal(TranslatedCSNode entity, String baseUrl, String dataType,
            ExpandDataTrunk expand, Number revision, boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTTranslatedCSNodeV1 retValue = new RESTTranslatedCSNodeV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseAuditedEntityV1.LOG_DETAILS_NAME);
        expandOptions.add(RESTTranslatedCSNodeV1.NODE_NAME);
        expandOptions.add(RESTTranslatedCSNodeV1.TRANSLATED_STRING_NAME);
        expandOptions.add(RESTTranslatedCSNodeV1.TRANSLATED_CONTENT_SPEC_NAME);
        expandOptions.add(RESTTranslatedCSNodeV1.TRANSLATED_TOPICS_NAME);
        if (revision == null) expandOptions.add(RESTBaseAuditedEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setNodeId(entity.getCSNodeId());
        retValue.setNodeRevision(entity.getCSNodeRevision());
        retValue.setOriginalString(entity.getOriginalString());

        // NODE
        if (expandParentReferences && expand != null && expand.contains(RESTTranslatedCSNodeV1.NODE_NAME) && entity.getEnversCSNode(
                entityManager) != null) {
            retValue.setNode(csNodeFactory.createRESTEntityFromDBEntity(entity.getEnversCSNode(entityManager), baseUrl, dataType,
                    expand.get(RESTTranslatedCSNodeV1.NODE_NAME), entity.getCSNodeRevision(), true));
            retValue.getNode().setRevision(entity.getCSNodeRevision());
        }

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseAuditedEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTTranslatedCSNodeCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseAuditedEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // TRANSLATED STRINGS
        if (expand != null && expand.contains(RESTTranslatedCSNodeV1.TRANSLATED_STRING_NAME)) {
            retValue.setTranslatedNodeStrings_OTM(
                    RESTEntityCollectionFactory.create(RESTTranslatedCSNodeStringCollectionV1.class, translatedCSNodeStringFactory,
                            entity.getTranslatedCSNodeStringsArray(), RESTTranslatedCSNodeV1.TRANSLATED_STRING_NAME, dataType, expand,
                            baseUrl, revision, false, entityManager));
        }

        // TRANSLATED CONTENT SPEC
        if (expandParentReferences && expand != null && expand.contains(
                RESTTranslatedCSNodeV1.TRANSLATED_CONTENT_SPEC_NAME) && entity.getTranslatedContentSpec() != null) {
            retValue.setTranslatedContentSpec(
                    translatedContentSpecFactory.createRESTEntityFromDBEntity(entity.getTranslatedContentSpec(), baseUrl, dataType,
                            expand.get(RESTTranslatedCSNodeV1.TRANSLATED_CONTENT_SPEC_NAME), revision, true));
        }

        // TRANSLATED TOPICS
        if (expand != null && expand.contains(RESTTranslatedCSNodeV1.TRANSLATED_TOPICS_NAME)) {
            retValue.setTranslatedTopics_OTM(
                    RESTEntityCollectionFactory.create(RESTTranslatedTopicCollectionV1.class, translatedTopicFactory,
                            new ArrayList<TranslatedTopicData>(entity.getTranslatedTopicDatas()),
                            RESTTranslatedCSNodeV1.TRANSLATED_TOPICS_NAME, dataType, expand, baseUrl, revision, true, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.CONTENT_SPEC_TRANSLATED_NODE_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(RESTChangeAction<RESTTranslatedCSNodeV1> parent, RESTTranslatedCSNodeV1 dataObject) {
        if (dataObject.getConfiguredParameters().contains(RESTTranslatedCSNodeV1.TRANSLATED_TOPICS_NAME)
                && dataObject.getTranslatedTopics_OTM() != null
                && dataObject.getTranslatedTopics_OTM().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getTranslatedTopics_OTM(), translatedTopicFactory);
        }

        if (dataObject.getConfiguredParameters().contains(RESTTranslatedCSNodeV1.TRANSLATED_STRING_NAME)
                && dataObject.getTranslatedNodeStrings_OTM() != null
                && dataObject.getTranslatedNodeStrings_OTM().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getTranslatedNodeStrings_OTM(), translatedCSNodeStringFactory);
        }
    }

    @Override
    public void syncBaseDetails(final TranslatedCSNode entity, final RESTTranslatedCSNodeV1 dataObject) {
        if (dataObject.hasParameterSet(RESTTranslatedCSNodeV1.NODE_ID_NAME)) entity.setCSNodeId(dataObject.getNodeId());
        if (dataObject.hasParameterSet(RESTTranslatedCSNodeV1.NODE_REVISION_NAME)) entity.setCSNodeRevision(dataObject.getNodeRevision());
        if (dataObject.hasParameterSet(RESTTranslatedCSNodeV1.ORIGINALSTRING_NAME))
            entity.setOriginalString(dataObject.getOriginalString());
    }

    @Override
    public void syncAdditionalDetails(final TranslatedCSNode entity, final RESTTranslatedCSNodeV1 dataObject) {
        // Translated Content Spec
        if (dataObject.hasParameterSet(RESTTranslatedCSNodeV1.TRANSLATED_CONTENT_SPEC_NAME)) {
            final RESTTranslatedContentSpecV1 restEntity = dataObject.getTranslatedContentSpec();
            final TranslatedContentSpec dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, restEntity,
                    TranslatedContentSpec.class);
            if (dbEntity == null) {
                throw new BadRequestException("No TranslatedContentSpec entity was found with the primary key " + restEntity.getId());
            }
            dbEntity.addTranslatedNode(entity);
        }
    }

    @Override
    protected void doDeleteChildAction(final TranslatedCSNode entity, final RESTTranslatedCSNodeV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();

        if (restEntity instanceof RESTTranslatedCSNodeStringV1) {
            final TranslatedCSNodeString dbEntity = entityManager.find(TranslatedCSNodeString.class, restEntity.getId());
            if (dbEntity == null) throw new BadRequestException(
                    "No TranslatedCSNodeString entity was found with the primary key " + restEntity.getId());

            entity.removeTranslatedString(dbEntity);
            entityManager.remove(dbEntity);
        } else if (restEntity instanceof RESTTranslatedTopicV1) {
            final TranslatedTopicData dbEntity = entityManager.find(TranslatedTopicData.class, restEntity.getId());
            if (dbEntity == null) throw new BadRequestException(
                    "No TranslatedTopicData entity was found with the primary key " + restEntity.getId());

            entity.removeTranslatedTopicData(dbEntity.getTranslatedTopic());
        }
    }

    @Override
    protected PressGangEntity doCreateChildAction(final TranslatedCSNode entity, final RESTTranslatedCSNodeV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();
        final PressGangEntity dbEntity;

        if (restEntity instanceof RESTTranslatedCSNodeStringV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            entity.addTranslatedString((TranslatedCSNodeString) dbEntity);
        } else if (restEntity instanceof RESTTranslatedTopicV1) {
            dbEntity = entityManager.find(TranslatedTopicData.class, restEntity.getId());
            if (dbEntity == null) {
                throw new BadRequestException("No TranslatedTopicData entity was found with the primary key " + restEntity.getId());
            }

            entity.addTranslatedTopic(((TranslatedTopicData) dbEntity).getTranslatedTopic());
        } else {
            throw new IllegalArgumentException("Item is not a child of TranslatedContentSpec");
        }

        return dbEntity;
    }

    @Override
    protected PressGangEntity getChildEntityForAction(final TranslatedCSNode entity, final RESTTranslatedCSNodeV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();

        final PressGangEntity dbEntity;
        if (restEntity instanceof RESTTranslatedCSNodeStringV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTTranslatedCSNodeStringV1) restEntity,
                    TranslatedCSNodeString.class);
            if (dbEntity == null) {
                throw new BadRequestException("No TranslatedCSNodeString entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getTranslatedCSNodeStrings().contains(dbEntity)) {
                throw new BadRequestException(
                        "No TranslatedCSNodeString entity was found with the primary key " + restEntity.getId() + " for TranslatedCSNode "
                                + entity.getId());
            }
        } else if (restEntity instanceof RESTTranslatedTopicV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTTranslatedTopicV1) restEntity,
                    TranslatedTopicData.class);
            if (dbEntity == null) {
                throw new BadRequestException("No TranslatedTopicData entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getTranslatedTopicDatas().contains(((TranslatedTopicData) dbEntity).getTranslatedTopic())) {
                throw new BadRequestException(
                        "No TranslatedTopic entity was found with the primary key " + restEntity.getId() + " for TranslatedCSNode "
                                + entity.getId());
            }
        } else {
            throw new IllegalArgumentException("Item is not a child of TranslatedCSNode");
        }

        return dbEntity;
    }

    @Override
    protected Class<TranslatedCSNode> getDatabaseClass() {
        return TranslatedCSNode.class;
    }
}
