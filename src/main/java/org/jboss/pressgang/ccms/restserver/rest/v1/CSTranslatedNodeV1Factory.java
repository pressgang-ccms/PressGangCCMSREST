package org.jboss.pressgang.ccms.restserver.rest.v1;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.contentspec.CSTranslatedNode;
import org.jboss.pressgang.ccms.model.contentspec.CSTranslatedNodeString;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTCSTranslatedNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTCSTranslatedNodeStringCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTCSTranslatedNodeCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTCSTranslatedNodeStringCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSTranslatedNodeStringV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSTranslatedNodeV1;
import org.jboss.pressgang.ccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.restserver.utils.EnversUtilities;

public class CSTranslatedNodeV1Factory extends RESTDataObjectFactory<RESTCSTranslatedNodeV1, CSTranslatedNode,
        RESTCSTranslatedNodeCollectionV1, RESTCSTranslatedNodeCollectionItemV1> {

    public CSTranslatedNodeV1Factory() {
        super(CSTranslatedNode.class);
    }

    @Override
    protected RESTCSTranslatedNodeV1 createRESTEntityFromDBEntityInternal(CSTranslatedNode entity, String baseUrl, String dataType,
            ExpandDataTrunk expand, Number revision, boolean expandParentReferences, EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTCSTranslatedNodeV1 retValue = new RESTCSTranslatedNodeV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        expandOptions.add(RESTCSTranslatedNodeV1.NODE_NAME);
        expandOptions.add(RESTCSTranslatedNodeV1.TRANSLATED_STRING_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setNodeId(entity.getCSNodeId());
        retValue.setNodeRevision(entity.getCSNodeRevision());

        // NODE
        if (expandParentReferences && expand != null && expand.contains(RESTCSTranslatedNodeV1.NODE_NAME) && entity.getEnversCSNode(
                entityManager) != null) {
            retValue.setNode(new CSNodeV1Factory().createRESTEntityFromDBEntity(entity.getEnversCSNode(entityManager), baseUrl, dataType,
                    expand.get(RESTCSTranslatedNodeV1.NODE_NAME), entity.getCSNodeRevision(), true, entityManager));
            retValue.getNode().setRevision(entity.getCSNodeRevision());
        }

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(
                    new RESTDataObjectCollectionFactory<RESTCSTranslatedNodeV1, CSTranslatedNode, RESTCSTranslatedNodeCollectionV1,
                            RESTCSTranslatedNodeCollectionItemV1>().create(
                            RESTCSTranslatedNodeCollectionV1.class, new CSTranslatedNodeV1Factory(), entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        // TRANSLATED STRINGS
        if (expand != null && expand.contains(RESTCSTranslatedNodeV1.TRANSLATED_STRING_NAME)) {
            retValue.setTranslatedNodeStrings_OTM(
                    new RESTDataObjectCollectionFactory<RESTCSTranslatedNodeStringV1, CSTranslatedNodeString,
                            RESTCSTranslatedNodeStringCollectionV1, RESTCSTranslatedNodeStringCollectionItemV1>().create(
                            RESTCSTranslatedNodeStringCollectionV1.class, new CSTranslatedNodeStringV1Factory(),
                            entity.getCSTranslatedNodeStringsArray(), RESTCSTranslatedNodeV1.TRANSLATED_STRING_NAME, dataType, expand,
                            baseUrl, false, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.CONTENT_SPEC_TRANSLATED_NODE_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final CSTranslatedNode entity,
            final RESTCSTranslatedNodeV1 dataObject) throws InvalidParameterException {
        if (dataObject.hasParameterSet(RESTCSTranslatedNodeV1.NODE_ID_NAME)) entity.setCSNodeId(dataObject.getNodeId());
        if (dataObject.hasParameterSet(RESTCSTranslatedNodeV1.NODE_REVISION_NAME)) entity.setCSNodeRevision(dataObject.getNodeRevision());

        entityManager.persist(entity);

        /* One To Many - Add will create a child entity */
        if (dataObject.hasParameterSet(
                RESTCSTranslatedNodeV1.TRANSLATED_STRING_NAME) && dataObject.getTranslatedNodeStrings_OTM() != null && dataObject
                .getTranslatedNodeStrings_OTM().getItems() != null) {
            dataObject.getTranslatedNodeStrings_OTM().removeInvalidChangeItemRequests();

            /* remove any items first */
            for (final RESTCSTranslatedNodeStringCollectionItemV1 restEntityItem : dataObject.getTranslatedNodeStrings_OTM().getItems()) {
                final RESTCSTranslatedNodeStringV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsRemoveItem()) {
                    final CSTranslatedNodeString dbEntity = entityManager.find(CSTranslatedNodeString.class, restEntity.getId());
                    if (dbEntity == null) throw new InvalidParameterException(
                            "No CSTranslatedNodeString entity was found with the primary key " + restEntity.getId());

                    entity.removeTranslatedString(dbEntity);
                    entityManager.remove(dbEntity);
                } else if (restEntityItem.returnIsAddItem()) {
                    final CSTranslatedNodeString dbEntity = new CSTranslatedNodeStringV1Factory().createDBEntityFromRESTEntity(
                            entityManager, restEntity);
                    entityManager.persist(dbEntity);
                    entity.addTranslatedString(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final CSTranslatedNodeString dbEntity = entityManager.find(CSTranslatedNodeString.class, restEntity.getId());
                    if (dbEntity == null) throw new InvalidParameterException(
                            "No CSTranslatedNodeString entity was found with the primary key " + restEntity.getId());

                    new CSTranslatedNodeStringV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);
                }
            }
        }
    }
}
