/*
  Copyright 2011-2014 Red Hat, Inc

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

package org.jboss.pressgang.ccms.server.rest;

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

    @Deployment(name = "server")
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "testserver.war").addPackages(true, "org.jboss.pressgang.ccms.server").addClass(
                EntityManager.class).addAsResource("test-persistence.xml", "META-INF/persistence.xml").addAsResource(
                "test-resourceserver.properties", "META-INF/resourceserver.properties").addAsResource("test-import.sql",
                "import.sql").addAsWebInfResource("testserver-ds.xml").addAsWebInfResource(EmptyAsset.INSTANCE,
                "beans.xml").addAsWebInfResource("test-web.xml", "web.xml").addAsWebInfResource("test-jboss-deployment-structure.xml",
                "jboss-deployment-structure.xml").addAsLibraries(
                DependencyResolvers.use(MavenDependencyResolver.class).includeDependenciesFromPom(
                        "src/test/resources/general/test-pom.xml").resolveAs(JavaArchive.class));
    }

    public static String getBaseTestUrl() {
        return "https://localhost:8443/testserver/rest";
    }
}
