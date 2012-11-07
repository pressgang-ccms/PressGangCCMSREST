package org.jboss.pressgang.ccms.restserver.filter.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.pressgang.ccms.rest.v1.constants.CommonFilterConstants;
import org.jboss.pressgang.ccms.restserver.entity.Filter;
import org.jboss.pressgang.ccms.restserver.entity.FilterField;
import org.jboss.pressgang.ccms.restserver.filter.structures.field.UIFieldDataBase;
import org.jboss.pressgang.ccms.restserver.filter.structures.field.UIFieldStringData;


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
    
    private List<UIFieldDataBase<?>> filterVars = new ArrayList<UIFieldDataBase<?>>();
    
    private UIFieldStringData logic;
    
    /**
     * Reset all of the Field Variables to their default values.
     */
    protected void resetAllValues()
    {
        logic = new UIFieldStringData(CommonFilterConstants.LOGIC_FILTER_VAR, CommonFilterConstants.LOGIC_FILTER_VAR_DESC);
        
        filterVars.clear();
        filterVars.add(logic);
    }
    
    /**
     * Get the Regex value of the Field Names that exist for the filter.
     */
    @Override
    public Map<String, String> getFieldNames()
    {
        return new HashMap<String, String>(filterNames);
    }

    /**
     * Get the unaltered Field names that exist for the filter.
     */
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
    
    protected void addFilterVar(final UIFieldDataBase<?> filterVar)
    {
        this.filterVars.add(filterVar);
    }
    
    protected List<UIFieldDataBase<?>> getFilterVars()
    {
        return Collections.unmodifiableList(filterVars);
    }
    
    @Override
    public String getFieldValue(final String fieldName)
    {
        final List<UIFieldDataBase<?>> filterVars = getFilterVars();
        for (final UIFieldDataBase<?> uiField : filterVars)
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
        final List<UIFieldDataBase<?>> filterVars = getFilterVars();
        for (final UIFieldDataBase<?> uiField : filterVars)
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
    
    public UIFieldStringData getLogic()
    {
        return logic;
    }
}
