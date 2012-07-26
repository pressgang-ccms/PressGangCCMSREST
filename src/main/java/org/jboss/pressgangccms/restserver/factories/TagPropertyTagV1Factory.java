package org.jboss.pressgangccms.restserver.factories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgangccms.rest.v1.collections.RESTPropertyTagCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTPropertyTagV1;
import org.jboss.pressgangccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgangccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgangccms.restserver.entities.TagToPropertyTag;
import org.jboss.pressgangccms.restserver.factories.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgangccms.restserver.factories.base.RESTDataObjectFactory;

class TagPropertyTagV1Factory extends RESTDataObjectFactory<RESTPropertyTagV1, TagToPropertyTag, RESTPropertyTagCollectionV1>
{
	TagPropertyTagV1Factory()
	{
		super(TagToPropertyTag.class);
	}
	
	@Override
	public RESTPropertyTagV1 createRESTEntityFromDBEntity(final TagToPropertyTag entity, final String baseUrl, String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager)
	{
		assert entity != null : "Parameter topic can not be null";
		assert baseUrl != null : "Parameter baseUrl can not be null";

		final RESTPropertyTagV1 retValue = new RESTPropertyTagV1();
		
		final List<String> expandOptions = new ArrayList<String>();
		if (revision == null)
			expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
		retValue.setExpand(expandOptions);

		retValue.setId(entity.getPropertyTag().getPropertyTagId());
		retValue.setDescription(entity.getPropertyTag().getPropertyTagDescription());
		retValue.setName(entity.getPropertyTag().getPropertyTagName());
		retValue.setRegex(entity.getPropertyTag().getPropertyTagRegex());
		retValue.setValid(entity.isValid(entityManager, revision));
		retValue.setValue(entity.getValue());
		
		if (revision == null)
		{
			retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTPropertyTagV1, TagToPropertyTag, RESTPropertyTagCollectionV1>().create(RESTPropertyTagCollectionV1.class, new TagPropertyTagV1Factory(), entity, entity.getRevisions(), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
		}
		
		return retValue;
	}

	@Override
	public
	void syncDBEntityWithRESTEntity(final EntityManager entityManager, final TagToPropertyTag entity, final RESTPropertyTagV1 dataObject)
	{
		if (dataObject.hasParameterSet(RESTPropertyTagV1.VALUE_NAME))
			entity.setValue(dataObject.getValue());
		
		entityManager.persist(entity);
	}
}
