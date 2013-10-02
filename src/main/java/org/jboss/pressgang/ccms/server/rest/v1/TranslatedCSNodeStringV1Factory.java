package org.jboss.pressgang.ccms.server.rest.v1;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.contentspec.TranslatedCSNodeString;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTTranslatedCSNodeStringCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTTranslatedCSNodeStringCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTTranslatedCSNodeStringV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;

@ApplicationScoped
public class TranslatedCSNodeStringV1Factory extends RESTDataObjectFactory<RESTTranslatedCSNodeStringV1, TranslatedCSNodeString,
        RESTTranslatedCSNodeStringCollectionV1, RESTTranslatedCSNodeStringCollectionItemV1> {
    @Inject
    protected TranslatedCSNodeV1Factory translatedCSNodeFactory;

    @Override
    public RESTTranslatedCSNodeStringV1 createRESTEntityFromDBEntityInternal(final TranslatedCSNodeString entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
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
            retValue.setRevisions(RESTDataObjectCollectionFactory.create(RESTTranslatedCSNodeStringCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        // Set the object references
        if (expandParentReferences && expand != null && expand.contains(
                RESTTranslatedCSNodeStringV1.TRANSLATEDNODE_NAME) && entity.getTranslatedCSNode() != null) {
            retValue.setTranslatedNode(translatedCSNodeFactory.createRESTEntityFromDBEntity(entity.getTranslatedCSNode(), baseUrl, dataType,
                    expand.get(RESTTranslatedCSNodeStringV1.TRANSLATEDNODE_NAME), revision, expandParentReferences));
        }

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntityFirstPass(final TranslatedCSNodeString entity, final RESTTranslatedCSNodeStringV1 dataObject) {

        if (dataObject.hasParameterSet(RESTTranslatedCSNodeStringV1.TRANSLATEDSTRING_NAME))
            entity.setTranslatedString(dataObject.getTranslatedString());
        if (dataObject.hasParameterSet(RESTTranslatedCSNodeStringV1.FUZZY_TRANSLATION_NAME))
            entity.setFuzzyTranslation(dataObject.getFuzzyTranslation());
        if (dataObject.hasParameterSet(RESTTranslatedCSNodeStringV1.LOCALE_NAME)) entity.setLocale(dataObject.getLocale());
    }

    @Override
    protected Class<TranslatedCSNodeString> getDatabaseClass() {
        return TranslatedCSNodeString.class;
    }
}
