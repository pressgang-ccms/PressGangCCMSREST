/*
  Copyright 2011-2014 Red Hat

  This file is part of PressGang CCMS.

  PressGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PressGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PressGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

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
import org.jboss.pressgang.ccms.server.contentspec.TeiidBugzillaBugLinkStrategy;

@Singleton
@Startup
public class StartUp {

    //@Inject
    //private ProcessManager processManager;

    @PostConstruct
    public void init() throws Exception {
        final String pressGangConfigurationDir = getPressGangConfigurationDirectory();

        // Load the application config
        final ApplicationConfig appConfig = ApplicationConfig.getInstance();
        appConfig.load(new File(pressGangConfigurationDir + ApplicationConfig.FILENAME));
        if (!appConfig.validate()) {
            throw new ConfigurationException(
                    "The application config file at " + pressGangConfigurationDir + ApplicationConfig.FILENAME + " " +
                            "failed to validate.");
        }

        // Load the entities config
        final EntitiesConfig entitiesConfig = EntitiesConfig.getInstance();
        entitiesConfig.load(new File(pressGangConfigurationDir + EntitiesConfig.FILENAME));
        if (!entitiesConfig.validate()) {
            throw new ConfigurationException(
                    "The entities config file at " + pressGangConfigurationDir + EntitiesConfig.FILENAME + " failed to validate.");
        }

        if (appConfig.getBugzillaTeiid()) {
            // Register the Teiid bug link helper
            BugLinkStrategyFactory.getInstance().registerStrategy(BugLinkType.BUGZILLA, 1, TeiidBugzillaBugLinkStrategy.class,
                    appConfig.getBugzillaUrl());
        }

        // Make a call to process manager so it's post construct will be called
        //processManager.toString();
    }

    protected String getPressGangConfigurationDirectory() {
        final String pressGangConfigurationDir = System.getProperties().getProperty("pressgang.config.dir");
        final String configurationDir;
        if (pressGangConfigurationDir == null) {
            // If the pressgang config directory isn't set then try and load from the jboss config directory
            final String jbossConfigurationDir = System.getProperties().getProperty("jboss.server.config.dir");
            if (jbossConfigurationDir == null) {
                // We aren't running on a jboss install, so just use the current working directory
                configurationDir = System.getProperty("user.dir");
            } else {
                configurationDir = jbossConfigurationDir + File.separator + "pressgang";
            }
        } else {
            configurationDir = pressGangConfigurationDir;
        }
        return configurationDir.endsWith(File.separator) ? configurationDir : (configurationDir + File.separator);
    }
}
