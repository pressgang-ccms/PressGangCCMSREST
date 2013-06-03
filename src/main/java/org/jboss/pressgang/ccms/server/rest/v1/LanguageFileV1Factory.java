package org.jboss.pressgang.ccms.server.rest.v1;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.pressgang.ccms.model.LanguageFile;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTLanguageFileCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTLanguageFileCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTLanguageFileV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;

public class LanguageFileV1Factory extends RESTDataObjectFactory<RESTLanguageFileV1, LanguageFile, RESTLanguageFileCollectionV1,
        RESTLanguageFileCollectionItemV1> {
    public LanguageFileV1Factory() {
        super(LanguageFile.class);
    }

    @Override
    public RESTLanguageFileV1 createRESTEntityFromDBEntityInternal(final LanguageFile entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager) {
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

        // Potentially large fields need to be expanded manually
        if (expand != null && expand.contains(RESTLanguageFileV1.FILE_DATA_NAME))
            retValue.setFileData(entity.getFileData());

        // Set the object references
        if (expandParentReferences && expand != null && expand.contains(RESTLanguageFileV1.FILE_NAME) && entity.getFile() != null) {
            retValue.setFile(new FileV1Factory().createRESTEntityFromDBEntity(entity.getFile(), baseUrl, dataType,
                    expand.get(RESTLanguageFileV1.FILE_NAME), entityManager));
        }

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(
                    new RESTDataObjectCollectionFactory<RESTLanguageFileV1, LanguageFile, RESTLanguageFileCollectionV1,
                            RESTLanguageFileCollectionItemV1>().create(
                            RESTLanguageFileCollectionV1.class, new LanguageFileV1Factory(), entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntityFirstPass(final EntityManager entityManager,
            Map<RESTBaseEntityV1<?, ?, ?>, AuditedEntity> newEntityCache, final LanguageFile entity, final RESTLanguageFileV1 dataObject) {
        if (dataObject.hasParameterSet(RESTLanguageFileV1.LOCALE_NAME))
            entity.setLocale(dataObject.getLocale());
        if (dataObject.hasParameterSet(RESTLanguageFileV1.FILE_DATA_NAME))
            entity.setFileData(dataObject.getFileData());
    }
}
