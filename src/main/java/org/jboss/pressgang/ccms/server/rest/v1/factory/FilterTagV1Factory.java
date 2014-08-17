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

import org.jboss.pressgang.ccms.model.FilterTag;
import org.jboss.pressgang.ccms.model.Tag;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTFilterTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTFilterTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseAuditedEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class FilterTagV1Factory extends RESTEntityFactory<RESTFilterTagV1, FilterTag, RESTFilterTagCollectionV1,
        RESTFilterTagCollectionItemV1> {
    @Inject
    protected FilterV1Factory filterFactory;
    @Inject
    protected TagV1Factory tagFactory;

    @Override
    public RESTFilterTagV1 createRESTEntityFromDBEntityInternal(final FilterTag entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTFilterTagV1 retValue = new RESTFilterTagV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTFilterTagV1.TAG_NAME);
        expandOptions.add(RESTBaseAuditedEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseAuditedEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        retValue.setId(entity.getFilterTagId());
        retValue.setState(entity.getTagState());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseAuditedEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTFilterTagCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseAuditedEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // PARENT
        if (expandParentReferences && expand != null && expand.contains(RESTFilterTagV1.FILTER_NAME) && entity.getFilter() != null) {
            retValue.setFilter(filterFactory.createRESTEntityFromDBEntity(entity.getFilter(), baseUrl, dataType,
                    expand.get(RESTFilterTagV1.FILTER_NAME), revision, expandParentReferences));
        }

        // FILTER TAG
        if (expand != null && expand.contains(RESTFilterTagV1.TAG_NAME) && entity.getTag() != null) {
            retValue.setTag(tagFactory.createRESTEntityFromDBEntity(entity.getTag(), baseUrl, dataType,
                    expand.get(RESTFilterTagV1.TAG_NAME), revision, expandParentReferences));
        }

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTFilterTagV1> parent, final RESTFilterTagV1 dataObject) {
        // FilterTag has no children that can be changed, so we have no changes to collect
    }

    @Override
    public void syncBaseDetails(final FilterTag entity, final RESTFilterTagV1 dataObject) {
        if (dataObject.hasParameterSet(RESTFilterTagV1.STATE_NAME)) entity.setTagState(dataObject.getState());
    }

    @Override
    public void syncAdditionalDetails(FilterTag entity, RESTFilterTagV1 dataObject) {
        // Set the Tag for the FilterTag
        if (dataObject.hasParameterSet(RESTFilterTagV1.TAG_NAME)) {
            final RESTTagV1 restEntity = dataObject.getTag();

            if (restEntity != null) {
                final Tag dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, restEntity, Tag.class);
                if (dbEntity == null)
                    throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());

                entity.setTag(dbEntity);
            } else {
                entity.setTag(null);
            }
        }
    }

    @Override
    protected Class<FilterTag> getDatabaseClass() {
        return FilterTag.class;
    }
}
