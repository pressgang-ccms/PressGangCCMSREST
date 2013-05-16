package org.jboss.pressgang.ccms.server.webdav.managers;

import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.jboss.pressgang.ccms.server.webdav.constants.WebDavConstants;

/**
 * There are a number of cases where working with database fields is not the same as working with files. This class
 * provides work around for these edge cases.
 *
 * Database fields can not be deleted, but when they are exposed as files, some applications expect to be able to delete
 * them (e.g. Kate will delete the file it is editing, and then check to make sure it is deleted before saving any changes).
 * This manager simply keeps a track of delete requests, and shows a file as being deleted for a short period of time,
 * or until it is "created" again.
 *
 * Saving content will quite often update the content as part of the save process. e.g. Topic's XML will be formatted
 * and the title changed upon save. This results in annoying messages in text editors about updated files, and usually
 * a second or two after the save was completed (with plenty of time to start typing again; changes that will be lost when the
 * file is reloaded).
 *
 * The following workaround has been implemented:
 *
 * 1.   Text editor saves XML
 * 2.   WebDAV saves XML as formatted text in the database
 * 3.   Text editor gets XML
 * 4.   a) If the text in the database is the same as the result of the save in step 1, the original, unformatted text is returned.
 *         The text editor does not see that the XML was actually formatted when it was saved.
 *      b) If the text in the database is different, it is returned as is.
 *
 */
@ApplicationScoped
public class CompatibilityManager {

    /**
     * TODO: make an object to represent a delete request
     * A resource type mapped to a remote address mapped to an id mapped to a deletion time.
     */
    final Map<ResourceData, Calendar> deletedResources = new HashMap<ResourceData, Calendar>();
    final Map<ResourceData, Calendar> createdResources = new HashMap<ResourceData, Calendar>();
    final Map<ResourceData, DataCache> databaseCache = new HashMap<ResourceData, DataCache>();

    synchronized public boolean isDeleted(@NotNull final ResourceTypes resourceType, @NotNull final String remoteAddress, @NotNull final Integer id) {

        final ResourceData resourceData = new ResourceData(resourceType, remoteAddress, id);

        if (deletedResources.containsKey(resourceData)) {

            final Calendar deletionDate = deletedResources.get(resourceData);
            final Calendar window = Calendar.getInstance();
            window.add(Calendar.SECOND, -WebDavConstants.DELETE_WINDOW);

            if (deletionDate.before(window)) {
                deletedResources.remove(resourceData);
                return false;
            }

            return true;

        }

        return false;
    }

    synchronized public void delete(@NotNull final ResourceTypes resourceType, @NotNull final String remoteAddress,
            @NotNull final Integer id) {
        final ResourceData resourceData = new ResourceData(resourceType, remoteAddress, id);
        deletedResources.put(resourceData, Calendar.getInstance());
        databaseCache.remove(resourceData);
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

        final ResourceData resourceData = new ResourceData(resourceType, remoteAddress, id);

        if (createdResources.containsKey(resourceData)) {
            createdResources.get(resourceData);
        }

        return null;
    }

    synchronized public void create(@NotNull final ResourceTypes resourceType, @NotNull final String remoteAddress,
            @NotNull final Integer id) {

        final ResourceData resourceData = new ResourceData(resourceType, remoteAddress, id);

        if (deletedResources.containsKey(resourceData)) {
            deletedResources.remove(resourceData);
        }

        final Calendar now = Calendar.getInstance();
        now.add(1, Calendar.SECOND);
        createdResources.put(resourceData, now);
    }

    synchronized public void put(@NotNull final ResourceTypes resourceType, @NotNull final String remoteAddress, @NotNull final Integer id, @NotNull final byte[] databaseData, @NotNull final byte[] originalData) {
        final ResourceData resourceData = new ResourceData(resourceType, remoteAddress, id);
        final DataCache dataCache = new DataCache(originalData, databaseData, Calendar.getInstance());
        databaseCache.put(resourceData, dataCache);
    }

    synchronized public byte[] get(@NotNull final ResourceTypes resourceType, @NotNull final String remoteAddress, @NotNull final Integer id, @NotNull final byte[] databaseData) {

        final ResourceData resourceData = new ResourceData(resourceType, remoteAddress, id);

        if (!databaseCache.containsKey(resourceData)) {
            return databaseData;
        }

        final DataCache dataCache = databaseCache.get(resourceData);

        /*
            If the database data does not equal what was saved in response to the original data, clear the cache
            and return the database text.
         */
        if (!Arrays.equals(databaseData, dataCache.getDatabase())) {
            databaseCache.remove(resourceData);
            return databaseData;
        }

        /*
            If the cache has expired, clear the cache and return the database text.
         */
        final Calendar window = Calendar.getInstance();
        window.add(Calendar.SECOND, -WebDavConstants.DELETE_WINDOW);

        if (dataCache.getTime().before(window)) {
            databaseCache.remove(resourceData);
            return databaseData;
        }

        /*
            If we have arrived at this point the client has requested database text that is
            equal to the text created by a write by this client.

            In this situation we say that the text in the database is equivalent to the
            text that was submitted for saving, even if there was some reformatting done on
            what was submitted in order to generate what was saved.
         */
        return dataCache.getOriginal();
    }

}
