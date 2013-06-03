package org.jboss.pressgang.ccms.server.rest.v1;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.model.contentspec.TranslatedCSNode;
import org.jboss.pressgang.ccms.model.contentspec.TranslatedContentSpec;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTTranslatedCSNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTTranslatedContentSpecCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTTranslatedCSNodeCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTTranslatedContentSpecCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTranslatedCSNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTranslatedContentSpecV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

public class TranslatedContentSpecV1Factory extends RESTDataObjectFactory<RESTTranslatedContentSpecV1, TranslatedContentSpec,
        RESTTranslatedContentSpecCollectionV1, RESTTranslatedContentSpecCollectionItemV1> {
    private static final TranslatedCSNodeV1Factory translatedCSNodeFactory = new TranslatedCSNodeV1Factory();

    public TranslatedContentSpecV1Factory() {
        super(TranslatedContentSpec.class);
    }

    @Override
    protected RESTTranslatedContentSpecV1 createRESTEntityFromDBEntityInternal(TranslatedContentSpec entity, String baseUrl,
            String dataType, ExpandDataTrunk expand, Number revision, boolean expandParentReferences, EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTTranslatedContentSpecV1 retValue = new RESTTranslatedContentSpecV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        expandOptions.add(RESTTranslatedContentSpecV1.CONTENT_SPEC_NAME);
        expandOptions.add(RESTTranslatedContentSpecV1.TRANSLATED_NODES_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setContentSpecId(entity.getContentSpecId());
        retValue.setContentSpecRevision(entity.getContentSpecRevision());

        // NODE
        if (expandParentReferences && expand != null && expand.contains(
                RESTTranslatedContentSpecV1.CONTENT_SPEC_NAME) && entity.getEnversContentSpec(entityManager) != null) {
            retValue.setContentSpec(
                    new ContentSpecV1Factory().createRESTEntityFromDBEntity(entity.getEnversContentSpec(entityManager), baseUrl, dataType,
                            expand.get(RESTTranslatedContentSpecV1.CONTENT_SPEC_NAME), entity.getContentSpecRevision(), true,
                            entityManager));
            retValue.getContentSpec().setRevision(entity.getContentSpecRevision());
        }

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(
                    new RESTDataObjectCollectionFactory<RESTTranslatedContentSpecV1, TranslatedContentSpec,
                            RESTTranslatedContentSpecCollectionV1, RESTTranslatedContentSpecCollectionItemV1>().create(
                            RESTTranslatedContentSpecCollectionV1.class, new TranslatedContentSpecV1Factory(), entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        // TRANSLATED NODES
        if (expand != null && expand.contains(RESTTranslatedContentSpecV1.TRANSLATED_NODES_NAME)) {
            retValue.setTranslatedNodes_OTM(
                    new RESTDataObjectCollectionFactory<RESTTranslatedCSNodeV1, TranslatedCSNode, RESTTranslatedCSNodeCollectionV1,
                            RESTTranslatedCSNodeCollectionItemV1>().create(
                            RESTTranslatedCSNodeCollectionV1.class, new TranslatedCSNodeV1Factory(),
                            new ArrayList<TranslatedCSNode>(entity.getTranslatedCSNodes()),
                            RESTTranslatedContentSpecV1.TRANSLATED_NODES_NAME, dataType, expand, baseUrl, false, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.TRANSLATED_CONTENT_SPEC_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntityFirstPass(final EntityManager entityManager,
            final Map<RESTBaseEntityV1<?, ?, ?>, AuditedEntity> newEntityCache, final TranslatedContentSpec entity,
            final RESTTranslatedContentSpecV1 dataObject) {
        if (dataObject.hasParameterSet(RESTTranslatedContentSpecV1.CONTENT_SPEC_ID_NAME))
            entity.setContentSpecId(dataObject.getContentSpecId());
        if (dataObject.hasParameterSet(RESTTranslatedContentSpecV1.CONTENT_SPEC_REV_NAME))
            entity.setContentSpecRevision(dataObject.getContentSpecRevision());

        entityManager.persist(entity);

        /* One To Many - Add will create a child entity */
        if (dataObject.hasParameterSet(
                RESTTranslatedContentSpecV1.TRANSLATED_NODES_NAME) && dataObject.getTranslatedNodes_OTM() != null && dataObject
                .getTranslatedNodes_OTM().getItems() != null) {
            dataObject.getTranslatedNodes_OTM().removeInvalidChangeItemRequests();

            for (final RESTTranslatedCSNodeCollectionItemV1 restEntityItem : dataObject.getTranslatedNodes_OTM().getItems()) {
                final RESTTranslatedCSNodeV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final TranslatedCSNode dbEntity = entityManager.find(TranslatedCSNode.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new BadRequestException("No TranslatedCSNode entity was found with the primary key " + restEntity.getId());

                    entity.removeTranslatedNode(dbEntity);
                    entityManager.remove(dbEntity);
                } else if (restEntityItem.returnIsAddItem()) {
                    final TranslatedCSNode dbEntity = translatedCSNodeFactory.createDBEntityFromRESTEntity(entityManager, newEntityCache,
                            restEntity);
                    entity.addTranslatedNode(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final TranslatedCSNode dbEntity = entityManager.find(TranslatedCSNode.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new BadRequestException("No TranslatedCSNode entity was found with the primary key " + restEntity.getId());
                    if (!entity.getTranslatedCSNodes().contains(dbEntity)) throw new BadRequestException(
                            "No TranslatedCSNode entity was found with the primary key " + restEntity.getId() + " for " +
                                    "TranslatedContentSpec " + entity.getId());

                    translatedCSNodeFactory.syncDBEntityWithRESTEntityFirstPass(entityManager, newEntityCache, dbEntity, restEntity);
                }
            }
        }
    }

    @Override
    public void syncDBEntityWithRESTEntitySecondPass(final EntityManager entityManager,
            final Map<RESTBaseEntityV1<?, ?, ?>, AuditedEntity> newEntityCache, final TranslatedContentSpec entity,
            final RESTTranslatedContentSpecV1 dataObject) {
        // One To Many - perform the second pass on added/updated items
        if (dataObject.hasParameterSet(
                RESTTranslatedContentSpecV1.TRANSLATED_NODES_NAME) && dataObject.getTranslatedNodes_OTM() != null && dataObject
                .getTranslatedNodes_OTM().getItems() != null) {
            dataObject.getTranslatedNodes_OTM().removeInvalidChangeItemRequests();

            for (final RESTTranslatedCSNodeCollectionItemV1 restEntityItem : dataObject.getTranslatedNodes_OTM().getItems()) {
                final RESTTranslatedCSNodeV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsUpdateItem()) {
                    final TranslatedCSNode dbEntity = RESTv1Utilities.findEntity(entityManager, newEntityCache, restEntity,
                            TranslatedCSNode.class);
                    if (dbEntity == null)
                        throw new BadRequestException("No TranslatedCSNode entity was found with the primary key " + restEntity.getId());

                    translatedCSNodeFactory.syncDBEntityWithRESTEntitySecondPass(entityManager, newEntityCache, dbEntity, restEntity);
                }
            }
        }
    }
}
