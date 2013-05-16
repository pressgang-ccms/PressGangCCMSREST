package org.jboss.pressgang.ccms.server.filter;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.server.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.server.filter.structures.FilterFieldStringData;


public class StringConstantFieldFilter extends BaseFieldFilter
{
    /**
     * A map of the base filter field names that can not have multiple
     *         mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>()
    {
        private static final long serialVersionUID = 4454656533723964663L;
        
        {
            put(CommonFilterConstants.STRING_CONSTANT_IDS_FILTER_VAR, CommonFilterConstants.STRING_CONSTANT_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.STRING_CONSTANT_NAME_FILTER_VAR, CommonFilterConstants.STRING_CONSTANT_NAME_FILTER_VAR_DESC);
            put(CommonFilterConstants.STRING_CONSTANT_VALUE_FILTER_VAR, CommonFilterConstants.STRING_CONSTANT_VALUE_FILTER_VAR_DESC);
        }
    });
    
    private FilterFieldStringData stringConstantIds;
    private FilterFieldStringData stringConstantName;
    private FilterFieldStringData stringConstantValue;
        
    public StringConstantFieldFilter()
    {
        resetAllValues();
    }
    
    @Override
    protected void resetAllValues()
    {
        stringConstantIds = new FilterFieldStringData(CommonFilterConstants.STRING_CONSTANT_IDS_FILTER_VAR, CommonFilterConstants.STRING_CONSTANT_IDS_FILTER_VAR_DESC);
        stringConstantName = new FilterFieldStringData(CommonFilterConstants.STRING_CONSTANT_NAME_FILTER_VAR, CommonFilterConstants.STRING_CONSTANT_NAME_FILTER_VAR_DESC);
        stringConstantValue = new FilterFieldStringData(CommonFilterConstants.STRING_CONSTANT_VALUE_FILTER_VAR, CommonFilterConstants.STRING_CONSTANT_VALUE_FILTER_VAR_DESC);
        
        addFilterVar(stringConstantIds);
        addFilterVar(stringConstantName);
        addFilterVar(stringConstantValue);
    }

    @Override
    public Map<String, String> getFieldNames()
    {
        final Map<String, String> retValue = super.getFieldNames();
        retValue.putAll(filterNames);
        return retValue;
    }

    @Override
    public Map<String, String> getBaseFieldNames()
    {
        final Map<String, String> retValue = super.getFieldNames();
        retValue.putAll(filterNames);
        return retValue;
    }

}
