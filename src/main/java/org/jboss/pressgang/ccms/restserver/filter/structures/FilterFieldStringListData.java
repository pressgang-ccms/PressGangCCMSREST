package org.jboss.pressgang.ccms.restserver.filter.structures;

import java.util.Arrays;
import java.util.List;

import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterFieldStringListData extends FilterFieldListDataBase<String> {
    private static final Logger log = LoggerFactory.getLogger(FilterFieldStringListData.class);

    public FilterFieldStringListData(final String name, final String description) {
        super(name, description);
    }

    @Override
    public List<String> getData() {
        return this.data;
    }

    @Override
    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public void setData(String value) {
        try {
            this.data = Arrays.asList(value.split(","));
        } catch (final Exception ex) {
            // could not parse, so silently fail
            log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", description, value);
        }
    }

    @Override
    public String toString() {
        return data == null ? null : CollectionUtilities.toSeperatedString(data, ",");
    }
}
