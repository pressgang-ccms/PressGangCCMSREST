package org.jboss.pressgang.ccms.restserver.filter.structures.field;

public class UIFieldBooleanData extends UIFieldDataBase<Boolean> {

    public UIFieldBooleanData(final String name, final String description) {
        super(name, description);
    }

    @Override
    public Boolean getData() {
        return this.data;
    }

    @Override
    public void setData(final Boolean data) {
        this.data = data ? true : null;
    }

    @Override
    public void setData(final String value) {
        this.data = (value == null ? null : (Boolean.parseBoolean(value) ? true : null));
    }

    @Override
    public String toString() {
        return data == null ? "" : data.toString();
    }
}
