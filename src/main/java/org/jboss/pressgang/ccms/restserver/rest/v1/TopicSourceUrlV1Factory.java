package org.jboss.pressgang.ccms.restserver.rest.v1;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import org.jboss.pressgang.ccms.model.TopicSourceUrl;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTopicSourceUrlCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTopicSourceUrlCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicSourceUrlV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.restserver.utils.EnversUtilities;

public class TopicSourceUrlV1Factory extends RESTDataObjectFactory<RESTTopicSourceUrlV1, TopicSourceUrl, RESTTopicSourceUrlCollectionV1,
        RESTTopicSourceUrlCollectionItemV1> {

    public TopicSourceUrlV1Factory() {
        super(TopicSourceUrl.class);
    }

    @Override
    public RESTTopicSourceUrlV1 createRESTEntityFromDBEntityInternal(final TopicSourceUrl entity, final String baseUrl, String dataType,
            final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTTopicSourceUrlV1 retValue = new RESTTopicSourceUrlV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null) expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getTopicSourceUrlId());
        retValue.setTitle(entity.getTitle());
        retValue.setDescription(entity.getDescription());
        retValue.setUrl(entity.getSourceUrl());

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTTopicV1.REVISIONS_NAME)) {
            retValue.setRevisions(
                    new RESTDataObjectCollectionFactory<RESTTopicSourceUrlV1, TopicSourceUrl, RESTTopicSourceUrlCollectionV1,
                            RESTTopicSourceUrlCollectionItemV1>().create(
                            RESTTopicSourceUrlCollectionV1.class, new TopicSourceUrlV1Factory(), entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl,
                            entityManager));
        }

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final TopicSourceUrl entity,
            final RESTTopicSourceUrlV1 dataObject) {
        if (dataObject.hasParameterSet(RESTTopicSourceUrlV1.TITLE_NAME)) entity.setTitle(dataObject.getTitle());
        if (dataObject.hasParameterSet(RESTTopicSourceUrlV1.DESCRIPTION_NAME)) entity.setDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTTopicSourceUrlV1.URL_NAME)) entity.setSourceUrl(dataObject.getUrl());

        entityManager.persist(entity);
    }

}
