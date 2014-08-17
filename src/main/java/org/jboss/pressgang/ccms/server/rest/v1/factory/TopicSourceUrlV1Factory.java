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
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.TopicSourceUrl;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTopicSourceUrlCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTopicSourceUrlCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicSourceUrlV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseAuditedEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;

@ApplicationScoped
public class TopicSourceUrlV1Factory extends RESTEntityFactory<RESTTopicSourceUrlV1, TopicSourceUrl, RESTTopicSourceUrlCollectionV1,
        RESTTopicSourceUrlCollectionItemV1> {

    @Override
    public RESTTopicSourceUrlV1 createRESTEntityFromDBEntityInternal(final TopicSourceUrl entity, final String baseUrl, String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTTopicSourceUrlV1 retValue = new RESTTopicSourceUrlV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseAuditedEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseAuditedEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getTopicSourceUrlId());
        retValue.setTitle(entity.getTitle());
        retValue.setDescription(entity.getDescription());
        retValue.setUrl(entity.getSourceUrl());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTTopicV1.REVISIONS_NAME)) {
            retValue.setRevisions(
                    RESTEntityCollectionFactory.create(RESTTopicSourceUrlCollectionV1.class, this, entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseAuditedEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTTopicSourceUrlV1> parent, final RESTTopicSourceUrlV1 dataObject) {
        // TopicToSourceUrl has no children that can be changed, so we have no changes to collect
    }

    @Override
    public void syncBaseDetails(final TopicSourceUrl entity, final RESTTopicSourceUrlV1 dataObject) {
        if (dataObject.hasParameterSet(RESTTopicSourceUrlV1.TITLE_NAME)) entity.setTitle(dataObject.getTitle());
        if (dataObject.hasParameterSet(RESTTopicSourceUrlV1.DESCRIPTION_NAME)) entity.setDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTTopicSourceUrlV1.URL_NAME)) entity.setSourceUrl(dataObject.getUrl());
    }

    @Override
    protected Class<TopicSourceUrl> getDatabaseClass() {
        return TopicSourceUrl.class;
    }
}
