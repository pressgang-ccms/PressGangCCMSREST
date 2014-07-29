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

import org.jboss.pressgang.ccms.model.PropertyTagToPropertyTagCategory;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTPropertyCategoryInPropertyTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTPropertyCategoryInPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTPropertyTagInPropertyCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTPropertyCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTPropertyCategoryInPropertyTagV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;

@ApplicationScoped
public class PropertyCategoryInPropertyTagV1Factory extends RESTEntityFactory<RESTPropertyCategoryInPropertyTagV1,
        PropertyTagToPropertyTagCategory, RESTPropertyCategoryInPropertyTagCollectionV1,
        RESTPropertyCategoryInPropertyTagCollectionItemV1> {
    @Inject
    protected PropertyTagInPropertyCategoryV1Factory propertyTagInPropertyCategoryFactory;

    @Override
    public RESTPropertyCategoryInPropertyTagV1 createRESTEntityFromDBEntityInternal(final PropertyTagToPropertyTagCategory entity,
            final String baseUrl, final String dataType, final ExpandDataTrunk expand, final Number revision,
            final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTPropertyCategoryInPropertyTagV1 retValue = new RESTPropertyCategoryInPropertyTagV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTPropertyCategoryV1.PROPERTY_TAGS_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getPropertyTagCategory().getId());
        retValue.setName(entity.getPropertyTagCategory().getPropertyTagCategoryName());
        retValue.setDescription(entity.getPropertyTagCategory().getPropertyTagCategoryDescription());
        retValue.setRelationshipId(entity.getId());
        retValue.setRelationshipSort(entity.getSorting());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTPropertyCategoryInPropertyTagCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // PROPERTY TAGS
        if (expand != null && expand.contains(RESTPropertyCategoryInPropertyTagV1.PROPERTY_TAGS_NAME)) {
            retValue.setPropertyTags(RESTEntityCollectionFactory.create(RESTPropertyTagInPropertyCategoryCollectionV1.class,
                    propertyTagInPropertyCategoryFactory, entity.getPropertyTagCategory().getPropertyTagToPropertyTagCategoriesList(),
                    RESTPropertyCategoryInPropertyTagV1.PROPERTY_TAGS_NAME, dataType, expand, baseUrl, revision, false, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.PROPERTY_CATEGORY_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTPropertyCategoryInPropertyTagV1> parent,
            final RESTPropertyCategoryInPropertyTagV1 dataObject) {
        // PropertyCategoryInPropertyTag has no children that can be changed, so we have no changes to collect
    }

    @Override
    public void syncBaseDetails(final PropertyTagToPropertyTagCategory entity,
            final RESTPropertyCategoryInPropertyTagV1 dataObject) {
        if (dataObject.hasParameterSet(RESTPropertyCategoryInPropertyTagV1.RELATIONSHIP_SORT_NAME))
            entity.setSorting(dataObject.getRelationshipSort());
    }

    @Override
    protected Class<PropertyTagToPropertyTagCategory> getDatabaseClass() {
        return PropertyTagToPropertyTagCategory.class;
    }
}
