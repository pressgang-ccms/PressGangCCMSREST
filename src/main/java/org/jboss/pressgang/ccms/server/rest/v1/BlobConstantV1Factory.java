package org.jboss.pressgang.ccms.server.rest.v1;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.pressgang.ccms.model.BlobConstants;
import org.jboss.pressgang.ccms.model.base.AuditedEntity;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTBlobConstantCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTBlobConstantCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTBlobConstantV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;


public class BlobConstantV1Factory extends RESTDataObjectFactory<RESTBlobConstantV1, BlobConstants, RESTBlobConstantCollectionV1,
        RESTBlobConstantCollectionItemV1> {

    public BlobConstantV1Factory() {
        super(BlobConstants.class);
    }

    @Override
    public RESTBlobConstantV1 createRESTEntityFromDBEntityInternal(final BlobConstants entity, final String baseUrl, final String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTBlobConstantV1 retValue = new RESTBlobConstantV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBlobConstantV1.VALUE_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getBlobConstantsId());
        retValue.setName(entity.getConstantName());
        if (expand != null && expand.contains(RESTBlobConstantV1.VALUE_NAME)) {
            retValue.setValue(entity.getConstantValue());
        }

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(
                    new RESTDataObjectCollectionFactory<RESTBlobConstantV1, BlobConstants, RESTBlobConstantCollectionV1,
                            RESTBlobConstantCollectionItemV1>().create(
                            RESTBlobConstantCollectionV1.class, new BlobConstantV1Factory(), entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.BLOBCONSTANT_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntityFirstPass(final EntityManager entityManager,
            Map<RESTBaseEntityV1<?, ?, ?>, AuditedEntity> newEntityCache, final BlobConstants entity, final RESTBlobConstantV1 dataObject) {
        if (dataObject.hasParameterSet(RESTBlobConstantV1.NAME_NAME)) entity.setConstantName(dataObject.getName());

        if (dataObject.hasParameterSet(RESTBlobConstantV1.VALUE_NAME)) entity.setConstantValue(dataObject.getValue());

        entityManager.persist(entity);
    }
}
