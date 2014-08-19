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

import org.jboss.pressgang.ccms.model.IntegerConstants;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTIntegerConstantCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTIntegerConstantCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTIntegerConstantV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTStringConstantV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseAuditedEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;

@ApplicationScoped
public class IntegerConstantV1Factory extends RESTEntityFactory<RESTIntegerConstantV1, IntegerConstants,
        RESTIntegerConstantCollectionV1, RESTIntegerConstantCollectionItemV1> {

    @Override
    public RESTIntegerConstantV1 createRESTEntityFromDBEntityInternal(final IntegerConstants entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTIntegerConstantV1 retValue = new RESTIntegerConstantV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseAuditedEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseAuditedEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setName(entity.getConstantName());
        retValue.setValue(entity.getConstantValue());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseAuditedEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(
                    RESTEntityCollectionFactory.create(RESTIntegerConstantCollectionV1.class, this, entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseAuditedEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.INTEGERCONSTANT_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTIntegerConstantV1> parent, final RESTIntegerConstantV1 dataObject) {
        // IntegerConstant has no children that can be changed, so we have no changes to collect
    }

    @Override
    public void syncBaseDetails(final IntegerConstants entity, final RESTIntegerConstantV1 dataObject) {
        if (dataObject.hasParameterSet(RESTStringConstantV1.NAME_NAME)) entity.setConstantName(dataObject.getName());
        if (dataObject.hasParameterSet(RESTStringConstantV1.VALUE_NAME)) entity.setConstantValue(dataObject.getValue());
    }

    @Override
    protected Class<IntegerConstants> getDatabaseClass() {
        return IntegerConstants.class;
    }
}
