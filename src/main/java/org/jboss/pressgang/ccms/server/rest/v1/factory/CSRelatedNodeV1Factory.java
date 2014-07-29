/*
  Copyright 2011-2014 Red Hat

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

import org.jboss.pressgang.ccms.model.contentspec.CSNodeToCSNode;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.join.RESTCSRelatedNodeCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.join.RESTCSRelatedNodeCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTAssignedPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSNodeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.enums.RESTCSNodeRelationshipModeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.enums.RESTCSNodeRelationshipTypeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.enums.RESTCSNodeTypeV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.join.RESTCSRelatedNodeV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;

@ApplicationScoped
public class CSRelatedNodeV1Factory extends RESTEntityFactory<RESTCSRelatedNodeV1, CSNodeToCSNode, RESTCSRelatedNodeCollectionV1,
        RESTCSRelatedNodeCollectionItemV1> {
    @Inject
    protected CSNodeV1Factory csNodeFactory;
    @Inject
    protected ContentSpecV1Factory contentSpecFactory;
    @Inject
    protected CSNodePropertyTagV1Factory csNodePropertyTagFactory;

    @Override
    public RESTCSRelatedNodeV1 createRESTEntityFromDBEntityInternal(final CSNodeToCSNode entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTCSRelatedNodeV1 retValue = new RESTCSRelatedNodeV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTCSRelatedNodeV1.INHERITED_CONDITION_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        expandOptions.add(RESTCSRelatedNodeV1.PARENT_NAME);
        expandOptions.add(RESTCSRelatedNodeV1.PROPERTIES_NAME);
        expandOptions.add(RESTCSRelatedNodeV1.RELATED_FROM_NAME);
        expandOptions.add(RESTCSRelatedNodeV1.RELATED_TO_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getRelatedNode().getId());
        retValue.setRelationshipId(entity.getId());
        retValue.setRelationshipSort(entity.getRelationshipSort());
        retValue.setTitle(entity.getRelatedNode().getCSNodeTitle());
        retValue.setAdditionalText(entity.getRelatedNode().getAdditionalText());
        retValue.setTargetId(entity.getRelatedNode().getCSNodeTargetId());
        retValue.setRelationshipType(RESTCSNodeRelationshipTypeV1.getRelationshipType(entity.getRelationshipType()));
        retValue.setRelationshipMode(RESTCSNodeRelationshipModeV1.getRelationshipMode(entity.getRelationshipMode()));
        retValue.setCondition(entity.getRelatedNode().getCondition());
        retValue.setNodeType(RESTCSNodeTypeV1.getNodeType(entity.getRelatedNode().getCSNodeType()));
        retValue.setEntityId(entity.getRelatedNode().getEntityId());
        retValue.setEntityRevision(entity.getRelatedNode().getEntityRevision());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTCSRelatedNodeCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // PARENT
        if (expandParentReferences && expand != null && expand.contains(
                RESTCSRelatedNodeV1.PARENT_NAME) && entity.getRelatedNode().getParent() != null) {
            retValue.setParent(csNodeFactory.createRESTEntityFromDBEntity(entity.getRelatedNode().getParent(), baseUrl, dataType,
                    expand.get(RESTCSNodeV1.PARENT_NAME), revision));
        }

        // CONTENT SPEC
        if (expand != null && expand.contains(RESTCSRelatedNodeV1.CONTENT_SPEC_NAME) && entity.getRelatedNode().getContentSpec() != null) {
            retValue.setContentSpec(
                    contentSpecFactory.createRESTEntityFromDBEntity(entity.getRelatedNode().getContentSpec(), baseUrl, dataType,
                            expand.get(RESTCSRelatedNodeV1.CONTENT_SPEC_NAME), revision, expandParentReferences));
        }

        // INHERITED CONDITION
        if (expand != null && expand.contains(RESTCSNodeV1.INHERITED_CONDITION_NAME)) {
            retValue.setInheritedCondition(entity.getRelatedNode().getInheritedCondition());
        }

        // PROPERTY TAGS
        if (expand != null && expand.contains(RESTCSRelatedNodeV1.PROPERTIES_NAME)) {
            retValue.setProperties(
                    RESTEntityCollectionFactory.create(RESTAssignedPropertyTagCollectionV1.class, csNodePropertyTagFactory,
                            entity.getRelatedNode().getPropertyTagsList(), RESTCSRelatedNodeV1.PROPERTIES_NAME, dataType, expand, baseUrl,
                            revision, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.CONTENT_SPEC_NODE_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntityFirstPass(final CSNodeToCSNode entity, final RESTCSRelatedNodeV1 dataObject) {
        if (dataObject.hasParameterSet(RESTCSRelatedNodeV1.RELATIONSHIP_TYPE_NAME))
            entity.setRelationshipType(RESTCSNodeRelationshipTypeV1.getRelationshipTypeId(dataObject.getRelationshipType()));
        if (dataObject.hasParameterSet(RESTCSRelatedNodeV1.RELATIONSHIP_SORT_NAME))
            entity.setRelationshipSort(dataObject.getRelationshipSort());
        if (dataObject.hasParameterSet(RESTCSRelatedNodeV1.RELATIONSHIP_MODE_NAME))
            entity.setRelationshipMode(RESTCSNodeRelationshipModeV1.getRelationshipModeId(dataObject.getRelationshipMode()));
    }

    @Override
    protected Class<CSNodeToCSNode> getDatabaseClass() {
        return CSNodeToCSNode.class;
    }
}
