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

import org.jboss.pressgang.ccms.model.MinHash;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTMinHashCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTMinHashCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTMinHashV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseAuditedEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;

@ApplicationScoped
public class MinHashV1Factory extends RESTEntityFactory<RESTMinHashV1, MinHash, RESTMinHashCollectionV1, RESTMinHashCollectionItemV1> {
    @Override
    public RESTMinHashV1 createRESTEntityFromDBEntityInternal(final MinHash entity, final String baseUrl, final String dataType,
                                                              final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTMinHashV1 retValue = new RESTMinHashV1();

        retValue.setId(entity.getMinHashId());
        retValue.setMinHashFuncId(entity.getMinHashFuncID());
        retValue.setMinHash(entity.getMinHash());

        final List<String> expandOptions = new ArrayList<String>();
        if (revision == null) expandOptions.add(RESTBaseAuditedEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseAuditedEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTMinHashCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseAuditedEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTMinHashV1> parent, final RESTMinHashV1 dataObject) {
        // MinHash has no children that can be changed, so we have no changes to collect
    }

    @Override
    public void syncBaseDetails(final MinHash entity, final RESTMinHashV1 dataObject) {
        // The generation of minhashes is done in the TopicV1Factory. These values can not be set by the client.
    }

    @Override
    protected Class<MinHash> getDatabaseClass() {
        return MinHash.class;
    }
}
