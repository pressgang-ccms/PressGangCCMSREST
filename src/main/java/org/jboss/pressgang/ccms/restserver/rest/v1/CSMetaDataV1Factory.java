package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.model.contentspec.CSMetaData;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTCSMetaDataCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTCSMetaDataCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSMetaDataV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.restserver.utils.EnversUtilities;

public class CSMetaDataV1Factory extends
        RESTDataObjectFactory<RESTCSMetaDataV1, CSMetaData, RESTCSMetaDataCollectionV1, RESTCSMetaDataCollectionItemV1> {

    public CSMetaDataV1Factory() {
        super(CSMetaData.class);
    }

    @Override
    public RESTCSMetaDataV1 createRESTEntityFromDBEntity(final CSMetaData entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTCSMetaDataV1 retValue = new RESTCSMetaDataV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null)
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setTitle(entity.getCSMetaDataTitle());
        retValue.setDescription(entity.getCSMetaDataDescription());

        // REVISIONS
        if (revision == null) {
            retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTCSMetaDataV1, CSMetaData, RESTCSMetaDataCollectionV1, RESTCSMetaDataCollectionItemV1>()
                    .create(RESTCSMetaDataCollectionV1.class, new CSMetaDataV1Factory(), entity, EnversUtilities.getRevisions(entityManager, entity),
                            RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.CONTENT_SPEC_META_DATA_URL_NAME, dataType, retValue.getId());
        retValue.setLogDetails(new LogDetailsV1Factory().create(entity, revision, RESTBaseEntityV1.LOG_DETAILS_NAME, expand,
                dataType, baseUrl, entityManager));

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final CSMetaData entity,
            final RESTCSMetaDataV1 dataObject) {
        if (dataObject.hasParameterSet(RESTCSMetaDataV1.TITLE_NAME))
            entity.setCSMetaDataTitle(dataObject.getTitle());
        
        if (dataObject.hasParameterSet(RESTCSMetaDataV1.DESCRIPTION_NAME))
            entity.setCSMetaDataDescription(dataObject.getDescription());

        entityManager.persist(entity);
    }
}
