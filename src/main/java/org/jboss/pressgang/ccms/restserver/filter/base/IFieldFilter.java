package org.jboss.pressgang.ccms.restserver.filter.base;

import java.util.Map;

import org.jboss.pressgang.ccms.restserver.entity.Filter;

/**
 * A Field Filter provides a mechanism to find what fields are valid for a filter and a place to temporarily store the data.
 */
public interface IFieldFilter {
    /**
     * Check if the field filter has a property field name.
     * 
     * @param fieldName the name of the field to be checked.
     * @return True if the field filter contains the field name, otherwise false.
     */
    boolean hasFieldName(String fieldName);

    /**
     * Get the Regex value of the Field Names that exist for the filter.
     */
    Map<String, String> getFieldNames();

    /**
     * Get the unaltered Field names that exist for the filter.
     */
    Map<String, String> getBaseFieldNames();

    /**
     * Get the description of a filter field for a property field name.
     * 
     * @param fieldName the name of the field to get the description for.
     * @return The Field's description if the field exists, otherwise null.
     */
    String getFieldDesc(String fieldName);

    /**
     * Get the value of a filter field for a property field name.
     * 
     * @param fieldName the name of the field to get the value for.
     * @return The Field's value if the field exists, otherwise null.
     */
    String getFieldValue(String fieldName);

    /**
     * Set the value of a filter field for a property field name.
     * 
     * @param fieldName The name of the field.
     * @param fieldValue The value of the field.
     */
    void setFieldValue(String fieldName, String fieldValue);

    /**
     * Sync the values saved in a Filter with this Field Filter.
     * 
     * @param filter The Filter Entity to be synced with.
     */
    void syncWithFilter(Filter filter);
}
