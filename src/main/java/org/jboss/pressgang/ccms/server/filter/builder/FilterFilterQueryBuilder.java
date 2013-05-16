package org.jboss.pressgang.ccms.server.filter.builder;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.model.Filter;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.server.filter.base.BaseFilterQueryBuilder;

public class FilterFilterQueryBuilder extends BaseFilterQueryBuilder<Filter>
{
    public FilterFilterQueryBuilder(final EntityManager entityManager) {
        super(Filter.class, entityManager);
    }
    
    @Override
    public void processFilterString(final String fieldName, final String fieldValue)
    {
        if (fieldName.equals(CommonFilterConstants.FILTER_IDS_FILTER_VAR))
        {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdInCommaSeperatedListCondition("filterId", fieldValue);
            }
        }
        else if (fieldName.equals(CommonFilterConstants.FILTER_NAME_FILTER_VAR))
        {
            addLikeIgnoresCaseCondition("filterName", fieldValue);
        }
        else if (fieldName.equals(CommonFilterConstants.FILTER_DESCRIPTION_FILTER_VAR))
        {
            addLikeIgnoresCaseCondition("filterDescription", fieldValue);
        }
        else
        {
            super.processFilterString(fieldName, fieldValue);
        }
    }
}
