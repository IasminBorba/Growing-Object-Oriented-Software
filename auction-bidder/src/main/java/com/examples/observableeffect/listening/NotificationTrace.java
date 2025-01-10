package com.examples.observableeffect.listening;

import com.examples.observableeffect.Timeout;
import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.List;

public class NotificationTrace<T> {
    private final Object traceLock = new Object();
    private final List<T> trace = new ArrayList<T>();
    private long timeoutMs;

    public void append(T message) {
        synchronized (traceLock) {
            trace.add(message);
            traceLock.notifyAll();
        }
    }

    public void containsNotification(Matcher<? super T> criteria) throws InterruptedException {
        Timeout timeout = new Timeout(timeoutMs);
        synchronized (traceLock) {
            NotificationStream<T> stream = new NotificationStream<T>(trace, criteria);
            while (! stream.hasMatched()) {
                if (timeout.hasTimedOut())
                    throw new AssertionError(failureDescriptionFrom(criteria));

                timeout.waitOn(traceLock);
            }
        }
    }

    private String failureDescriptionFrom(Matcher<? super T> matcher) {
        //Unable to find a notification that matches the criteria received.
        //Notifications received = {
        //  [...]
        // }
        //
        // Criteria received = matcher

        StringBuilder failureDescription = new StringBuilder(
                """
                        Unable to find a notification that matches the criteria received.
                        Notifications received = {
                       \s
               \s"""
        );

        for (T notification : trace)
            failureDescription.append("\t").append(notification.toString()).append("\n");

        failureDescription.append("}\n");
        failureDescription.append("Criteria received = ").append(matcher.toString());

        return failureDescription.toString();
    }

    private static class NotificationStream<N> {
        private final List<N> notifications;
        private final Matcher<? super N> criteria;
        private int next = 0;

        public NotificationStream(List<N> notifications, Matcher<? super N> criteria) {
            this.notifications = notifications;
            this.criteria = criteria;
        }

        public boolean hasMatched() {
            while (next < notifications.size()) {
                if (criteria.matches(notifications.get(next)))
                    return true;
                next++;
            }
            return false;
        }
    }
}

