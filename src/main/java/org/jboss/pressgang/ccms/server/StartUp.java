package org.jboss.pressgang.ccms.server;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.jboss.pressgang.ccms.contentspec.buglinks.BugLinkStrategyFactory;
import org.jboss.pressgang.ccms.contentspec.enums.BugLinkType;
import org.jboss.pressgang.ccms.server.contentspec.TeiidBugLinkStrategy;

@Singleton
@Startup
public class StartUp {
    @PostConstruct
    public void init() {
        // Register the Teiid bug link helper
        BugLinkStrategyFactory.getInstance().registerStrategy(BugLinkType.BUGZILLA, 1, TeiidBugLinkStrategy.class,
                "https://bugzilla.redhat.com/");
    }
}
