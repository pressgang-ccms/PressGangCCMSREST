package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.model.contentspec.CSTranslatedString;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTCSTranslatedStringCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTCSTranslatedStringCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSTranslatedStringV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.restserver.utils.EnversUtilities;

public class CSTranslatedStringV1Factory
        extends
        RESTDataObjectFactory<RESTCSTranslatedStringV1, CSTranslatedString, RESTCSTranslatedStringCollectionV1, RESTCSTranslatedStringCollectionItemV1> {
    
    public CSTranslatedStringV1Factory() {
        super(CSTranslatedString.class);
    }

    @Override
    public RESTCSTranslatedStringV1 createRESTEntityFromDBEntity(final CSTranslatedString entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTCSTranslatedStringV1 retValue = new RESTCSTranslatedStringV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null)
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setOriginalString(entity.getOriginalString());
        retValue.setTranslatedString(entity.getTranslatedString());
        retValue.setFuzzyTranslation(entity.getFuzzyTranslation());
        retValue.setLocale(entity.getLocale());

        if (revision == null) {
            retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTCSTranslatedStringV1, CSTranslatedString, RESTCSTranslatedStringCollectionV1, RESTCSTranslatedStringCollectionItemV1>()
                    .create(RESTCSTranslatedStringCollectionV1.class, new CSTranslatedStringV1Factory(), entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
        }

        retValue.setLogDetails(new LogDetailsV1Factory().create(entity, revision, RESTBaseEntityV1.LOG_DETAILS_NAME, expand,
                dataType, baseUrl, entityManager));

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final CSTranslatedString entity,
            final RESTCSTranslatedStringV1 dataObject) {
        if (dataObject.hasParameterSet(RESTCSTranslatedStringV1.ORIGINALSTRING_NAME))
            entity.setOriginalString(dataObject.getOriginalString());
        if (dataObject.hasParameterSet(RESTCSTranslatedStringV1.TRANSLATEDSTRING_NAME))
            entity.setTranslatedString(dataObject.getTranslatedString());
        if (dataObject.hasParameterSet(RESTCSTranslatedStringV1.FUZZY_TRANSLATION_NAME))
            entity.setFuzzyTranslation(dataObject.getFuzzyTranslation());

        entityManager.persist(entity);
    }
}
