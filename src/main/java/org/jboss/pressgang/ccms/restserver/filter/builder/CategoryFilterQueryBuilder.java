package org.jboss.pressgang.ccms.restserver.filter.builder;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.entity.Category;
import org.jboss.pressgang.ccms.restserver.filter.base.BaseFilterQueryBuilder;

public class CategoryFilterQueryBuilder extends BaseFilterQueryBuilder<Category> {

    public CategoryFilterQueryBuilder(final EntityManager entityManager) {
        super(Category.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.CATEGORY_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdInCommaSeperatedListCondition("categoryId", fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.CATEGORY_NAME_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("categoryName", fieldValue);
        } else if (fieldName.equals(CommonFilterConstants.CATEGORY_DESCRIPTION_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("categoryDescription", fieldValue);
        } else if (fieldName.equals(CommonFilterConstants.CATEGORY_IS_MUTUALLY_EXCLUSIVE_VAR)) {
            final Boolean fieldValueBoolean = Boolean.parseBoolean(fieldValue);
            if (fieldValueBoolean) {
                addFieldCondition(getCriteriaBuilder().isTrue(getRootPath().get("mutuallyExclusive").as(Boolean.class)));
            }
        } else if (fieldName.equals(CommonFilterConstants.CATEGORY_IS_NOT_MUTUALLY_EXCLUSIVE_VAR)) {
            final Boolean fieldValueBoolean = Boolean.parseBoolean(fieldValue);
            if (fieldValueBoolean) {
                addFieldCondition(getCriteriaBuilder().isFalse(getRootPath().get("mutuallyExclusive").as(Boolean.class)));
            }
        } else {
            super.processFilterString(fieldName, fieldValue);
        }
    }

}
