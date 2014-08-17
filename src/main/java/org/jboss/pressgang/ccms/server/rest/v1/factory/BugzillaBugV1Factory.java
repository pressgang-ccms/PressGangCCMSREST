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

import org.jboss.pressgang.ccms.model.BugzillaBug;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTBugzillaBugCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTBugzillaBugCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTBugzillaBugV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseAuditedEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;

@ApplicationScoped
public class BugzillaBugV1Factory extends RESTEntityFactory<RESTBugzillaBugV1, BugzillaBug, RESTBugzillaBugCollectionV1,
        RESTBugzillaBugCollectionItemV1> {
    @Override
    public RESTBugzillaBugV1 createRESTEntityFromDBEntityInternal(final BugzillaBug entity, final String baseUrl, String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTBugzillaBugV1 retValue = new RESTBugzillaBugV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseAuditedEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseAuditedEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getBugzillaBugId());
        retValue.setIsOpen(entity.getBugzillaBugOpen());
        retValue.setBugId(entity.getBugzillaBugBugzillaId());
        retValue.setSummary(entity.getBugzillaBugSummary());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseAuditedEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(
                    RESTEntityCollectionFactory.create(RESTBugzillaBugCollectionV1.class, this, entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseAuditedEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTBugzillaBugV1> parent,
            final RESTBugzillaBugV1 dataObject) {
        // Bugzilla Bugs has no children so we have no changes to collect
    }

    @Override
    public void syncBaseDetails(final BugzillaBug entity, final RESTBugzillaBugV1 dataObject) {
        if (dataObject.hasParameterSet(RESTBugzillaBugV1.BUG_ID)) entity.setBugzillaBugBugzillaId(dataObject.getBugId());
        if (dataObject.hasParameterSet(RESTBugzillaBugV1.BUG_ISOPEN)) entity.setBugzillaBugOpen(dataObject.getIsOpen());
        if (dataObject.hasParameterSet(RESTBugzillaBugV1.BUG_SUMMARY)) entity.setBugzillaBugSummary(dataObject.getSummary());
    }

    @Override
    protected Class<BugzillaBug> getDatabaseClass() {
        return BugzillaBug.class;
    }
}
