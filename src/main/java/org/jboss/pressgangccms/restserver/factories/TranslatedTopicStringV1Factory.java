package org.jboss.pressgangccms.restserver.factories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgangccms.rest.v1.collections.RESTTranslatedTopicStringCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTTranslatedTopicStringV1;
import org.jboss.pressgangccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgangccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgangccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgangccms.restserver.BaseRESTv1;
import org.jboss.pressgangccms.restserver.entities.TranslatedTopicString;
import org.jboss.pressgangccms.restserver.factories.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgangccms.restserver.factories.base.RESTDataObjectFactory;

public class TranslatedTopicStringV1Factory extends RESTDataObjectFactory<RESTTranslatedTopicStringV1, TranslatedTopicString, RESTTranslatedTopicStringCollectionV1>
{
	public TranslatedTopicStringV1Factory()
	{
		super(TranslatedTopicString.class);
	}

	@Override
	public RESTTranslatedTopicStringV1 createRESTEntityFromDBEntity(final TranslatedTopicString entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager) throws InvalidParameterException
	{
		assert entity != null : "Parameter topic can not be null";
		assert baseUrl != null : "Parameter baseUrl can not be null";
		assert expand != null : "Parameter expand can not be null";
		
		final RESTTranslatedTopicStringV1 retValue = new RESTTranslatedTopicStringV1();

		/* Set the expansion options */
		final List<String> expandOptions = new ArrayList<String>();

		if (revision == null)
			expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
		
		retValue.setExpand(expandOptions);
		
		/* Set the simple values */ 
		retValue.setId(entity.getTranslatedTopicStringID());
		retValue.setOriginalString(entity.getOriginalString());
		retValue.setTranslatedString(entity.getTranslatedString());

		/* Set the object references */
		if (expandParentReferences)
		{
			retValue.setTranslatedTopic(new TranslatedTopicV1Factory().createRESTEntityFromDBEntity(entity.getTranslatedTopicData(), baseUrl, dataType, expand.contains(RESTTranslatedTopicStringV1.TRANSLATEDTOPIC_NAME)));
		}
		
		if (revision == null) 
		{
			retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTTranslatedTopicStringV1, TranslatedTopicString, RESTTranslatedTopicStringCollectionV1>().create(RESTTranslatedTopicStringCollectionV1.class, new TranslatedTopicStringV1Factory(), entity, entity.getRevisions(), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
		}
		
		retValue.setLinks(baseUrl, BaseRESTv1.TRANSLATEDTOPICSTRING_URL_NAME, dataType, retValue.getId());
				
		return retValue;
	}

	@Override
	public
	void syncDBEntityWithRESTEntity(final EntityManager entityManager, final TranslatedTopicString entity, final RESTTranslatedTopicStringV1 dataObject)
	{
		if (dataObject.hasParameterSet(RESTTranslatedTopicStringV1.ORIGINALSTRING_NAME))
			entity.setOriginalString(dataObject.getOriginalString());
		if (dataObject.hasParameterSet(RESTTranslatedTopicStringV1.TRANSLATEDSTRING_NAME))
			entity.setTranslatedString(dataObject.getTranslatedString());
		
		entityManager.persist(entity);		
	}
}
