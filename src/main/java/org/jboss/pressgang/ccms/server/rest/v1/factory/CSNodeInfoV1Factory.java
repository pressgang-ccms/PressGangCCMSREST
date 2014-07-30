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

import org.jboss.pressgang.ccms.model.Topic;
import org.jboss.pressgang.ccms.model.contentspec.CSInfoNode;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTCSInfoNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTCSInfoNodeCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSInfoNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSNodeV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
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
    public void collectChangeInformation(RESTChangeAction<RESTCSInfoNodeV1> parent, RESTCSInfoNodeV1 dataObject) {
        // CSNodeInfo has no children that can be changed, so we have no changes to collect
    }

    @Override
    public void syncBaseDetails(CSInfoNode entity, RESTCSInfoNodeV1 dataObject) {
        if (dataObject.hasParameterSet(RESTCSInfoNodeV1.TOPIC_ID_NAME)) entity.setTopicId(dataObject.getTopicId());
        if (dataObject.hasParameterSet(RESTCSInfoNodeV1.TOPIC_REVISION_NAME)) entity.setTopicRevision(dataObject.getTopicRevision());
        if (dataObject.hasParameterSet(RESTCSNodeV1.CONDITION_NAME)) entity.setCondition(dataObject.getCondition());
    }

    @Override
    protected Class<CSInfoNode> getDatabaseClass() {
        return CSInfoNode.class;
    }
}
