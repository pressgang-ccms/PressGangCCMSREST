package org.jboss.pressgang.ccms.server.filter.builder;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.model.BlobConstants;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.server.filter.base.BaseFilterQueryBuilder;

public class BlobConstantFilterQueryBuilder extends BaseFilterQueryBuilder<BlobConstants> {

    public BlobConstantFilterQueryBuilder(final EntityManager entityManager) {
        super(BlobConstants.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.BLOB_CONSTANT_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdInCommaSeperatedListCondition("blobConstantsId", fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.BLOB_CONSTANT_NAME_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("constantName", fieldValue);
        } else {
            super.processFilterString(fieldName, fieldValue);
        }
    }

}
