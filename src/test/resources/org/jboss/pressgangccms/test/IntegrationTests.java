package org.jboss.pressgangccms.test;

import java.util.Map;

import org.junit.Test;

import com.jayway.restassured.RestAssured;

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
			
			given()
				.param("expand", "{\"branches\":[{\"trunk\":{\"name\":\"tags\",\"showSize\":true}}]}")
			.expect()
				.statusCode(200)
			.when()
				.get(path);
		}
	}
}
