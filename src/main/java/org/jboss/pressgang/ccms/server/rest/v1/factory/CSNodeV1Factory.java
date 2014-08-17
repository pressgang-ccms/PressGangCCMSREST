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

import static com.google.common.base.Strings.isNullOrEmpty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.PropertyTag;
import org.jboss.pressgang.ccms.model.base.PressGangEntity;
import org.jboss.pressgang.ccms.model.contentspec.CSInfoNode;
import org.jboss.pressgang.ccms.model.contentspec.CSNode;
import org.jboss.pressgang.ccms.model.contentspec.CSNodeToCSNode;
import org.jboss.pressgang.ccms.model.contentspec.CSNodeToPropertyTag;
import org.jboss.pressgang.ccms.model.contentspec.ContentSpecToPropertyTag;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTCSNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTTranslatedCSNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTCSNodeCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.join.RESTCSRelatedNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTAssignedPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseAuditedEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSInfoNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTContentSpecV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.enums.RESTCSNodeTypeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.join.RESTCSRelatedNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTAssignedPropertyTagV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class CSNodeV1Factory extends RESTEntityFactory<RESTCSNodeV1, CSNode, RESTCSNodeCollectionV1, RESTCSNodeCollectionItemV1> {
    @Inject
    protected ContentSpecV1Factory contentSpecFactory;
    @Inject
    protected CSNodePropertyTagV1Factory csNodePropertyTagFactory;
    @Inject
    protected CSRelatedNodeV1Factory csRelatedNodeFactory;
    @Inject
    protected CSNodeInfoV1Factory csNodeInfoFactory;
    @Inject
    protected TranslatedCSNodeV1Factory translatedCSNodeFactory;

    @Override
    public RESTCSNodeV1 createRESTEntityFromDBEntityInternal(final CSNode entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTCSNodeV1 retValue = new RESTCSNodeV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTCSNodeV1.CHILDREN_NAME);
        expandOptions.add(RESTCSNodeV1.INHERITED_CONDITION_NAME);
        expandOptions.add(RESTBaseAuditedEntityV1.LOG_DETAILS_NAME);
        expandOptions.add(RESTCSNodeV1.NEXT_NODE_NAME);
        expandOptions.add(RESTCSNodeV1.PARENT_NAME);
        expandOptions.add(RESTCSNodeV1.PROPERTIES_NAME);
        expandOptions.add(RESTCSNodeV1.RELATED_FROM_NAME);
        expandOptions.add(RESTCSNodeV1.RELATED_TO_NAME);
        expandOptions.add(RESTCSNodeV1.TRANSLATED_NODES_NAME);
        if (revision == null) expandOptions.add(RESTBaseAuditedEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setTitle(entity.getCSNodeTitle());
        retValue.setTargetId(entity.getCSNodeTargetId());
        retValue.setAdditionalText(entity.getAdditionalText());
        retValue.setCondition(entity.getCondition());
        retValue.setNodeType(RESTCSNodeTypeV1.getNodeType(entity.getCSNodeType()));
        retValue.setEntityId(entity.getEntityId());
        retValue.setEntityRevision(entity.getEntityRevision());

        if (entity.getCSNodeURL() != null) {
            retValue.setFixedUrl(entity.getCSNodeURL().getUrl());
        }

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseAuditedEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTCSNodeCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseAuditedEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // PARENT
        if (expandParentReferences && expand != null && expand.contains(RESTCSNodeV1.PARENT_NAME) && entity.getParent() != null) {
            retValue.setParent(
                    createRESTEntityFromDBEntity(entity.getParent(), baseUrl, dataType, expand.get(RESTCSNodeV1.PARENT_NAME), revision));
        }

        // CONTENT SPEC
        if (expand != null && expand.contains(RESTCSNodeV1.CONTENT_SPEC_NAME) && entity.getContentSpec() != null) {
            retValue.setContentSpec(contentSpecFactory.createRESTEntityFromDBEntity(entity.getContentSpec(), baseUrl, dataType,
                    expand.get(RESTCSNodeV1.CONTENT_SPEC_NAME), revision, expandParentReferences));
        }

        // INHERITED CONDITION
        if (expand != null && expand.contains(RESTCSNodeV1.INHERITED_CONDITION_NAME)) {
            retValue.setInheritedCondition(entity.getInheritedCondition());
        }

        // NEXT
        if (expand != null && expand.contains(RESTCSNodeV1.NEXT_NODE_NAME) && entity.getNext() != null) {
            retValue.setNextNode(
                    createRESTEntityFromDBEntity(entity.getNext(), baseUrl, dataType, expand.get(RESTCSNodeV1.NEXT_NODE_NAME), revision));
        }

        // CHILDREN NODES
        if (expand != null && expand.contains(RESTContentSpecV1.CHILDREN_NAME)) {
            retValue.setChildren_OTM(RESTEntityCollectionFactory.create(RESTCSNodeCollectionV1.class, this, entity.getChildrenList(),
                    RESTCSNodeV1.CHILDREN_NAME, dataType, expand, baseUrl, revision, expandParentReferences, entityManager));
        }

        // RELATED FROM
        if (expand != null && expand.contains(RESTCSNodeV1.RELATED_FROM_NAME)) {
            retValue.setRelatedFromNodes(RESTEntityCollectionFactory.create(RESTCSRelatedNodeCollectionV1.class, csRelatedNodeFactory,
                    entity.getRelatedFromNodesList(), RESTCSNodeV1.RELATED_FROM_NAME, dataType, expand, baseUrl, revision, entityManager));
        }

        // RELATED TO
        if (expand != null && expand.contains(RESTCSNodeV1.RELATED_TO_NAME)) {
            retValue.setRelatedToNodes(RESTEntityCollectionFactory.create(RESTCSRelatedNodeCollectionV1.class, csRelatedNodeFactory,
                    entity.getRelatedToNodesList(), RESTCSNodeV1.RELATED_TO_NAME, dataType, expand, baseUrl, revision, entityManager));
        }

        // TRANSLATED STRINGS
        if (expand != null && expand.contains(RESTCSNodeV1.TRANSLATED_NODES_NAME)) {
            retValue.setTranslatedNodes_OTM(
                    RESTEntityCollectionFactory.create(RESTTranslatedCSNodeCollectionV1.class, translatedCSNodeFactory,
                            entity.getTranslatedNodes(entityManager, revision), RESTCSNodeV1.TRANSLATED_NODES_NAME, dataType, expand,
                            baseUrl, false, entityManager));
        }

        // PROPERTY TAGS
        if (expand != null && expand.contains(RESTCSNodeV1.PROPERTIES_NAME)) {
            retValue.setProperties(
                    RESTEntityCollectionFactory.create(RESTAssignedPropertyTagCollectionV1.class, csNodePropertyTagFactory,
                            entity.getPropertyTagsList(), RESTCSNodeV1.PROPERTIES_NAME, dataType, expand, baseUrl, revision,
                            entityManager));
        }

        // CONTENT SPEC NODE INFO
        if (expand != null && expand.contains(RESTCSNodeV1.INFO_TOPIC_NODE_NAME) && entity.getCSInfoNode() != null) {
            retValue.setInfoTopicNode(csNodeInfoFactory.createRESTEntityFromDBEntity(entity.getCSInfoNode(), baseUrl, dataType,
                    expand.get(RESTCSNodeV1.INFO_TOPIC_NODE_NAME), revision, expandParentReferences));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.CONTENT_SPEC_NODE_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTCSNodeV1> parent, final RESTCSNodeV1 dataObject) {
        // ENTITIES
        if (dataObject.hasParameterSet(RESTCSNodeV1.INFO_TOPIC_NODE_NAME)) {
            collectChangeInformationFromEntity(parent, dataObject.getInfoTopicNode(), csNodeInfoFactory, RESTCSNodeV1.INFO_TOPIC_NODE_NAME);
        }

        // COLLECTIONS
        if (dataObject.hasParameterSet(RESTCSNodeV1.PROPERTIES_NAME)
                && dataObject.getProperties() != null
                && dataObject.getProperties().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getProperties(), csNodePropertyTagFactory);
        }

        if (dataObject.hasParameterSet(RESTCSNodeV1.CHILDREN_NAME)
                && dataObject.getChildren_OTM() != null
                && dataObject.getChildren_OTM().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getChildren_OTM(), this);
        }

        if (dataObject.hasParameterSet(RESTCSNodeV1.RELATED_TO_NAME)
                && dataObject.getRelatedToNodes() != null
                && dataObject.getRelatedToNodes().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getRelatedToNodes(), csRelatedNodeFactory,
                    RESTCSNodeV1.RELATED_TO_NAME);
        }

        if (dataObject.hasParameterSet(RESTCSNodeV1.RELATED_FROM_NAME)
                && dataObject.getRelatedFromNodes() != null
                && dataObject.getRelatedFromNodes().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getRelatedFromNodes(), csRelatedNodeFactory,
                    RESTCSNodeV1.RELATED_FROM_NAME);
        }
    }

    @Override
    public void syncBaseDetails(final CSNode entity, final RESTCSNodeV1 dataObject) {
        if (dataObject.hasParameterSet(RESTCSNodeV1.TITLE_NAME)) entity.setCSNodeTitle(dataObject.getTitle());
        if (dataObject.hasParameterSet(RESTCSNodeV1.TARGET_ID_NAME)) entity.setCSNodeTargetId(dataObject.getTargetId());
        if (dataObject.hasParameterSet(RESTCSNodeV1.ADDITIONAL_TEXT_NAME)) entity.setAdditionalText(dataObject.getAdditionalText());
        if (dataObject.hasParameterSet(RESTCSNodeV1.CONDITION_NAME)) entity.setCondition(dataObject.getCondition());
        if (dataObject.hasParameterSet(RESTCSNodeV1.NODE_TYPE_NAME))
            entity.setCSNodeType(RESTCSNodeTypeV1.getNodeTypeId(dataObject.getNodeType()));
        if (dataObject.hasParameterSet(RESTCSNodeV1.ENTITY_ID_NAME)) entity.setEntityId(dataObject.getEntityId());
        if (dataObject.hasParameterSet(RESTCSNodeV1.ENTITY_REVISION_NAME)) entity.setEntityRevision(dataObject.getEntityRevision());
        if (dataObject.hasParameterSet(RESTCSNodeV1.FIXED_URL_NAME)) {
            // If the value is null or empty then we should remove the CSNodeURL
            if (isNullOrEmpty(dataObject.getFixedUrl()) && entity.getCSNodeURL() != null) {
                entityManager.remove(entity.getCSNodeURL());
                entity.setCSNodeURL(null);
            } else {
                entity.setFixedUrl(dataObject.getFixedUrl());
            }
        }
    }

    @Override
    public void syncAdditionalDetails(final CSNode entity, final RESTCSNodeV1 dataObject) {
        // Set the Next Node
        if (dataObject.hasParameterSet(RESTCSNodeV1.NEXT_NODE_NAME)) {
            final RESTCSNodeV1 restEntity = dataObject.getNextNode();

            if (restEntity != null) {
                final CSNode dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, restEntity, CSNode.class);
                if (dbEntity == null)
                    throw new BadRequestException("No CSNode entity was found with the primary key " + restEntity.getId());

                entity.setNextAndClean(dbEntity);
            } else if (entity.getNext() != null) {
                entity.getNext().setPrevious(null);
                entity.setNext(null);
            } else {
                entity.setNext(null);
            }
        }
    }

    @Override
    protected void doDeleteChildAction(final CSNode entity, final RESTCSNodeV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();

        if (restEntity instanceof RESTCSRelatedNodeV1) {
            final RESTCSRelatedNodeV1 relatedNode = (RESTCSRelatedNodeV1) restEntity;
            final CSNodeToCSNode dbEntity = entityManager.find(CSNodeToCSNode.class, relatedNode.getRelationshipId());
            if (dbEntity == null) {
                throw new BadRequestException("No CSNodeToCSNode entity was found with the primary key " + relatedNode.getRelationshipId
                        ());
            }

            if (RESTCSNodeV1.RELATED_FROM_NAME.equals(action.getUniqueId())) {
                entity.removeRelationshipFrom(dbEntity);
            } else {
                entity.removeRelationshipFrom(dbEntity);
            }
        } else if (restEntity instanceof RESTAssignedPropertyTagV1) {
            final RESTAssignedPropertyTagV1 propertyTag = (RESTAssignedPropertyTagV1) restEntity;
            final CSNodeToPropertyTag dbEntity = entityManager.find(CSNodeToPropertyTag.class, propertyTag.getRelationshipId());
            if (dbEntity == null) {
                throw new BadRequestException(
                        "No CSNodeToPropertyTag entity was found with the primary key " + propertyTag.getRelationshipId());
            }

            entity.removePropertyTag(dbEntity);
        } else if (restEntity instanceof RESTCSNodeV1) {
            final CSNode dbEntity = entityManager.find(CSNode.class, restEntity.getId());
            if (dbEntity == null) {
                throw new BadRequestException("No CSNode entity was found with the primary key " + restEntity.getId());
            }

            entity.removeChild(dbEntity);
            entityManager.remove(dbEntity);
        } else if (restEntity instanceof RESTCSInfoNodeV1 || RESTCSNodeV1.INFO_TOPIC_NODE_NAME.equals(action.getUniqueId())) {
            if (entity.getCSInfoNode() != null) {
                final CSInfoNode infoNode = entity.getCSInfoNode();
                infoNode.setCSNode(null);
                entityManager.remove(infoNode);
            }
            entity.setCSInfoNode(null);
        }
    }

    @Override
    protected PressGangEntity doCreateChildAction(final CSNode entity, final RESTCSNodeV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();
        final PressGangEntity dbEntity;

        if (restEntity instanceof RESTCSRelatedNodeV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            final CSNode relatedNode = entityManager.find(CSNode.class, restEntity.getId());
            if (relatedNode == null) {
                throw new BadRequestException("No CSNode entity was found with the primary key " + restEntity.getId());
            }

            final CSNodeToCSNode csNodeToCSNode = (CSNodeToCSNode) dbEntity;
            if (RESTCSNodeV1.RELATED_FROM_NAME.equals(action.getUniqueId())) {
                csNodeToCSNode.setMainNode(relatedNode);
                entity.addRelationshipFrom(csNodeToCSNode);
            } else {
                csNodeToCSNode.setRelatedNode(relatedNode);
                entity.addRelationshipTo(csNodeToCSNode);
            }
        } else if (restEntity instanceof RESTAssignedPropertyTagV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            final PropertyTag propertyTag = entityManager.find(PropertyTag.class, restEntity.getId());
            if (propertyTag == null) {
                throw new BadRequestException("No PropertyTag entity was found with the primary key " + restEntity.getId());
            }

            final CSNodeToPropertyTag csNodeToProp = (CSNodeToPropertyTag) dbEntity;
            csNodeToProp.setPropertyTag(propertyTag);
            entity.addPropertyTag(csNodeToProp);
        } else if (restEntity instanceof RESTCSNodeV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            entity.addChild((CSNode) dbEntity);
        } else if (restEntity instanceof RESTCSInfoNodeV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            final CSInfoNode csInfoNode = (CSInfoNode) dbEntity;
            csInfoNode.setCSNode(entity);
            entity.setCSInfoNode(csInfoNode);
        } else {
            throw new IllegalArgumentException("Item is not a child of CSNode");
        }

        return dbEntity;
    }

    @Override
    protected PressGangEntity getChildEntityForAction(final CSNode entity, final RESTCSNodeV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();

        final PressGangEntity dbEntity;
        if (restEntity instanceof RESTCSRelatedNodeV1) {
            final RESTCSRelatedNodeV1 relatedNode = (RESTCSRelatedNodeV1) restEntity;
            dbEntity = entityManager.find(CSNodeToCSNode.class, relatedNode.getRelationshipId());
            if (dbEntity == null) {
                if (dbEntity == null) throw new BadRequestException(
                        "No CSNodeToCSNode entity was found with the primary key " + relatedNode.getRelationshipId());
            } else {
                if (RESTCSNodeV1.RELATED_FROM_NAME.equals(action.getUniqueId()) && !entity.getRelatedFromNodes().contains(dbEntity)) {
                    throw new BadRequestException(
                            "No CSNodeToCSNode entity was found with the primary key " + relatedNode.getRelationshipId() + " for CSNode "
                                    + entity.getId());
                } else if (RESTCSNodeV1.RELATED_TO_NAME.equals(action.getUniqueId()) && !entity.getRelatedToNodes().contains(dbEntity)) {
                    throw new BadRequestException(
                            "No CSNodeToCSNode entity was found with the primary key " + relatedNode.getRelationshipId() + " for CSNode "
                                    + entity.getId());
                }
            }
        } else if (restEntity instanceof RESTAssignedPropertyTagV1) {
            final RESTAssignedPropertyTagV1 assignedPropertyTag = (RESTAssignedPropertyTagV1) restEntity;
            dbEntity = entityManager.find(ContentSpecToPropertyTag.class, assignedPropertyTag.getRelationshipId());
            if (dbEntity == null) {
                throw new BadRequestException("No CSNodeToPropertyTag entity was found with the primary key " + assignedPropertyTag
                        .getRelationshipId());
            } else if (!entity.getCSNodeToPropertyTags().contains(dbEntity)) {
                throw new BadRequestException(
                        "No CSNodeToPropertyTag entity was found with the primary key " + assignedPropertyTag.getRelationshipId() +
                                " for ContentSpec " + entity.getId());
            }
        } else if (restEntity instanceof RESTCSNodeV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTCSNodeV1) restEntity, CSNode.class);
            if (dbEntity == null) {
                throw new BadRequestException("No CSNode entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getChildren().contains(dbEntity)) {
                throw new BadRequestException(
                        "No CSNode entity was found with the primary key " + restEntity.getId() + " for CSNode " + entity.getId());
            }
        } else if (restEntity instanceof RESTCSInfoNodeV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTCSInfoNodeV1) restEntity, CSInfoNode.class);
            if (dbEntity == null) {
                throw new BadRequestException("No CSInfoNode entity was found with the primary key " + restEntity.getId());
            }
        } else {
            throw new IllegalArgumentException("Item is not a child of ContentSpec");
        }

        return dbEntity;
    }

    @Override
    protected Class<CSNode> getDatabaseClass() {
        return CSNode.class;
    }
}
