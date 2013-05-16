package org.jboss.pressgang.ccms.server.filter.builder;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.model.PropertyTag;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.server.filter.base.BaseFilterQueryBuilder;

public class PropertyTagFilterQueryBuilder extends BaseFilterQueryBuilder<PropertyTag>
{
    public PropertyTagFilterQueryBuilder(final EntityManager entityManager) {
        super(PropertyTag.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue)
    {
        if (fieldName.equals(CommonFilterConstants.PROP_TAG_IDS_FILTER_VAR))
        {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdInCommaSeperatedListCondition("propertyTagId", fieldValue);
            }
        }
        else if (fieldName.equals(CommonFilterConstants.PROP_TAG_NAME_FILTER_VAR))
        {
            addLikeIgnoresCaseCondition("propertyTagName", fieldValue);
        }
        else if (fieldName.equals(CommonFilterConstants.PROP_TAG_DESCRIPTION_FILTER_VAR))
        {
            addLikeIgnoresCaseCondition("propertyTagDescription", fieldValue);
        }
        else
        {
            super.processFilterString(fieldName, fieldValue);
        }
    }
}
