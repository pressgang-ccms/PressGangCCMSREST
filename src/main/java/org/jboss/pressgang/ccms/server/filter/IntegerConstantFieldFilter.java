package org.jboss.pressgang.ccms.server.filter;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.server.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.server.filter.structures.FilterFieldStringData;


public class IntegerConstantFieldFilter extends BaseFieldFilter
{
    /**
     * A map of the base filter field names that can not have multiple
     *         mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>()
    {
        private static final long serialVersionUID = 4454656533723964663L;
        
        {
            put(CommonFilterConstants.INTEGER_CONSTANT_IDS_FILTER_VAR, CommonFilterConstants.INTEGER_CONSTANT_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.INTEGER_CONSTANT_NAME_FILTER_VAR, CommonFilterConstants.INTEGER_CONSTANT_NAME_FILTER_VAR_DESC);
            put(CommonFilterConstants.INTEGER_CONSTANT_VALUE_FILTER_VAR, CommonFilterConstants.INTEGER_CONSTANT_VALUE_FILTER_VAR_DESC);
        }
    });
    
    private FilterFieldStringData integerConstantIds;
    private FilterFieldStringData integerConstantName;
    private FilterFieldStringData integerConstantValue;
        
    public IntegerConstantFieldFilter()
    {
        resetAllValues();
    }
    
    @Override
    protected void resetAllValues()
    {
        integerConstantIds = new FilterFieldStringData(CommonFilterConstants.INTEGER_CONSTANT_IDS_FILTER_VAR, CommonFilterConstants.INTEGER_CONSTANT_IDS_FILTER_VAR_DESC);
        integerConstantName = new FilterFieldStringData(CommonFilterConstants.INTEGER_CONSTANT_NAME_FILTER_VAR, CommonFilterConstants.INTEGER_CONSTANT_NAME_FILTER_VAR_DESC);
        integerConstantValue = new FilterFieldStringData(CommonFilterConstants.INTEGER_CONSTANT_VALUE_FILTER_VAR, CommonFilterConstants.INTEGER_CONSTANT_VALUE_FILTER_VAR_DESC);
        
        addFilterVar(integerConstantIds);
        addFilterVar(integerConstantName);
        addFilterVar(integerConstantValue);
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
