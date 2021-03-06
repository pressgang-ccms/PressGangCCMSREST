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

package org.jboss.pressgang.ccms.server.rest.v1.utils;

import static com.google.common.base.Strings.isNullOrEmpty;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.pressgang.ccms.model.Locale;
import org.jboss.pressgang.ccms.model.Process;
import org.jboss.pressgang.ccms.model.ProcessType;
import org.jboss.pressgang.ccms.model.TranslationServer;
import org.jboss.pressgang.ccms.model.config.ApplicationConfig;
import org.jboss.pressgang.ccms.model.contentspec.CSTranslationDetail;
import org.jboss.pressgang.ccms.model.contentspec.ContentSpec;
import org.jboss.pressgang.ccms.model.contentspec.ContentSpecToProcess;
import org.jboss.pressgang.ccms.provider.exception.BadRequestException;
import org.jboss.pressgang.ccms.server.async.ProcessManager;
import org.jboss.pressgang.ccms.server.async.process.PGProcess;
import org.jboss.pressgang.ccms.server.async.process.task.ZanataPushTask;
import org.jboss.pressgang.ccms.server.async.process.task.ZanataSyncTask;
import org.jboss.pressgang.ccms.server.utils.EntityManagerWrapper;
import org.jboss.pressgang.ccms.server.utils.EntityUtilities;
import org.jboss.pressgang.ccms.server.utils.EnversUtilities;
import org.jboss.pressgang.ccms.zanata.ZanataDetails;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.InternalServerErrorException;
import org.jboss.resteasy.spi.NotFoundException;
import org.jppf.JPPFException;
import org.zanata.common.LocaleId;

@RequestScoped
public class ProcessHelper {
    private static final ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
    @Inject
    private ProcessManager processManager;
    @Inject
    private EntityManagerWrapper entityManager;
    @Resource
    private UserTransaction transaction;

    /**
     * Get the process for a specific uuid.
     *
     * @param processId The Processes UUID.
     * @return Null if no process exists with the specified UUID, otherwise a {@link Process} representing the process will be returned.
     */
    public Process getProcess(final String processId) {
        final PGProcess liveProcess = processManager.getLiveProcess(processId);
        if (liveProcess != null) {
            return liveProcess.getDBEntity();
        } else {
            final Process entity = entityManager.find(Process.class, processId);
            if (entity == null) throw new NotFoundException("No entity was found with the primary key " + processId);
            return entity;
        }
    }

    public boolean isProcessAlreadyRunning(final Integer contentSpecId, final ProcessType type) {
        final Long numProcesses = EntityUtilities.getNumberOfRunningContentSpecProcessesForType(entityManager, contentSpecId, type);
        return numProcesses > 0;
    }

    public Process createZanataPushProcess(final String baseUrl, final Integer contentSpecId, final String processName,
            final boolean contentSpecOnly, final boolean disableCopyTrans, final boolean allowUnfrozenPush, final String username,
            final String apikey) throws Failure {

        try {
            transaction.begin();
            entityManager.joinTransaction();

            // Load the content spec
            final ContentSpec contentSpec = getContentSpec(contentSpecId);
            final Number revision = EnversUtilities.getLatestRevision(entityManager, contentSpec);

            // Load the zanata details
            final ZanataDetails zanataDetails = createZanataDetails(contentSpec.getTranslationDetails(), username, apikey);

            // Fix the process name if one wasn't specified
            final String fixedProcessName = "Content Spec Translation Push for " + contentSpec.getId() + "-" + revision.intValue() +
                    (isNullOrEmpty(processName) ? "" : (" - " + processName));

            // Create the process
            final Process entity = new Process();
            final PGProcess process = new PGProcess(entity);

            // Set the core fields
            entity.setType(ProcessType.TRANSLATION_PUSH);
            entity.setStartTime(new Date());
            process.setName(fixedProcessName);
            // TODO add user who started the process
            // process.setStartedBy();

            // Add the task
            process.addTask(new ZanataPushTask(baseUrl, contentSpecId, contentSpecOnly, disableCopyTrans, allowUnfrozenPush, zanataDetails));

            // Add the process to the content spec
            final ContentSpecToProcess contentSpecToProcess = new ContentSpecToProcess();
            contentSpecToProcess.setProcess(entity);
            contentSpec.addProcess(contentSpecToProcess);

            // Save the newly created process entity and content spec mapping
            entityManager.persist(entity);
            entityManager.persist(contentSpecToProcess);
            entityManager.flush();

            // Start the process
            processManager.startProcess(process);

            transaction.commit();

            return entity;
        } catch (JPPFException e) {
            throw RESTv1Utilities.processError(transaction, new InternalServerErrorException(e));
        } catch (Throwable e) {
            throw RESTv1Utilities.processError(transaction, e);
        }
    }

