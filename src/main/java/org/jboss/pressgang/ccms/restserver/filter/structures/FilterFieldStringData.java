package org.jboss.pressgang.ccms.restserver.filter.structures;

import java.util.List;

import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;

public class FilterFieldStringData extends FilterFieldDataBase<String> {
    public FilterFieldStringData(final String name, final String description) {
        super(name, description);
    }

    @Override
    public String getData() {
        return this.data;
    }

    @Override
    public void setData(final String data) {
        this.data = data;
    }

    public <T> void setData(final List<T> data) throws Exception {
        this.data = CollectionUtilities.toSeperatedString(data, ",");
    }

    @Override
    public String toString() {
        return data == null ? null : data.toString();
    }
}
