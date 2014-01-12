package org.jboss.pressgang.ccms.server.rest.v1.factory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.File;
import org.jboss.pressgang.ccms.model.LanguageFile;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTFileCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTLanguageFileCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTFileCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTLanguageFileCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFileV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTLanguageFileV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
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
    public void syncDBEntityWithRESTEntityFirstPass(final File entity, final RESTFileV1 dataObject) {
        if (dataObject.hasParameterSet(RESTFileV1.DESCRIPTION_NAME)) entity.setDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTFileV1.FILE_NAME)) entity.setFileName(dataObject.getFileName());
        if (dataObject.hasParameterSet(RESTFileV1.FILE_PATH_NAME)) entity.setFilePath(dataObject.getFilePath());
        if (dataObject.hasParameterSet(RESTFileV1.EXPLODE_ARCHIVE_NAME)) entity.setExplodeArchive(dataObject.getExplodeArchive());

        /* One To Many - Add will create a child entity */
        if (dataObject.hasParameterSet(
                RESTFileV1.LANGUAGE_FILES_NAME) && dataObject.getLanguageFiles_OTM() != null && dataObject.getLanguageFiles_OTM()
                .getItems() != null) {
            dataObject.getLanguageFiles_OTM().removeInvalidChangeItemRequests();

            for (final RESTLanguageFileCollectionItemV1 restEntityItem : dataObject.getLanguageFiles_OTM().getItems()) {
                final RESTLanguageFileV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem()) {
                    final LanguageFile dbEntity = languageFileFactory.createDBEntityFromRESTEntity(restEntity);
                    entity.addLanguageFile(dbEntity);
                } else if (restEntityItem.returnIsRemoveItem()) {
                    final LanguageFile dbEntity = entityManager.find(LanguageFile.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new BadRequestException("No LanguageFile entity was found with the primary key " + restEntity.getId());

                    entity.removeLanguageFile(dbEntity);
                    entityManager.remove(dbEntity);
                } else if (restEntityItem.returnIsUpdateItem()) {
                    final LanguageFile dbEntity = entityManager.find(LanguageFile.class, restEntity.getId());
                    if (dbEntity == null)
                        throw new BadRequestException("No LanguageFile entity was found with the primary key " + restEntity.getId());
                    if (!entity.getLanguageFiles().contains(dbEntity)) throw new BadRequestException(
                            "No LanguageFile entity was found with the primary key " + restEntity.getId() + " for File " + entity.getId());

                    languageFileFactory.updateDBEntityFromRESTEntity(dbEntity, restEntity);
                }
            }
        }
    }

    @Override
    public void syncDBEntityWithRESTEntitySecondPass(final File entity, final RESTFileV1 dataObject) {
        // One To Many - Do the second pass on update or added items
        if (dataObject.hasParameterSet(
                RESTFileV1.LANGUAGE_FILES_NAME) && dataObject.getLanguageFiles_OTM() != null && dataObject.getLanguageFiles_OTM()
                .getItems() != null) {
            dataObject.getLanguageFiles_OTM().removeInvalidChangeItemRequests();

            for (final RESTLanguageFileCollectionItemV1 restEntityItem : dataObject.getLanguageFiles_OTM().getItems()) {
                final RESTLanguageFileV1 restEntity = restEntityItem.getItem();

                if (restEntityItem.returnIsAddItem() || restEntityItem.returnIsUpdateItem()) {
                    final LanguageFile dbEntity = RESTv1Utilities.findEntity(entityManager, entityCache, restEntity, LanguageFile.class);
                    if (dbEntity == null)
                        throw new BadRequestException("No LanguageFile entity was found with the primary key " + restEntity.getId());

                    languageFileFactory.syncDBEntityWithRESTEntitySecondPass(dbEntity, restEntity);
                }
            }
        }
    }

    @Override
    protected Class<File> getDatabaseClass() {
        return File.class;
    }
}
