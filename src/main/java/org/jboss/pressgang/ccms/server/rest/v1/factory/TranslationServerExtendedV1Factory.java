/*
 * Copyright 2011-2014 Red Hat, Inc.
 *
 * This file is part of PressGang CCMS.
 *
 * PressGang CCMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PressGang CCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with PressGang CCMS. If not, see <http://www.gnu.org/licenses/>.
 */

package org.jboss.pressgang.ccms.server.rest.v1.factory;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.TranslationServer;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTranslationServerExtendedCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTranslationServerExtendedCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTranslationServerExtendedV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;

@ApplicationScoped
public class TranslationServerExtendedV1Factory extends RESTEntityFactory<RESTTranslationServerExtendedV1, TranslationServer,
        RESTTranslationServerExtendedCollectionV1, RESTTranslationServerExtendedCollectionItemV1> {

    @Override
    public RESTTranslationServerExtendedV1 createRESTEntityFromDBEntityInternal(final TranslationServer entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTTranslationServerExtendedV1 retValue = new RESTTranslationServerExtendedV1();

        final List<String> expandOptions = new ArrayList<String>();
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setName(entity.getName());
        retValue.setUrl(entity.getUrl());
        retValue.setUsername(entity.getUsername());
        retValue.setKey(entity.getApikey());

        retValue.setLinks(baseUrl, RESTv1Constants.LOCALE_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTTranslationServerExtendedV1> parent,
            final RESTTranslationServerExtendedV1 dataObject) {
        // TranslationServer has no children so we have no changes to collect
    }

    @Override
    public void syncBaseDetails(final TranslationServer entity, final RESTTranslationServerExtendedV1 dataObject) {
        if (dataObject.hasParameterSet(RESTTranslationServerExtendedV1.NAME_NAME)) entity.setName(dataObject.getName());
        if (dataObject.hasParameterSet(RESTTranslationServerExtendedV1.URL_NAME)) entity.setUrl(dataObject.getUrl());
        if (dataObject.hasParameterSet(RESTTranslationServerExtendedV1.USERNAME_NAME)) entity.setUsername(dataObject.getUsername());
        if (dataObject.hasParameterSet(RESTTranslationServerExtendedV1.KEY_NAME)) entity.setApikey(dataObject.getKey());
    }

    @Override
    protected Class<TranslationServer> getDatabaseClass() {
        return TranslationServer.class;
    }
}
