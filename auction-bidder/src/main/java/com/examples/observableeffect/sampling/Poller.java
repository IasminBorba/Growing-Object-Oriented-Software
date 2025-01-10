package com.examples.observableeffect.sampling;

import com.examples.observableeffect.Timeout;

public class Poller {
    private final long timeoutMillis;
    private final long pollDelayMillis;

    public Poller(long timeoutMillis, long pollDelayMillis) {
        this.timeoutMillis = timeoutMillis;
        this.pollDelayMillis = pollDelayMillis;
    }

    public void check(Probe probe) throws InterruptedException {
        Timeout timeout = new Timeout(timeoutMillis);
        while (!probe.isSatisfied()) {
            if (timeout.hasTimedOut()) {
                throw new AssertionError(describeFailureOf(probe));
            }
            Thread.sleep(pollDelayMillis);
            probe.sample();
        }
    }
    private String describeFailureOf(Probe probe) {
        StringBuilder failureDescription = new StringBuilder();
        probe.describeFailureTo(new org.hamcrest.StringDescription(failureDescription));
        return failureDescription.toString();
    }
}

