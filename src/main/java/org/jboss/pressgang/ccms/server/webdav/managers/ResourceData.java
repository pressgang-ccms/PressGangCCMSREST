package org.jboss.pressgang.ccms.server.webdav.managers;

import javax.validation.constraints.NotNull;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A structure to represent data that was saved to the database, and the final database content.
 */
public class ResourceData {
    private final ResourceTypes resourceType;
    private final String client;
    private final Integer resId;

    public ResourceData(@NotNull final ResourceTypes resourceType, @NotNull final String client, @NotNull final Integer resId) {
        checkArgument(resourceType != null, "resourceType cannot be null");
        checkArgument(client != null, "client cannot be null");
        checkArgument(resId != null, "resId cannot be null");


        this.resourceType = resourceType;
        this.client = client;
        this.resId = resId;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof ResourceData)) {
            return false;
        }

        final ResourceData otherGetCacheData = (ResourceData) other;

        if (this.resourceType != otherGetCacheData.resourceType) {
            return false;
        }

        if (!this.client.equals(otherGetCacheData.client)) {
            return false;
        }

        if (!this.resId.equals(otherGetCacheData.resId)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int retValue = 0;

        retValue = 37 * retValue + resourceType.hashCode();
        retValue = 37 * retValue + client.hashCode();
        retValue = 37 * retValue + resId.hashCode();

        return retValue;
    }

    public ResourceTypes getResourceType() {
        return resourceType;
    }

    public String getClient() {
        return client;
    }

    public Integer getResId() {
        return resId;
    }
}
