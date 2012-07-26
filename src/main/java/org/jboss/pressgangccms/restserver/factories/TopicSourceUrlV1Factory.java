package org.jboss.pressgangccms.restserver.factories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgangccms.rest.v1.collections.RESTTopicSourceUrlCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTTopicSourceUrlV1;
import org.jboss.pressgangccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgangccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgangccms.restserver.BaseRESTv1;
import org.jboss.pressgangccms.restserver.entities.TopicSourceUrl;


class TopicSourceUrlV1Factory extends RESTDataObjectFactory<RESTTopicSourceUrlV1, TopicSourceUrl, RESTTopicSourceUrlCollectionV1>
{
	TopicSourceUrlV1Factory()
	{
		super(TopicSourceUrl.class);
	}
	
	@Override
	RESTTopicSourceUrlV1 createRESTEntityFromDBEntity(final TopicSourceUrl entity, final String baseUrl, String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager)
	{
		assert entity != null : "Parameter entity can not be null";
		assert baseUrl != null : "Parameter baseUrl can not be null";

		final RESTTopicSourceUrlV1 retValue = new RESTTopicSourceUrlV1();
		
		final List<String> expandOptions = new ArrayList<String>();
		if (revision == null)
			expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
		retValue.setExpand(expandOptions);

		retValue.setId(entity.getTopicSourceUrlid());
		retValue.setTitle(entity.getTitle());
		retValue.setDescription(entity.getDescription());
		retValue.setUrl(entity.getSourceUrl());
		
		if (revision == null)
		{
			retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTTopicSourceUrlV1, TopicSourceUrl, RESTTopicSourceUrlCollectionV1>().create(RESTTopicSourceUrlCollectionV1.class, new TopicSourceUrlV1Factory(), entity, entity.getRevisions(), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
		}
		
		retValue.setLinks(baseUrl, BaseRESTv1.TOPICSOURCEURL_URL_NAME, dataType, retValue.getId());

		return retValue;
	}

	@Override
	public
	void syncDBEntityWithRESTEntity(final EntityManager entityManager, final TopicSourceUrl entity, final RESTTopicSourceUrlV1 dataObject)
	{
		if (dataObject.hasParameterSet(RESTTopicSourceUrlV1.TITLE_NAME))
			entity.setTitle(dataObject.getTitle());
		if (dataObject.hasParameterSet(RESTTopicSourceUrlV1.DESCRIPTION_NAME))
			entity.setDescription(dataObject.getDescription());
		if (dataObject.hasParameterSet(RESTTopicSourceUrlV1.URL_NAME))
			entity.setSourceUrl(dataObject.getUrl());
		
		entityManager.persist(entity);
	}

}

