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

import org.jboss.pressgang.ccms.model.Locale;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTLocaleCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTLocaleCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTLocaleV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;

@ApplicationScoped
public class LocaleV1Factory extends RESTEntityFactory<RESTLocaleV1, Locale, RESTLocaleCollectionV1, RESTLocaleCollectionItemV1> {

    @Override
    public RESTLocaleV1 createRESTEntityFromDBEntityInternal(final Locale entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTLocaleV1 retValue = new RESTLocaleV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getLocaleId());
        retValue.setValue(entity.getValue());
        retValue.setBuildValue(entity.getBuildValue());
        retValue.setTranslationValue(entity.getTranslationValue());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(new RESTLocaleCollectionV1());
        }

        retValue.setLinks(baseUrl, RESTv1Constants.LOCALE_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTLocaleV1> parent,
            final RESTLocaleV1 dataObject) {
        // Locale has no children so we have no changes to collect
    }

    @Override
    public void syncBaseDetails(final Locale entity, final RESTLocaleV1 dataObject) {
        if (dataObject.hasParameterSet(RESTLocaleV1.VALUE_NAME)) entity.setValue(dataObject.getValue());
        if (dataObject.hasParameterSet(RESTLocaleV1.TRANSLATION_VALUE_NAME)) entity.setTranslationValue(dataObject.getTranslationValue());
        if (dataObject.hasParameterSet(RESTLocaleV1.BUILD_VALUE_NAME)) entity.setBuildValue(dataObject.getBuildValue());
    }

    @Override
    protected Class<Locale> getDatabaseClass() {
        return Locale.class;
    }
}
