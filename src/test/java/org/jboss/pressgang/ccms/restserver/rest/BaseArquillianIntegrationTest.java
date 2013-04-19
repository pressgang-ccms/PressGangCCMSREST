package org.jboss.pressgang.ccms.restserver.rest;

import javax.persistence.EntityManager;
import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

/**
 * @author kamiller@redhat.com (Katie Miller)
 */
public class BaseArquillianIntegrationTest {

    @Deployment(name = "authServer")
    public static WebArchive createAuthServerDeployment() {
        return ShrinkWrap.create(ZipImporter.class, "testauthserver.war").importFrom(new File("target/OAuth2AuthServer.war")).as(
                WebArchive.class);
    }

    @Deployment(name = "restServer")
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "testrestserver.war").addPackages(true, "org.jboss.pressgang.ccms.restserver").addClass(
                EntityManager.class).addAsResource("test-persistence.xml", "META-INF/persistence.xml").addAsResource(
                "test-resourceserver.properties", "META-INF/resourceserver.properties").addAsResource("test-import.sql",
                "import.sql").addAsWebInfResource("testrestserver-ds.xml").addAsWebInfResource(EmptyAsset.INSTANCE,
                "beans.xml").addAsWebInfResource("test-web.xml", "web.xml").addAsWebInfResource("test-jboss-deployment-structure.xml",
                "jboss-deployment-structure.xml").addAsLibraries(
                DependencyResolvers.use(MavenDependencyResolver.class).includeDependenciesFromPom(
                        "src/test/resources/general/test-pom.xml").resolveAs(JavaArchive.class));
    }

    public static String getBaseTestUrl() {
        return "https://localhost:8443/testrestserver/rest";
    }
}
