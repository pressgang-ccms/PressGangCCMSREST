package org.jboss.pressgang.ccms.restserver.filter.builder;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.model.Role;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.filter.base.BaseFilterQueryBuilder;

public class RoleFilterQueryBuilder extends BaseFilterQueryBuilder<Role>
{
    public RoleFilterQueryBuilder(final EntityManager entityManager) {
        super(Role.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue)
    {
        if (fieldName.equals(CommonFilterConstants.ROLE_IDS_FILTER_VAR))
        {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdInCommaSeperatedListCondition("roleId", fieldValue);
            }
        }
        else if (fieldName.equals(CommonFilterConstants.ROLE_NAME_FILTER_VAR))
        {
            addLikeIgnoresCaseCondition("roleName", fieldValue);
        }
        else if (fieldName.equals(CommonFilterConstants.ROLE_DESCRIPTION_FILTER_VAR))
        {
            addLikeIgnoresCaseCondition("description", fieldValue);
        }
        else
        {
            super.processFilterString(fieldName, fieldValue);
        }
    }
}
