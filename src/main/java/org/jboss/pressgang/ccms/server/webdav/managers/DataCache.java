package org.jboss.pressgang.ccms.server.webdav.managers;

import java.util.Calendar;

/**
 * Represents cached data used to send the saved text back to a client, even if the database text was reformatted.
 */
public class DataCache {
    private final byte[] original;
    private final byte[] database;
    private final Calendar time;

    public DataCache(final byte[] original, final byte[] database, final Calendar time) {
        this.original = original;
        this.database = database;
        this.time = time;
    }

    public byte[] getOriginal() {
        return original;
    }

    public byte[] getDatabase() {
        return database;
    }

    public Calendar getTime() {
        return time;
    }
}
