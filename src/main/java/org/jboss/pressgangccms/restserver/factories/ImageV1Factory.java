package org.jboss.pressgangccms.restserver.factories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgangccms.rest.v1.collections.RESTImageCollectionV1;
import org.jboss.pressgangccms.rest.v1.collections.RESTLanguageImageCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTImageV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTLanguageImageV1;
import org.jboss.pressgangccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgangccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgangccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgangccms.restserver.BaseRESTv1;
import org.jboss.pressgangccms.restserver.entities.ImageFile;
import org.jboss.pressgangccms.restserver.entities.LanguageImage;

public class ImageV1Factory extends RESTDataObjectFactory<RESTImageV1, ImageFile, RESTImageCollectionV1>
{
	public ImageV1Factory()
	{
		super(ImageFile.class);
	}
	
	@Override
	RESTImageV1 createRESTEntityFromDBEntity(final ImageFile entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager)
	{
		assert entity != null : "Parameter topic can not be null";
		assert baseUrl != null : "Parameter baseUrl can not be null";
		
		final RESTImageV1 retValue = new RESTImageV1();
		
		final List<String> expandOptions = new ArrayList<String>();
		if (revision == null)
		{
			expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
		}
		expandOptions.add(RESTImageV1.LANGUAGEIMAGES_NAME);
		retValue.setExpand(expandOptions);
		
		/* Set the collections */
		retValue.setLanguageImages_OTM
		(
			new RESTDataObjectCollectionFactory<RESTLanguageImageV1, LanguageImage, RESTLanguageImageCollectionV1>().create
			(
				RESTLanguageImageCollectionV1.class,
				new LanguageImageV1Factory(), 
				entity.getLanguageImagesArray(), 
				RESTImageV1.LANGUAGEIMAGES_NAME, 
				dataType, 
				expand, 
				baseUrl, 
				false,	/* don't set the reference to this entity on the children */
				entityManager
			)
		);
		retValue.setId(entity.getImageFileId());
		retValue.setDescription(entity.getDescription());
		
		if (revision == null)
		{
			retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTImageV1, ImageFile, RESTImageCollectionV1>().create(RESTImageCollectionV1.class, new ImageV1Factory(), entity, entity.getRevisions(), RESTBaseEntityV1.REVISIONS_NAME, dataType, expand, baseUrl, entityManager));
		}
		
		retValue.setLinks(baseUrl, BaseRESTv1.IMAGE_URL_NAME, dataType, retValue.getId());
		
		return retValue;
	}

	@Override
	public
	void syncDBEntityWithRESTEntity(final EntityManager entityManager, final ImageFile entity, final RESTImageV1 dataObject) throws InvalidParameterException
	{
		if (dataObject.hasParameterSet(RESTImageV1.DESCRIPTION_NAME))
			entity.setDescription(dataObject.getDescription());
		
		/* One To Many - Add will create a child entity */
		if (dataObject.hasParameterSet(RESTImageV1.LANGUAGEIMAGES_NAME) && dataObject.getLanguageImages_OTM() != null && dataObject.getLanguageImages_OTM().getItems() != null)
		{
			for (final RESTLanguageImageV1 restEntity : dataObject.getLanguageImages_OTM().getItems())
			{
				if (restEntity.getAddItem() || restEntity.getRemoveItem())
				{
					if (restEntity.getAddItem())
					{
						final LanguageImage dbEntity = new LanguageImage();
						dbEntity.setImageFile(entity);
						new LanguageImageV1Factory().syncDBEntityWithRESTEntity(entityManager, dbEntity, restEntity);						
						entity.getLanguageImages().add(dbEntity);
					}
					else if (restEntity.getRemoveItem())
					{
						final LanguageImage dbEntity = entityManager.find(LanguageImage.class, restEntity.getId());
						if (dbEntity == null)
							throw new InvalidParameterException("No LanguageImage entity was found with the primary key " + restEntity.getId());
						entity.getLanguageImages().remove(dbEntity);
					}
				}
			}
		}
		
		entityManager.persist(entity);
	}
}
