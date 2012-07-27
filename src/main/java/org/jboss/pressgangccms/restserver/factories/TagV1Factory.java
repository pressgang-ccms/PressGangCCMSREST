package org.jboss.pressgangccms.restserver.factories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgangccms.rest.v1.collections.RESTCategoryCollectionV1;
import org.jboss.pressgangccms.rest.v1.collections.RESTProjectCollectionV1;
import org.jboss.pressgangccms.rest.v1.collections.RESTPropertyTagCollectionV1;
import org.jboss.pressgangccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTCategoryV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTProjectV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTPropertyTagV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgangccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgangccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgangccms.restserver.BaseRESTv1;
import org.jboss.pressgangccms.restserver.entities.Category;
import org.jboss.pressgangccms.restserver.entities.Project;
import org.jboss.pressgangccms.restserver.entities.PropertyTag;
import org.jboss.pressgangccms.restserver.entities.Tag;
import org.jboss.pressgangccms.restserver.entities.TagToCategory;
import org.jboss.pressgangccms.restserver.entities.TagToPropertyTag;
import org.jboss.pressgangccms.restserver.factories.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgangccms.restserver.factories.base.RESTDataObjectFactory;
import org.jboss.pressgangccms.utils.common.CollectionUtilities;
import org.jboss.resteasy.spi.BadRequestException;

public class TagV1Factory extends RESTDataObjectFactory<RESTTagV1, Tag, RESTTagCollectionV1>
{
	public TagV1Factory()
	{
		super(Tag.class);
	}

