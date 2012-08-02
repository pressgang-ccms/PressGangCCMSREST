package org.jboss.pressgangccms.test;

/**
 * This interface contains some shared constants
 * @author Matthew Casperson
 */
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
	/** HTTP Custom Error Response Code */
	int HTTP_CUSTOM_ERROR = 500;
	/** A marker used to replace with the ID */
	String ID_MARKER = "#ID#";
}
