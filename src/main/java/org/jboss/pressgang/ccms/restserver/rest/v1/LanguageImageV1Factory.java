package org.jboss.pressgang.ccms.restserver.rest.v1;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.model.LanguageImage;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTLanguageImageCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTLanguageImageCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTLanguageImageV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseEntityV1;
import org.jboss.pressgang.ccms.rest.v1.expansion.ExpandDataTrunk;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectCollectionFactory;
import org.jboss.pressgang.ccms.restserver.rest.v1.base.RESTDataObjectFactory;
import org.jboss.pressgang.ccms.restserver.utils.EnversUtilities;

public class LanguageImageV1Factory
        extends
        RESTDataObjectFactory<RESTLanguageImageV1, LanguageImage, RESTLanguageImageCollectionV1, RESTLanguageImageCollectionItemV1> {
    public LanguageImageV1Factory() {
        super(LanguageImage.class);
    }

    @Override
    public RESTLanguageImageV1 createRESTEntityFromDBEntityInternal(final LanguageImage entity, final String baseUrl,
            final String dataType, final ExpandDataTrunk expand, final Number revision, final boolean expandParentReferences,
            final EntityManager entityManager) {
        assert entity != null : "Parameter topic can not be null";
        assert baseUrl != null : "Parameter baseUrl can not be null";

        final RESTLanguageImageV1 retValue = new RESTLanguageImageV1();

        final List<String> expandOptions = new ArrayList<String>();
        expandOptions.add(RESTLanguageImageV1.IMAGEDATA_NAME);
        expandOptions.add(RESTLanguageImageV1.IMAGEDATABASE64_NAME);
        expandOptions.add(RESTLanguageImageV1.IMAGEDATABASE64_NAME);
        expandOptions.add(RESTBaseEntityV1.LOG_DETAILS_NAME);

        if (revision == null)
            expandOptions.add(RESTBaseEntityV1.REVISIONS_NAME);

        retValue.setExpand(expandOptions);

        retValue.setId(entity.getLanguageImageId());
        retValue.setLocale(entity.getLocale());
        retValue.setFilename(entity.getOriginalFileName());

        /* potentially large fields need to be expanded manually */
        if (expand != null && expand.contains(RESTLanguageImageV1.IMAGEDATA_NAME))
            retValue.setImageData(entity.getImageData());
        if (expand != null && expand.contains(RESTLanguageImageV1.IMAGEDATABASE64_NAME))
            retValue.setImageDataBase64(entity.getImageDataBase64());
        if (expand != null && expand.contains(RESTLanguageImageV1.THUMBNAIL_NAME))
            retValue.setThumbnail(entity.getThumbnailData());

        /* Set the object references */
        if (expandParentReferences && expand != null && expand.contains(RESTLanguageImageV1.IMAGE_NAME)
                && entity.getImageFile() != null) {
            retValue.setImage(new ImageV1Factory().createRESTEntityFromDBEntity(entity.getImageFile(), baseUrl, dataType,
                    expand.get(RESTLanguageImageV1.IMAGE_NAME), entityManager));
        }

        // REVISIONS
        if (revision == null && expand != null && expand.contains(RESTBaseEntityV1.REVISIONS_NAME)) {
            retValue.setRevisions(new RESTDataObjectCollectionFactory<RESTLanguageImageV1, LanguageImage, RESTLanguageImageCollectionV1, RESTLanguageImageCollectionItemV1>()
                    .create(RESTLanguageImageCollectionV1.class, new LanguageImageV1Factory(), entity,
                            EnversUtilities.getRevisions(entityManager, entity), RESTBaseEntityV1.REVISIONS_NAME, dataType,
                            expand, baseUrl, entityManager));
        }

        return retValue;
    }

    @Override
    public void syncDBEntityWithRESTEntity(final EntityManager entityManager, final LanguageImage entity,
            final RESTLanguageImageV1 dataObject) {
        if (dataObject.hasParameterSet(RESTLanguageImageV1.LOCALE_NAME))
            entity.setLocale(dataObject.getLocale());
        if (dataObject.hasParameterSet(RESTLanguageImageV1.IMAGEDATA_NAME))
            entity.setImageData(dataObject.getImageData());
        if (dataObject.hasParameterSet(RESTLanguageImageV1.FILENAME_NAME))
            entity.setOriginalFileName(dataObject.getFilename());
    }

}
