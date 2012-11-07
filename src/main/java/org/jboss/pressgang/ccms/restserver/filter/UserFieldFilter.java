package org.jboss.pressgang.ccms.restserver.filter;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.structures.field.UIFieldStringData;


public class UserFieldFilter extends BaseFieldFilter
{
    /**
     * A map of the base filter field names that can not have multiple
     *         mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>()
    {
        private static final long serialVersionUID = 4454656533723964663L;
        
        {
            put(CommonFilterConstants.USER_IDS_FILTER_VAR, CommonFilterConstants.USER_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.USER_NAME_FILTER_VAR, CommonFilterConstants.USER_NAME_FILTER_VAR_DESC);
            put(CommonFilterConstants.USER_DESCRIPTION_FILTER_VAR, CommonFilterConstants.USER_DESCRIPTION_FILTER_VAR_DESC);
        }
    });
    
    private UIFieldStringData userIds;
    private UIFieldStringData userTitle;
    private UIFieldStringData userDescription;
    
    public UserFieldFilter()
    {
        resetAllValues();
    }
    
    @Override
    protected void resetAllValues()
    {
        super.resetAllValues();
        
        userIds = new UIFieldStringData(CommonFilterConstants.USER_IDS_FILTER_VAR, CommonFilterConstants.USER_IDS_FILTER_VAR_DESC);
        userTitle = new UIFieldStringData(CommonFilterConstants.USER_NAME_FILTER_VAR, CommonFilterConstants.USER_NAME_FILTER_VAR_DESC);
        userDescription = new UIFieldStringData(CommonFilterConstants.USER_DESCRIPTION_FILTER_VAR, CommonFilterConstants.USER_DESCRIPTION_FILTER_VAR_DESC);
        
        addFilterVar(userIds);
        addFilterVar(userTitle);
        addFilterVar(userDescription);
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
