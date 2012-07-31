package org.jboss.pressgangccms.test;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*; 

public class IntegrationTests
{
	private static final String RESTPASS = "REST_PASSWORD";
	private static final String RESTUSER = "REST_USERNAME";
	
	//@Test
	public void getRESTInfo()
	{
		RestAssured.reset();
		
		expect().statusCode(200).when().get("http://devrest-pressgangccms.rhcloud.com");
	}
	
	@Test
	public void getTags()
	{
		final String path = "/1/tags/get/json/all";
		
		final Map<String, String> env = System.getenv();
		if (env.containsKey(RESTPASS) && env.containsKey(RESTUSER))
		{
			final String restUser = env.get(RESTUSER);
			final String restPass = env.get(RESTPASS);
			
			RestAssured.reset();
			RestAssured.baseURI = "http://devrest-pressgangccms.rhcloud.com";
			RestAssured.authentication = basic(restUser, restPass);
			RestAssured.urlEncodingEnabled = true;
			RestAssured.port = 80;
			
			/* Test the response */
			final Response res = get(path);
			assertEquals(200, res.getStatusCode());
			
			final String json = res.asString();
			final JsonPath jp = new JsonPath(json);
						
			assertEquals(jp.getInt("size"), 0);
			assertNull(jp.get("endExpandIndex"));
			assertNull(jp.get("startExpandIndex"));
			assertNull(jp.get("items"));
			assertTrue("tags".equals(jp.getString("expand")));
		}
	}
	
	@Test
	public void getTagsExpanded()
	{
		final String path = "/1/tags/get/json/all";
		
		final Map<String, String> env = System.getenv();
		if (env.containsKey(RESTPASS) && env.containsKey(RESTUSER))
		{
			final String restUser = env.get(RESTUSER);
			final String restPass = env.get(RESTPASS);
			
			RestAssured.reset();
			RestAssured.baseURI = "http://devrest-pressgangccms.rhcloud.com";
			RestAssured.authentication = basic(restUser, restPass);
			RestAssured.urlEncodingEnabled = true;
			RestAssured.port = 80;
			
			/* Test the response */
			final Response res = given().param("expand", "{\"branches\":[{\"trunk\":{\"name\":\"tags\",\"showSize\":true}}]}").get(path);
			assertEquals(200, res.getStatusCode());
			
			final String json = res.asString();
			final JsonPath jp = new JsonPath(json);
						
			assertEquals(jp.getInt("endExpandIndex"), jp.getInt("size"));
			assertEquals(jp.getInt("startExpandIndex"), 0);
			assertTrue("tags".equals(jp.getString("expand")));
			
			final List<Map> items = jp.getList("items", Map.class);
			
			assertNotNull(items);			
			assertTrue(items.size() == jp.getInt("size"));
			
			for (final Map item : items)
			{
				assertNotNull(item.get("id"));
				assertTrue(item.get("id").toString().matches("\\d+"));
				assertNull(item.get("revision"));
				assertNull(item.get("configuredParameters"));
			}
		}
	}
}
