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

import org.jboss.pressgang.ccms.model.TagToCategory;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTCategoryInTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTCategoryInTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTTagInCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseAuditedEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTCategoryInTagV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;

@ApplicationScoped
public class CategoryInTagV1Factory extends RESTEntityFactory<RESTCategoryInTagV1, TagToCategory, RESTCategoryInTagCollectionV1,
        RESTCategoryInTagCollectionItemV1> {
    @Inject
    protected TagInCategoryV1Factory tagInCategoryFactory;

    @Override
    public RESTCategoryInTagV1 createRESTEntityFromDBEntityInternal(final TagToCategory entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTCategoryInTagV1 retValue = new RESTCategoryInTagV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTv1Constants.TAGS_EXPANSION_NAME);
        expandOptions.add(RESTBaseAuditedEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseAuditedEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        retValue.setId(entity.getCategory().getCategoryId());
        retValue.setRelationshipId(entity.getId());
        retValue.setName(entity.getCategory().getCategoryName());
        retValue.setDescription(entity.getCategory().getCategoryDescription());
        retValue.setMutuallyExclusive(entity.getCategory().isMutuallyExclusive());
        retValue.setSort(entity.getCategory().getCategorySort());
        retValue.setRelationshipSort(entity.getSorting());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseAuditedEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTCategoryInTagCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseAuditedEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // TAGS
        if (expand != null && expand.contains(RESTCategoryInTagV1.TAGS_NAME)) {
            retValue.setTags(RESTEntityCollectionFactory.create(RESTTagInCategoryCollectionV1.class, tagInCategoryFactory,
                    entity.getCategory().getTagToCategoriesArray(), RESTCategoryInTagV1.TAGS_NAME, dataType, expand, baseUrl, revision,
                    entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.CATEGORY_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTCategoryInTagV1> parent,
            final RESTCategoryInTagV1 dataObject) {
        // CategoryInTags has no children that can be changed, so we have no changes to collect
    }

    @Override
    public void syncBaseDetails(final TagToCategory entity, final RESTCategoryInTagV1 dataObject) {
        if (dataObject.hasParameterSet(RESTCategoryInTagV1.RELATIONSHIP_SORT_NAME)) entity.setSorting(dataObject.getRelationshipSort());
    }

    @Override
    protected Class<TagToCategory> getDatabaseClass() {
        return TagToCategory.class;
    }
}