	@Override
	public RESTTagV1 createRESTEntityFromDBEntity(final Tag entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager)
	{
		assert entity != null : "Parameter topic can not be null";
		assert baseUrl != null : "Parameter baseUrl can not be null";

		final RESTTagV1 retValue = new RESTTagV1();

		final List<String> expandOptions = new ArrayList<String>();
		expandOptions.add(RESTTagV1.CATEGORIES_NAME);
		expandOptions.add(RESTTagV1.PARENT_TAGS_NAME);
		expandOptions.add(RESTTagV1.CHILD_TAGS_NAME);
		expandOptions.add(RESTTagV1.PROJECTS_NAME);
		expandOptions.add(BaseRESTv1.PROPERTIES_EXPANSION_NAME);

		if (revision == null)
			expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

		retValue.setExpand(expandOptions);

		/* Set simple properties */
		retValue.setId(entity.getTagId());
		retValue.setName(entity.getTagName());
		retValue.setDescription(entity.getTagDescription());

		/* Set collections */
		if (revision == null)
		{
			retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTTagV1, Tag, RESTTagCollectionV1>().create(RESTTagCollectionV1.class, new TagV1Factory(), entity, entity.getRevisions(entityManager), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
		}
		retValue.setCategories(new RESTDataObjectCollectionFactory<RESTCategoryV1, TagToCategory, RESTCategoryCollectionV1>().create(RESTCategoryCollectionV1.class, new TagCategoryV1Factory(), CollectionUtilities.toArrayList(entity.getTagToCategories()), RESTTagV1.CATEGORIES_NAME, dataType, expand, baseUrl, entityManager));
		retValue.setParentTags(new RESTDataObjectCollectionFactory<RESTTagV1, Tag, RESTTagCollectionV1>().create(RESTTagCollectionV1.class, new TagV1Factory(), entity.getParentTags(), RESTTagV1.PARENT_TAGS_NAME, dataType, expand, baseUrl, entityManager));
		retValue.setChildTags(new RESTDataObjectCollectionFactory<RESTTagV1, Tag, RESTTagCollectionV1>().create(RESTTagCollectionV1.class, new TagV1Factory(), entity.getChildTags(), RESTTagV1.CHILD_TAGS_NAME, dataType, expand, baseUrl, entityManager));
		retValue.setProperties(new RESTDataObjectCollectionFactory<RESTPropertyTagV1, TagToPropertyTag, RESTPropertyTagCollectionV1>().create(RESTPropertyTagCollectionV1.class, new TagPropertyTagV1Factory(), entity.getTagToPropertyTagsArray(), BaseRESTv1.PROPERTIES_EXPANSION_NAME, dataType, expand, baseUrl, entityManager));
		retValue.setProjects(new RESTDataObjectCollectionFactory<RESTProjectV1, Project, RESTProjectCollectionV1>().create(RESTProjectCollectionV1.class, new ProjectV1Factory(), entity.getProjects(), RESTTagV1.PROJECTS_NAME, dataType, expand, baseUrl, entityManager));

		retValue.setLinks(baseUrl, BaseRESTv1.TAG_URL_NAME, dataType, retValue.getId());

		return retValue;
	}

	@Override
	public
	void syncDBEntityWithRESTEntity(final EntityManager entityManager, final Tag entity, final RESTTagV1 dataObject)
	{
		if (dataObject.hasParameterSet(RESTTagV1.DESCRIPTION_NAME))
			entity.setTagDescription(dataObject.getDescription());
		if (dataObject.hasParameterSet(RESTTagV1.NAME_NAME))
			entity.setTagName(dataObject.getName());

		entityManager.persist(entity);

		if (dataObject.hasParameterSet(RESTTagV1.CATEGORIES_NAME) && dataObject.getCategories() != null && dataObject.getCategories().getItems() != null)
		{
			for (final RESTCategoryV1 restEntity : dataObject.getCategories().getItems())
			{
				if (restEntity.getAddItem() || restEntity.getRemoveItem())
				{
					final Category dbEntity = entityManager.find(Category.class, restEntity.getId());
					if (dbEntity == null)
						throw new BadRequestException("No Category entity was found with the primary key " + restEntity.getId());

					if (restEntity.getAddItem())
					{
						if (restEntity.hasParameterSet(RESTCategoryV1.SORT_NAME))
							dbEntity.addTagRelationship(entity, restEntity.getSort());
						else
							dbEntity.addTagRelationship(entity);
					}
					else if (restEntity.getRemoveItem())
					{
						dbEntity.removeTagRelationship(entity);
					}
				}
			}

		}

		if (dataObject.hasParameterSet(RESTTagV1.CHILD_TAGS_NAME) && dataObject.getChildTags() != null && dataObject.getChildTags().getItems() != null)
		{
			for (final RESTTagV1 restEntity : dataObject.getChildTags().getItems())
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

		if (dataObject.hasParameterSet(RESTTagV1.PARENT_TAGS_NAME) && dataObject.getParentTags() != null && dataObject.getParentTags().getItems() != null)
		{
			for (final RESTTagV1 restEntity : dataObject.getParentTags().getItems())
			{
				if (restEntity.getAddItem() || restEntity.getRemoveItem())
				{
					final Tag dbEntity = entityManager.find(Tag.class, restEntity.getId());
					if (dbEntity == null)
						throw new BadRequestException("No Tag entity was found with the primary key " + restEntity.getId());

					if (restEntity.getAddItem())
					{
						dbEntity.addTagRelationship(entity);
					}
					else if (restEntity.getRemoveItem())
					{
						dbEntity.removeTagRelationship(entity);
					}
				}
			}
		}

		if (dataObject.hasParameterSet(RESTTagV1.PROPERTIES_NAME) && dataObject.getProperties() != null && dataObject.getProperties().getItems() != null)
		{
			for (final RESTPropertyTagV1 restEntity : dataObject.getProperties().getItems())
			{
				if (restEntity.getAddItem() || restEntity.getRemoveItem())
				{
					final PropertyTag dbEntity = entityManager.find(PropertyTag.class, restEntity.getId());
					if (dbEntity == null)
						throw new BadRequestException("No PropertyTag entity was found with the primary key " + restEntity.getId());

					if (restEntity.getAddItem())
					{
						entity.addPropertyTag(dbEntity, restEntity.getValue());
					}
					else if (restEntity.getRemoveItem())
					{
						entity.removePropertyTag(dbEntity, restEntity.getValue());
					}
				}
			}
		}
	}

}
