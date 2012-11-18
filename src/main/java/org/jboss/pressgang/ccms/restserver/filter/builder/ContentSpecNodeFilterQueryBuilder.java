package org.jboss.pressgang.ccms.restserver.filter.builder;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.model.contentspec.CSNode;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.filter.base.BaseFilterQueryBuilder;

public class ContentSpecNodeFilterQueryBuilder extends BaseFilterQueryBuilder<CSNode> {
    public ContentSpecNodeFilterQueryBuilder(final EntityManager entityManager) {
        super(CSNode.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_NODE_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdInCommaSeperatedListCondition("CSNodeId", fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_NODE_TITLE_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("CSNodeTitle", fieldValue);
        } else {
            super.processFilterString(fieldName, fieldValue);
        }
    }
}
