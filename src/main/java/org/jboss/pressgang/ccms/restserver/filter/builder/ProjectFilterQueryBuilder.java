package org.jboss.pressgang.ccms.restserver.filter.builder;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.model.Project;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.filter.base.BaseFilterQueryBuilder;

public class ProjectFilterQueryBuilder extends BaseFilterQueryBuilder<Project>
{
    public ProjectFilterQueryBuilder(final EntityManager entityManager) {
        super(Project.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue)
    {
        if (fieldName.equals(CommonFilterConstants.PROJECT_IDS_FILTER_VAR))
        {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdInCommaSeperatedListCondition("projectId", fieldValue);
            }
        }
        else if (fieldName.equals(CommonFilterConstants.PROJECT_NAME_FILTER_VAR))
        {
            addLikeIgnoresCaseCondition("projectName", fieldValue);
        }
        else if (fieldName.equals(CommonFilterConstants.PROJECT_DESCRIPTION_FILTER_VAR))
        {
            addLikeIgnoresCaseCondition("projectDescription", fieldValue);
        }
        else
        {
            super.processFilterString(fieldName, fieldValue);
        }
    }
}
