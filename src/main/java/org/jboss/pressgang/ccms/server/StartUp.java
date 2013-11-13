package org.jboss.pressgang.ccms.server;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.naming.ConfigurationException;
import java.io.File;

import org.jboss.pressgang.ccms.contentspec.buglinks.BugLinkStrategyFactory;
import org.jboss.pressgang.ccms.contentspec.enums.BugLinkType;
import org.jboss.pressgang.ccms.model.config.ApplicationConfig;
import org.jboss.pressgang.ccms.model.config.EntitiesConfig;
import org.jboss.pressgang.ccms.server.contentspec.TeiidBugLinkStrategy;
import org.jboss.pressgang.ccms.utils.constants.CommonConstants;

@Singleton
@Startup
public class StartUp {

    @PostConstruct
    public void init() throws Exception {
        final String pressGangConfigurationDir = getPressGangConfigurationDirectory();

        // Load the application config
        final ApplicationConfig appConfig = ApplicationConfig.getInstance();
        appConfig.load(new File(pressGangConfigurationDir + ApplicationConfig.FILENAME));
        // Set the default locale system property
        System.setProperty(CommonConstants.DEFAULT_LOCALE_PROPERTY, appConfig.getDefaultLocale());

        // Load the entities config
        final EntitiesConfig entitiesConfig = EntitiesConfig.getInstance();
        entitiesConfig.load(new File(pressGangConfigurationDir + EntitiesConfig.FILENAME));
        if (!entitiesConfig.validate()) {
            throw new ConfigurationException();
        }

        if (appConfig.getBugzillaTeiid()) {
            // Register the Teiid bug link helper
            BugLinkStrategyFactory.getInstance().registerStrategy(BugLinkType.BUGZILLA, 1, TeiidBugLinkStrategy.class,
                    appConfig.getBugzillaUrl());
        }
    }

    protected String getPressGangConfigurationDirectory() {
        final String pressGangConfigurationDir = System.getProperties().getProperty("pressgang.config.dir");
        final String configurationDir;
        if (pressGangConfigurationDir == null) {
            // If the pressgang config directory isn't set then try and load from the jboss config directory
            final String jbossConfigurationDir = System.getProperties().getProperty("jboss.server.config.dir");
            configurationDir = jbossConfigurationDir + File.separator + "pressgang";
        } else {
            configurationDir = pressGangConfigurationDir;
        }
        return configurationDir.endsWith(File.separator) ? configurationDir : (configurationDir + File.separator);
    }
}
