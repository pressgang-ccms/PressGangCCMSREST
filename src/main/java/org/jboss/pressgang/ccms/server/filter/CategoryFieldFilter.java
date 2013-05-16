package org.jboss.pressgang.ccms.server.filter;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.server.filter.base.BaseFieldFilter;
import org.jboss.pressgang.ccms.server.filter.structures.FilterFieldBooleanData;
import org.jboss.pressgang.ccms.server.filter.structures.FilterFieldStringData;


public class CategoryFieldFilter extends BaseFieldFilter
{
    /**
     * A map of the base filter field names that can not have multiple
     *         mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>()
    {
        private static final long serialVersionUID = 4454656533723964663L;
        
        {
            put(CommonFilterConstants.CATEGORY_IDS_FILTER_VAR, CommonFilterConstants.CATEGORY_IDS_FILTER_VAR_DESC);
            put(CommonFilterConstants.CATEGORY_NAME_FILTER_VAR, CommonFilterConstants.CATEGORY_NAME_FILTER_VAR_DESC);
            put(CommonFilterConstants.CATEGORY_DESCRIPTION_FILTER_VAR, CommonFilterConstants.CATEGORY_DESCRIPTION_FILTER_VAR_DESC);
            put(CommonFilterConstants.CATEGORY_IS_MUTUALLY_EXCLUSIVE_VAR, CommonFilterConstants.CATEGORY_IS_MUTUALLY_EXCLUSIVE_VAR_DESC);
            put(CommonFilterConstants.CATEGORY_IS_NOT_MUTUALLY_EXCLUSIVE_VAR, CommonFilterConstants.CATEGORY_IS_NOT_MUTUALLY_EXCLUSIVE_VAR_DESC);
        }
    });
    
    private FilterFieldStringData categoryIds;
    private FilterFieldStringData categoryName;
    private FilterFieldStringData categoryDescription;
    private FilterFieldBooleanData categoryIsMutuallyExclusive;
    private FilterFieldBooleanData categoryIsNotMutuallyExclusive;
    
    public CategoryFieldFilter()
    {
        resetAllValues();
    }
    
    @Override
    protected void resetAllValues()
    {
        categoryIds = new FilterFieldStringData(CommonFilterConstants.CATEGORY_IDS_FILTER_VAR, CommonFilterConstants.CATEGORY_IDS_FILTER_VAR_DESC);
        categoryName = new FilterFieldStringData(CommonFilterConstants.CATEGORY_NAME_FILTER_VAR, CommonFilterConstants.CATEGORY_NAME_FILTER_VAR_DESC);
        categoryDescription = new FilterFieldStringData(CommonFilterConstants.CATEGORY_DESCRIPTION_FILTER_VAR, CommonFilterConstants.CATEGORY_DESCRIPTION_FILTER_VAR_DESC);
        categoryIsMutuallyExclusive = new FilterFieldBooleanData(CommonFilterConstants.CATEGORY_IS_MUTUALLY_EXCLUSIVE_VAR, CommonFilterConstants.CATEGORY_IS_MUTUALLY_EXCLUSIVE_VAR_DESC);
        categoryIsNotMutuallyExclusive = new FilterFieldBooleanData(CommonFilterConstants.CATEGORY_IS_NOT_MUTUALLY_EXCLUSIVE_VAR, CommonFilterConstants.CATEGORY_IS_NOT_MUTUALLY_EXCLUSIVE_VAR_DESC);

        addFilterVar(categoryIds);
        addFilterVar(categoryName);
        addFilterVar(categoryDescription);
        addFilterVar(categoryIsMutuallyExclusive);
        addFilterVar(categoryIsNotMutuallyExclusive);
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
