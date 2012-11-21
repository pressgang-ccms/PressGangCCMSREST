package org.jboss.pressgang.ccms.restserver.filter.builder;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.jboss.pressgang.ccms.model.ImageFile;
import org.jboss.pressgang.ccms.model.LanguageImage;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.filter.base.ILocaleFilterQueryBuilder;
import org.jboss.pressgang.ccms.restserver.utils.EntityUtilities;


public class ImageFilterQueryBuilder extends BaseFilterQueryBuilder<ImageFile> implements ILocaleFilterQueryBuilder
{
    public ImageFilterQueryBuilder(final EntityManager entityManager) {
        super(ImageFile.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue)
    {
        if (fieldName.equals(CommonFilterConstants.IMAGE_IDS_FILTER_VAR))
        {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdInCommaSeperatedListCondition("imageFileId", fieldValue);
            }
        }
        else if (fieldName.equals(CommonFilterConstants.IMAGE_DESCRIPTION_FILTER_VAR))
        {
            addLikeIgnoresCaseCondition("description", fieldValue);
        }
        else if (fieldName.equals(CommonFilterConstants.IMAGE_ORIGINAL_FILENAME_FILTER_VAR))
        {            
            final List<Integer> imageIds = EntityUtilities.getImagesWithFileName(getEntityManager(), fieldValue);
            addIdInCollectionCondition("imageFileId", imageIds);
        }
        else
        {
            super.processFilterString(fieldName, fieldValue);
        }
    }

    @Override
    public Predicate getMatchingLocaleString(final String locale)
    {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<LanguageImage> subquery = getCriteriaQuery().subquery(LanguageImage.class);
        final Root<LanguageImage> from = subquery.from(LanguageImage.class);
        final Predicate languageImageEqual = criteriaBuilder.equal(getRootPath(), from.get("imageFile"));
        final Predicate localeEqual = criteriaBuilder.equal(from.get("locale"), locale);
        subquery.where(criteriaBuilder.and(languageImageEqual, localeEqual));
        
        return criteriaBuilder.exists(subquery);
    }

    @Override
    public Predicate getNotMatchingLocaleString(final String locale)
    {
        final CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        final Subquery<LanguageImage> subquery = getCriteriaQuery().subquery(LanguageImage.class);
        final Root<LanguageImage> from = subquery.from(LanguageImage.class);
        final Predicate languageImageEqual = criteriaBuilder.equal(getRootPath(), from.get("imageFile"));
        final Predicate localeEqual = criteriaBuilder.equal(from.get("locale"), locale);
        subquery.where(criteriaBuilder.and(languageImageEqual, localeEqual));
        
        return criteriaBuilder.not(criteriaBuilder.exists(subquery));
    }

}
