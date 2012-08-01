package org.jboss.pressgangccms.test;

public interface TestBase
{
	/** The environment variable that holds the REST password */
	String RESTPASS = "REST_PASSWORD";
	/** The environment variable that holds the REST username */
	String RESTUSER = "REST_USERNAME";
	/** JSON content type */
	String JSON_CONTENT_TYPE = "application/json";
	/** HTTP OK Response Code */
	int HTTP_OK = 200;
	/** A marker used to replace with the ID */
	String ID_MARKER = "#ID#";
}
