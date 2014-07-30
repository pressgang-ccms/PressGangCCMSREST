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

package org.jboss.pressgang.ccms.server.rest.v1.factory.base;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.base.ToPropertyTag;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTAssignedPropertyTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTAssignedPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTPropertyCategoryInPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBasePropertyTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTAssignedPropertyTagV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.PropertyCategoryInPropertyTagV1Factory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;

public abstract class BaseAssignedPropertyTagV1Factory<T extends ToPropertyTag<T>, U extends BaseAssignedPropertyTagV1Factory<T,
        U>> extends RESTEntityFactory<RESTAssignedPropertyTagV1, T, RESTAssignedPropertyTagCollectionV1,
                        RESTAssignedPropertyTagCollectionItemV1> {
    @Inject
    protected PropertyCategoryInPropertyTagV1Factory propertyCategoryInPropertyTagFactory;

    @Override
    public RESTAssignedPropertyTagV1 createRESTEntityFromDBEntityInternal(final T entity, final String baseUrl, String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter topic can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTAssignedPropertyTagV1 retValue = new RESTAssignedPropertyTagV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getPropertyTag().getPropertyTagId());
        retValue.setRelationshipId(entity.getId());
        retValue.setDescription(entity.getPropertyTag().getPropertyTagDescription());
        retValue.setName(entity.getPropertyTag().getPropertyTagName());
        retValue.setRegex(entity.getPropertyTag().getPropertyTagRegex());
        retValue.setValid(entity.isValid(entityManager, revision));
        retValue.setValue(entity.getValue());
        retValue.setIsUnique(entity.getPropertyTag().getPropertyTagIsUnique());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTAssignedPropertyTagCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // PROPERTY CATEGORIES
        if (expand != null && expand.contains(RESTBasePropertyTagV1.PROPERTY_CATEGORIES_NAME)) {
            retValue.setPropertyCategories(RESTEntityCollectionFactory.create(RESTPropertyCategoryInPropertyTagCollectionV1.class,
                    propertyCategoryInPropertyTagFactory, entity.getPropertyTag().getPropertyTagToPropertyTagCategoriesList(),
                    RESTBasePropertyTagV1.PROPERTY_CATEGORIES_NAME, dataType, expand, baseUrl, revision, false, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.PROPERTYTAG_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTAssignedPropertyTagV1> parent,
            final RESTAssignedPropertyTagV1 dataObject) {
        // Assigned Properties has no children that can be changed, so we have no changes to collect
    }

    @Override
    public void syncBaseDetails(final T entity, final RESTAssignedPropertyTagV1 dataObject) {
        if (dataObject.hasParameterSet(RESTAssignedPropertyTagV1.VALUE_NAME)) entity.setValue(dataObject.getValue());
    }
}
