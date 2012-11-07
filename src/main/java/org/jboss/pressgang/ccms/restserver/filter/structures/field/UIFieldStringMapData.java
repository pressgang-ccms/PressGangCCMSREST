package org.jboss.pressgang.ccms.restserver.filter.structures.field;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UIFieldStringMapData extends UIFieldMapDataBase<String, String> {
    private static final Logger log = LoggerFactory.getLogger(UIFieldStringMapData.class);

    public UIFieldStringMapData(final String name, final String description) {
        super(name, description);
    }

    @Override
    public Map<String, String> getData() {
        return this.data;
    }

    @Override
    public void setData(Map<String, String> data) {
        this.data = data;
    }

    @Override
    public void setData(String value) {
        try {
            final List<String> vars = Arrays.asList(value.split(","));

            this.data = new HashMap<String, String>();

            for (final String var : vars) {
                String[] dataVars = var.split("=", 2);

                data.put(dataVars[0], dataVars[1]);
            }
        } catch (final Exception ex) {
            // could not parse, so silently fail
            log.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", description, value);
        }
    }

    @Override
    public String toString() {
        if (data == null)
            return null;

        final StringBuilder retValue = new StringBuilder();

        for (final String key : data.keySet()) {
            if (retValue.length() > 0)
                retValue.append(",");

            retValue.append(key + "=" + data.get(key));
        }

        return retValue.toString();
    }
}
