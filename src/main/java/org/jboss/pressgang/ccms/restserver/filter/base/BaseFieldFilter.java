package org.jboss.pressgang.ccms.restserver.filter.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.pressgang.ccms.model.Filter;
import org.jboss.pressgang.ccms.model.FilterField;
import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.restserver.filter.structures.FilterFieldStringData;

/**
 * This class provides the bases for a mechanism to temporarily store and easily convert a set of fields for a filter until it needs to be
 * saved to a database entity.
 */
public abstract class BaseFieldFilter implements IFieldFilter
{
    /**
     * A map of the base filter field names that can not have multiple
     *         mappings
     */
    private static final Map<String, String> filterNames = Collections.unmodifiableMap(new HashMap<String, String>()
    {
        private static final long serialVersionUID = 1530842914263615035L;

        {
            put(CommonFilterConstants.LOGIC_FILTER_VAR, CommonFilterConstants.LOGIC_FILTER_VAR_DESC);
            
        }
    });
    
    private List<FilterFieldDataBase<?>> filterVars = new ArrayList<FilterFieldDataBase<?>>();
    
    private FilterFieldStringData logic;
    
    /**
     * Reset all of the Field Variables to their default values.
     */
    protected void resetAllValues()
    {
        logic = new FilterFieldStringData(CommonFilterConstants.LOGIC_FILTER_VAR, CommonFilterConstants.LOGIC_FILTER_VAR_DESC);
        
        filterVars.clear();
        filterVars.add(logic);
    }
    
    @Override
    public Map<String, String> getFieldNames()
    {
        return new HashMap<String, String>(filterNames);
    }

    @Override
    public Map<String, String> getBaseFieldNames()
    {
        return new HashMap<String, String>(filterNames);
    }
    
    @Override
    public boolean hasFieldName(final String fieldName)
    {
        return getFieldNames().containsKey(fieldName);
    }
    
    @Override
    public String getFieldDesc(final String fieldName)
    {
        return getFieldNames().get(fieldName);
    }
    
    protected void addFilterVar(final FilterFieldDataBase<?> filterVar)
    {
        this.filterVars.add(filterVar);
    }
    
    protected List<FilterFieldDataBase<?>> getFilterVars()
    {
        return Collections.unmodifiableList(filterVars);
    }
    
    @Override
    public String getFieldValue(final String fieldName)
    {
        final List<FilterFieldDataBase<?>> filterVars = getFilterVars();
        for (final FilterFieldDataBase<?> uiField : filterVars)
        {
            if (fieldName.equals(uiField.getName()))
            {
                return uiField.toString();
            }
        }
        
        return null;
    }

    @Override
    public void setFieldValue(final String fieldName, final String fieldValue)
    {
        final List<FilterFieldDataBase<?>> filterVars = getFilterVars();
        for (final FilterFieldDataBase<?> uiField : filterVars)
        {
            if (fieldName.equals(uiField.getName()))
            {
                uiField.setData(fieldValue);
            }
        }
    }

    @Override
    public void syncWithFilter(final Filter filter)
    {
        for (final FilterField field : filter.getFilterFields())
            this.setFieldValue(field.getField(), field.getValue());
    }
    
    public FilterFieldStringData getLogic()
    {
        return logic;
    }
}
