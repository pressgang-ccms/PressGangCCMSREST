package org.jboss.pressgang.ccms.server.rest.v1;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.PropertyTag;
import org.jboss.pressgang.ccms.model.contentspec.CSNode;
import org.jboss.pressgang.ccms.model.contentspec.CSNodeToCSNode;
import org.jboss.pressgang.ccms.model.contentspec.CSNodeToPropertyTag;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTCSNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTTranslatedCSNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTCSNodeCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.join.RESTCSRelatedNodeCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.join.RESTCSRelatedNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTAssignedPropertyTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTAssignedPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTContentSpecV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.enums.RESTCSNodeRelationshipTypeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.enums.RESTCSNodeTypeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.join.RESTCSRelatedNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTAssignedPropertyTagV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class CSNodeV1Factory extends RESTDataObjectFactory<RESTCSNodeV1, CSNode, RESTCSNodeCollectionV1, RESTCSNodeCollectionItemV1> {
    @Inject
    protected ContentSpecV1Factory contentSpecFactory;
    @Inject
    protected CSNodePropertyTagV1Factory csNodePropertyTagFactory;
    @Inject
    protected CSRelatedNodeV1Factory csRelatedNodeFactory;
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
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        expandOptions.add(RESTCSNodeV1.NEXT_NODE_NAME);
        expandOptions.add(RESTCSNodeV1.PARENT_NAME);
        expandOptions.add(RESTCSNodeV1.PROPERTIES_NAME);
        expandOptions.add(RESTCSNodeV1.RELATED_FROM_NAME);
        expandOptions.add(RESTCSNodeV1.RELATED_TO_NAME);
        expandOptions.add(RESTCSNodeV1.TRANSLATED_NODES_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setTitle(entity.getCSNodeTitle());
        retValue.setTargetId(entity.getCSNodeTargetId());
        retValue.setAdditionalText(entity.getAdditionalText());
        retValue.setCondition(entity.getCondition());
        retValue.setNodeType(RESTCSNodeTypeV1.getNodeType(entity.getCSNodeType()));
        retValue.setEntityId(entity.getEntityId());
        retValue.setEntityRevision(entity.getEntityRevision());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTDataObjectCollectionFactory.create(RESTCSNodeCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // PARENT
        if (expandParentReferences && expand != null && expand.contains(RESTCSNodeV1.PARENT_NAME) && entity.getParent() != null) {
            retValue.setParent(
                    createRESTEntityFromDBEntity(entity.getParent(), baseUrl, dataType, expand.get(RESTCSNodeV1.PARENT_NAME), revision));
        }

        // CONTENT SPEC
        if (expand != null && expand.contains(RESTCSNodeV1.CONTENT_SPEC_NAME) && entity.getContentSpec() != null) retValue.setContentSpec(
                contentSpecFactory.createRESTEntityFromDBEntity(entity.getContentSpec(), baseUrl, dataType,
                        expand.get(RESTCSNodeV1.CONTENT_SPEC_NAME), revision, expandParentReferences));

        // NEXT
        if (expand != null && expand.contains(RESTCSNodeV1.NEXT_NODE_NAME) && entity.getNext() != null) {
            retValue.setNextNode(
                    createRESTEntityFromDBEntity(entity.getNext(), baseUrl, dataType, expand.get(RESTCSNodeV1.NEXT_NODE_NAME), revision));
        }

        // CHILDREN NODES
        if (expand != null && expand.contains(RESTContentSpecV1.CHILDREN_NAME)) {
            retValue.setChildren_OTM(RESTDataObjectCollectionFactory.create(RESTCSNodeCollectionV1.class, this, entity.getChildrenList(),
                    RESTCSNodeV1.CHILDREN_NAME, dataType, expand, baseUrl, revision, expandParentReferences, entityManager));
        }

        // RELATED FROM
        if (expand != null && expand.contains(RESTCSNodeV1.RELATED_FROM_NAME)) {
            retValue.setRelatedFromNodes(RESTDataObjectCollectionFactory.create(RESTCSRelatedNodeCollectionV1.class, csRelatedNodeFactory,
                    entity.getRelatedFromNodesList(), RESTCSNodeV1.RELATED_FROM_NAME, dataType, expand, baseUrl, revision, entityManager));
        }

        // RELATED TO
        if (expand != null && expand.contains(RESTCSNodeV1.RELATED_TO_NAME)) {
            retValue.setRelatedToNodes(RESTDataObjectCollectionFactory.create(RESTCSRelatedNodeCollectionV1.class, csRelatedNodeFactory,
                    entity.getRelatedToNodesList(), RESTCSNodeV1.RELATED_TO_NAME, dataType, expand, baseUrl, revision, entityManager));
        }

        // TRANSLATED STRINGS
        if (expand != null && expand.contains(RESTCSNodeV1.TRANSLATED_NODES_NAME)) {
            retValue.setTranslatedNodes_OTM(
                    RESTDataObjectCollectionFactory.create(RESTTranslatedCSNodeCollectionV1.class, translatedCSNodeFactory,
                            entity.getTranslatedNodes(entityManager, revision), RESTCSNodeV1.TRANSLATED_NODES_NAME, dataType, expand,
                            baseUrl, false, entityManager));
        }

        // PROPERTY TAGS
        if (expand != null && expand.contains(RESTCSNodeV1.PROPERTIES_NAME)) {
            retValue.setProperties(
                    RESTDataObjectCollectionFactory.create(RESTAssignedPropertyTagCollectionV1.class, csNodePropertyTagFactory,
                            entity.getPropertyTagsList(), RESTCSNodeV1.PROPERTIES_NAME, dataType, expand, baseUrl, revision,
                            entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.CONTENT_SPEC_NODE_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntityFirstPass(final CSNode entity, final RESTCSNodeV1 dataObject) {
        if (dataObject.hasParameterSet(RESTCSNodeV1.TITLE_NAME)) entity.setCSNodeTitle(dataObject.getTitle());

        if (dataObject.hasParameterSet(RESTCSNodeV1.TARGET_ID_NAME)) entity.setCSNodeTargetId(dataObject.getTargetId());

        if (dataObject.hasParameterSet(RESTCSNodeV1.ADDITIONAL_TEXT_NAME)) entity.setAdditionalText(dataObject.getAdditionalText());

        if (dataObject.hasParameterSet(RESTCSNodeV1.CONDITION_NAME)) entity.setCondition(dataObject.getCondition());

        if (dataObject.hasParameterSet(RESTCSNodeV1.NODE_TYPE_NAME))
            entity.setCSNodeType(RESTCSNodeTypeV1.getNodeTypeId(dataObject.getNodeType()));

        if (dataObject.hasParameterSet(RESTCSNodeV1.ENTITY_ID_NAME)) entity.setEntityId(dataObject.getEntityId());

        if (dataObject.hasParameterSet(RESTCSNodeV1.ENTITY_REVISION_NAME)) entity.setEntityRevision(dataObject.getEntityRevision());

        // One To Many - Add will create a new mapping
        if (dataObject.hasParameterSet(
                RESTCSNodeV1.CHILDREN_NAME) && dataObject.getChildren_OTM() != null && dataObject.getChildren_OTM().getItems() != null) {
            dataObject.getChildren_OTM().removeInvalidChangeItemRequests();

            for (final RESTCSNodeCollectionItemV1 restEntityItem : dataObject.getChildren_OTM().getItems()) {
                final RESTCSNodeV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final CSNode dbEntity = entityManager.find(CSNode.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new BadRequestException("No CSNode entity was found with the primary key " + restEntity.getId());

                    entity.removeChild(dbEntity);
                    entityManager.remove(dbEntity);
                } else if (restEntityItem.returnIsAddItem()) {
                    final CSNode dbEntity = createDBEntityFromRESTEntity(restEntity);
                    entity.addChild(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final CSNode dbEntity = entityManager.find(CSNode.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new BadRequestException("No CSNode entity was found with the primary key " + restEntity.getId());
                    if (!entity.getChildren().contains(dbEntity)) throw new BadRequestException(
                            "No CSNode entity was found with the primary key " + restEntity.getId() + " for CSNode " + entity.getId());

                    updateDBEntityFromRESTEntity(dbEntity, restEntity);
                }
            }
        }

        // Many to Many
        if (dataObject.hasParameterSet(
                RESTCSNodeV1.PROPERTIES_NAME) && dataObject.getProperties() != null && dataObject.getProperties().getItems() != null) {
            dataObject.getProperties().removeInvalidChangeItemRequests();

            for (final RESTAssignedPropertyTagCollectionItemV1 restEntityItem : dataObject.getProperties().getItems()) {
                final RESTAssignedPropertyTagV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final PropertyTag dbEntity = entityManager.find(PropertyTag.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new BadRequestException("No PropertyTag entity was found with the primary key " + restEntity.getId());

                    entity.removePropertyTag(dbEntity, restEntity.getValue());
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final CSNodeToPropertyTag dbEntity = entityManager.find(CSNodeToPropertyTag.class, restEntity.getRelationshipId());
                    if (dbEntity == null) throw new BadRequestException(
                            "No CSNodeToPropertyTag entity was found with the primary key " + restEntity.getRelationshipId());
                    if (!entity.getCSNodeToPropertyTags().contains(dbEntity)) throw new BadRequestException(
                            "No CSNodeToPropertyTag entity was found with the primary key " + restEntity.getRelationshipId() + " for " +
                                    "CSNode " + entity.getId());

                    csNodePropertyTagFactory.updateDBEntityFromRESTEntity(dbEntity, restEntity);
                }
            }
        }

        // Many to Many
        if (dataObject.hasParameterSet(
                RESTCSNodeV1.RELATED_TO_NAME) && dataObject.getRelatedToNodes() != null && dataObject.getRelatedToNodes().getItems() !=
                null) {
            dataObject.getRelatedToNodes().removeInvalidChangeItemRequests();

            for (final RESTCSRelatedNodeCollectionItemV1 restEntityItem : dataObject.getRelatedToNodes().getItems()) {
                final RESTCSRelatedNodeV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final CSNodeToCSNode dbEntity = entityManager.find(CSNodeToCSNode.class, restEntity.getRelationshipId());
                    if (dbEntity == null) throw new BadRequestException(
                            "No CSNodeToCSNode entity was found with the primary key " + restEntity.getRelationshipId());

                    entity.removeRelationshipTo(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final CSNodeToCSNode dbEntity = entityManager.find(CSNodeToCSNode.class, restEntity.getRelationshipId());
                    if (dbEntity == null) throw new BadRequestException(
                            "No CSNodeToCSNode entity was found with the primary key " + restEntity.getRelationshipId());
                    if (!entity.getRelatedToNodes().contains(dbEntity)) throw new BadRequestException(
                            "No CSNodeToCSNode entity was found with the primary key " + restEntity.getRelationshipId() + " for " +
                                    "CSNode " + entity.getId());

                    csRelatedNodeFactory.updateDBEntityFromRESTEntity(dbEntity, restEntity);
                }
            }
        }

        // Many to Many
        if (dataObject.hasParameterSet(
                RESTCSNodeV1.RELATED_FROM_NAME) && dataObject.getRelatedFromNodes() != null && dataObject.getRelatedFromNodes().getItems
                () != null) {
            dataObject.getRelatedFromNodes().removeInvalidChangeItemRequests();

            for (final RESTCSRelatedNodeCollectionItemV1 restEntityItem : dataObject.getRelatedFromNodes().getItems()) {
                final RESTCSRelatedNodeV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final CSNodeToCSNode dbEntity = entityManager.find(CSNodeToCSNode.class, restEntity.getRelationshipId());
                    if (dbEntity == null) throw new BadRequestException(
                            "No CSNodeToCSNode entity was found with the primary key " + restEntity.getRelationshipId());

                    entity.removeRelationshipFrom(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final CSNodeToCSNode dbEntity = entityManager.find(CSNodeToCSNode.class, restEntity.getRelationshipId());
                    if (dbEntity == null) throw new BadRequestException(
                            "No CSNodeToCSNode entity was found with the primary key " + restEntity.getRelationshipId());
                    if (!entity.getRelatedFromNodes().contains(dbEntity)) throw new BadRequestException(
                            "No CSNodeToCSNode entity was found with the primary key " + restEntity.getRelationshipId() + " for " +
                                    "CSNode " + entity.getId());

                    csRelatedNodeFactory.updateDBEntityFromRESTEntity(dbEntity, restEntity);
                }
            }
        }
    }

    @Override
    public void syncDBEntityWithRESTEntitySecondPass(final CSNode entity, final RESTCSNodeV1 dataObject) {
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

        // One To Many - Do the second pass for the added and updated items
        if (dataObject.hasParameterSet(
                RESTCSNodeV1.CHILDREN_NAME) && dataObject.getChildren_OTM() != null && dataObject.getChildren_OTM().getItems() != null) {
            dataObject.getChildren_OTM().removeInvalidChangeItemRequests();

            for (final RESTCSNodeCollectionItemV1 restEntityItem : dataObject.getChildren_OTM().getItems()) {
                final RESTCSNodeV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsUpdateItem()) {
                    final CSNode dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, restEntity, CSNode.class);
                    if (dbEntity == null)
                        throw new BadRequestException("No CSNode entity was found with the primary key " + restEntity.getId());

                    syncDBEntityWithRESTEntitySecondPass(dbEntity, restEntity);
                }
            }
        }

        // Many to Many
        if (dataObject.hasParameterSet(
                RESTCSNodeV1.PROPERTIES_NAME) && dataObject.getProperties() != null && dataObject.getProperties().getItems() != null) {
            dataObject.getProperties().removeInvalidChangeItemRequests();

            for (final RESTAssignedPropertyTagCollectionItemV1 restEntityItem : dataObject.getProperties().getItems()) {
                final RESTAssignedPropertyTagV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem()) {
                    final PropertyTag dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, restEntity, PropertyTag.class);
                    if (dbEntity == null)
                        throw new BadRequestException("No PropertyTag entity was found with the primary key " + restEntity.getId());

                    entity.addPropertyTag(dbEntity, restEntity.getValue());
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final CSNodeToPropertyTag dbEntity = entityManager.find(CSNodeToPropertyTag.class, restEntity.getRelationshipId());
                    if (dbEntity == null) throw new BadRequestException(
                            "No CSNodeToPropertyTag entity was found with the primary key " + restEntity.getRelationshipId());

                    csNodePropertyTagFactory.syncDBEntityWithRESTEntitySecondPass(dbEntity, restEntity);
                }
            }
        }

        // Many to Many
        if (dataObject.hasParameterSet(
                RESTCSNodeV1.RELATED_TO_NAME) && dataObject.getRelatedToNodes() != null && dataObject.getRelatedToNodes().getItems() !=
                null) {
            dataObject.getRelatedToNodes().removeInvalidChangeItemRequests();

            for (final RESTCSRelatedNodeCollectionItemV1 restEntityItem : dataObject.getRelatedToNodes().getItems()) {
                final RESTCSRelatedNodeV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem()) {
                    final CSNode dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, restEntity, CSNode.class);
                    if (dbEntity == null)
                        throw new BadRequestException("No CSNode entity was found with the primary key " + restEntity.getId());

                    entity.addRelationshipTo(dbEntity,
                            RESTCSNodeRelationshipTypeV1.getRelationshipTypeId(restEntity.getRelationshipType()));
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final CSNodeToCSNode dbEntity = entityManager.find(CSNodeToCSNode.class, restEntity.getRelationshipId());
                    if (dbEntity == null) throw new BadRequestException(
                            "No CSNodeToCSNode entity was found with the primary key " + restEntity.getRelationshipId());
                    if (!entity.getRelatedToNodes().contains(dbEntity)) throw new BadRequestException(
                            "No CSNodeToCSNode entity was found with the primary key " + restEntity.getRelationshipId() + " for" +
                                    " ContentSpec " + entity.getId());

                    csRelatedNodeFactory.syncDBEntityWithRESTEntitySecondPass(dbEntity, restEntity);
                }
            }
        }

        // Many to Many
        if (dataObject.hasParameterSet(
                RESTCSNodeV1.RELATED_FROM_NAME) && dataObject.getRelatedFromNodes() != null && dataObject.getRelatedFromNodes().getItems
                () != null) {
            dataObject.getRelatedFromNodes().removeInvalidChangeItemRequests();

            for (final RESTCSRelatedNodeCollectionItemV1 restEntityItem : dataObject.getRelatedFromNodes().getItems()) {
                final RESTCSRelatedNodeV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem()) {
                    final CSNode dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, restEntity, CSNode.class);
                    if (dbEntity == null)
                        throw new BadRequestException("No CSNode entity was found with the primary key " + restEntity.getId());

                    entity.addRelationshipFrom(dbEntity,
                            RESTCSNodeRelationshipTypeV1.getRelationshipTypeId(restEntity.getRelationshipType()));
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final CSNodeToCSNode dbEntity = entityManager.find(CSNodeToCSNode.class, restEntity.getRelationshipId());
                    if (dbEntity == null) throw new BadRequestException(
                            "No CSNodeToCSNode entity was found with the primary key " + restEntity.getRelationshipId());
                    if (!entity.getRelatedFromNodes().contains(dbEntity)) throw new BadRequestException(
                            "No CSNodeToCSNode entity was found with the primary key " + restEntity.getRelationshipId() + " for" +
                                    " ContentSpec " + entity.getId());

                    csRelatedNodeFactory.syncDBEntityWithRESTEntitySecondPass(dbEntity, restEntity);
                }
            }
        }
    }

    @Override
    protected Class<CSNode> getDatabaseClass() {
        return CSNode.class;
    }
}
