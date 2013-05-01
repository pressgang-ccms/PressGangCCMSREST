package org.jboss.pressgang.ccms.restserver.rest;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author kamiller@redhat.com (Katie Miller)
 */
@RunWith(Arquillian.class)
public class AuthIntegrationTest extends BaseArquillianIntegrationTest {

    @Test
    @OperateOnDeployment("restServer")
    public void shouldGetResourceMatchingId() {
        // Given a request for category 1 JSON with valid token authorization
        // When the request is made
        // Then the expected JSON for category 1 should be returned
        given().header("Authorization", "Bearer access_token").expect().statusCode(200).and().expect().body("name",
                equalTo("Audiences")).and().expect().body("sort", equalTo(15)).when().get(getBaseTestUrl() + "/1/category/get/json/1");
    }

    @Test
    @OperateOnDeployment("restServer")
    public void shouldGetResourceMatchingIdWithJsonp() {
        // Given a JSONP request for category 1 with valid token authorization
        // When the request is made
        String result = given().header("Authorization", "Bearer access_token").expect().statusCode(200).when().get(
                getBaseTestUrl() + "/1/category/get/jsonp/1?callback=functionx").asString();
        // Then the callback function should be called with the expected JSON for category 1
        assertThat(result.contains("functionx"), is(true));
        String json = result.replace("functionx(", "");
        json = json.substring(0, json.length() - 1); // Remove trailing bracket
        assertEquals(1, from(json).getInt("id"));
        assertEquals("Audiences", from(json).getString("name"));
    }

    @Test
    @OperateOnDeployment("restServer")
    public void shouldGetExpandedResource() {
        // Given a request for all category JSON with valid token authorization
        // When the request is made
        // Then the expected JSON should be returned
        given().header("Authorization", "Bearer access_token").expect().statusCode(200).and().expect().body("items.item.name",
                hasItems("Audiences", "Concerns", "Technologies")).and().expect().body("items.item.sort", hasItems(15, 25, 20)).when().get(
                getBaseTestUrl() + "/1/categories/get/json/all?expand=" +
                        "{\"branches\":[{\"trunk\":{\"name\":\"categories\"}}]})");
    }

    @Test
    @OperateOnDeployment("restServer")
    public void shouldGetResourceByQuery() {
        // Given a request for JSON based on a query with valid token authorization
        // When the request is made
        // Then the expected JSON should be returned
        given().header("Authorization", "Bearer access_token").expect().statusCode(200).and().expect().body("size",
                equalTo(1)).and().expect().body("items.item.name", hasItem("Technologies")).and().expect().body("items.item.sort",
                hasItem(20)).when().get(getBaseTestUrl() + "/1/categories/get/json/query;catName=Technologies?expand=" +
                "{\"branches\":[{\"trunk\":{\"name\":\"categories\"}}]})");
    }

    @Test
    @OperateOnDeployment("restServer")
    public void shouldDeleteResourcesByIds() {
        // Given a request to delete resources by multiple ids with valid token authorization
        // When the request is made
        given().header("Authorization", "Bearer access_token").expect().statusCode(200).when().delete(
                getBaseTestUrl() + "/1/tags/delete/json/ids;1;3;");
        // Then then the resources should be deleted
        given().header("Authorization", "Bearer access_token").expect().body("size", equalTo(1)).and().expect().body("items.item.name",
                hasItem("Concept")).and().expect().body("items.item.name", not(hasItem("Task"))).and().expect().body("items.item.name",
                not(hasItem("Reference"))).when().get(getBaseTestUrl() + "/1/tags/get/json/all?expand=" +
                "{\"branches\":[{\"trunk\":{\"name\":\"tags\"}}]})");
    }

    @Test
    @OperateOnDeployment("restServer")
    public void shouldFailToGetResourceWhenTokenInvalid() {
        // Given a request with invalid token authorization
        // When the request is made
        // Then an authorization error should be returned
        given().header("Authorization", "Bearer fake_access_token").expect().statusCode(401).when().get(
                getBaseTestUrl() + "/1/category/get/json/1");
    }

    @Test
    @OperateOnDeployment("restServer")
    public void shouldFailToGetResourceWhenIncorrectScopeForEndpoint() {
        // Given a request with otherwise valid token authorization that doesn't include that endpoint's scope
        // When the request is made
        // Then a forbidden error should be returned
        given().header("Authorization", "Bearer access_token").expect().statusCode(403).when().delete(
                getBaseTestUrl() + "/1/category/delete/json/1");
    }

    @Test
    @OperateOnDeployment("restServer")
    public void shouldFailToGetResourceWhenHttpMethodIncorrect() {
        // Given a request with valid token authorization to a valid endpoint URL with the wrong HTTP method
        // When the request is made
        // Then a bad request error should be returned
        given().header("Authorization", "Bearer access_token").expect().statusCode(400).when().put(
                getBaseTestUrl() + "/1/category/get/json/1");
    }

    @Test
    @OperateOnDeployment("restServer")
    public void shouldFailToGetResourceWhenTokenExpired() {
        // Given a request with otherwise valid token authorization that is past its expiry
        // When the request is made
        // Then an authorization error should be returned
        given().header("Authorization", "Bearer expired_access_token").expect().statusCode(401).when().get(
                getBaseTestUrl() + "/1/category/get/json/1");
    }

    @Test
    @OperateOnDeployment("restServer")
    public void shouldFailToGetResourceWhenTokenNonCurrent() {
        // Given a request with otherwise valid token authorization that has been marked as non-current
        // When the request is made
        // Then an authorization error should be returned
        given().header("Authorization", "Bearer noncurrent_access_token").expect().statusCode(401).when().get(
                getBaseTestUrl() + "/1/category/get/json/1");
    }

    @Test
    @OperateOnDeployment("restServer")
    public void shouldFailToGetResourceWhenEndpointNotMapped() {
        // Given a request with otherwise valid token authorization to an existing endpoint not mapped to any scope
        // When the request is made
        // Then a bad request error should be returned
        given().header("Authorization", "Bearer access_token").expect().statusCode(400).when().get(getBaseTestUrl() + "/1/user/get/json/1");
    }

}
