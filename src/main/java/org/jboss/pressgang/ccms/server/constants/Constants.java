package org.jboss.pressgang.ccms.server.constants;

public class Constants {
    /**
     * The number of min hashes to generate a topic signature with.
     */
    public static final int NUM_MIN_HASHES = 200;

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

}
