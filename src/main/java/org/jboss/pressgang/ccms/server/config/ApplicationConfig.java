package org.jboss.pressgang.ccms.server.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;

public class ApplicationConfig extends AbstractConfiguration {
    public static final String FILENAME = "application.properties";
    private static final String KEY_DEFAULT_LOCALE = "locale.default";
    private static final String KEY_LOCALES = "locales";
    private static final String KEY_UI_URL = "ui.url";
    private static final String KEY_DOCBOOK_TEMPLATE_IDS = "docbook.template.ids";
    private static final String KEY_DOCBUILDER_URL = "docbuilder.url";
    private static final String KEY_BUGZILLA_TEIID = "bugzilla.teiid";
    private static final String KEY_BUGZILLA_URL = "bugzilla.url";

    private static final List<String> RESERVED_KEYS = Arrays.asList(
            KEY_BUGZILLA_TEIID,
            KEY_BUGZILLA_URL,
            KEY_DEFAULT_LOCALE,
            KEY_DOCBOOK_TEMPLATE_IDS,
            KEY_DOCBUILDER_URL,
            KEY_LOCALES,
            KEY_UI_URL
    );

    private static ApplicationConfig INSTANCE = new ApplicationConfig();

    public static ApplicationConfig getInstance() {
        return INSTANCE;
    }

    private ApplicationConfig() {
    }

    public String getDefaultLocale() {
        return getConfiguration().getString(KEY_DEFAULT_LOCALE, "en-US");
    }

    public void setDefaultLocale(final String defaultLocale) {
        getConfiguration().setProperty(KEY_DEFAULT_LOCALE, defaultLocale);
    }

    public List<String> getLocales() {
        final List<Object> list = getConfiguration().getList(KEY_LOCALES);
        final List<String> retValue = new ArrayList<String>();

        for (final Object o : list) {
            retValue.add(o.toString());
        }

        return retValue;
    }

    public void setLocales(final List<String> locales) {
        getConfiguration().setProperty(KEY_LOCALES, locales);
    }

    public String getUIUrl() {
        return getConfiguration().getString(KEY_UI_URL);
    }

    public void setUIUrl(final String uiUrl){
        getConfiguration().setProperty(KEY_UI_URL, uiUrl);
    }

    public List<Integer> getDocBookTemplateStringConstantIds() {
        final List<Object> list = getConfiguration().getList(KEY_DOCBOOK_TEMPLATE_IDS);
        final List<Integer> retValue = new ArrayList<Integer>();

        for (final Object o : list) {
            retValue.add(Integer.parseInt((String) o));
        }

        return retValue;
    }

    public void setDocBookTemplateStringConstantIds(final List<Integer> docbookTemplateIds) {
        getConfiguration().setProperty(KEY_DOCBOOK_TEMPLATE_IDS, docbookTemplateIds);
    }

    public String getDocBuilderUrl() {
        return getConfiguration().getString(KEY_DOCBUILDER_URL);
    }

    public void setDocBuilderUrl(final String docBuilderUrl) {
        getConfiguration().setProperty(KEY_DOCBUILDER_URL, docBuilderUrl);
    }

    public Boolean getBugzillaTeiid() {
        return getConfiguration().getBoolean(KEY_BUGZILLA_TEIID, false);
    }

    public void setBugzillaTeiid(final Boolean useTeiid) {
        getConfiguration().setProperty(KEY_BUGZILLA_TEIID, useTeiid);
    }

    public String getBugzillaUrl() {
        return getConfiguration().getString(KEY_BUGZILLA_URL);
    }

    public void addUndefinedProperty(final String key, final String value) throws ConfigurationException {
        if (RESERVED_KEYS.contains(key)) {
            throw new ConfigurationException("\"" + key + "\" is already defined.");
        } else {
            getConfiguration().setProperty(key, value);
        }
    }

    public Map<String, String> getUndefinedProperties() {
        final Map<String, String> undefinedProperties = new HashMap<String, String>();
        for (final String key : getKeys()) {
            if (!RESERVED_KEYS.contains(key)) {
                final Object value = getConfiguration().getProperty(key);
                if (value instanceof List) {
                    final String joinedValue = CollectionUtilities.toSeperatedString((List<Object>) value,
                            getConfiguration().getListDelimiter() + "");
                    undefinedProperties.put(key, joinedValue);
                } else {
                    undefinedProperties.put(key, value.toString());
                }
            }
        }
        return undefinedProperties;
    }
}
