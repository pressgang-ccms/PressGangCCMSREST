package org.jboss.pressgangccms.restserver.factories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgangccms.rest.v1.collections.RESTCategoryCollectionV1;
import org.jboss.pressgangccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTCategoryV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgangccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgangccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgangccms.restserver.BaseRESTv1;
import org.jboss.pressgangccms.restserver.entities.Category;
import org.jboss.pressgangccms.restserver.entities.Tag;
import org.jboss.resteasy.spi.BadRequestException;

public class CategoryV1Factory extends RESTDataObjectFactory<RESTCategoryV1, Category, RESTCategoryCollectionV1>
{
	public CategoryV1Factory()
	{
		super(Category.class);
	}

	@Override
	RESTCategoryV1 createRESTEntityFromDBEntity(final Category entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager)
	{
		assert entity != null : "Parameter topic can not be null";
		assert baseUrl != null : "Parameter baseUrl can not be null";
		
		final RESTCategoryV1 retValue = new RESTCategoryV1();

		retValue.setId(entity.getCategoryId());
		retValue.setName(entity.getCategoryName());
		retValue.setDescription(entity.getCategoryDescription());
		retValue.setMutuallyExclusive(entity.isMutuallyExclusive());
		retValue.setSort(entity.getCategorySort());
		
		final List<String> expandOptions = new ArrayList<String>();
		expandOptions.add(BaseRESTv1.TAGS_EXPANSION_NAME);
		if (revision == null)
			expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
		
		retValue.setExpand(expandOptions);	

		if (revision == null)
		{
			retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTCategoryV1, Category, RESTCategoryCollectionV1>().create(RESTCategoryCollectionV1.class, new CategoryV1Factory(), entity, entity.getRevisions(), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
		}
		retValue.setTags(new RESTDataObjectCollectionFactory<RESTTagV1, Tag, RESTTagCollectionV1>().create(RESTTagCollectionV1.class, new TagV1Factory(), entity.getTags(), BaseRESTv1.TAGS_EXPANSION_NAME, dataType, expand, baseUrl, entityManager));

		retValue.setLinks(baseUrl, BaseRESTv1.CATEGORY_URL_NAME, dataType, retValue.getId());

		return retValue;
	}

	@Override
	public
	void syncDBEntityWithRESTEntity(final EntityManager entityManager, final Category entity, final RESTCategoryV1 dataObject)
	{
		if (dataObject.hasParameterSet(RESTCategoryV1.DESCRIPTION_NAME))
			entity.setCategoryDescription(dataObject.getDescription());
		if (dataObject.hasParameterSet(RESTCategoryV1.MUTUALLYEXCLUSIVE_NAME))
			entity.setMutuallyExclusive(dataObject.getMutuallyExclusive());
		if (dataObject.hasParameterSet(RESTCategoryV1.NAME_NAME))
			entity.setCategoryName(dataObject.getName());
		if (dataObject.hasParameterSet(RESTCategoryV1.SORT_NAME))
			entity.setCategorySort(dataObject.getSort());
		
		entityManager.persist(entity);

		/* Many To Many - Add will create a mapping */
		if (dataObject.hasParameterSet(RESTCategoryV1.TAGS_NAME) && dataObject.getTags() != null  && dataObject.getTags().getItems() != null)
		{
			for (final RESTTagV1 restEntity : dataObject.getTags().getItems())
			{
				if (restEntity.getAddItem() || restEntity.getRemoveItem())
				{
					final Tag dbEntity = entityManager.find(Tag.class, restEntity.getId());
					if (dbEntity == null)
						throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());

					if (restEntity.getAddItem())
					{
						entity.addTagRelationship(dbEntity);
					}
					else if (restEntity.getRemoveItem())
					{
						entity.removeTagRelationship(dbEntity);
					}
				}
			}
		}
	}
}
