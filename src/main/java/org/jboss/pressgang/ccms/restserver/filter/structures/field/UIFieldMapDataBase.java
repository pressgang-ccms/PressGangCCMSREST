package org.jboss.pressgang.ccms.restserver.filter.structures.field;

import java.util.HashMap;
import java.util.Map;

public abstract class UIFieldMapDataBase<T, U> extends UIFieldDataBase<Map<T, U>> {

    /** The data stored within this UIField */
    protected Map<T, U> data = new HashMap<T, U>();
    /** Whether or not this object has been "negated" */
    protected boolean negated = false;
    /** The name of the data field */
    protected String name = "";
    /** The description */
    protected String description = "";

    public UIFieldMapDataBase(final String name, final String description) {
        super(name, description);
    }

    public boolean isNegated() {
        return negated;
    }

    public void setNegated(final boolean negated) {
        this.negated = negated;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
