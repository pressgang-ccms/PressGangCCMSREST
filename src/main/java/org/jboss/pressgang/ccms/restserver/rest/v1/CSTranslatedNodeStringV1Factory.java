package org.jboss.pressgang.ccms.restserver.rest.v1;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.contentspec.CSTranslatedNodeString;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTCSTranslatedNodeStringCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTCSTranslatedNodeStringCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTCSTranslatedNodeStringV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.restserver.utils.EnversUtilities;

public class CSTranslatedNodeStringV1Factory extends RESTDataObjectFactory<RESTCSTranslatedNodeStringV1, CSTranslatedNodeString,
        RESTCSTranslatedNodeStringCollectionV1, RESTCSTranslatedNodeStringCollectionItemV1> {

    public CSTranslatedNodeStringV1Factory() {
        super(CSTranslatedNodeString.class);
    }

    @Override
    public RESTCSTranslatedNodeStringV1 createRESTEntityFromDBEntityInternal(final CSTranslatedNodeString entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTCSTranslatedNodeStringV1 retValue = new RESTCSTranslatedNodeStringV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setOriginalString(entity.getOriginalString());
        retValue.setTranslatedString(entity.getTranslatedString());
        retValue.setFuzzyTranslation(entity.getFuzzyTranslation());
        retValue.setLocale(entity.getLocale());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(
                    new RESTDataObjectCollectionFactory<RESTCSTranslatedNodeStringV1, CSTranslatedNodeString,
                            RESTCSTranslatedNodeStringCollectionV1, RESTCSTranslatedNodeStringCollectionItemV1>().create(
                            RESTCSTranslatedNodeStringCollectionV1.class, new CSTranslatedNodeStringV1Factory(), entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final CSTranslatedNodeString entity,
            final RESTCSTranslatedNodeStringV1 dataObject) {
        if (dataObject.hasParameterSet(RESTCSTranslatedNodeStringV1.ORIGINALSTRING_NAME))
            entity.setOriginalString(dataObject.getOriginalString());
        if (dataObject.hasParameterSet(RESTCSTranslatedNodeStringV1.TRANSLATEDSTRING_NAME))
            entity.setTranslatedString(dataObject.getTranslatedString());
        if (dataObject.hasParameterSet(RESTCSTranslatedNodeStringV1.FUZZY_TRANSLATION_NAME))
            entity.setFuzzyTranslation(dataObject.getFuzzyTranslation());
        if (dataObject.hasParameterSet(RESTCSTranslatedNodeStringV1.LOCALE_NAME)) entity.setLocale(dataObject.getLocale());

        entityManager.persist(entity);
    }
}
