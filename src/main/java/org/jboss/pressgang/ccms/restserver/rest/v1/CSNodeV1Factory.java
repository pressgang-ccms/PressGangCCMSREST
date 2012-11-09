package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTCSNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTCSTranslatedStringCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTCSNodeCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTCSTranslatedStringCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.join.RESTCSRelatedNodeCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.join.RESTCSRelatedNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTAssignedPropertyTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTAssignedPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSTranslatedStringV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTContentSpecV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.join.RESTCSRelatedNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTAssignedPropertyTagV1;
import org.jboss.pressgang.ccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.entity.PropertyTag;
import org.jboss.pressgang.ccms.restserver.entity.contentspec.CSNode;
import org.jboss.pressgang.ccms.restserver.entity.contentspec.CSNodeToCSNode;
import org.jboss.pressgang.ccms.restserver.entity.contentspec.CSNodeToPropertyTag;
import org.jboss.pressgang.ccms.restserver.entity.contentspec.CSTranslatedString;
import org.jboss.pressgang.ccms.restserver.entity.contentspec.ContentSpec;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.BaseRESTv1;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;


public class CSNodeV1Factory extends
        RESTDataObjectFactory<RESTCSNodeV1, CSNode, RESTCSNodeCollectionV1, RESTCSNodeCollectionItemV1> {

    public CSNodeV1Factory() {
        super(CSNode.class);
    }

    @Override
    public RESTCSNodeV1 createRESTEntityFromDBEntity(final CSNode entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTCSNodeV1 retValue = new RESTCSNodeV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        expandOptions.add(RESTCSNodeV1.PARENT_NAME);
        expandOptions.add(RESTCSNodeV1.PROPERTIES_NAME);
        expandOptions.add(RESTCSNodeV1.RELATED_FROM_NAME);
        expandOptions.add(RESTCSNodeV1.RELATED_TO_NAME);
        expandOptions.add(RESTCSNodeV1.TRANSLATED_STRINGS_NAME);
        if (revision == null)
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setTitle(entity.getCSNodeTitle());
        retValue.setCondition(entity.getCondition());
        retValue.setFlag(entity.getFlag());
        retValue.setNodeType(entity.getCSNodeType());
        retValue.setTopicId(entity.getTopicId());
        retValue.setTopicRevision(entity.getTopicRevision());

        // REVISIONS
        if (revision == null) {
            retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTCSNodeV1, CSNode, RESTCSNodeCollectionV1, RESTCSNodeCollectionItemV1>()
                    .create(RESTCSNodeCollectionV1.class, new CSNodeV1Factory(), entity, entity.getRevisions(entityManager),
                            RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
        }

        if (expandParentReferences && entity.getParent() != null && expand != null) {
            retValue.setParent(new CSNodeV1Factory().createRESTEntityFromDBEntity(entity.getParent(), baseUrl, dataType,
                    expand.get(RESTCSNodeV1.PARENT_NAME), entityManager));
        }

        // CONTENT SPEC
        if (entity.getContentSpec() != null && expand != null && expand.contains(RESTCSNodeV1.CONTENT_SPEC_NAME))
            retValue.setContentSpec(new ContentSpecV1Factory().createRESTEntityFromDBEntity(entity.getContentSpec(), baseUrl,
                    dataType, expand.get(RESTCSNodeV1.CONTENT_SPEC_NAME), revision, expandParentReferences, entityManager));

        // NEXT / PREVIOUS
        if (entity.getNext() != null)
            retValue.setNextNodeId(entity.getNext().getId());
        
        if (entity.getPrevious() != null)
            retValue.setPreviousNodeId(entity.getPrevious().getId());

        // RELATED FROM
        retValue.setRelatedFromNodes(new RESTDataObjectCollectionFactory<RESTCSRelatedNodeV1, CSNodeToCSNode, RESTCSRelatedNodeCollectionV1, RESTCSRelatedNodeCollectionItemV1>()
                .create(RESTCSRelatedNodeCollectionV1.class, new CSRelatedNodeV1Factory(), entity.getRelatedFromNodesList(),
                        RESTCSNodeV1.RELATED_FROM_NAME, dataType, expand, baseUrl, entityManager));

        // RELATED TO
        retValue.setRelatedToNodes(new RESTDataObjectCollectionFactory<RESTCSRelatedNodeV1, CSNodeToCSNode, RESTCSRelatedNodeCollectionV1, RESTCSRelatedNodeCollectionItemV1>()
                .create(RESTCSRelatedNodeCollectionV1.class, new CSRelatedNodeV1Factory(), entity.getRelatedToNodesList(),
                        RESTCSNodeV1.RELATED_FROM_NAME, dataType, expand, baseUrl, entityManager));

        // TRANSLATED STRINGS
        retValue.setTranslatedStrings_OTM(new RESTDataObjectCollectionFactory<RESTCSTranslatedStringV1, CSTranslatedString, RESTCSTranslatedStringCollectionV1, RESTCSTranslatedStringCollectionItemV1>()
                .create(RESTCSTranslatedStringCollectionV1.class, new CSTranslatedStringV1Factory(),
                        entity.getCSTranslatedStringsList(), RESTCSNodeV1.TRANSLATED_STRINGS_NAME, dataType, expand, baseUrl,
                        false, entityManager));

        // PROPERTY TAGS
        retValue.setProperties(new RESTDataObjectCollectionFactory<RESTAssignedPropertyTagV1, CSNodeToPropertyTag, RESTAssignedPropertyTagCollectionV1, RESTAssignedPropertyTagCollectionItemV1>()
                .create(RESTAssignedPropertyTagCollectionV1.class, new CSNodePropertyTagV1Factory(),
                        entity.getCSNodeToPropertyTagsList(), RESTCSNodeV1.PROPERTIES_NAME, dataType, expand, baseUrl,
                        revision, entityManager));

        retValue.setLinks(baseUrl, BaseRESTv1.CONTENT_SPEC_NODE_URL_NAME, dataType, retValue.getId());
        retValue.setLogDetails(new LogDetailsV1Factory().create(entity, revision, RESTBaseEntityV1.LOG_DETAILS_NAME, expand,
                dataType, baseUrl, entityManager));

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final CSNode entity, final RESTCSNodeV1 dataObject)
            throws InvalidParameterException {
        if (dataObject.hasParameterSet(RESTCSNodeV1.TITLE_NAME))
            entity.setCSNodeTitle(dataObject.getTitle());

        if (dataObject.hasParameterSet(RESTCSNodeV1.CONDITION_NAME))
            entity.setCondition(dataObject.getCondition());

        if (dataObject.hasParameterSet(RESTCSNodeV1.NODE_TYPE_NAME))
            entity.setCSNodeType(dataObject.getNodeType());
        
        if (dataObject.hasParameterSet(RESTCSNodeV1.TOPIC_ID_NAME))
            entity.setTopicId(dataObject.getTopicId());
        
        if (dataObject.hasParameterSet(RESTCSNodeV1.TOPIC_REVISION_NAME))
            entity.setTopicRevision(dataObject.getTopicRevision());
        
        /* Set the ContentSpec for the Node */
        if (dataObject.hasParameterSet(RESTCSNodeV1.CONTENT_SPEC_NAME)) {
            final RESTContentSpecV1 restEntity = dataObject.getContentSpec();

            if (restEntity != null) {
                final ContentSpec dbEntity = entityManager.find(ContentSpec.class, restEntity.getId());
                if (dbEntity == null)
                    throw new InvalidParameterException("No ContentSpec entity was found with the primary key " + restEntity.getId());

                dbEntity.addChild(entity);
            } else if (entity.getContentSpec() != null) {
                entity.getContentSpec().removeChild(entity);
            } else {
                entity.setContentSpec(null);
            }
        }

        entityManager.persist(entity);

        if (dataObject.hasParameterSet(RESTCSNodeV1.PROPERTIES_NAME) && dataObject.getProperties() != null
                && dataObject.getProperties().getItems() != null) {
            dataObject.getProperties().removeInvalidChangeItemRequests();

            /* remove children first */
            for (final RESTAssignedPropertyTagCollectionItemV1 restEntityItem : dataObject.getProperties().getItems()) {
                final RESTAssignedPropertyTagV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final PropertyTag dbEntity = entityManager.find(PropertyTag.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No PropertyTag entity was found with the primary key "
                                + restEntity.getId());

                    entity.removePropertyTag(dbEntity, restEntity.getValue());
                } else if (restEntityItem.returnIsAddItem()) {
                    final PropertyTag dbEntity = entityManager.find(PropertyTag.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No PropertyTag entity was found with the primary key "
                                + restEntity.getId());

                    entity.addPropertyTag(dbEntity, restEntity.getValue());
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final CSNodeToPropertyTag dbEntity = entityManager.find(CSNodeToPropertyTag.class,
                            restEntity.getRelationshipId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No CSNodeToPropertyTag entity was found with the primary key "
                                + restEntity.getRelationshipId());

                    new CSNodePropertyTagV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                }
            }
        }

        /* One To Many - Add will create a new mapping */
        if (dataObject.hasParameterSet(RESTCSNodeV1.CHILDREN_NAME) && dataObject.getChildren_OTM() != null
                && dataObject.getChildren_OTM().getItems() != null) {
            dataObject.getChildren_OTM().removeInvalidChangeItemRequests();

            for (final RESTCSNodeCollectionItemV1 restEntityItem : dataObject.getChildren_OTM().getItems()) {
                final RESTCSNodeV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final CSNode dbEntity = entityManager.find(CSNode.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No CSNode entity was found with the primary key "
                                + restEntity.getId());

                    entity.removeChild(dbEntity);
                } else if (restEntityItem.returnIsAddItem()) {
                    final CSNode dbEntity = new CSNodeV1Factory().createDBEntityFromRESTEntity(entityManager, restEntity);
                    entityManager.persist(dbEntity);
                    entity.addChild(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final CSNode dbEntity = entityManager.find(CSNode.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No CSNode entity was found with the primary key "
                                + restEntity.getId());

                    new CSNodeV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                }
            }
        }
        
        /* One To Many - Add will create a child entity */
        if (dataObject.hasParameterSet(RESTCSNodeV1.TRANSLATED_STRINGS_NAME)
                && dataObject.getTranslatedStrings_OTM() != null
                && dataObject.getTranslatedStrings_OTM().getItems() != null) {
            dataObject.getTranslatedStrings_OTM().removeInvalidChangeItemRequests();

            /* remove any items first */
            for (final RESTCSTranslatedStringCollectionItemV1 restEntityItem : dataObject.getTranslatedStrings_OTM()
                    .getItems()) {
                final RESTCSTranslatedStringV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final CSTranslatedString dbEntity = entityManager.find(CSTranslatedString.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No CSTranslatedString entity was found with the primary key "
                                + restEntity.getId());

                    entity.removeTranslatedString(dbEntity);
                } else if (restEntityItem.returnIsAddItem()) {
                    final CSTranslatedString dbEntity = new CSTranslatedStringV1Factory().createDBEntityFromRESTEntity(entityManager, restEntity);
                    entityManager.persist(dbEntity);
                    entity.addTranslatedString(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final CSTranslatedString dbEntity = entityManager.find(CSTranslatedString.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new InvalidParameterException("No CSTranslatedString entity was found with the primary key "
                                + restEntity.getId());

                    new CSTranslatedStringV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                }
            }
        }
    }
}
