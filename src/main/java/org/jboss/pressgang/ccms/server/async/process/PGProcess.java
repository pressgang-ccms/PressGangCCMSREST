package org.jboss.pressgang.ccms.server.async.process;

import java.util.Collection;

import org.jboss.pressgang.ccms.model.Process;
import org.jboss.pressgang.ccms.model.ProcessStatus;
import org.jboss.pressgang.ccms.server.async.process.task.ProcessTask;
import org.jppf.JPPFException;
import org.jppf.client.JPPFJob;
import org.jppf.client.taskwrapper.JPPFAnnotatedTask;
import org.jppf.node.protocol.Task;

public class PGProcess extends JPPFJob {
    private transient Process dbEntity;

    public PGProcess() {
        this(new Process());
    }

    public PGProcess(final Process dbEntity) {
        setBlocking(false);
        getSLA().setMaxNodes(1);
        getSLA().setCancelUponClientDisconnect(true);
        getSLA().setPriority(ProcessPriority.NORMAL.value());

        this.dbEntity = dbEntity;
        dbEntity.setUuid(getUuid());
        dbEntity.setName(getName());
    }

    public void setName(final String name) {
        super.setName(name);
        dbEntity.setName(name);
    }

    public void setPriority(final ProcessPriority priority) {
        getSLA().setPriority(priority.value());
    }

    public ProcessPriority getPriority() {
        switch (getSLA().getPriority()) {
            case 10: return ProcessPriority.HIGH;
            case 5: return ProcessPriority.HIGH;
            case 1: return ProcessPriority.HIGH;
            default: return null;
        }
    }

    public ProcessTask addTask(final ProcessTask task) throws JPPFException {
        if (task.getId() == null) {
            task.setId(getUuid() + "-" + (getJobTasks().size() + 1));
        }
        return (ProcessTask) super.add(task);
    }

    @Override
    public ProcessTask add(final Object taskObject, final Object...args) throws JPPFException {
        Task<?> task;
        if (taskObject instanceof Task) {
            task = (Task<?>) taskObject;
        } else {
            task = new JPPFAnnotatedTask(taskObject, args);
        }
        if (task.getId() == null) {
            task.setId(getUuid() + "-" + (getJobTasks().size() + 1));
        }
        return (ProcessTask) super.add(task, args);
    }

    @Override
    public ProcessTask add(final String method, final Object taskObject, final Object...args) throws JPPFException {
        Task<?> task;
        if (taskObject instanceof Task) {
            task = (Task<?>) taskObject;
        } else {
            task = new JPPFAnnotatedTask(taskObject, method, args);
        }
        if (task.getId() == null) {
            task.setId(getUuid() + "-" + (getJobTasks().size() + 1));
        }
        return (ProcessTask) super.add(method, task, args);
    }

    public String getLogs() {
        final StringBuilder retValue = new StringBuilder();
        final Collection<Task<?>> tasks = getResults().getAllResults();
        for (final Task<?> task : tasks) {
            if (task instanceof ProcessTask) {
                final ProcessTask processTask  = ((ProcessTask) task);
                if (tasks.size() > 1) {
                    final String cleanedId = processTask.getId().replace(getUuid() + "-", "");
                    retValue.append("----- Task ").append(cleanedId).append(" -----\n");
                    retValue.append(processTask.getLogs()).append("\n\n");
                } else {
                    retValue.append(processTask.getLogs()).append("\n");
                }
            }
        }
        return retValue.toString();
    }

    public String getStartedBy() {
        return dbEntity.getStartedBy();
    }

    public void setStartedBy(String startedBy) {
        dbEntity.setStartedBy(startedBy);
    }

    public Process getDBEntity() {
        return dbEntity;
    }

    public void setDbEntity(final Process dbEntity) {
        this.dbEntity = dbEntity;
    }

    public synchronized ProcessStatus getStatus() {
        return dbEntity.getStatus();
    }

    public synchronized void setStatus(final ProcessStatus status) {
        dbEntity.setStatus(status);
    }

    public boolean wasSuccessful() {
        for (final Task<?> task : getResults().getAllResults()) {
            if (task instanceof ProcessTask) {
                if (!((ProcessTask<?>) task).wasSuccessful()) {
                    return false;
                }
            }
        }

        return true;
    }
}
