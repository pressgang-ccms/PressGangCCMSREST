package org.jboss.pressgang.ccms.restserver.rest.v1.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTAssignedPropertyTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTAssignedPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTAssignedPropertyTagV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.entity.base.ToPropertyTag;
import org.jboss.pressgang.ccms.restserver.rest.v1.LogDetailsV1Factory;


public abstract class BaseAssignedPropertyTagV1Factory<T extends ToPropertyTag<T>, U extends BaseAssignedPropertyTagV1Factory<T, U>> extends RESTDataObjectFactory<RESTAssignedPropertyTagV1, T, RESTAssignedPropertyTagCollectionV1, RESTAssignedPropertyTagCollectionItemV1>
{
    final Class<U> factoryClass;
    
    public BaseAssignedPropertyTagV1Factory(final Class<T> databaseClass, final Class<U> factoryClass) {
        super(databaseClass);
        this.factoryClass = factoryClass;
    }

    @Override
    public RESTAssignedPropertyTagV1 createRESTEntityFromDBEntity(final T entity, final String baseUrl, String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager)
	{
		assert entity != null : "Parameter topic can not be null";
		assert baseUrl != null : "Parameter baseUrl can not be null";

		final RESTAssignedPropertyTagV1 retValue = new RESTAssignedPropertyTagV1();
		
		final List<String> expandOptions = new ArrayList<String>();
		expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);
		if (revision == null)
			expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
		retValue.setExpand(expandOptions);

		retValue.setId(entity.getPropertyTag().getPropertyTagId());
		retValue.setRelationshipId(entity.getId());
		retValue.setDescription(entity.getPropertyTag().getPropertyTagDescription());
		retValue.setName(entity.getPropertyTag().getPropertyTagName());
		retValue.setRegex(entity.getPropertyTag().getPropertyTagRegex());
		retValue.setValid(entity.isValid(entityManager, revision));
		retValue.setValue(entity.getValue());
		retValue.setIsUnique(entity.getPropertyTag().getPropertyTagIsUnique());
		
		// REVISIONS
		if (revision == null)
		{
		    U factory = null;
            try {
                factory = factoryClass.newInstance();
            } catch (Exception ex) {
                
            }
			retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTAssignedPropertyTagV1, T, RESTAssignedPropertyTagCollectionV1, RESTAssignedPropertyTagCollectionItemV1>().create(RESTAssignedPropertyTagCollectionV1.class, factory, entity, entity.getRevisions(entityManager), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
		}
		
		retValue.setLinks(baseUrl, BaseRESTv1.PROPERTYTAG_URL_NAME, dataType, retValue.getId());
		retValue.setLogDetails(new LogDetailsV1Factory().create(entity, revision, RESTBaseEntityV1.LOG_DETAILS_NAME, expand, dataType, baseUrl, entityManager));
		
		return retValue;
	}

	@Override
	public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final T entity, final RESTAssignedPropertyTagV1 dataObject)
	{
		if (dataObject.hasParameterSet(RESTAssignedPropertyTagV1.VALUE_NAME))
			entity.setValue(dataObject.getValue());
		
		entityManager.persist(entity);
	}
}
