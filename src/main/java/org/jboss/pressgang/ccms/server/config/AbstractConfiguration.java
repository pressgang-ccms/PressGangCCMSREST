package org.jboss.pressgang.ccms.server.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public abstract class AbstractConfiguration {
    private PropertiesConfiguration configuration = new PropertiesConfiguration();

    protected AbstractConfiguration() {
        configuration.getLayout().setForceSingleLine(true);
    }

    protected PropertiesConfiguration getConfiguration() {
        return configuration;
    }

    public void load(final File file) throws ConfigurationException {
        configuration.load(file);
        configuration.setBasePath(file.getParent());
        configuration.setFileName(file.getName());
    }

    public void save() throws ConfigurationException {
        configuration.save();
    }

    public Object getValue(final String key) {
        return configuration.getProperty(key);
    }

    public boolean containsKey(final String key) {
        return configuration.containsKey(key);
    }

    public void removeProperty(final String key) {
        getConfiguration().clearProperty(key);
    }

    public List<String> getKeys() {
        final List<String> keys = new ArrayList<String>();
        final Iterator<String> it = configuration.getKeys();
        while (it.hasNext()) {
            keys.add(it.next());
        }
        return keys;
    }
}
