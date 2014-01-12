package org.jboss.pressgang.ccms.server;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;
import java.sql.SQLException;

import liquibase.integration.cdi.CDILiquibaseConfig;
import liquibase.integration.cdi.annotations.LiquibaseType;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 * A Simple CDI Producer to configure the CDI Liquibase integration
 */
public class LiquibaseProducer {

    @Resource(lookup = "java:/PressGangCCMSDatasource")
    private DataSource myDataSource;

    @Produces
    @LiquibaseType
    public CDILiquibaseConfig createConfig() {
        final CDILiquibaseConfig config = new CDILiquibaseConfig();
        config.setChangeLog("db/db.changelog.xml");
        return config;
    }

    @Produces
    @LiquibaseType
    public DataSource createDataSource() throws SQLException {
        return myDataSource;
    }

    @Produces
    @LiquibaseType
    public liquibase.resource.ResourceAccessor create() {
        return new ClassLoaderResourceAccessor(getClass().getClassLoader());
    }

}
