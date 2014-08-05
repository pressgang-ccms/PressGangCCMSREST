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

import org.jboss.pressgang.ccms.model.File;
import org.jboss.pressgang.ccms.model.LanguageFile;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTFileCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTLanguageFileCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTFileCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFileV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTLanguageFileV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.RESTChangeAction;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.utils.RESTv1Utilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.resteasy.spi.BadRequestException;

@ApplicationScoped
public class FileV1Factory extends RESTEntityFactory<RESTFileV1, File, RESTFileCollectionV1, RESTFileCollectionItemV1> {
    @Inject
    protected LanguageFileV1Factory languageFileFactory;

    @Override
    public RESTFileV1 createRESTEntityFromDBEntityInternal(final File entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTFileV1 retValue = new RESTFileV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTFileV1.LANGUAGE_FILES_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) {
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        }
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getFileId());
        retValue.setDescription(entity.getDescription());
        retValue.setFileName(entity.getFileName());
        retValue.setFilePath(entity.getFilePath());
        retValue.setExplodeArchive(entity.getExplodeArchive());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTEntityCollectionFactory.create(RESTFileCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // LANGUAGE IMAGES
        if (expand != null && expand.contains(RESTFileV1.LANGUAGE_FILES_NAME)) {
            retValue.setLanguageFiles_OTM(RESTEntityCollectionFactory.create(RESTLanguageFileCollectionV1.class, languageFileFactory,
                    entity.getLanguageFilesArray(), RESTFileV1.LANGUAGE_FILES_NAME, dataType, expand, baseUrl, revision, false,
                    entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.FILE_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void collectChangeInformation(final RESTChangeAction<RESTFileV1> parent, final RESTFileV1 dataObject) {
        if (dataObject.hasParameterSet(RESTFileV1.LANGUAGE_FILES_NAME)
                && dataObject.getLanguageFiles_OTM() != null
                && dataObject.getLanguageFiles_OTM().getItems() != null) {
            collectChangeInformationFromCollection(parent, dataObject.getLanguageFiles_OTM(), languageFileFactory);
        }
    }

    @Override
    public void syncBaseDetails(final File entity, final RESTFileV1 dataObject) {
        if (dataObject.hasParameterSet(RESTFileV1.DESCRIPTION_NAME)) entity.setDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTFileV1.FILE_NAME)) entity.setFileName(dataObject.getFileName());
        if (dataObject.hasParameterSet(RESTFileV1.FILE_PATH_NAME)) entity.setFilePath(dataObject.getFilePath());
        if (dataObject.hasParameterSet(RESTFileV1.EXPLODE_ARCHIVE_NAME)) entity.setExplodeArchive(dataObject.getExplodeArchive());
    }

    @Override
    protected void doDeleteChildAction(final File entity, final RESTFileV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        if (restEntity instanceof RESTLanguageFileV1) {
            final LanguageFile dbEntity = entityManager.find(LanguageFile.class, restEntity.getId());
            if (dbEntity == null) throw new BadRequestException(
                    "No LanguageFile entity was found with the primary key " + restEntity.getId());

            entity.removeLanguageFile(dbEntity);
            entityManager.remove(dbEntity);
        }
    }

    @Override
    protected AuditedEntity doCreateChildAction(final File entity, final RESTFileV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();
        final AuditedEntity dbEntity;

        if (restEntity instanceof RESTLanguageFileV1) {
            dbEntity = action.getFactory().createDBEntity(restEntity);
            entity.addLanguageFile((LanguageFile) dbEntity);
        } else {
            throw new IllegalArgumentException("Item is not a child of File");
        }

        return dbEntity;
    }

    @Override
    protected AuditedEntity getChildEntityForAction(final File entity, final RESTFileV1 dataObject, final RESTChangeAction<?> action) {
        final RESTBaseEntityV1<?, ?, ?> restEntity = action.getRESTEntity();

        final AuditedEntity dbEntity;
        if (restEntity instanceof RESTLanguageFileV1) {
            dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, (RESTLanguageFileV1) restEntity, LanguageFile.class);
            if (dbEntity == null) {
                throw new BadRequestException("No LanguageFile entity was found with the primary key " + restEntity.getId());
            } else if (!entity.getLanguageFiles().contains(dbEntity)) {
                throw new BadRequestException(
                        "No LanguageFile entity was found with the primary key " + restEntity.getId() + " for File " + entity.getId());
            }
        } else {
            throw new IllegalArgumentException("Item is not a child of File");
        }

        return dbEntity;
    }

    @Override
    protected Class<File> getDatabaseClass() {
        return File.class;
    }
}