    public Process createZanataSyncProcess(final String baseUrl, final Integer contentSpecId, final String processName,
            final String locales, final String username, final String apikey) throws Failure {

        try {
            transaction.begin();
            entityManager.joinTransaction();

            // Load the content spec to make sure it exists
            final ContentSpec contentSpec = getContentSpec(contentSpecId);

            // Get the translation details
            final ZanataDetails zanataDetails = createZanataDetails(contentSpec.getTranslationDetails(), username, apikey);

            // Convert and check the locales
            final Collection<LocaleId> localeList = buildLocaleList(contentSpec.getTranslationDetails().getLocales(), locales);

            // Fix the process name if one wasn't specified
            final String fixedProcessName = "Content Spec Translation Sync for " + contentSpec.getId() +
                    (isNullOrEmpty(processName) ? "" : (" - " + processName));

            // Create the process
            final Process entity = new Process();
            final PGProcess process = new PGProcess(entity);

            // Set the core fields
            entity.setType(ProcessType.TRANSLATION_SYNC);
            entity.setStartTime(new Date());
            process.setName(fixedProcessName);
            // TODO add user who started the process
            // process.setStartedBy();

            // Add the task
            process.addTask(new ZanataSyncTask(baseUrl, contentSpecId, localeList, zanataDetails));

            // Add the process to the content spec
            final ContentSpecToProcess contentSpecToProcess = new ContentSpecToProcess();
            contentSpecToProcess.setProcess(entity);
            contentSpec.addProcess(contentSpecToProcess);

            // Save the newly created process entity and content spec mapping
            entityManager.persist(entity);
            entityManager.persist(contentSpecToProcess);
            entityManager.flush();

            // Start the process
            processManager.startProcess(process);

            transaction.commit();

            return entity;
        } catch (JPPFException e) {
            throw RESTv1Utilities.processError(transaction, new InternalServerErrorException(e));
        } catch (Throwable e) {
            throw RESTv1Utilities.processError(transaction, e);
        }
    }

    protected ZanataDetails createZanataDetails(final CSTranslationDetail translationDetails, final String username, final String apikey) {
        if (translationDetails == null || translationDetails.getTranslationServer() == null) {
            throw new BadRequestException("No Zanata Server has been configured for the Content Specification");
        }

        // Create the zanata details
        final TranslationServer translationServer = translationDetails.getTranslationServer();
        final ZanataDetails zanataDetails = new ZanataDetails();
        zanataDetails.setServer(translationServer.getUrl());
        zanataDetails.setProject(translationDetails.getProject());
        zanataDetails.setVersion(translationDetails.getProjectVersion());
        zanataDetails.setUsername(username);
        zanataDetails.setToken(apikey);
        return zanataDetails;
    }

    protected List<LocaleId> buildLocaleList(final List<Locale> contentSpecLocales, final String locales) {
        // Build the translation locale list
        final Map<String, Locale> validLocales = new HashMap<String, Locale>();
        for (final Locale locale : contentSpecLocales) {
            validLocales.put(locale.getValue(), locale);
        }

        // Convert the locales
        final String[] localeArray = locales == null ? new String[0] : locales.split(",");
        final List<LocaleId> localeList = new ArrayList<LocaleId>();
        for (final String locale : localeArray) {
            if (validLocales.containsKey(locale)) {
                final Locale localeEntity = validLocales.get(locale);
                localeList.add(LocaleId.fromJavaName(localeEntity.getTranslationValue()));
            } else {
                throw new BadRequestException(locale + " is not configured as a translation locale for the Content Specification");
            }
        }

        return localeList;
    }

    /**
     * Gets the latest Content Spec for a specific ID
     *
     * @param id The Content Spec ID.
     * @return The Content Spec that matched the ID
     */
    protected ContentSpec getContentSpec(final Object id) {
        try {
            final ContentSpec entity = entityManager.find(ContentSpec.class, id);
            if (entity == null) throw new NotFoundException("No entity was found with the primary key " + id);

            return entity;
        } catch (Throwable e) {
            throw RESTv1Utilities.processError(e);
        }
    }
}
