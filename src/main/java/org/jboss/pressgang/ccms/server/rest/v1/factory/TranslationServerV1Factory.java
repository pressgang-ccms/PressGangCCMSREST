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

import org.jboss.pressgang.ccms.model.TranslationServer;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTranslationServerCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTranslationServerCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTranslationServerV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;

@ApplicationScoped
public class TranslationServerV1Factory extends RESTEntityFactory<RESTTranslationServerV1, TranslationServer,
        RESTTranslationServerCollectionV1, RESTTranslationServerCollectionItemV1> {

    @Override
    public RESTTranslationServerV1 createRESTEntityFromDBEntityInternal(final TranslationServer entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTTranslationServerV1 retValue = new RESTTranslationServerV1();

        final List<String> expandOptions = new ArrayList<String>();
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setName(entity.getName());
        retValue.setUrl(entity.getUrl());

        retValue.setLinks(baseUrl, RESTv1Constants.LOCALE_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTTranslationServerV1> parent,
            final RESTTranslationServerV1 dataObject) {
        // TranslationServer has no children so we have no changes to collect
    }

    @Override
    public void syncBaseDetails(final TranslationServer entity, final RESTTranslationServerV1 dataObject) {
        if (dataObject.hasParameterSet(RESTTranslationServerV1.NAME_NAME)) entity.setName(dataObject.getName());
        if (dataObject.hasParameterSet(RESTTranslationServerV1.URL_NAME)) entity.setUrl(dataObject.getUrl());
    }

    @Override
    protected Class<TranslationServer> getDatabaseClass() {
        return TranslationServer.class;
    }
}
