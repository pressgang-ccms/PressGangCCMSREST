package org.jboss.pressgang.ccms.server.async.process;

import org.jboss.pressgang.ccms.server.async.process.task.ProcessTask;
import org.jppf.JPPFException;
import org.jppf.client.JPPFJob;
import org.jppf.client.taskwrapper.JPPFAnnotatedTask;
import org.jppf.server.protocol.JPPFTask;

public class PGProcess extends JPPFJob {
    public PGProcess() {
        setBlocking(false);
        getSLA().setMaxNodes(1);
        getSLA().setCancelUponClientDisconnect(true);
        getSLA().setPriority(ProcessPriority.NORMAL.value());
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
            task.setId(getUuid() + "-" + (getTasks().size() + 1));
        }
        return (ProcessTask) super.addTask(task);
    }

    @Override
    public ProcessTask addTask(final Object taskObject, final Object...args) throws JPPFException {
        JPPFTask task;
        if (taskObject instanceof JPPFTask) {
            task = (JPPFTask) taskObject;
        } else {
            task = new JPPFAnnotatedTask(taskObject, args);
        }
        if (task.getId() == null) {
            task.setId(getUuid() + "-" + (getTasks().size() + 1));
        }
        return (ProcessTask) super.addTask(task, args);
    }

    @Override
    public ProcessTask addTask(final String method, final Object taskObject, final Object...args) throws JPPFException {
        JPPFTask task;
        if (taskObject instanceof JPPFTask) {
            task = (JPPFTask) taskObject;
        } else {
            task = new JPPFAnnotatedTask(taskObject, method, args);
        }
        if (task.getId() == null) {
            task.setId(getUuid() + "-" + (getTasks().size() + 1));
        }
        return (ProcessTask) super.addTask(method, task, args);
    }
}
