package org.jboss.pressgang.ccms.server.async;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.pressgang.ccms.server.async.process.PGProcess;
import org.jboss.pressgang.ccms.server.async.process.ProcessPriority;
import org.jboss.pressgang.ccms.server.async.process.ProcessResults;
import org.jboss.pressgang.ccms.server.async.process.task.ProcessTask;
import org.jppf.JPPFException;
import org.jppf.client.JPPFClient;
import org.jppf.client.JPPFJob;
import org.jppf.client.JPPFResultCollector;
import org.jppf.client.submission.SubmissionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ProcessManager {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessManager.class);
    private Map<String, PGProcess> currentRunningJobs = new ConcurrentHashMap<String, PGProcess>();
    private JPPFClient client;

    @PostConstruct
    protected void init() {
        client = new JPPFClient();
    }

    @PreDestroy
    protected void destroy() {
        client.close();
    }

    protected JPPFClient getClient() {
        return client;
    }

    public String startProcess(final ProcessTask task) throws JPPFException {
        return startProcess(task, null);
    }

    public String startProcess(final ProcessTask task, final ProcessPriority priority) throws JPPFException {
        final PGProcess job = new PGProcess();
        job.addTask(task);
        if (priority != null) {
            job.getSLA().setPriority(priority.value());
        }
        task.setId(job.getUuid());
        return startProcess(job);
    }

    public String startProcess(final PGProcess process) throws JPPFException {
        process.setResultListener(new JPPFResultCollector(process));
        try {
            getClient().submit(process);
            currentRunningJobs.put(process.getUuid(), process);
        } catch (Exception e) {
            LOG.error("Failed to start process " + process, e);
        }
        return process.getUuid();
    }

    public void cancelProcess(final String processId) {
        if (currentRunningJobs.containsKey(processId)) {
            try {
                getClient().cancelJob(processId);
            } catch (Exception e) {
                LOG.error("Failed to cancel process " + processId, e);
            }
        }
    }

    public PGProcess getProcess(final String processId) {
        if (currentRunningJobs.containsKey(processId)) {
            return currentRunningJobs.get(processId);
        } else {
            return null;
        }
    }

    public ProcessResults getProcessResults(final String processId) throws Exception {
        if (currentRunningJobs.containsKey(processId)) {
            final JPPFJob job = currentRunningJobs.get(processId);
            return new ProcessResults(job.getResults());
        } else {
            return null;
        }
    }

    public SubmissionStatus getProcessStatus(final String processId) {
        if (currentRunningJobs.containsKey(processId)) {
            final JPPFJob job = currentRunningJobs.get(processId);
            final JPPFResultCollector resultCollector = (JPPFResultCollector) job.getResultListener();
            return resultCollector.getStatus();
        } else {
            return null;
        }
    }
}
