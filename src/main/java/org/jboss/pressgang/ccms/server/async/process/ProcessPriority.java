package org.jboss.pressgang.ccms.server.async.process;

public enum ProcessPriority {
    LOW(1), NORMAL(5), HIGH(10);

    private final int value;

    ProcessPriority(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
