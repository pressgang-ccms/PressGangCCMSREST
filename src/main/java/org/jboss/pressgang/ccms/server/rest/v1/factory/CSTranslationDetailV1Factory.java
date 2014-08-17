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
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.Locale;
import org.jboss.pressgang.ccms.model.TranslationServer;
import org.jboss.pressgang.ccms.model.base.PressGangEntity;
import org.jboss.pressgang.ccms.model.contentspec.CSTranslationDetail;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTLocaleCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTCSTranslationDetailCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTCSTranslationDetailCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTLocaleV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTranslationServerV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSTranslationDetailV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class CSTranslationDetailV1Factory extends RESTEntityFactory<RESTCSTranslationDetailV1, CSTranslationDetail,
        RESTCSTranslationDetailCollectionV1, RESTCSTranslationDetailCollectionItemV1> {

    @Inject
    protected LocaleV1Factory localeFactory;
    @Inject
    protected TranslationServerV1Factory translationServerFactory;

    @Override
    public RESTCSTranslationDetailV1 createRESTEntityFromDBEntityInternal(final CSTranslationDetail entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTCSTranslationDetailV1 retValue = new RESTCSTranslationDetailV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTCSTranslationDetailV1.LOCALES_NAME);
        expandOptions.add(RESTCSTranslationDetailV1.TRANSLATION_SERVER_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setEnabled(entity.getEnabled());
        retValue.setProject(entity.getProject());
        retValue.setProjectVersion(entity.getProjectVersion());

        // TRANSLATION SERVER
        if (expand != null && expand.contains(RESTCSTranslationDetailV1.TRANSLATION_SERVER_NAME) && entity.getTranslationServer() != null) {
            final ExpandDataTrunk childExpand = expand == null ? null: expand.get(RESTCSTranslationDetailV1.TRANSLATION_SERVER_NAME);
            retValue.setTranslationServer(translationServerFactory.createRESTEntityFromDBEntity(entity.getTranslationServer(), baseUrl,
                    dataType, childExpand, revision));
        }

        // LOCALES
        if (expand != null && expand.contains(RESTCSTranslationDetailV1.LOCALES_NAME)) {
            retValue.setLocales(RESTEntityCollectionFactory.create(RESTLocaleCollectionV1.class, localeFactory, entity.getLocales(),
                    RESTCSTranslationDetailV1.LOCALES_NAME, dataType, expand, baseUrl, revision, expandParentReferences, entityManager));
        }

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTCSTranslationDetailV1> parent,
            final RESTCSTranslationDetailV1 dataObject) {
        // ENTITIES
        if (dataObject.hasParameterSet(RESTCSTranslationDetailV1.TRANSLATION_SERVER_NAME)) {
            collectChangeInformationFromEntity(parent, dataObject.getTranslationServer(), translationServerFactory,
                    RESTCSTranslationDetailV1.TRANSLATION_SERVER_NAME);
        }

        // COLLECTIONS
        if (dataObject.hasParameterSet(RESTCSTranslationDetailV1.LOCALES_NAME)
                && dataObject.getLocales() != null
                && dataObject.getLocales().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getLocales(), localeFactory);
        }
    }

    @Override
    public void syncBaseDetails(final CSTranslationDetail entity, final RESTCSTranslationDetailV1 dataObject) {
        if (dataObject.hasParameterSet(RESTCSTranslationDetailV1.ENABLED_NAME)) entity.setEnabled(dataObject.isEnabled());
        if (dataObject.hasParameterSet(RESTCSTranslationDetailV1.PROJECT_NAME)) entity.setProject(dataObject.getProject());
        if (dataObject.hasParameterSet(RESTCSTranslationDetailV1.PROJECT_VERSION_NAME)) entity.setProjectVersion(
                dataObject.getProjectVersion());
    }

    @Override
    protected void doDeleteChildAction(final CSTranslationDetail entity, final RESTCSTranslationDetailV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();

        if (restEntity instanceof RESTLocaleV1) {
            final Locale dbEntity = entityManager.find(Locale.class, restEntity.getId());
            if (dbEntity == null) throw new BadRequestException(
                    "No Locale entity was found with the primary key " + restEntity.getId());

            entity.removeLocale(dbEntity);
        } else if (restEntity instanceof RESTTranslationServerV1
                || RESTCSTranslationDetailV1.TRANSLATION_SERVER_NAME.equals(action.getUniqueId())) {
            entity.setTranslationServer(null);
        }
    }

    @Override
    protected PressGangEntity doCreateChildAction(final CSTranslationDetail entity, final RESTCSTranslationDetailV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();
        final PressGangEntity dbEntity;

        if (restEntity instanceof RESTLocaleV1) {
            dbEntity = entityManager.find(Locale.class, restEntity.getId());
            if (dbEntity == null) {
                throw new BadRequestException("No Locale entity was found with the primary key " + restEntity.getId());
            }

            entity.addLocale((Locale) dbEntity);
        } else if (restEntity instanceof RESTTranslationServerV1) {
            dbEntity = entityManager.find(TranslationServer.class, restEntity.getId());
            if (dbEntity == null) {
                throw new BadRequestException("No TranslationServer entity was found with the primary key " + restEntity.getId());
            }

            entity.setTranslationServer((TranslationServer) dbEntity);
        } else {
            throw new IllegalArgumentException("Item is not a child of CSTranslationDetail");
        }

        return dbEntity;
    }

    @Override
    protected PressGangEntity getChildEntityForAction(final CSTranslationDetail entity, final RESTCSTranslationDetailV1 dataObject,
            final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?> restEntity = action.getRESTEntity();

        final PressGangEntity dbEntity;
        if (restEntity instanceof RESTLocaleV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTLocaleV1) restEntity, Locale.class);
            if (dbEntity == null) {
                throw new BadRequestException("No Locale entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getLocales().contains(dbEntity)) {
                throw new BadRequestException(
                        "No Locale entity was found with the primary key " + restEntity.getId() + " for CSTranslationDetail "
                                + entity.getId());
            }
        } else if (restEntity instanceof RESTTranslationServerV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTTranslationServerV1) restEntity, TranslationServer.class);
            if (dbEntity == null) {
                throw new BadRequestException("No TranslationServer entity was found with the primary key " + restEntity.getId());
            }

            entity.setTranslationServer((TranslationServer) dbEntity);
        } else {
            throw new IllegalArgumentException("Item is not a child of Locale");
        }

        return dbEntity;
    }

    @Override
    protected Class<CSTranslationDetail> getDatabaseClass() {
        return CSTranslationDetail.class;
    }
}
