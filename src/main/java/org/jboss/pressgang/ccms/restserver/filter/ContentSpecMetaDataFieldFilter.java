package org.jboss.pressgang.ccms.restserver.filter;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.structures.field.UIFieldStringData;


public class ContentSpecMetaDataFieldFilter extends BaseFieldFilter
{
    /**
     * A map of the base filter field names that can not have multiple
     *         mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>()
    {
        private static final long serialVersionUID = 4454656533723964663L;
        
        {
            put(CommonFilterConstants.CONTENT_SPEC_META_DATA_IDS_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_META_DATA_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.CONTENT_SPEC_META_DATA_TITLE_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_META_DATA_TITLE_FILTER_VAR_DESC);
        }
    });
    
    private UIFieldStringData csMetaDataIds;
    private UIFieldStringData csMetaDataTitle;
    
    public ContentSpecMetaDataFieldFilter()
    {
        resetAllValues();
    }
    
    @Override
    protected void resetAllValues()
    {
        super.resetAllValues();
        
        csMetaDataIds = new UIFieldStringData(CommonFilterConstants.CONTENT_SPEC_META_DATA_IDS_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_META_DATA_IDS_FILTER_VAR_DESC);
        csMetaDataTitle = new UIFieldStringData(CommonFilterConstants.CONTENT_SPEC_META_DATA_TITLE_FILTER_VAR, CommonFilterConstants.CONTENT_SPEC_META_DATA_TITLE_FILTER_VAR_DESC);
        
        addFilterVar(csMetaDataIds);
        addFilterVar(csMetaDataTitle);
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
