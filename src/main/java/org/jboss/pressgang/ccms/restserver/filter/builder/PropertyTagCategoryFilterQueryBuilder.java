package org.jboss.pressgang.ccms.restserver.filter.builder;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.entity.PropertyTagCategory;
import org.jboss.pressgang.ccms.restserver.filter.base.BaseFilterQueryBuilder;

public class PropertyTagCategoryFilterQueryBuilder extends BaseFilterQueryBuilder<PropertyTagCategory>
{
    public PropertyTagCategoryFilterQueryBuilder(final EntityManager entityManager) {
        super(PropertyTagCategory.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue)
    {
        if (fieldName.equals(CommonFilterConstants.PROP_CATEGORY_IDS_FILTER_VAR))
        {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdInCommaSeperatedListCondition("propertyTagCategoryId", fieldValue);
            }
        }
        else if (fieldName.equals(CommonFilterConstants.PROP_CATEGORY_NAME_FILTER_VAR))
        {
            addLikeIgnoresCaseCondition("propertyTagCategoryName", fieldValue);
        }
        else if (fieldName.equals(CommonFilterConstants.PROP_CATEGORY_DESCRIPTION_FILTER_VAR))
        {
            addLikeIgnoresCaseCondition("propertyTagCategoryDescription", fieldValue);
        }
        else
        {
            super.processFilterString(fieldName, fieldValue);
        }
    }
}
