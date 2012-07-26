package org.jboss.pressgangccms.restserver;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgangccms.rest.v1.collections.RESTProjectCollectionV1;
import org.jboss.pressgangccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTProjectV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgangccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgangccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgangccms.restserver.entities.Project;
import org.jboss.pressgangccms.restserver.entities.Tag;
import org.jboss.resteasy.spi.BadRequestException;

class ProjectV1Factory extends RESTDataObjectFactory<RESTProjectV1, Project, RESTProjectCollectionV1>
{
	ProjectV1Factory()
	{
		super(Project.class);
	}

	@Override
	RESTProjectV1 createRESTEntityFromDBEntity(final Project entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager) 
	{
		assert entity != null : "Parameter topic can not be null";
		assert baseUrl != null : "Parameter baseUrl can not be null";

		final RESTProjectV1 retValue = new RESTProjectV1();

		retValue.setId(entity.getProjectId());
		retValue.setDescription(entity.getProjectDescription());
		retValue.setName(entity.getProjectName());
		
		final List<String> expandOptions = new ArrayList<String>();
		expandOptions.add(BaseRESTv1.TAGS_EXPANSION_NAME);
		if (revision == null)
			expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
		
		retValue.setExpand(expandOptions);	

		if (revision == null)
		{
			retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTProjectV1, Project, RESTProjectCollectionV1>().create(RESTProjectCollectionV1.class, new ProjectV1Factory(), entity, entity.getRevisions(), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
		}
		retValue.setTags(new RESTDataObjectCollectionFactory<RESTTagV1, Tag, RESTTagCollectionV1>().create(RESTTagCollectionV1.class, new TagV1Factory(), entity.getTags(), BaseRESTv1.TAGS_EXPANSION_NAME, dataType, expand, baseUrl, entityManager));

		retValue.setLinks(baseUrl, BaseRESTv1.PROJECT_URL_NAME, dataType, retValue.getId());

		return retValue;
	}

	@Override
	void syncDBEntityWithRESTEntity(final EntityManager entityManager, final Project entity, final RESTProjectV1 dataObject)
	{
		if (dataObject.hasParameterSet(RESTProjectV1.DESCRIPTION_NAME))
			entity.setProjectDescription(dataObject.getDescription());
		if (dataObject.hasParameterSet(RESTProjectV1.NAME_NAME))
			entity.setProjectName(dataObject.getName());
		
		entityManager.persist(entity);

		/* Many To Many - Add will create a mapping */
		if (dataObject.hasParameterSet(RESTProjectV1.TAGS_NAME) && dataObject.getTags() != null && dataObject.getTags().getItems() != null)
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
						entity.addRelationshipTo(dbEntity);
					}
					else if (restEntity.getRemoveItem())
					{
						entity.removeRelationshipTo(dbEntity.getTagId());
					}
				}
			}
		}
	}
}
