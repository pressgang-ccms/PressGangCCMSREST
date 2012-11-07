package org.jboss.pressgang.ccms.restserver.filter.builder;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.entity.contentspec.CSMetaData;
import org.jboss.pressgang.ccms.restserver.filter.base.BaseFilterQueryBuilder;

public class ContentSpecMetaDataFilterQueryBuilder extends BaseFilterQueryBuilder<CSMetaData> {
    public ContentSpecMetaDataFilterQueryBuilder(final EntityManager entityManager) {
        super(CSMetaData.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_META_DATA_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdInCommaSeperatedListCondition("CSMetaDataId", fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.CONTENT_SPEC_META_DATA_TITLE_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("CSMetaDataTitle", fieldValue);
        } else {
            super.processFilterString(fieldName, fieldValue);
        }
    }
}