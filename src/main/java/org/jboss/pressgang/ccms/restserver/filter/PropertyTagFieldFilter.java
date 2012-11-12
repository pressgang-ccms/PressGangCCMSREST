package org.jboss.pressgang.ccms.restserver.filter;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.structures.FilterFieldStringData;


public class PropertyTagFieldFilter extends BaseFieldFilter
{
    /**
     * A map of the base filter field names that can not have multiple
     *         mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>()
    {
        private static final long serialVersionUID = 4454656533723964663L;
        
        {
            put(CommonFilterConstants.PROP_TAG_IDS_FILTER_VAR, CommonFilterConstants.PROP_TAG_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.PROP_TAG_NAME_FILTER_VAR, CommonFilterConstants.PROP_TAG_NAME_FILTER_VAR_DESC);
            put(CommonFilterConstants.PROP_TAG_DESCRIPTION_FILTER_VAR, CommonFilterConstants.PROP_TAG_DESCRIPTION_FILTER_VAR_DESC);
        }
    });
    
    private FilterFieldStringData propTagIds;
    private FilterFieldStringData propTagName;
    private FilterFieldStringData propTagDescription;
    
    public PropertyTagFieldFilter()
    {
        resetAllValues();
    }
    
    @Override
    protected void resetAllValues()
    {
        super.resetAllValues();
        
        propTagIds = new FilterFieldStringData(CommonFilterConstants.PROP_TAG_IDS_FILTER_VAR, CommonFilterConstants.PROP_TAG_IDS_FILTER_VAR_DESC);
        propTagName = new FilterFieldStringData(CommonFilterConstants.PROP_TAG_NAME_FILTER_VAR, CommonFilterConstants.PROP_TAG_NAME_FILTER_VAR_DESC);
        propTagDescription = new FilterFieldStringData(CommonFilterConstants.PROP_TAG_DESCRIPTION_FILTER_VAR, CommonFilterConstants.PROP_TAG_DESCRIPTION_FILTER_VAR_DESC);
        
        addFilterVar(propTagIds);
        addFilterVar(propTagName);
        addFilterVar(propTagDescription);
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
