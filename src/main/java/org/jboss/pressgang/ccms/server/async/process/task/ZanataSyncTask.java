/*
  Copyright 2011-2014 Red Hat, Inc

  This file is part of PressGang CCMS.

  PressGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PressGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PressGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.server.async.process.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.pressgang.ccms.provider.LocaleProvider;
import org.jboss.pressgang.ccms.provider.RESTContentSpecProvider;
import org.jboss.pressgang.ccms.provider.RESTProviderFactory;
import org.jboss.pressgang.ccms.provider.RESTTopicProvider;
import org.jboss.pressgang.ccms.provider.ServerSettingsProvider;
import org.jboss.pressgang.ccms.server.utils.ProcessUtilities;
import org.jboss.pressgang.ccms.services.zanatasync.ZanataSyncService;
import org.jboss.pressgang.ccms.utils.common.CollectionUtilities;
import org.jboss.pressgang.ccms.wrapper.LocaleWrapper;
import org.jboss.pressgang.ccms.wrapper.ServerSettingsWrapper;
import org.jboss.pressgang.ccms.wrapper.collection.CollectionWrapper;
import org.jboss.pressgang.ccms.zanata.ETagCache;
import org.jboss.pressgang.ccms.zanata.ETagInterceptor;
import org.jboss.pressgang.ccms.zanata.ZanataDetails;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.spi.UnauthorizedException;
import org.zanata.common.LocaleId;
import org.zanata.rest.client.ITranslatedDocResource;
import org.zanata.rest.service.TranslatedDocResource;

public class ZanataSyncTask extends ProcessRESTTask<Boolean> {
    private static final List<Class<?>> ALLOWED_RESOURCES = Arrays.<Class<?>>asList(ITranslatedDocResource.class,
            TranslatedDocResource.class);
    private final String ZANATA_CACHE_LOCATION = System.getProperty("java.io.tmpdir") + File.separator + ".zanata-cache";

    private final String restServerUrl;
    private final ZanataDetails zanataDetails;
    private final Set<String> ids = new HashSet<String>();
    private final List<LocaleId> locales = new ArrayList<LocaleId>();
    private final boolean useETagCache = false;

    public ZanataSyncTask(final String restServerUrl, final Integer contentSpecId, final ZanataDetails zanataDetails) {
        this(restServerUrl, contentSpecId, null, zanataDetails);
    }

    public ZanataSyncTask(final String restServerUrl, final Integer contentSpecId, final Collection<LocaleId> locales,
            final ZanataDetails zanataDetails) {
        this(restServerUrl, Arrays.asList(contentSpecId.toString()), locales, zanataDetails);
    }

    public ZanataSyncTask(final String restServerUrl, final Collection<String> contentSpecIds, final ZanataDetails zanataDetails) {
        this(restServerUrl, contentSpecIds, null, zanataDetails);
    }

    public ZanataSyncTask(final String restServerUrl, final Collection<String> contentSpecIds, final Collection<LocaleId> locales,
            final ZanataDetails zanataDetails) {
        this.restServerUrl = restServerUrl;
        this.zanataDetails = zanataDetails;
        ids.addAll(contentSpecIds);
        if (locales != null) {
            this.locales.addAll(locales);
        }
    }

    @Override
    public void execute() {
        final RESTProviderFactory providerFactory = RESTProviderFactory.create(restServerUrl);
        // Set topics to expand their translations by default
        providerFactory.getProvider(RESTTopicProvider.class).setExpandTranslations(true);
        providerFactory.getProvider(RESTContentSpecProvider.class).setExpandTranslationDetails(true);
        final ServerSettingsProvider settingsProvider = providerFactory.getProvider(ServerSettingsProvider.class);
        final ServerSettingsWrapper settings = settingsProvider.getServerSettings();
        final ETagCache eTagCache = new ETagCache();

        // Log some basic details about the sync
        logDetails();

        // Make sure the Zanata server isn't down
        if (!ProcessUtilities.validateServerExists(zanataDetails.getServer())) {
            getLogger().error("Unable to connect to the Zanata Server. Please make sure that the server is online and try again.");
            setSuccessful(false);
            return;
        }

        // Load the etag cache
        if (useETagCache) {
            try {
                eTagCache.load(new File(ZANATA_CACHE_LOCATION));
            } catch (IOException e) {
                getLogger().error("Failed to load the Zanata Cache from {}", ZANATA_CACHE_LOCATION);
            }
            final ETagInterceptor interceptor = new ETagInterceptor(eTagCache, ALLOWED_RESOURCES);
            ResteasyProviderFactory.getInstance().getClientExecutionInterceptorRegistry().register(interceptor);
        }

        // Load the available locales into the zanata interface
        final List<LocaleId> localeIds = new ArrayList<LocaleId>();
        final CollectionWrapper<LocaleWrapper> locales = providerFactory.getProvider(LocaleProvider.class).getLocales();
        for (final LocaleWrapper locale : locales.getItems()) {
            // Covert the language into a LocaleId
            localeIds.add(LocaleId.fromJavaName(locale.getTranslationValue()));
        }

        // Create the sync service and perform the sync
        try {
            final ZanataSyncService syncService = new ZanataSyncService(providerFactory, settings, 0.2, zanataDetails);
            syncService.syncContentSpecs(ids, localeIds);
        } catch (UnauthorizedException e) {
            getLogger().error("Unauthorised Request! Please check your Zanata username and api key are correct.");
            setSuccessful(false);
            return;
        }

        // Save the etag cache
        if (useETagCache) {
            try {
                eTagCache.save(new File(ZANATA_CACHE_LOCATION));
            } catch (IOException e) {
                getLogger().error("Failed to save the Zanata Cache to {}", ZANATA_CACHE_LOCATION);
            }
        }

        // Set the result as true since the sync completed successfully
        setResult(true);
    }

    protected void logDetails() {
        getLogger().info("Connecting to " + zanataDetails.getServer() + " using project \"" + zanataDetails.getProject() + "\", " +
                "version \"" + zanataDetails.getVersion() + "\"");
        getLogger().info("Syncing the following locales: " + CollectionUtilities.toSeperatedString(locales, ", "));
    }
}
