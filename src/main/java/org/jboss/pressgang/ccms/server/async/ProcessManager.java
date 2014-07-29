/*
  Copyright 2011-2014 Red Hat

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

package org.jboss.pressgang.ccms.server.async;

import static com.google.common.base.Strings.isNullOrEmpty;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.DependsOn;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.ApplicationScoped;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.jboss.pressgang.ccms.model.Process;
import org.jboss.pressgang.ccms.model.ProcessStatus;
import org.jboss.pressgang.ccms.model.config.ApplicationConfig;
import org.jboss.pressgang.ccms.server.async.process.PGProcess;
import org.jboss.pressgang.ccms.server.async.process.ProcessResults;
import org.jppf.JPPFException;
import org.jppf.client.JPPFClient;
import org.jppf.client.JPPFClientConnection;
import org.jppf.client.JPPFClientConnectionStatus;
import org.jppf.client.JPPFResultCollector;
import org.jppf.client.event.ClientConnectionStatusEvent;
import org.jppf.client.event.ClientConnectionStatusListener;
import org.jppf.client.event.ClientEvent;
import org.jppf.client.event.ClientListener;
import org.jppf.client.event.SubmissionStatusEvent;
import org.jppf.client.event.SubmissionStatusListener;
import org.jppf.client.persistence.DefaultFilePersistenceManager;
import org.jppf.client.persistence.JobPersistence;
import org.jppf.client.persistence.JobPersistenceException;
import org.jppf.client.submission.SubmissionStatus;
import org.jppf.job.JobEventType;
import org.jppf.job.JobNotification;
import org.jppf.node.protocol.Task;
import org.jppf.server.job.management.DriverJobManagementMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DependsOn("StartUp")
@Singleton
@Startup
@ApplicationScoped
@TransactionManagement(TransactionManagementType.BEAN)
@Lock(LockType.READ)
public class ProcessManager {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessManager.class);
    private static final String DEFAULT_JOB_PERSISTENCE_DIR = System.getProperty("user.home") + File.separator + "pressgang" + File.separator +
            "processes";
    private static final String PROCESS_PREFIX = "process-";

    // Use a cache so that job results aren't held forever
    private Cache<String, PGProcess> currentLiveProcesses = CacheBuilder.newBuilder().build();
    private Cache<String, ProcessResults> processResults = CacheBuilder.newBuilder().softValues().expireAfterWrite(2,
            TimeUnit.HOURS).build();
    private AtomicInteger numRunningOrQueuedProcesses = new AtomicInteger(0);
    private Map<String, JobEventType> jobStates = new ConcurrentHashMap<String, JobEventType>();
    private ExtendedJMXDriverConnectionWrapper jmxConnection;
    private JPPFClient client;
    private JobPersistence<String> persistenceManager;
    private AtomicBoolean resetRequired = new AtomicBoolean(true);

    @PersistenceContext(unitName="PressGangCCMS")
    private EntityManager em;
    @Resource
    private UserTransaction transactionManager;

    /**
     * A JMX Notification listener, that listens for changes in job states on the server. This is used to get a more accurate job state
     * since we can tell if the job is queued on the server, or being processed in a node.
     */
    private final NotificationListener listener = new NotificationListener() {
        @Override
        public void handleNotification(final Notification notification, Object o) {
            if (notification instanceof JobNotification) {
                final JobNotification jobNotification = (JobNotification) notification;
                final String uuid = jobNotification.getJobInformation().getJobUuid();
                final JobEventType type = jobNotification.getEventType();
                if (type == JobEventType.JOB_UPDATED || type == JobEventType.JOB_RETURNED) {
                    // ignore update and returned events
                } else if (type.equals(JobEventType.JOB_ENDED)) {
                    jobStates.remove(uuid);
                } else {
                    jobStates.put(uuid, type);
                }
            }
        }
    };

    /**
     * A JMX Notification Listener that listens to the connection and closes it when it has been disconnected
     */
    private final NotificationListener connectionListener = new NotificationListener() {
        @Override
        public void handleNotification(final Notification notification, final Object o) {
            if ("jmx.remote.connection.failed".equals(notification.getType())) {
                resetRequired.set(true);
            } else if ("jmx.remote.connection.closed".equals(notification.getType())) {
                try {
                    // The connection will be lost at this point so just do basic clean up
                    closeJMXConnection(false);
                } catch (Exception e) {
                    LOG.error("Failed to close the JMX connection to the DriverMBean", e);
                }
            }
        }
    };

    /**
     * The JPPF client doesn't handle disconnects properly with regards to the JMX connection to the DriverMBean, this lets us know we
     * have got the connection back so that we can restart the JMX connection properly.
     */
    private final ClientListener clientListener = new ClientListener() {
        private final ClientConnectionStatusListener connectionListener = new ClientConnectionStatusListener() {
            @Override
            public void statusChanged(ClientConnectionStatusEvent event) {
                final JPPFClientConnection conn = (JPPFClientConnection) event.getClientConnectionStatusHandler();
                if (conn.getStatus() == JPPFClientConnectionStatus.ACTIVE) {
                    try {
                        initDriverNotifications();
                    } catch (Exception e) {
                        LOG.error("Failed to init the driver notification listener", e);
                    }
                }
            }
        };

        @Override
        public void newConnection(ClientEvent event) {
            event.getConnection().addClientConnectionStatusListener(connectionListener);
        }

        @Override
        public void connectionFailed(ClientEvent event) {
            event.getConnection().removeClientConnectionStatusListener(connectionListener);
        }
    };

    @PostConstruct
    protected void init() throws Exception {
        LOG.info("Starting the Process Manager");
        client = new JPPFClient(clientListener);

        // Wait for the client to connect properly
        while (!client.hasAvailableConnection()) {
            Thread.sleep(1000L);
        }

        // Get the connection to the server
        if (client.getConfig().getBoolean("jppf.remote.execution.enabled", true)) {
            // Get the JMX Connection
            final Boolean sslEnabled = client.getConfig().getBoolean("jppf.management.ssl.enabled", false);
            final String host = client.getConfig().getString("jppf.management.host", "localhost");
            final Integer port = client.getConfig().getInt("jppf.management.port", 11198);
            jmxConnection = new ExtendedJMXDriverConnectionWrapper(host, port, sslEnabled);
            initDriverNotifications();
        } else {
            resetRequired.set(false);
        }

        persistenceManager = createPersistenceManager();

        // Load and start any saved processes
        loadAndStartProcesses();
    }

    @PreDestroy
    protected void destroy() throws Exception {
        // Stop any queued processes and wait for running processes to complete
        if (numRunningOrQueuedProcesses.get() > 0) {
            LOG.info("Stopping and saving all queued or executing processes");
            for (final Map.Entry<String, PGProcess> entry : currentLiveProcesses.asMap().entrySet()) {
                /*
                 * Note: We don't wait for processes to try and finish, as by the time the destroy method is closed the DataSource will
                 * have been closed. This means that we won't be able to save the state so we might as well just kill it straight away.
                 */
                if (getProcessStatus(entry.getKey(), false).ordinal() < ProcessStatus.COMPLETED.ordinal()) {
                    saveAndKillProcess(entry.getKey(), persistenceManager);
                }
            }
        }

        persistenceManager.close();

        // Kill the connection and cleanup
        LOG.info("Destroying the Process Manager");
        try {
            closeJMXConnection(true);
        } catch (Exception e) {
            LOG.error("Failed to close the JMX connection to the DriverMBean", e);
        }
        jmxConnection = null;
        client.close();
        client = null;
    }

    protected JPPFClient getClient() {
        return client;
    }

    @Lock(LockType.WRITE)
    protected synchronized void initDriverNotifications() throws Exception {
        // We only need to proceed if a reset is required (ie due to a dropout)
        if (!resetRequired.get()) return;

        if (jmxConnection != null && !jmxConnection.isConnected()) {
            jmxConnection.connectAndWait(-1);
            // subscribe to all notifications from the MBean
            jmxConnection.addNotificationListener(DriverJobManagementMBean.MBEAN_NAME, listener);
            jmxConnection.addConnectionNotificationListener(connectionListener);
            resetRequired.set(false);
        }
    }

    @Lock(LockType.WRITE)
    protected synchronized void closeJMXConnection(boolean removeListeners) throws Exception {
        if (jmxConnection != null && jmxConnection.isConnected()) {
            if (removeListeners) {
                jmxConnection.removeNotificationListener(DriverJobManagementMBean.MBEAN_NAME, listener);
                jmxConnection.removeConnectionNotificationListener(connectionListener);
            }
            jmxConnection.close();
        }
    }

    /**
     * Loads any saved processes and starts them.
     */
    protected void loadAndStartProcesses() {
        try {
            // Load each job that was stopped when the process manager was destroyed
            final Collection<String> keys = persistenceManager.allKeys();
            if (!keys.isEmpty()) {
                LOG.info("Loading and starting saved processes");

                // Load each process
                for (final String key : keys) {
                    final PGProcess process = (PGProcess) persistenceManager.loadJob(key);
                    final Process entity = em.find(Process.class, process.getUuid());

                    if (entity != null) {
                        // Init the entity/process
                        process.setDbEntity(entity);
                        entity.setName(process.getName());

                        startProcessInternal(process);
                    } else {
                        // This shouldn't happen but incase it does print a warning
                        LOG.warn("No process exists in the database for " + process.getUuid());
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Failed to load saved processes", e);
        }

        persistenceManager.close();
    }

    protected JobPersistence<String> createPersistenceManager() {
        // Load the directory from file
        final String processDir = ApplicationConfig.getInstance().getProcessDirectory();
        File dir;
        if (!isNullOrEmpty(processDir)) {
            final File customDir = new File(processDir);
            customDir.mkdirs();
            if (customDir.isDirectory()) {
                dir = customDir;
            } else {
                dir = new File(DEFAULT_JOB_PERSISTENCE_DIR);
            }
        } else {
            dir = new File(DEFAULT_JOB_PERSISTENCE_DIR);
        }
        LOG.info("Storing processes in " + dir.getAbsolutePath());
        return new DefaultFilePersistenceManager(dir, PROCESS_PREFIX, DefaultFilePersistenceManager.DEFAULT_EXT);
    }

    /**
     * Gets all the current processes that are stored in memory.
     *
     * @return A list of {@link PGProcess} objects which contain the information about the process.
     */
    public Collection<PGProcess> getAllLiveProcesses() {
        return Collections.unmodifiableCollection(currentLiveProcesses.asMap().values());
    }

    /**
     * Gets all the current process ids that are stored in memory.
     *
     * @return A set of UUIDs for all the processes stored in memory.
     */
    public Set<String> getAllLiveProcessIds() {
        return Collections.unmodifiableSet(currentLiveProcesses.asMap().keySet());
    }

    /**
     * Start a process by submitting it to the JPPF server for processing.
     *
     * @param process The process to be started.
     * @return The UUID of the started process.
     * @throws JPPFException Thrown when the process fails to be sent to the JPPF server.
     */
    public String startProcess(final PGProcess process) throws JPPFException {
        try {
            // Save the job incase the server crashes
            persistenceManager.storeJob(persistenceManager.computeKey(process), process, Collections.EMPTY_LIST);

            // Set the start date on the process
            if (process.getDBEntity().getStartTime() != null) {
                process.getDBEntity().setStartTime(new Date());
            }
            startProcessInternal(process);
        } catch (Exception e) {
            LOG.error("Failed to start process " + process.getName(), e);
        }
        return process.getUuid();
    }

    protected void startProcessInternal(final PGProcess process) throws Exception {
        final JPPFResultCollector resultCollector = new JPPFResultCollector(process);
        process.setResultListener(resultCollector);
        resultCollector.addSubmissionStatusListener(new SubmissionStatusListener() {
            @Override
            public void submissionStatusChanged(SubmissionStatusEvent event) {
                // Ignore FAILED events as they probably mean there was a connection issue, so it will try to re-submit later
                if (event.getStatus() == SubmissionStatus.COMPLETE) {
                    processEnded(process);
                }
            }
        });

        // Submit the job to the JPPF server
        getClient().submitJob(process);
        currentLiveProcesses.put(process.getUuid(), process);
        numRunningOrQueuedProcesses.getAndIncrement();
    }

    /**
     * Updates the processes database entity when the job has been completed.
     *
     * @param process The process to be marked as finished and saved to the database.
     */
    @Lock(LockType.WRITE)
    protected synchronized void processEnded(final PGProcess process) {
        // Make sure the process is actually running
        if (currentLiveProcesses.getIfPresent(process.getUuid()) == null) {
            return;
        }

        try {
            // Start a Transaction
            transactionManager.begin();

            // Join the transaction we just started
            em.joinTransaction();

            final Process entity = process.getDBEntity();
            entity.setEndTime(new Date());
            entity.setLogs(process.getLogs());

            // Make sure we have the latest status
            if (entity.getStatus() != ProcessStatus.CANCELLED) {
                entity.setStatus(getProcessStatus(process));
            }
            em.merge(entity);

            // Commit the changes
            transactionManager.commit();
        } catch (final Exception ex) {
            LOG.error("Failed to end process " + process.getUuid(), ex);
            try {
                if (transactionManager != null) {
                    transactionManager.rollback();
                }
            } catch (final SystemException ex2) {
                // nothing to do here
                LOG.debug("Failed to rollback the transaction", ex2);
            }
        }

        // Remove the process from the live processes
        currentLiveProcesses.invalidate(process.getUuid());
        // Decrease the number of running processes
        numRunningOrQueuedProcesses.decrementAndGet();
        // Cache the results
        processResults.put(process.getUuid(), new ProcessResults(process.getResults()));

        // Delete the job now that it has been completed
        try {
            persistenceManager.deleteJob(persistenceManager.computeKey(process));
        } catch (JobPersistenceException e) {
            LOG.debug("Failed to delete process " + process.getUuid() + " from the persistence store", e);
        }
    }

    /**
     * Cancel a process from running.
     * <br /><br />
     * Note: Once a job has been submitted to a node for processing there isn't any way to cancel it.
     *
     * @param processId The UUID of the process to be cancelled.
     */
    public boolean cancelProcess(final String processId) {
        return cancelProcess(processId, false);
    }

    /**
     * Cancel a process from running.
     * <br /><br />
     * Note: Once a job has been submitted to a node for processing there isn't any way to cancel it.
     *
     * @param processId The UUID of the process to be cancelled.
     * @param force     If the process should be forced to cancel, even if it is currently executing.
     */
    public boolean cancelProcess(final String processId, boolean force) {
        final PGProcess process = currentLiveProcesses.getIfPresent(processId);
        if (process != null) {
            // Check that the job isn't currently executing, as killing it when it is executing could be very dangerous
            final ProcessStatus status = getProcessStatus(processId, false);
            if ((force && status == ProcessStatus.EXECUTING) || status == ProcessStatus.QUEUED || status == ProcessStatus.PENDING) {
                try {
                    process.setStatus(ProcessStatus.CANCELLED);
                    if (jmxConnection != null && jmxConnection.isConnected()) {
                        jmxConnection.cancelJob(processId);
                    }
                    getClient().cancelJob(processId);
                    processEnded(process);
                    return true;
                } catch (Exception e) {
                    LOG.error("Failed to cancel process " + processId, e);
                }
            } else {
                throw new IllegalStateException("The process is not in a state that can be cancelled");
            }
        }

        return false;
    }

    /**
     * Get the live process for a specific uuid.
     *
     * @param processId The Processes UUID.
     * @return Null if no process exists with the specified UUID, otherwise a {@link PGProcess} representing the live process will be
     *         returned.
     */
    public PGProcess getLiveProcess(final String processId) {
        PGProcess process = currentLiveProcesses.getIfPresent(processId);
        if (process != null) {
            // Update the state
            if (process.getStatus() != ProcessStatus.CANCELLED) {
                process.setStatus(getProcessStatus(process.getUuid(), false));
            }
        }
        return process;
    }

    /**
     * Get the process for a specific uuid.
     *
     * @param processId The Processes UUID.
     * @return Null if no process exists with the specified UUID, otherwise a {@link Process} representing the process will be returned.
     */
    public Process getProcess(final String processId) {
        final PGProcess liveProcess = getLiveProcess(processId);
        if (liveProcess != null) {
            return liveProcess.getDBEntity();
        } else {
            return em.find(Process.class, processId);
        }
    }

    /**
     * Get the results of a live process.
     * <br /><br />
     * Note: Process Results are not persisted to the Database, so once a process has been removed from memory the results will be lost.
     *
     * @param processId The UUID of the process to get results for.
     * @return A {@link ProcessResults} that contains all the information about the results of the process,
     *         or null if no process with the specified UUID exists.
     */
    public ProcessResults getProcessResults(final String processId) {
        return processResults.getIfPresent(processId);
    }

    /**
     * Get the status of a process.
     *
     * @param processId The UUID of the process to get the status for.
     * @return A {@link ProcessStatus} that contains the state of the process.
     */
    public ProcessStatus getProcessStatus(final String processId) {
        return getProcessStatus(processId, true);
    }

    /**
     * Get the status of a process.
     *
     * @param processId  The UUID of the process to get the status for.
     * @param loadFromDB Whether the status should be loaded from a Database Entity (ie it doesn't exist in memory anymore).
     * @return A {@link ProcessStatus} that contains the state of the process.
     */
    public ProcessStatus getProcessStatus(final String processId, boolean loadFromDB) {
        final PGProcess process = currentLiveProcesses.getIfPresent(processId);
        if (process != null) {
            return getProcessStatus(process);
        } else if (loadFromDB) {
            // The process isn't live, so fetch the status from memory
            final Process entity = em.find(Process.class, processId);
            if (entity == null) {
                return null;
            } else {
                return entity.getStatus();
            }
        } else {
            return null;
        }
    }

    /**
     * Get the status of a process.
     *
     * @param process  The process to get the status for.
     * @return A {@link ProcessStatus} that contains the state of the process.
     */
    protected ProcessStatus getProcessStatus(final PGProcess process) {
        final JPPFResultCollector resultCollector = (JPPFResultCollector) process.getResultListener();
        final SubmissionStatus status = resultCollector.getStatus();

        // If the status is executing then we need to check the job states (which come from the server) and see if it's actually
        // executing on the server
        if (status == SubmissionStatus.EXECUTING) {
            if (jobStates.containsKey(process.getUuid())) {
                final JobEventType eventType = jobStates.get(process.getUuid());
                if (eventType == JobEventType.JOB_QUEUED) {
                    return ProcessStatus.QUEUED;
                }
            }
        }

        switch (status) {
            case SUBMITTED:
                return ProcessStatus.PENDING;
            case PENDING:
                return ProcessStatus.QUEUED;
            case FAILED:
                return ProcessStatus.FAILED;
            case COMPLETE:
                if (process.wasSuccessful()) {
                    return ProcessStatus.COMPLETED;
                } else {
                    return ProcessStatus.FAILED;
                }
            case EXECUTING:
                return ProcessStatus.EXECUTING;
            default:
                return null;
        }
    }

    /**
     * Stops a process and stores its state using the provided persistence manager.
     *
     * @param processId          The UUID of the process to be cancelled.
     * @param persistenceManager The process persistence manager, that stores process state.
     */
    public boolean saveAndKillProcess(final String processId, final JobPersistence<String> persistenceManager) {
        final PGProcess process = currentLiveProcesses.getIfPresent(processId);
        if (process != null) {
            if (getProcessStatus(processId, false).ordinal() < ProcessStatus.COMPLETED.ordinal()) {
                process.setDbEntity(null);

                try {
                    // Update the task with any completed results
                    final Collection<Task<?>> results = process.getResults().getAllResults();
                    persistenceManager.storeJob(persistenceManager.computeKey(process), process, new ArrayList<Task<?>>(results));

                    /*
                     * Kill the process
                     *
                     * Note: Ensure to remove the process from the live processes, first otherwise the processEnded() method will be
                     * called when we cancel the job.
                     */
                    currentLiveProcesses.invalidate(processId);
                    numRunningOrQueuedProcesses.decrementAndGet();
                    if (jmxConnection != null && jmxConnection.isConnected()) {
                        jmxConnection.cancelJob(processId);
                    }
                    getClient().cancelJob(processId);
                    return true;
                } catch (Exception e) {
                    LOG.error("Failed to save and kill process " + processId, e);
                }
            }
        }

        return false;
    }
}
