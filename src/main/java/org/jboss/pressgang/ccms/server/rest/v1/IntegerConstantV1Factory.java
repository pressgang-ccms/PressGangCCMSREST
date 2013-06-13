package org.jboss.pressgang.ccms.server.rest.v1;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.IntegerConstants;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTIntegerConstantCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTIntegerConstantCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.constants.RESTv1Constants;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTIntegerConstantV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTStringConstantV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;

public class IntegerConstantV1Factory extends RESTDataObjectFactory<RESTIntegerConstantV1, IntegerConstants,
        RESTIntegerConstantCollectionV1, RESTIntegerConstantCollectionItemV1> {
    public IntegerConstantV1Factory() {
        super(IntegerConstants.class);
    }

    @Override
    public RESTIntegerConstantV1 createRESTEntityFromDBEntityInternal(final IntegerConstants entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTIntegerConstantV1 retValue = new RESTIntegerConstantV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setName(entity.getConstantName());
        retValue.setValue(entity.getConstantValue());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(
                    RESTDataObjectCollectionFactory.create(RESTIntegerConstantCollectionV1.class, new IntegerConstantV1Factory(), entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        retValue.setLinks(baseUrl, RESTv1Constants.INTEGERCONSTANT_URL_NAME, dataType, retValue.getId());

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final IntegerConstants entity,
            final RESTIntegerConstantV1 dataObject) {
        if (dataObject.hasParameterSet(RESTStringConstantV1.NAME_NAME)) entity.setConstantName(dataObject.getName());
        if (dataObject.hasParameterSet(RESTStringConstantV1.VALUE_NAME)) entity.setConstantValue(dataObject.getValue());

        entityManager.persist(entity);
    }
}
