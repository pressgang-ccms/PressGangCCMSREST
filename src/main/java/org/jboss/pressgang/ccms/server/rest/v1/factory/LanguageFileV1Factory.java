package org.jboss.pressgang.ccms.server.rest.v1.factory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.LanguageFile;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTLanguageFileCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTLanguageFileCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTLanguageFileV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityFactory;
import org.jboss.pressgang.ccms.server.rest.v1.factory.base.RESTEntityCollectionFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;

@ApplicationScoped
public class LanguageFileV1Factory extends RESTEntityFactory<RESTLanguageFileV1, LanguageFile, RESTLanguageFileCollectionV1,
        RESTLanguageFileCollectionItemV1> {
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
        retValue.setLocale(entity.getLocale());
        retValue.setFilename(entity.getOriginalFileName());
        retValue.setContentHash(entity.getFileContentHash());

        // Potentially large fields need to be expanded manually
        if (expand != null && expand.contains(RESTLanguageFileV1.FILE_DATA_NAME)) retValue.setFileData(entity.getFileData());

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
    public void syncDBEntityWithRESTEntityFirstPass(final LanguageFile entity, final RESTLanguageFileV1 dataObject) {
        if (dataObject.hasParameterSet(RESTLanguageFileV1.LOCALE_NAME)) entity.setLocale(dataObject.getLocale());
        if (dataObject.hasParameterSet(RESTLanguageFileV1.FILE_DATA_NAME)) entity.setFileData(dataObject.getFileData());
        if (dataObject.hasParameterSet(RESTLanguageFileV1.FILENAME_NAME)) entity.setOriginalFileName(dataObject.getFilename());
    }

    @Override
    protected Class<LanguageFile> getDatabaseClass() {
        return LanguageFile.class;
    }
}
