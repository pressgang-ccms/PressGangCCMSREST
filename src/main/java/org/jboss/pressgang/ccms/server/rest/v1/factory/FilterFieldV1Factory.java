/*
  Copyright 2011-2014 Red Hat

  This file is part of PresGang CCMS.

  PresGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PresGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PresGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.server.rest.v1.factory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.FilterField;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTFilterFieldCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTFilterFieldCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterFieldV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;

@ApplicationScoped
public class FilterFieldV1Factory extends RESTEntityFactory<RESTFilterFieldV1, FilterField, RESTFilterFieldCollectionV1,
        RESTFilterFieldCollectionItemV1> {
    @Inject
    protected FilterV1Factory filterFactory;

    @Override
    public RESTFilterFieldV1 createRESTEntityFromDBEntityInternal(final FilterField entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTFilterFieldV1 retValue = new RESTFilterFieldV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        retValue.setId(entity.getFilterFieldId());
        retValue.setDescription(entity.getDescription());
        retValue.setName(entity.getField());
        retValue.setValue(entity.getValue());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(
                    RESTEntityCollectionFactory.create(RESTFilterFieldCollectionV1.class, this, entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        // PARENT
        if (expandParentReferences && expand != null && expand.contains(RESTFilterFieldV1.FILTER_NAME) && entity.getFilter() != null) {
            retValue.setFilter(filterFactory.createRESTEntityFromDBEntity(entity.getFilter(), baseUrl, dataType,
                    expand.get(RESTFilterFieldV1.FILTER_NAME), revision, expandParentReferences));
        }

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntityFirstPass(final FilterField entity, final RESTFilterFieldV1 dataObject) {
        if (dataObject.hasParameterSet(RESTFilterFieldV1.DESCRIPTION_NAME)) entity.setDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTFilterFieldV1.NAME_NAME)) entity.setField(dataObject.getName());
        if (dataObject.hasParameterSet(RESTFilterFieldV1.VALUE_NAME)) entity.setValue(dataObject.getValue());
    }

    @Override
    protected Class<FilterField> getDatabaseClass() {
        return FilterField.class;
    }

}
