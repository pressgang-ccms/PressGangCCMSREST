package org.jboss.pressgang.ccms.restserver.filter;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.structures.field.UIFieldStringData;


public class ProjectFieldFilter extends BaseFieldFilter
{
    /**
     * A map of the base filter field names that can not have multiple
     *         mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>()
    {
        private static final long serialVersionUID = 4454656533723964663L;
        
        {
            put(CommonFilterConstants.PROJECT_IDS_FILTER_VAR, CommonFilterConstants.PROJECT_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.PROJECT_NAME_FILTER_VAR, CommonFilterConstants.PROJECT_NAME_FILTER_VAR_DESC);
            put(CommonFilterConstants.PROJECT_DESCRIPTION_FILTER_VAR, CommonFilterConstants.PROJECT_DESCRIPTION_FILTER_VAR_DESC);
        }
    });
    
    private UIFieldStringData projectIds;
    private UIFieldStringData projectName;
    private UIFieldStringData projectDescription;

    public ProjectFieldFilter()
    {
        resetAllValues();
    }
    
    @Override
    protected void resetAllValues()
    {
        super.resetAllValues();
        
        projectIds = new UIFieldStringData(CommonFilterConstants.PROJECT_IDS_FILTER_VAR, CommonFilterConstants.PROJECT_IDS_FILTER_VAR_DESC);
        projectName = new UIFieldStringData(CommonFilterConstants.PROJECT_NAME_FILTER_VAR, CommonFilterConstants.PROJECT_NAME_FILTER_VAR_DESC);
        projectDescription = new UIFieldStringData(CommonFilterConstants.PROJECT_DESCRIPTION_FILTER_VAR, CommonFilterConstants.PROJECT_DESCRIPTION_FILTER_VAR_DESC);
        
        addFilterVar(projectIds);
        addFilterVar(projectName);
        addFilterVar(projectDescription);
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
