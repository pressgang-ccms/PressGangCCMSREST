package org.jboss.pressgang.ccms.server.webdav.managers;

import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.jboss.pressgang.ccms.server.webdav.constants.WebDavConstants;

/**
 * Database fields can not be deleted, but when they are exposed as files, some applications expect to be able to delete
 * them (e.g. Kate will delete the file it is editing, and then check to make sure it is deleted before saving any changes).
 * This manager simply keeps a track of delete requests, and shows a file as being deleted for a short period of time,
 * or until it is "created" again.
 */
@ApplicationScoped
public class DeleteManager {

    /**
     * TODO: make an object to represent a delete request
     * A resource type mapped to a remote address mapped to an id mapped to a deletion time.
     */
    final Map<ResourceTypes, HashMap<String, HashMap<Integer, Calendar>>> deletedResources = new HashMap<ResourceTypes, HashMap<String,
            HashMap<Integer, Calendar>>>();
    final Map<ResourceTypes, HashMap<String, HashMap<Integer, Calendar>>> createdResources = new HashMap<ResourceTypes, HashMap<String,
            HashMap<Integer, Calendar>>>();

    synchronized public boolean isDeleted(@NotNull final ResourceTypes resourceType, @NotNull final String remoteAddress,
            @NotNull final Integer id) {
        if (deletedResources.containsKey(resourceType)) {
            final HashMap<String, HashMap<Integer, Calendar>> specificDeletedResources = deletedResources.get(resourceType);

            if (specificDeletedResources.containsKey(remoteAddress)) {
                if (specificDeletedResources.get(remoteAddress).containsKey(id)) {
                    final Calendar deletionDate = specificDeletedResources.get(remoteAddress).get(id);
                    final Calendar window = Calendar.getInstance();
                    window.add(Calendar.SECOND, -WebDavConstants.DELETE_WINDOW);

                    if (deletionDate.before(window)) {
                        specificDeletedResources.remove(id);
                        return false;
                    }

                    return true;
                }
            }
        }

        return false;
    }

    synchronized public void delete(@NotNull final ResourceTypes resourceType, @NotNull final String remoteAddress,
            @NotNull final Integer id) {
        if (!deletedResources.containsKey(resourceType)) {
            deletedResources.put(resourceType, new HashMap<String, HashMap<Integer, Calendar>>());
        }

        if (!deletedResources.get(resourceType).containsKey(remoteAddress)) {
            deletedResources.get(resourceType).put(remoteAddress, new HashMap<Integer, Calendar>());
        }

        deletedResources.get(resourceType).get(remoteAddress).put(id, Calendar.getInstance());
    }

    /**
     * Deleted files that are recreated with an empty put (e.g. where they are touched) need to have an updated
     * last modified date to invalidate the clients cache.
     *
     * @return The date that this file was recreated, or null if it has not been recreated.
     */
    @Nullable
    synchronized public Calendar lastCreatedDate(@NotNull final ResourceTypes resourceType, @NotNull final String remoteAddress,
            @NotNull final Integer id) {
        if (createdResources.containsKey(resourceType) &&
                createdResources.get(resourceType).containsKey(remoteAddress) &&
                createdResources.get(resourceType).get(remoteAddress).containsKey(id)) {
            createdResources.get(resourceType).get(remoteAddress).get(id);
        }

        return null;
    }

    synchronized public void create(@NotNull final ResourceTypes resourceType, @NotNull final String remoteAddress,
            @NotNull final Integer id) {
        if (deletedResources.containsKey(resourceType) &&
                deletedResources.get(resourceType).containsKey(remoteAddress) &&
                deletedResources.get(resourceType).get(remoteAddress).containsKey(id)) {
            deletedResources.get(resourceType).get(remoteAddress).remove(id);
        }

        if (!createdResources.containsKey(resourceType)) {
            createdResources.put(resourceType, new HashMap<String, HashMap<Integer, Calendar>>());
        }

        if (!createdResources.get(resourceType).containsKey(remoteAddress)) {
            createdResources.get(resourceType).put(remoteAddress, new HashMap<Integer, Calendar>());
        }

        final Calendar now = Calendar.getInstance();
        now.add(1, Calendar.SECOND);
        createdResources.get(resourceType).get(remoteAddress).put(id, now);
    }

}
