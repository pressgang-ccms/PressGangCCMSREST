package org.jboss.pressgang.ccms.server.rest.v1;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.TranslatedTopicString;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTranslatedTopicStringCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTranslatedTopicStringCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTranslatedTopicStringV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.server.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;

@ApplicationScoped
public class TranslatedTopicStringV1Factory extends RESTDataObjectFactory<RESTTranslatedTopicStringV1, TranslatedTopicString,
        RESTTranslatedTopicStringCollectionV1, RESTTranslatedTopicStringCollectionItemV1> {
    @Inject
    protected TranslatedTopicV1Factory translatedTopicFactory;

    @Override
    public RESTTranslatedTopicStringV1 createRESTEntityFromDBEntityInternal(final TranslatedTopicString entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTTranslatedTopicStringV1 retValue = new RESTTranslatedTopicStringV1();

        /* Set the expansion options */
        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);

        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        /* Set the simple values */
        retValue.setId(entity.getTranslatedTopicStringID());
        retValue.setOriginalString(entity.getOriginalString());
        retValue.setTranslatedString(entity.getTranslatedString());
        retValue.setFuzzyTranslation(entity.getFuzzyTranslation());

        /* Set the object references */
        if (expandParentReferences && expand != null && expand.contains(
                RESTTranslatedTopicStringV1.TRANSLATEDTOPIC_NAME) && entity.getTranslatedTopicData() != null) {
            retValue.setTranslatedTopic(
                    translatedTopicFactory.createRESTEntityFromDBEntity(entity.getTranslatedTopicData(), baseUrl, dataType,
                            expand.get(RESTTranslatedTopicStringV1.TRANSLATEDTOPIC_NAME), revision, expandParentReferences));
        }

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTTopicV1.REVISIONS_NAME)) {
            retValue.setRevisions(RESTDataObjectCollectionFactory.create(RESTTranslatedTopicStringCollectionV1.class, this, entity,
                    EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                    entityManager));
        }

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntityFirstPass(final TranslatedTopicString entity, final RESTTranslatedTopicStringV1 dataObject) {
        if (dataObject.hasParameterSet(RESTTranslatedTopicStringV1.ORIGINALSTRING_NAME))
            entity.setOriginalString(dataObject.getOriginalString());
        if (dataObject.hasParameterSet(RESTTranslatedTopicStringV1.TRANSLATEDSTRING_NAME))
            entity.setTranslatedString(dataObject.getTranslatedString());
        if (dataObject.hasParameterSet(RESTTranslatedTopicStringV1.FUZZY_TRANSLATION_NAME))
            entity.setFuzzyTranslation(dataObject.getFuzzyTranslation());
    }

    @Override
    protected Class<TranslatedTopicString> getDatabaseClass() {
        return TranslatedTopicString.class;
    }
}
