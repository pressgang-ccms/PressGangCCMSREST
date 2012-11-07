package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.rest.v1.collections.RESTTopicSourceUrlCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTopicSourceUrlCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicSourceUrlV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.entity.TopicSourceUrl;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;


public class TopicSourceUrlV1Factory
        extends
        RESTDataObjectFactory<RESTTopicSourceUrlV1, TopicSourceUrl, RESTTopicSourceUrlCollectionV1, RESTTopicSourceUrlCollectionItemV1> {
    public TopicSourceUrlV1Factory() {
        super(TopicSourceUrl.class);
    }

    @Override
    public RESTTopicSourceUrlV1 createRESTEntityFromDBEntity(final TopicSourceUrl entity, final String baseUrl,
            String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter entity can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTTopicSourceUrlV1 retValue = new RESTTopicSourceUrlV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
        if (revision == null)
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
        retValue.setExpand(expandOptions);

        retValue.setId(entity.getTopicSourceUrlid());
        retValue.setTitle(entity.getTitle());
        retValue.setDescription(entity.getDescription());
        retValue.setUrl(entity.getSourceUrl());

        // REVISIONS
        if (revision == null) {
            retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTTopicSourceUrlV1, TopicSourceUrl, RESTTopicSourceUrlCollectionV1, RESTTopicSourceUrlCollectionItemV1>()
                    .create(RESTTopicSourceUrlCollectionV1.class, new TopicSourceUrlV1Factory(), entity, entity.getRevisions(entityManager),
                            RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
        }

        retValue.setLogDetails(new LogDetailsV1Factory().create(entity, revision, RESTBaseEntityV1.LOG_DETAILS_NAME, expand,
                dataType, baseUrl, entityManager));

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final TopicSourceUrl entity,
            final RESTTopicSourceUrlV1 dataObject) {
        if (dataObject.hasParameterSet(RESTTopicSourceUrlV1.TITLE_NAME))
            entity.setTitle(dataObject.getTitle());
        if (dataObject.hasParameterSet(RESTTopicSourceUrlV1.DESCRIPTION_NAME))
            entity.setDescription(dataObject.getDescription());
        if (dataObject.hasParameterSet(RESTTopicSourceUrlV1.URL_NAME))
            entity.setSourceUrl(dataObject.getUrl());

        entityManager.persist(entity);
    }

}
