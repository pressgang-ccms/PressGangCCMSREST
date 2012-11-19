package org.jboss.pressgang.ccms.restserver.rest;

import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author kamiller@redhat.com (Katie Miller)
 */
@RunWith(Arquillian.class)
public class CategoryIntegrationTest extends BaseArquillianIntegrationTest {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(CategoryIntegrationTest.class);

    @Test @OperateOnDeployment("restServer")
    public void shouldGetCategoryJsonpById() {
        // Given a request for category 1 JSONP with valid token authorization
        // When the request is made
        String result = given().header("Authorization", "Bearer access_token")
//                .expect().statusCode(200)
//                .when().get(getBaseTestUrl() + "/1/category/get/jsonp/1")
                .get(getBaseTestUrl() + "/1/category/get/jsonp/1")
                .andReturn().asString();
        log.info(result);
        // Then the expected JSONP should be returned
        assertThat(result.equals("willfail"), is(true));
    }
}
