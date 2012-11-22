package org.jboss.pressgang.ccms.restserver.filter.builder;


import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.model.User;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.filter.base.BaseFilterQueryBuilder;

public class UserFilterQueryBuilder extends BaseFilterQueryBuilder<User> {

    public UserFilterQueryBuilder(final EntityManager entityManager) {
        super(User.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.USER_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdInCommaSeperatedListCondition("userId", fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.USER_NAME_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("userName", fieldValue);
        } else if (fieldName.equals(CommonFilterConstants.USER_DESCRIPTION_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("userDescription", fieldValue);
        } else {
            super.processFilterString(fieldName, fieldValue);
        }
    }
}
