package org.jboss.pressgang.ccms.server.webdav.resources;

import net.java.dev.webdav.jaxrs.xml.elements.MultiStatus;

import javax.annotation.Nullable;

/**
 * A wrapper that contains the HTTP return code, and the returned MultiStatus.
 */
public final class MultiStatusReturnValue {
    private final int statusCode;
    @Nullable
    private final MultiStatus value;

    public MultiStatusReturnValue(final int statusCode, @Nullable final MultiStatus value) {
        this.statusCode = statusCode;
        this.value = value;
    }
    public MultiStatusReturnValue(final int statusCode) {
        this.statusCode = statusCode;
        this.value = null;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Nullable
    public MultiStatus getValue() {
        return value;
    }
}
