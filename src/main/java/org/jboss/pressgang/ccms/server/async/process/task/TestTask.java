package org.jboss.pressgang.ccms.server.async.process.task;

public class TestTask extends ProcessTask<String> {
    public TestTask() {

    }

    @Override
    public void execute() {
        getLogger().info("Starting to execute...");
        try {
            Thread.sleep(30000L);
        } catch (Exception e) {

        }
        getLogger().info("Finished executing...");
    }
}
