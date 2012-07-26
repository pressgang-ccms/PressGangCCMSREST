package org.jboss.pressgangccms.restserver;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgangccms.rest.v1.collections.RESTBlobConstantCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTBlobConstantV1;
import org.jboss.pressgangccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgangccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgangccms.restserver.entities.BlobConstants;

class BlobConstantV1Factory extends RESTDataObjectFactory<RESTBlobConstantV1, BlobConstants, RESTBlobConstantCollectionV1>
{
	BlobConstantV1Factory()
	{
		super(BlobConstants.class);
	}
	
	@Override
	RESTBlobConstantV1 createRESTEntityFromDBEntity(final BlobConstants entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager)
	{
		assert entity != null : "Parameter topic can not be null";
		assert baseUrl != null : "Parameter baseUrl can not be null";
		
		final RESTBlobConstantV1 retValue = new RESTBlobConstantV1();
		
		final List<String> expandOptions = new ArrayList<String>();
		if (revision == null)
			expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
		retValue.setExpand(expandOptions);
		
		retValue.setId(entity.getBlobConstantsId());
		retValue.setName(entity.getConstantName());
		retValue.setValue(entity.getConstantValue());
		
		if (revision == null)
		{
			retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTBlobConstantV1, BlobConstants, RESTBlobConstantCollectionV1>().create(RESTBlobConstantCollectionV1.class, new BlobConstantV1Factory(), entity, entity.getRevisions(), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
		}
		
		retValue.setLinks(baseUrl, BaseRESTv1.BLOBCONSTANT_URL_NAME, dataType, retValue.getId());
		
		return retValue;
	}

	@Override
	void syncDBEntityWithRESTEntity(final EntityManager entityManager, final BlobConstants entity, final RESTBlobConstantV1 dataObject)
	{
		if (dataObject.hasParameterSet(RESTBlobConstantV1.NAME_NAME))
			entity.setConstantName(dataObject.getName());
		
		if (dataObject.hasParameterSet(RESTBlobConstantV1.VALUE_NAME))
			entity.setConstantValue(dataObject.getValue());
		
		entityManager.persist(entity);
	}
}
