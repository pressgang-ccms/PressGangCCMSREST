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

import org.jboss.pressgang.ccms.model.Locale;
import org.jboss.pressgang.ccms.model.base.PressGangEntity;
import org.jboss.pressgang.ccms.model.contentspec.TranslatedCSNodeString;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTTranslatedCSNodeStringCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTTranslatedCSNodeStringCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTLocaleV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseAuditedEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTranslatedCSNodeStringV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class TranslatedCSNodeStringV1Factory extends RESTEntityFactory<RESTTranslatedCSNodeStringV1, TranslatedCSNodeString,
        RESTTranslatedCSNodeStringCollectionV1, RESTTranslatedCSNodeStringCollectionItemV1> {
    @Inject
    protected LocaleV1Factory localeFactory;
    @Inject
    protected TranslatedCSNodeV1Factory translatedCSNodeFactory;

    @Override
    public RESTTranslatedCSNodeStringV1 createRESTEntityFromDBEntityInternal(final TranslatedCSNodeString entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTTranslatedCSNodeStringV1 retValue = new RESTTranslatedCSNodeStringV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseAuditedEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseAuditedEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setTranslatedString(entity.getTranslatedString());
        retValue.setFuzzyTranslation(entity.getFuzzyTranslation());

        // LOCALE
        if (entity.getLocale() != null) {
            final ExpandDataTrunk childExpand = expand == null ? null: expand.get(RESTTranslatedCSNodeStringV1.LOCALE_NAME);
            retValue.setLocale(localeFactory.createRESTEntityFromDBEntity(entity.getLocale(), baseUrl, dataType, childExpand, revision));
        }

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseAuditedEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTTranslatedCSNodeStringCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseAuditedEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // Set the object references
        if (expandParentReferences && expand != null && expand.contains(
                RESTTranslatedCSNodeStringV1.TRANSLATEDNODE_NAME) && entity.getTranslatedCSNode() != null) {
            retValue.setTranslatedNode(translatedCSNodeFactory.createRESTEntityFromDBEntity(entity.getTranslatedCSNode(), baseUrl, dataType,
                    expand.get(RESTTranslatedCSNodeStringV1.TRANSLATEDNODE_NAME), revision, expandParentReferences));
        }

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTTranslatedCSNodeStringV1> parent,
            final RESTTranslatedCSNodeStringV1 dataObject) {
        // ENTITIES
        if (dataObject.hasParameterSet(RESTTranslatedCSNodeStringV1.LOCALE_NAME)) {
            collectChangeInformationFromEntity(parent, dataObject.getLocale(), localeFactory, RESTTranslatedCSNodeStringV1.LOCALE_NAME);
        }
    }

    @Override
    public void syncBaseDetails(final TranslatedCSNodeString entity, final RESTTranslatedCSNodeStringV1 dataObject) {
        if (dataObject.hasParameterSet(RESTTranslatedCSNodeStringV1.TRANSLATEDSTRING_NAME))
            entity.setTranslatedString(dataObject.getTranslatedString());
        if (dataObject.hasParameterSet(RESTTranslatedCSNodeStringV1.FUZZY_TRANSLATION_NAME))
            entity.setFuzzyTranslation(dataObject.getFuzzyTranslation());
    }

    @Override
    protected void doDeleteChildAction(final TranslatedCSNodeString entity, final RESTTranslatedCSNodeStringV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();

        if (restEntity instanceof RESTLocaleV1 || RESTTranslatedCSNodeStringV1.LOCALE_NAME.equals(action.getUniqueId())) {
            entity.setLocale(null);
        }
    }

    @Override
    protected PressGangEntity doCreateChildAction(final TranslatedCSNodeString entity, final RESTTranslatedCSNodeStringV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();
        final PressGangEntity dbEntity;

        if (restEntity instanceof RESTLocaleV1) {
            dbEntity = entityManager.find(Locale.class, restEntity.getId());
            if (dbEntity == null) {
                throw new BadRequestException("No Locale entity was found with the primary key " + restEntity.getId());
            }

            entity.setLocale((Locale) dbEntity);
        } else {
            throw new IllegalArgumentException("Item is not a child of TranslatedCSNodeString");
        }

        return dbEntity;
    }

    @Override
    protected PressGangEntity getChildEntityForAction(final TranslatedCSNodeString entity, final RESTTranslatedCSNodeStringV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();

        final PressGangEntity dbEntity;
        if (restEntity instanceof RESTLocaleV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTLocaleV1) restEntity, Locale.class);
            if (dbEntity == null) {
                throw new BadRequestException("No Locale entity was found with the primary key " + restEntity.getId());
            }
            entity.setLocale((Locale) dbEntity);
        } else {
            throw new IllegalArgumentException("Item is not a child of TranslatedCSNodeString");
        }

        return dbEntity;
    }

    @Override
    protected Class<TranslatedCSNodeString> getDatabaseClass() {
        return TranslatedCSNodeString.class;
    }
}
