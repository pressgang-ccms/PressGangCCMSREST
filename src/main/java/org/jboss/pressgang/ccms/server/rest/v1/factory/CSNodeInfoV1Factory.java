package org.jboss.pressgang.ccms.server.rest.v1.factory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.contentspec.CSInfoNode;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTCSInfoNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTCSInfoNodeCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSInfoNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSNodeV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;

@ApplicationScoped
public class CSNodeInfoV1Factory extends RESTEntityFactory<RESTCSInfoNodeV1, CSInfoNode, RESTCSInfoNodeCollectionV1, RESTCSInfoNodeCollectionItemV1> {
    @Inject
    protected CSNodeV1Factory csNodeFactory;

    @Inject
    protected TopicV1Factory topicFactory;

    @Override
    public RESTCSInfoNodeV1 createRESTEntityFromDBEntityInternal(final CSInfoNode entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTCSInfoNodeV1 retValue = new RESTCSInfoNodeV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTCSInfoNodeV1.INHERITED_CONDITION_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        expandOptions.add(RESTCSInfoNodeV1.NODE_NAME);
        expandOptions.add(RESTCSInfoNodeV1.TOPIC_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setTopicId(entity.getTopicId());
        retValue.setTopicRevision(entity.getTopicRevision());
        retValue.setCondition(entity.getCondition());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTCSInfoNodeCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // INHERITED CONDITION
        if (expand != null && expand.contains(RESTCSInfoNodeV1.INHERITED_CONDITION_NAME)) {
            retValue.setInheritedCondition(entity.getInheritedCondition());
        }

        // PARENT
        if (expandParentReferences && expand != null && expand.contains(RESTCSNodeV1.PARENT_NAME) && entity.getCSNode() != null) {
            retValue.setCSNode(
                    csNodeFactory.createRESTEntityFromDBEntity(entity.getCSNode(), baseUrl, dataType, expand.get(RESTCSNodeV1.PARENT_NAME),
                            revision));
        }

        // TOPIC
        if (expand != null && expand.contains(RESTCSInfoNodeV1.TOPIC_NAME)) {
            final Topic topic = entity.getTopic(entityManager);
            if (topic != null) {
                retValue.setTopic(topicFactory.createRESTEntityFromDBEntity(topic, baseUrl, dataType,
                        expand.get(RESTCSInfoNodeV1.TOPIC_NAME), revision, expandParentReferences));
            }
        }

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntityFirstPass(final CSInfoNode entity, final RESTCSInfoNodeV1 dataObject) {
        if (dataObject.hasParameterSet(RESTCSInfoNodeV1.TOPIC_ID_NAME)) entity.setTopicId(dataObject.getTopicId());

        if (dataObject.hasParameterSet(RESTCSInfoNodeV1.TOPIC_REVISION_NAME)) entity.setTopicRevision(dataObject.getTopicRevision());


        if (dataObject.hasParameterSet(RESTCSNodeV1.CONDITION_NAME)) entity.setCondition(dataObject.getCondition());
    }

    @Override
    public void syncDBEntityWithRESTEntitySecondPass(final CSInfoNode entity, final RESTCSInfoNodeV1 dataObject) {

    }

    @Override
    protected Class<CSInfoNode> getDatabaseClass() {
        return CSInfoNode.class;
    }
}
