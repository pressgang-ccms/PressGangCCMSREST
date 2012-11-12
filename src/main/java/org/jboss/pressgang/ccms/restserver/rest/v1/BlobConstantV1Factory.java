package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.rest.v1.collections.RESTBlobConstantCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTBlobConstantCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTBlobConstantV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.entity.BlobConstants;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;


public class BlobConstantV1Factory
        extends
        RESTDataObjectFactory<RESTBlobConstantV1, BlobConstants, RESTBlobConstantCollectionV1, RESTBlobConstantCollectionItemV1> {

    public BlobConstantV1Factory() {
        super(BlobConstants.class);
    }

    @Override
    public RESTBlobConstantV1 createRESTEntityFromDBEntity(final BlobConstants entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTBlobConstantV1 retValue = new RESTBlobConstantV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null)
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getBlobConstantsId());
        retValue.setName(entity.getConstantName());
        retValue.setValue(entity.getConstantValue());

        // REVISIONS
        if (revision == null) {
            retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTBlobConstantV1, BlobConstants, RESTBlobConstantCollectionV1, RESTBlobConstantCollectionItemV1>()
                    .create(RESTBlobConstantCollectionV1.class, new BlobConstantV1Factory(), entity, entity.getRevisions(entityManager),
                            RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.BLOBCONSTANT_URL_NAME, dataType, retValue.getId());
        retValue.setLogDetails(new LogDetailsV1Factory().create(entity, revision, RESTBaseEntityV1.LOG_DETAILS_NAME, expand,
                dataType, baseUrl, entityManager));

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final BlobConstants entity,
            final RESTBlobConstantV1 dataObject) {
        if (dataObject.hasParameterSet(RESTBlobConstantV1.NAME_NAME))
            entity.setConstantName(dataObject.getName());

        if (dataObject.hasParameterSet(RESTBlobConstantV1.VALUE_NAME))
            entity.setConstantValue(dataObject.getValue());

        entityManager.persist(entity);
    }
}
