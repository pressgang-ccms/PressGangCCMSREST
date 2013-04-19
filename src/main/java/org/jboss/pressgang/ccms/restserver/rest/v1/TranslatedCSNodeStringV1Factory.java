package org.jboss.pressgang.ccms.restserver.rest.v1;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.contentspec.TranslatedCSNodeString;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTTranslatedCSNodeStringCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTTranslatedCSNodeStringCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTranslatedCSNodeStringV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.restserver.utils.EnversUtilities;

public class TranslatedCSNodeStringV1Factory extends RESTDataObjectFactory<RESTTranslatedCSNodeStringV1, TranslatedCSNodeString, RESTTranslatedCSNodeStringCollectionV1, RESTTranslatedCSNodeStringCollectionItemV1> {

    public TranslatedCSNodeStringV1Factory() {
        super(TranslatedCSNodeString.class);
    }

    @Override
    public RESTTranslatedCSNodeStringV1 createRESTEntityFromDBEntityInternal(final TranslatedCSNodeString entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTTranslatedCSNodeStringV1 retValue = new RESTTranslatedCSNodeStringV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getId());
        retValue.setTranslatedString(entity.getTranslatedString());
        retValue.setFuzzyTranslation(entity.getFuzzyTranslation());
        retValue.setLocale(entity.getLocale());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(
                    new RESTDataObjectCollectionFactory<RESTTranslatedCSNodeStringV1, TranslatedCSNodeString, RESTTranslatedCSNodeStringCollectionV1, RESTTranslatedCSNodeStringCollectionItemV1>().create(
                            RESTTranslatedCSNodeStringCollectionV1.class, new TranslatedCSNodeStringV1Factory(), entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final TranslatedCSNodeString entity,
            final RESTTranslatedCSNodeStringV1 dataObject) {

        if (dataObject.hasParameterSet(RESTTranslatedCSNodeStringV1.TRANSLATEDSTRING_NAME))
            entity.setTranslatedString(dataObject.getTranslatedString());
        if (dataObject.hasParameterSet(RESTTranslatedCSNodeStringV1.FUZZY_TRANSLATION_NAME))
            entity.setFuzzyTranslation(dataObject.getFuzzyTranslation());
        if (dataObject.hasParameterSet(RESTTranslatedCSNodeStringV1.LOCALE_NAME)) entity.setLocale(dataObject.getLocale());

        entityManager.persist(entity);
    }
}
