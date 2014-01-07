package org.jboss.pressgang.ccms.server.async.process;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.jboss.pressgang.ccms.server.async.process.task.ProcessTask;
import org.jppf.client.JobResults;
import org.jppf.server.protocol.JPPFTask;

/*
 * A Wrapper class to make return custom classes from the normal results.
 */
public class ProcessResults {
    private final JobResults results;

    public ProcessResults(final JobResults results) {
        this.results = results;
    }

    /**
     * Get the current number of received results.
     *
     * @return the number of results as an int.
     */
    public synchronized int size() {
        return results.size();
    }

    /**
     * Determine whether this job received a result for the task at the specified position.
     *
     * @param position the task position to check.
     * @return <code>true</code> if a result was received, <code>false</code> otherwise.
     */
    public synchronized boolean hasResult(final int position) {
        return results.hasResult(position);
    }

    /**
     * Get the result for the task at the specified position.
     *
     * @param position the position of the task to get.
     * @return a <code>ProcessTask</code> instance, or null if no result was received for a task at this position.
     */
    public ProcessTask getResult(final int position) {
        return (ProcessTask) results.getResult(position);
    }

    /**
     * Get all the tasks received as results for this job.
     *
     * @return a collection of {@link ProcessTask} instances.
     */
    public synchronized Collection<ProcessTask> getAll() {
        final Collection<ProcessTask> tasks = new ArrayList<ProcessTask>();
        final Collection<JPPFTask> jppfTasks = results.getAll();
        for (final JPPFTask task : jppfTasks) {
            tasks.add((ProcessTask) task);
        }
        return Collections.unmodifiableCollection(tasks);
    }
}
