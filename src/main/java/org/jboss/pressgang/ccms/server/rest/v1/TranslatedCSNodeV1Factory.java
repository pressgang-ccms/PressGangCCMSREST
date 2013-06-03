package org.jboss.pressgang.ccms.server.rest.v1;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.pressgang.ccms.model.TranslatedTopicData;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.model.contentspec.TranslatedCSNode;
import org.jboss.pressgang.ccms.model.contentspec.TranslatedCSNodeString;
import org.jboss.pressgang.ccms.model.contentspec.TranslatedContentSpec;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTTranslatedCSNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTTranslatedCSNodeStringCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTTranslatedCSNodeCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTTranslatedCSNodeStringCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTranslatedTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTranslatedCSNodeStringV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTranslatedCSNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTranslatedContentSpecV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

public class TranslatedCSNodeV1Factory extends RESTDataObjectFactory<RESTTranslatedCSNodeV1, TranslatedCSNode,
        RESTTranslatedCSNodeCollectionV1, RESTTranslatedCSNodeCollectionItemV1> {
    private final TranslatedCSNodeStringV1Factory translatedCSNodeStringFactory = new TranslatedCSNodeStringV1Factory();

    public TranslatedCSNodeV1Factory() {
        super(TranslatedCSNode.class);
    }

    @Override
    protected RESTTranslatedCSNodeV1 createRESTEntityFromDBEntityInternal(TranslatedCSNode entity, String baseUrl, String dataType,
            ExpandDataTrunk expand, Number revision, boolean expandParentReferences, EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTTranslatedCSNodeV1 retValue = new RESTTranslatedCSNodeV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        expandOptions.add(RESTTranslatedCSNodeV1.NODE_NAME);
        expandOptions.add(RESTTranslatedCSNodeV1.TRANSLATED_STRING_NAME);
        expandOptions.add(RESTTranslatedCSNodeV1.TRANSLATED_CONTENT_SPEC_NAME);
        expandOptions.add(RESTTranslatedCSNodeV1.TRANSLATED_TOPIC_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setNodeId(entity.getCSNodeId());
        retValue.setNodeRevision(entity.getCSNodeRevision());
        retValue.setOriginalString(entity.getOriginalString());

        // NODE
        if (expandParentReferences && expand != null && expand.contains(RESTTranslatedCSNodeV1.NODE_NAME) && entity.getEnversCSNode(
                entityManager) != null) {
            retValue.setNode(new CSNodeV1Factory().createRESTEntityFromDBEntity(entity.getEnversCSNode(entityManager), baseUrl, dataType,
                    expand.get(RESTTranslatedCSNodeV1.NODE_NAME), entity.getCSNodeRevision(), true, entityManager));
            retValue.getNode().setRevision(entity.getCSNodeRevision());
        }

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(
                    new RESTDataObjectCollectionFactory<RESTTranslatedCSNodeV1, TranslatedCSNode, RESTTranslatedCSNodeCollectionV1,
                            RESTTranslatedCSNodeCollectionItemV1>().create(
                            RESTTranslatedCSNodeCollectionV1.class, new TranslatedCSNodeV1Factory(), entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        // TRANSLATED STRINGS
        if (expand != null && expand.contains(RESTTranslatedCSNodeV1.TRANSLATED_STRING_NAME)) {
            retValue.setTranslatedNodeStrings_OTM(
                    new RESTDataObjectCollectionFactory<RESTTranslatedCSNodeStringV1, TranslatedCSNodeString,
                            RESTTranslatedCSNodeStringCollectionV1, RESTTranslatedCSNodeStringCollectionItemV1>().create(
                            RESTTranslatedCSNodeStringCollectionV1.class, new TranslatedCSNodeStringV1Factory(),
                            entity.getTranslatedCSNodeStringsArray(), RESTTranslatedCSNodeV1.TRANSLATED_STRING_NAME, dataType, expand,
                            baseUrl, false, entityManager));
        }

        // TRANSLATED CONTENT SPEC
        if (expandParentReferences && expand != null && expand.contains(
                RESTTranslatedCSNodeV1.TRANSLATED_CONTENT_SPEC_NAME) && entity.getTranslatedContentSpec() != null) {
            retValue.setTranslatedContentSpec(
                    new TranslatedContentSpecV1Factory().createRESTEntityFromDBEntity(entity.getTranslatedContentSpec(), baseUrl, dataType,
                            expand.get(RESTTranslatedCSNodeV1.TRANSLATED_CONTENT_SPEC_NAME), revision, true, entityManager));
        }

        // TRANSLATED TOPIC
        if (expand != null && expand.contains(RESTTranslatedCSNodeV1.TRANSLATED_TOPIC_NAME) && entity.getTranslatedTopicData() != null) {
            retValue.setTranslatedTopic(
                    new TranslatedTopicV1Factory().createRESTEntityFromDBEntity(entity.getTranslatedTopicData(), baseUrl, dataType,
                            expand.get(RESTTranslatedCSNodeV1.TRANSLATED_TOPIC_NAME), revision, true, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.CONTENT_SPEC_TRANSLATED_NODE_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntityFirstPass(final EntityManager entityManager,
            final Map<RESTBaseEntityV1<?, ?, ?>, AuditedEntity> newEntityCache, final TranslatedCSNode entity,
            final RESTTranslatedCSNodeV1 dataObject) {
        if (dataObject.hasParameterSet(RESTTranslatedCSNodeV1.NODE_ID_NAME)) entity.setCSNodeId(dataObject.getNodeId());
        if (dataObject.hasParameterSet(RESTTranslatedCSNodeV1.NODE_REVISION_NAME)) entity.setCSNodeRevision(dataObject.getNodeRevision());
        if (dataObject.hasParameterSet(RESTTranslatedCSNodeV1.ORIGINALSTRING_NAME))
            entity.setOriginalString(dataObject.getOriginalString());

        entityManager.persist(entity);

        /* One To Many - Add will create a child entity */
        if (dataObject.hasParameterSet(
                RESTTranslatedCSNodeV1.TRANSLATED_STRING_NAME) && dataObject.getTranslatedNodeStrings_OTM() != null && dataObject
                .getTranslatedNodeStrings_OTM().getItems() != null) {
            dataObject.getTranslatedNodeStrings_OTM().removeInvalidChangeItemRequests();

            for (final RESTTranslatedCSNodeStringCollectionItemV1 restEntityItem : dataObject.getTranslatedNodeStrings_OTM().getItems()) {
                final RESTTranslatedCSNodeStringV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final TranslatedCSNodeString dbEntity = entityManager.find(TranslatedCSNodeString.class, restEntity.getId());
                    if (dbEntity == null) throw new BadRequestException(
                            "No TranslatedCSNodeString entity was found with the primary key " + restEntity.getId());

                    entity.removeTranslatedString(dbEntity);
                    entityManager.remove(dbEntity);
                } else if (restEntityItem.returnIsAddItem()) {
                    final TranslatedCSNodeString dbEntity = translatedCSNodeStringFactory.createDBEntityFromRESTEntity(entityManager,
                            newEntityCache, restEntity);
                    entity.addTranslatedString(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final TranslatedCSNodeString dbEntity = entityManager.find(TranslatedCSNodeString.class, restEntity.getId());
                    if (dbEntity == null) throw new BadRequestException(
                            "No TranslatedCSNodeString entity was found with the primary key " + restEntity.getId());
                    if (!entity.getTranslatedCSNodeStrings().contains(dbEntity)) throw new BadRequestException(
                            "No TranslatedCSNodeString entity was found with the primary key " + restEntity.getId() + " for " +
                                    "TranslatedCSNode " + entity.getId());

                    translatedCSNodeStringFactory.syncDBEntityWithRESTEntityFirstPass(entityManager, newEntityCache, dbEntity, restEntity);
                }
            }
        }
    }

    @Override
    public void syncDBEntityWithRESTEntitySecondPass(final EntityManager entityManager,
            final Map<RESTBaseEntityV1<?, ?, ?>, AuditedEntity> newEntityCache, final TranslatedCSNode entity,
            final RESTTranslatedCSNodeV1 dataObject) {
        // Translated Content Spec
        if (dataObject.hasParameterSet(RESTTranslatedCSNodeV1.TRANSLATED_CONTENT_SPEC_NAME)) {
            final RESTTranslatedContentSpecV1 restEntity = dataObject.getTranslatedContentSpec();
            final TranslatedContentSpec dbEntity = RESTv1Utilities.findEntity(entityManager, newEntityCache, restEntity,
                    TranslatedContentSpec.class);
            if (dbEntity == null)
                throw new BadRequestException("No TranslatedContentSpec entity was found with the primary key " + restEntity.getId());
            dbEntity.addTranslatedNode(entity);
        }
        // Translated Topic
        if (dataObject.hasParameterSet(RESTTranslatedCSNodeV1.TRANSLATED_TOPIC_NAME)) {
            final RESTTranslatedTopicV1 restEntity = dataObject.getTranslatedTopic();
            final TranslatedTopicData dbEntity = RESTv1Utilities.findEntity(entityManager, newEntityCache, restEntity,
                    TranslatedTopicData.class);
            if (dbEntity == null)
                throw new BadRequestException("No TranslatedTopicData entity was found with the primary key " + restEntity.getId());
            entity.setTranslatedTopicData(dbEntity);
        }

        /* One To Many - just do the second pass on added or updated items */
        if (dataObject.hasParameterSet(
                RESTTranslatedCSNodeV1.TRANSLATED_STRING_NAME) && dataObject.getTranslatedNodeStrings_OTM() != null && dataObject
                .getTranslatedNodeStrings_OTM().getItems() != null) {
            dataObject.getTranslatedNodeStrings_OTM().removeInvalidChangeItemRequests();

            for (final RESTTranslatedCSNodeStringCollectionItemV1 restEntityItem : dataObject.getTranslatedNodeStrings_OTM().getItems()) {
                final RESTTranslatedCSNodeStringV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsUpdateItem()) {
                    final TranslatedCSNodeString dbEntity = RESTv1Utilities.findEntity(entityManager, newEntityCache, restEntity,
                            TranslatedCSNodeString.class);
                    if (dbEntity == null) throw new BadRequestException(
                            "No TranslatedCSNodeString entity was found with the primary key " + restEntity.getId());

                    translatedCSNodeStringFactory.syncDBEntityWithRESTEntityFirstPass(entityManager, newEntityCache, dbEntity, restEntity);
                }
            }
        }
    }
}
