package org.jboss.pressgangccms.restserver.factories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgangccms.rest.v1.collections.RESTBugzillaBugCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTBugzillaBugV1;
import org.jboss.pressgangccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgangccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgangccms.restserver.BaseRESTv1;
import org.jboss.pressgangccms.restserver.entities.BugzillaBug;
import org.jboss.pressgangccms.restserver.factories.base.RESTDataObjectFactory;


class BugzillaBugV1Factory extends RESTDataObjectFactory<RESTBugzillaBugV1, BugzillaBug, RESTBugzillaBugCollectionV1>
{
	BugzillaBugV1Factory()
	{
		super(BugzillaBug.class);
	}
	
	@Override
	public RESTBugzillaBugV1 createRESTEntityFromDBEntity(final BugzillaBug entity, final String baseUrl, String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager)
	{
		assert entity != null : "Parameter entity can not be null";
		assert baseUrl != null : "Parameter baseUrl can not be null";

		final RESTBugzillaBugV1 retValue = new RESTBugzillaBugV1();
		
		final List<String> expandOptions = new ArrayList<String>();
		if (revision == null)
			expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
		retValue.setExpand(expandOptions);
		retValue.setId(entity.getBugzillaBugId());
		retValue.setIsOpen(entity.getBugzillaBugOpen());
		retValue.setBugId(entity.getBugzillaBugBugzillaId());
		retValue.setSummary(entity.getBugzillaBugSummary());
		
		retValue.setLinks(baseUrl, BaseRESTv1.BUGZILLABUG_URL_NAME, dataType, retValue.getId());
		
		return retValue;
	}

	@Override
	public
	void syncDBEntityWithRESTEntity(final EntityManager entityManager, final BugzillaBug entity, final RESTBugzillaBugV1 dataObject)
	{
		if (dataObject.hasParameterSet(RESTBugzillaBugV1.BUG_ID))
			entity.setBugzillaBugBugzillaId(dataObject.getBugId());
		if (dataObject.hasParameterSet(RESTBugzillaBugV1.BUG_ISOPEN))
			entity.setBugzillaBugOpen(dataObject.getIsOpen());
		if (dataObject.hasParameterSet(RESTBugzillaBugV1.BUG_SUMMARY))
			entity.setBugzillaBugSummary(dataObject.getSummary());
	}

}

