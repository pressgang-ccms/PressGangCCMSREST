package org.jboss.pressgang.ccms.server.filter.structures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterFieldIntegerData extends FilterFieldDataBase<Integer> {
    private static final Logger log = LoggerFactory.getLogger(FilterFieldIntegerData.class);

    public FilterFieldIntegerData(final String name, final String description) {
        super(name, description);
    }

    @Override
    public Integer getData() {
        return this.data;
    }

    @Override
    public void setData(final Integer data) {
        this.data = data;
    }

    @Override
    public void setData(final String value) {
        try {
            this.data = (value == null ? null : Integer.parseInt(value));
        } catch (final NumberFormatException ex) {
            // could not parse integer, so silently fail
            log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", description, value);
        }
    }

    @Override
    public String toString() {
        return data == null ? null : data.toString();
    }
}
