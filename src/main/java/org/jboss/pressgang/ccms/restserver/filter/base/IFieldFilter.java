package org.jboss.pressgang.ccms.restserver.filter.base;

import java.util.Map;

import org.jboss.pressgang.ccms.restserver.entity.Filter;

public interface IFieldFilter
{
    boolean hasFieldName(String fieldName);
    Map<String, String> getFieldNames();
    Map<String, String> getBaseFieldNames();
    String getFieldDesc(String fieldName);
    String getFieldValue(String fieldName);
    void setFieldValue(String fieldName, String fieldValue);
    void syncWithFilter(Filter filter);
}
