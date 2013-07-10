package org.jboss.pressgang.ccms.server.utils;

public class Constants {
    /**
     * The system property that defines the CORS Access-Control-Allow-Origin Header
     */
    public static final String CORS_ALLOW_ORIGIN_SYSTEM_PROPERTY = "topicIndex.CORS.allowOrigin";
    public static final String CORS_ALLOW_ORIGIN_HEADER = System.getProperty(Constants.CORS_ALLOW_ORIGIN_SYSTEM_PROPERTY, null);

    /**
     * A Topic ID that no topic should ever match
     */
    public static final String NULL_TOPIC_ID_STRING = "-1";

    /**
     * A Topic ID that no topic should ever match
     */
    public static final Integer NULL_TOPIC_ID = -1;

    /**
     * The base URL from which the REST interface can be accessed
     */
    public static final String BASE_REST_PATH = "/rest";

    /**
     * A header sent back with every request.
     */
    public static final String X_PRESSGANG_VERSION_HEADER = "X-PressGang-Version";
}
