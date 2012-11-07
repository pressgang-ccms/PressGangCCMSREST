package org.jboss.pressgang.ccms.restserver.filter.structures.field;

public abstract class UIFieldDataBase<T> {
    /** The data stored within this UIField */
    protected T data = null;
    /** The name of the data field */
    protected String name = "";
    /** The description */
    protected String description = "";

    public UIFieldDataBase(final String name, final String description) {
        this.name = name;
        this.description = description;
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

    public abstract T getData();

    public abstract void setData(T data);

    public abstract void setData(String value);
}
