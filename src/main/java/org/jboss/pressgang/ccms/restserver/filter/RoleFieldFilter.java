package org.jboss.pressgang.ccms.restserver.filter;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.structures.field.UIFieldStringData;


public class RoleFieldFilter extends BaseFieldFilter
{
    /**
     * A map of the base filter field names that can not have multiple
     *         mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>()
    {
        private static final long serialVersionUID = 4454656533723964663L;
        
        {
            put(CommonFilterConstants.ROLE_IDS_FILTER_VAR, CommonFilterConstants.ROLE_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.ROLE_NAME_FILTER_VAR, CommonFilterConstants.ROLE_NAME_FILTER_VAR_DESC);
            put(CommonFilterConstants.ROLE_DESCRIPTION_FILTER_VAR, CommonFilterConstants.ROLE_DESCRIPTION_FILTER_VAR_DESC);
        }
    });
    
    private UIFieldStringData roleIds;
    private UIFieldStringData roleName;
    private UIFieldStringData roleDescription;
    
    public RoleFieldFilter()
    {
        resetAllValues();
    }
    
    @Override
    protected void resetAllValues()
    {
        super.resetAllValues();
        
        roleIds = new UIFieldStringData(CommonFilterConstants.ROLE_IDS_FILTER_VAR, CommonFilterConstants.ROLE_IDS_FILTER_VAR_DESC);
        roleName = new UIFieldStringData(CommonFilterConstants.ROLE_NAME_FILTER_VAR, CommonFilterConstants.ROLE_NAME_FILTER_VAR_DESC);
        roleDescription = new UIFieldStringData(CommonFilterConstants.ROLE_DESCRIPTION_FILTER_VAR, CommonFilterConstants.ROLE_DESCRIPTION_FILTER_VAR_DESC);
        
        addFilterVar(roleIds);
        addFilterVar(roleName);
        addFilterVar(roleDescription);
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
