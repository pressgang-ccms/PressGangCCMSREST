package org.jboss.pressgangccms.test;

import java.util.Map;

import org.junit.Test;
import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*; 

public class IntegrationTests
{
	private static final String RESTPASS = "REST_PASSWORD";
	private static final String RESTUSER = "REST_USERNAME";
	
	@Test
	public void getRESTInfo()
	{
		expect().statusCode(200).when().get("http://devrest-pressgangccms.rhcloud.com");
	}
	
	@Test
	public void getTags()
	{
		final Map<String, String> env = System.getenv();
		if (env.containsKey(RESTPASS) && env.containsKey(RESTUSER))
		{
			final String restUser = env.get(RESTUSER);
			final String restPass = env.get(RESTPASS);
			
			given().auth().basic(restUser, restPass).expect().statusCode(200).when().get("http://devrest-pressgangccms.rhcloud.com/1/tags/get/json/all?expand=%7B%22branches%22%3A%5B%7B%22trunk%22%3A%7B%22name%22%3A%20%22tags%22%2C%22showSize%22%3Atrue%7D%7D%5D%7D");
		}
	}
}
