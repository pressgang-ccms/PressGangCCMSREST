package org.jboss.pressgang.ccms.server.webdav.constants;

/**
 * Constants related to the WebDAV server.
 */
public final class WebDavConstants {
    /** The location of temporary files uploaded to the web server */
    public static final String TEMP_LOCATION = "/tmp/pressgang-webdav-temp";
    /** The length of time in seconds that a fixed resource will appear to be deleted */
    public static final int DELETE_WINDOW = 10 * 60;
    /** The length of time in seconds that a temp resource will be availble for */
    public static final int TEMP_WINDOW = 10 * 60;
    /** The header that we need to check to get the client ip */
    public static final String X_FORWARD_FOR_HEADER = "X-Forwarded-For";

    /**
     * private constructor to prevent instantiation.
     */
    private WebDavConstants() {

    }
}
