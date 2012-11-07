package org.jboss.pressgang.ccms.restserver.filter;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.restserver.filter.structures.field.UIFieldStringData;


public class ImageFieldFilter extends BaseFieldFilter
{
    /**
     * A map of the base filter field names that can not have multiple
     *         mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>()
    {
        private static final long serialVersionUID = 4454656533723964663L;
        
        {
            put(CommonFilterConstants.IMAGE_IDS_FILTER_VAR, CommonFilterConstants.IMAGE_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.IMAGE_DESCRIPTION_FILTER_VAR, CommonFilterConstants.IMAGE_DESCRIPTION_FILTER_VAR_DESC);
            put(CommonFilterConstants.IMAGE_ORIGINAL_FILENAME_FILTER_VAR, CommonFilterConstants.IMAGE_ORIGINAL_FILENAME_FILTER_VAR_DESC);
        }
    });
    
    private UIFieldStringData imageIds;
    private UIFieldStringData imageOriginialFilename;
    private UIFieldStringData imageDescription;
    
    public ImageFieldFilter()
    {
        resetAllValues();
    }
    
    @Override
    protected void resetAllValues()
    {
        super.resetAllValues();
        
        imageIds = new UIFieldStringData(CommonFilterConstants.IMAGE_IDS_FILTER_VAR, CommonFilterConstants.IMAGE_IDS_FILTER_VAR_DESC);
        imageOriginialFilename = new UIFieldStringData(CommonFilterConstants.IMAGE_ORIGINAL_FILENAME_FILTER_VAR, CommonFilterConstants.IMAGE_ORIGINAL_FILENAME_FILTER_VAR_DESC);
        imageDescription = new UIFieldStringData(CommonFilterConstants.IMAGE_DESCRIPTION_FILTER_VAR, CommonFilterConstants.IMAGE_DESCRIPTION_FILTER_VAR_DESC);

        addFilterVar(imageIds);
        addFilterVar(imageOriginialFilename);
        addFilterVar(imageDescription);
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
