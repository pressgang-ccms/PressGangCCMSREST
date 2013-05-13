package org.jboss.pressgang.ccms.restserver.webdav.resources;

import javax.annotation.Nullable;

/**
 * A wrapper that contains the HTTP return code, and the returned byte array.
 */
public class ByteArrayReturnValue {
    private final int statusCode;
    @Nullable
    private final byte[] value;

    public ByteArrayReturnValue(final int statusCode, @Nullable final byte[] value) {
        this.statusCode = statusCode;
        this.value = value;
    }

    public ByteArrayReturnValue(final int statusCode) {
        this.statusCode = statusCode;
        this.value = null;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Nullable
    public byte[] getValue() {
        return value;
    }
}
