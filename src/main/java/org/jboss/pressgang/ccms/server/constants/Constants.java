package org.jboss.pressgang.ccms.server.constants;

public class Constants {

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
     * Query strings passed in as path segments always use this as the prefix
     */
    public static final String QUERY_PATHSEGMENT_PREFIX = "query";
    /**
     * The length of a string that contains a SHA 256 hash
     */
    public static final int SHA_256_HASH_LENGTH = 64;
}
