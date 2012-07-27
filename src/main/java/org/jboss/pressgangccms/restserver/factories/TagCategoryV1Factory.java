package org.jboss.pressgangccms.restserver.factories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgangccms.rest.v1.collections.RESTCategoryCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTCategoryV1;
import org.jboss.pressgangccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgangccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgangccms.restserver.entities.TagToCategory;
import org.jboss.pressgangccms.restserver.factories.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgangccms.restserver.factories.base.RESTDataObjectFactory;

/**
 * This factory is used when creating a collection of Categories for a Tag. It
 * will populate the sort property, which is left null by the CategoryV1Factory.
 */
public class TagCategoryV1Factory extends RESTDataObjectFactory<RESTCategoryV1, TagToCategory, RESTCategoryCollectionV1>
{
	TagCategoryV1Factory()
	{
		super(TagToCategory.class);
	}

	@Override
	public RESTCategoryV1 createRESTEntityFromDBEntity(final TagToCategory entity, final String baseUrl, String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager)
	{
		assert entity != null : "Parameter topic can not be null";
		assert baseUrl != null : "Parameter baseUrl can not be null";

		final RESTCategoryV1 retValue = new RESTCategoryV1();

		final List<String> expandOptions = new ArrayList<String>();
		if (revision == null)
			expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
		retValue.setExpand(expandOptions);

		retValue.setId(entity.getCategory().getCategoryId());
		retValue.setDescription(entity.getCategory().getCategoryDescription());
		retValue.setName(entity.getCategory().getCategoryName());
		retValue.setMutuallyExclusive(entity.getCategory().isMutuallyExclusive());
		retValue.setSort(entity.getSorting());

		if (revision == null)
		{
			retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTCategoryV1, TagToCategory, RESTCategoryCollectionV1>().create(RESTCategoryCollectionV1.class, new TagCategoryV1Factory(), entity, entity.getRevisions(entityManager), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
		}

		return retValue;
	}

	@Override
	public
	void syncDBEntityWithRESTEntity(final EntityManager entityManager, final TagToCategory entity, final RESTCategoryV1 dataObject)
	{
		if (dataObject.hasParameterSet(RESTCategoryV1.SORT_NAME))
			entity.setSorting(dataObject.getSort());
		
		entityManager.persist(entity);
	}
}
