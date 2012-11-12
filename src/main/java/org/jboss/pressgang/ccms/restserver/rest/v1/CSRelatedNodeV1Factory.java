package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTCSTranslatedStringCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTCSTranslatedStringCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.join.RESTCSRelatedNodeCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.join.RESTCSRelatedNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTAssignedPropertyTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTAssignedPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSTranslatedStringV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.join.RESTCSRelatedNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTAssignedPropertyTagV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.entity.contentspec.CSNodeToCSNode;
import org.jboss.pressgang.ccms.restserver.entity.contentspec.CSNodeToPropertyTag;
import org.jboss.pressgang.ccms.restserver.entity.contentspec.CSTranslatedString;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;

public class CSRelatedNodeV1Factory
        extends
        RESTDataObjectFactory<RESTCSRelatedNodeV1, CSNodeToCSNode, RESTCSRelatedNodeCollectionV1, RESTCSRelatedNodeCollectionItemV1> {

    public CSRelatedNodeV1Factory() {
        super(CSNodeToCSNode.class);
    }

    @Override
    public RESTCSRelatedNodeV1 createRESTEntityFromDBEntity(final CSNodeToCSNode entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTCSRelatedNodeV1 retValue = new RESTCSRelatedNodeV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        expandOptions.add(RESTCSRelatedNodeV1.PARENT_NAME);
        expandOptions.add(RESTCSRelatedNodeV1.PROPERTIES_NAME);
        expandOptions.add(RESTCSRelatedNodeV1.RELATED_FROM_NAME);
        expandOptions.add(RESTCSRelatedNodeV1.RELATED_TO_NAME);
        expandOptions.add(RESTCSRelatedNodeV1.TRANSLATED_STRINGS_NAME);
        if (revision == null)
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getRelatedNode().getId());
        retValue.setRelationshipId(entity.getId());
        retValue.setTitle(entity.getRelatedNode().getCSNodeTitle());
        retValue.setRelationshipType(entity.getRelationshipType());
        retValue.setCondition(entity.getRelatedNode().getCondition());
        retValue.setFlag(entity.getRelatedNode().getFlag());
        retValue.setNodeType(entity.getRelatedNode().getCSNodeType());
        retValue.setTopicId(entity.getRelatedNode().getTopicId());
        retValue.setTopicRevision(entity.getRelatedNode().getTopicRevision());

        // REVISIONS
        if (revision == null) {
            retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTCSRelatedNodeV1, CSNodeToCSNode, RESTCSRelatedNodeCollectionV1, RESTCSRelatedNodeCollectionItemV1>()
                    .create(RESTCSRelatedNodeCollectionV1.class, new CSRelatedNodeV1Factory(), entity, entity.getRevisions(entityManager),
                            RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
        }

        // PARENT
        if (expandParentReferences && entity.getRelatedNode().getParent() != null && expand != null) {
            retValue.setParent(new CSNodeV1Factory().createRESTEntityFromDBEntity(entity.getRelatedNode().getParent(), baseUrl,
                    dataType, expand.get(RESTCSNodeV1.PARENT_NAME), entityManager));
        }

        // CONTENT SPEC
        if (entity.getRelatedNode().getContentSpec() != null && expand != null && expand.contains(RESTCSRelatedNodeV1.CONTENT_SPEC_NAME))
            retValue.setContentSpec(new ContentSpecV1Factory().createRESTEntityFromDBEntity(entity.getRelatedNode()
                    .getContentSpec(), baseUrl, dataType, expand.get(RESTCSRelatedNodeV1.CONTENT_SPEC_NAME), revision,
                    expandParentReferences, entityManager));

        // NEXT / PREVIOUS
        if (entity.getRelatedNode().getNext() != null)
            retValue.setNextNodeId(entity.getRelatedNode().getNext().getId());

        if (entity.getRelatedNode().getPrevious() != null)
            retValue.setPreviousNodeId(entity.getRelatedNode().getPrevious().getId());

        // RELATED FROM
        retValue.setRelatedFromNodes(new RESTDataObjectCollectionFactory<RESTCSRelatedNodeV1, CSNodeToCSNode, RESTCSRelatedNodeCollectionV1, RESTCSRelatedNodeCollectionItemV1>()
                .create(RESTCSRelatedNodeCollectionV1.class, new CSRelatedNodeV1Factory(), entity.getRelatedNode()
                        .getRelatedFromNodesList(), RESTCSRelatedNodeV1.RELATED_FROM_NAME, dataType, expand, baseUrl, entityManager));

        // RELATED TO
        retValue.setRelatedToNodes(new RESTDataObjectCollectionFactory<RESTCSRelatedNodeV1, CSNodeToCSNode, RESTCSRelatedNodeCollectionV1, RESTCSRelatedNodeCollectionItemV1>()
                .create(RESTCSRelatedNodeCollectionV1.class, new CSRelatedNodeV1Factory(), entity.getRelatedNode()
                        .getRelatedToNodesList(), RESTCSRelatedNodeV1.RELATED_FROM_NAME, dataType, expand, baseUrl, entityManager));

        // TRANSLATED STRINGS
        retValue.setTranslatedStrings_OTM(new RESTDataObjectCollectionFactory<RESTCSTranslatedStringV1, CSTranslatedString, RESTCSTranslatedStringCollectionV1, RESTCSTranslatedStringCollectionItemV1>()
                .create(RESTCSTranslatedStringCollectionV1.class, new CSTranslatedStringV1Factory(), entity.getRelatedNode()
                        .getCSTranslatedStringsList(), RESTCSRelatedNodeV1.TRANSLATED_STRINGS_NAME, dataType, expand, baseUrl, false,
                        entityManager));

        // PROPERTY TAGS
        retValue.setProperties(new RESTDataObjectCollectionFactory<RESTAssignedPropertyTagV1, CSNodeToPropertyTag, RESTAssignedPropertyTagCollectionV1, RESTAssignedPropertyTagCollectionItemV1>()
                .create(RESTAssignedPropertyTagCollectionV1.class, new CSNodePropertyTagV1Factory(), entity.getRelatedNode()
                        .getCSNodeToPropertyTagsList(), RESTCSRelatedNodeV1.PROPERTIES_NAME, dataType, expand, baseUrl, revision,
                        entityManager));

        retValue.setLinks(baseUrl, RESTv1Constants.CONTENT_SPEC_NODE_URL_NAME, dataType, retValue.getId());
        retValue.setLogDetails(new LogDetailsV1Factory().create(entity, revision, RESTBaseEntityV1.LOG_DETAILS_NAME, expand,
                dataType, baseUrl, entityManager));

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final CSNodeToCSNode entity,
            final RESTCSRelatedNodeV1 dataObject) {

        if (dataObject.hasParameterSet(RESTCSRelatedNodeV1.RELATIONSHIP_TYPE_NAME))
            entity.setRelationshipType(dataObject.getRelationshipType());

        entityManager.persist(entity);
    }
}
