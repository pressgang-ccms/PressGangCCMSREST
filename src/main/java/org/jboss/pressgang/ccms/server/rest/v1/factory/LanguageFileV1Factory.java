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

import org.jboss.pressgang.ccms.model.LanguageFile;
import org.jboss.pressgang.ccms.model.Locale;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTLanguageFileCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTLanguageFileCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTLanguageFileV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTLocaleV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class LanguageFileV1Factory extends RESTEntityFactory<RESTLanguageFileV1, LanguageFile, RESTLanguageFileCollectionV1,
        RESTLanguageFileCollectionItemV1> {
    @Inject
    protected LocaleV1Factory localeFactory;
    @Inject
    protected FileV1Factory fileFactory;

    @Override
    public RESTLanguageFileV1 createRESTEntityFromDBEntityInternal(final LanguageFile entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTLanguageFileV1 retValue = new RESTLanguageFileV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTLanguageFileV1.FILE_DATA_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);

        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        retValue.setId(entity.getLanguageFileId());
        retValue.setFilename(entity.getOriginalFileName());
        retValue.setContentHash(entity.getFileContentHash());

        // Potentially large fields need to be expanded manually
        if (expand != null && expand.contains(RESTLanguageFileV1.FILE_DATA_NAME)) retValue.setFileData(entity.getFileData());

        // LOCALE
        if (entity.getLocale() != null) {
            final ExpandDataTrunk childExpand = expand == null ? null: expand.get(RESTLanguageFileV1.LOCALE_NAME);
            retValue.setLocale(localeFactory.createRESTEntityFromDBEntity(entity.getLocale(), baseUrl, dataType, childExpand, revision));
        }

        // Set the object references
        if (expandParentReferences && expand != null && expand.contains(RESTLanguageFileV1.FILE_NAME) && entity.getFile() != null) {
            retValue.setFile(fileFactory.createRESTEntityFromDBEntity(entity.getFile(), baseUrl, dataType,
                    expand.get(RESTLanguageFileV1.FILE_NAME), revision));
        }

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTLanguageFileCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTLanguageFileV1> parent, final RESTLanguageFileV1 dataObject) {
        // LanguageFile has no children that can be changed, so we have no changes to collect
    }

    @Override
    public void syncBaseDetails(final LanguageFile entity, final RESTLanguageFileV1 dataObject) {
        if (dataObject.hasParameterSet(RESTLanguageFileV1.FILE_DATA_NAME)) entity.setFileData(dataObject.getFileData());
        if (dataObject.hasParameterSet(RESTLanguageFileV1.FILENAME_NAME)) entity.setOriginalFileName(dataObject.getFilename());
    }

    @Override
    public void syncAdditionalDetails(final LanguageFile entity, final RESTLanguageFileV1 dataObject) {
        // Set the Locale
        if (dataObject.hasParameterSet(RESTLanguageFileV1.LOCALE_NAME)) {
            final RESTLocaleV1 restEntity = dataObject.getLocale();

            if (restEntity != null) {
                final Locale dbEntity;
                if (restEntity.getId() != null) {
                    dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, restEntity, Locale.class);
                } else {
                    dbEntity = localeFactory.createDBEntity(restEntity);
                }

                if (dbEntity == null)
                    throw new BadRequestException("No Locale entity was found with the primary key " + restEntity.getId());

                localeFactory.syncBaseDetails(dbEntity, restEntity);

                entity.setLocale(dbEntity);
            } else {
                entity.setLocale(null);
            }
        }
    }

    @Override
    protected Class<LanguageFile> getDatabaseClass() {
        return LanguageFile.class;
    }
}
