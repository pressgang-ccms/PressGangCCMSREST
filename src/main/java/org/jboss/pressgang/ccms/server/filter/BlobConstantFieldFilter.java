package org.jboss.pressgang.ccms.server.filter;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.server.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.server.filter.structures.FilterFieldStringData;


public class BlobConstantFieldFilter extends BaseFieldFilter
{
    /**
     * A map of the base filter field names that can not have multiple
     *         mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>()
    {
        private static final long serialVersionUID = 4454656533723964663L;
        
        {
            put(CommonFilterConstants.BLOB_CONSTANT_IDS_FILTER_VAR, CommonFilterConstants.BLOB_CONSTANT_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.BLOB_CONSTANT_NAME_FILTER_VAR, CommonFilterConstants.BLOB_CONSTANT_NAME_FILTER_VAR_DESC);
        }
    });
    
    private FilterFieldStringData blobConstantIds;
    private FilterFieldStringData blobConstantName;
    
    public BlobConstantFieldFilter()
    {
        resetAllValues();
    }
    
    @Override
    protected void resetAllValues()
    {
        super.resetAllValues();
        
        blobConstantIds = new FilterFieldStringData(CommonFilterConstants.BLOB_CONSTANT_IDS_FILTER_VAR, CommonFilterConstants.BLOB_CONSTANT_IDS_FILTER_VAR_DESC);
        blobConstantName = new FilterFieldStringData(CommonFilterConstants.BLOB_CONSTANT_NAME_FILTER_VAR, CommonFilterConstants.BLOB_CONSTANT_NAME_FILTER_VAR_DESC);
        
        addFilterVar(blobConstantIds);
        addFilterVar(blobConstantName);
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
