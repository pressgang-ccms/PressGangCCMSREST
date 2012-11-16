package org.jboss.pressgang.ccms.restserver.rest;

import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author kamiller@redhat.com (Katie Miller)
 */
@RunWith(Arquillian.class)
public class BasicIntegrationTest extends BaseArquillianIntegrationTest {

    @Test @OperateOnDeployment("restServer")
    public void should() {
        assertThat(true, is(true));
        // Given
        // When
        // Then
    }
}
