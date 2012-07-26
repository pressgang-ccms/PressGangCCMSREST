package org.jboss.pressgangccms.restserver.factories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgangccms.rest.v1.collections.RESTLanguageImageCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTLanguageImageV1;
import org.jboss.pressgangccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgangccms.rest.v1.exceptions.InvalidParameterException;
import org.jboss.pressgangccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgangccms.restserver.BaseRESTv1;
import org.jboss.pressgangccms.restserver.entities.LanguageImage;

public class LanguageImageV1Factory extends RESTDataObjectFactory<RESTLanguageImageV1, LanguageImage, RESTLanguageImageCollectionV1>
{
	LanguageImageV1Factory()
	{
		super(LanguageImage.class);
	}
	
	@Override
	RESTLanguageImageV1 createRESTEntityFromDBEntity(final LanguageImage entity, final String baseUrl, final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences, final EntityManager entityManager) throws InvalidParameterException
	{
		assert entity != null : "Parameter topic can not be null";
		assert baseUrl != null : "Parameter baseUrl can not be null";
		
		final RESTLanguageImageV1 retValue = new RESTLanguageImageV1();
		
		final List<String> expandOptions = new ArrayList<String>();
		if (revision == null)
			expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);
		expandOptions.add(RESTLanguageImageV1.IMAGEDATA_NAME);
		expandOptions.add(RESTLanguageImageV1.IMAGEDATABASE64_NAME);
		expandOptions.add(RESTLanguageImageV1.IMAGEDATABASE64_NAME);
		retValue.setExpand(expandOptions);
		
		retValue.setId(entity.getLanguageImageId());
		retValue.setLocale(entity.getLocale());
		retValue.setFilename(entity.getOriginalFileName());
		
		/* potentially large fields need to be expanded manually */
		if (expand.contains(RESTLanguageImageV1.IMAGEDATA_NAME) != null)
			retValue.setImageData(entity.getImageData());
		if (expand.contains(RESTLanguageImageV1.IMAGEDATABASE64_NAME) != null)
			retValue.setImageDataBase64(entity.getImageDataBase64());
		if (expand.contains(RESTLanguageImageV1.THUMBNAIL_NAME) != null)
			retValue.setThumbnail(entity.getThumbnailData());
		
		/* Set the object references */
		if (expandParentReferences)
		{
			retValue.setImage(new ImageV1Factory().createRESTEntityFromDBEntity(entity.getImageFile(), baseUrl, dataType, expand.contains(RESTLanguageImageV1.IMAGE_NAME)));
		}
		
		if (revision == null)
		{
			retValue.setRevisions(
				new RESTDataObjectCollectionFactory<RESTLanguageImageV1, LanguageImage, RESTLanguageImageCollectionV1>().create(
						RESTLanguageImageCollectionV1.class,
						new LanguageImageV1Factory(), 
						entity, 
						entity.getRevisions(), 
						RESTBaseEntityV1.REVISIONS_NAME, 
						dataType, 
						expand, 
						baseUrl, 
						entityManager));
		}
		
		retValue.setLinks(baseUrl, BaseRESTv1.LANGUAGEIMAGE_URL_NAME, dataType, retValue.getId());
		
		return retValue;
	}

	@Override
	public
	void syncDBEntityWithRESTEntity(final EntityManager entityManager, final LanguageImage entity, final RESTLanguageImageV1 dataObject)
	{
		if (dataObject.hasParameterSet(RESTLanguageImageV1.LOCALE_NAME))
			entity.setLocale(dataObject.getLocale());
		if (dataObject.hasParameterSet(RESTLanguageImageV1.IMAGEDATA_NAME))
			entity.setImageData(dataObject.getImageData());	
		if (dataObject.hasParameterSet(RESTLanguageImageV1.FILENAME_NAME))
			entity.setOriginalFileName(dataObject.getFilename());	
	}

}
