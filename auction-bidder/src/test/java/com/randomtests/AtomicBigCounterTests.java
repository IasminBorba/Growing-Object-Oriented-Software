package com.randomtests;

import org.junit.Test;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AtomicBigCounterTests {
    final AtomicBigCounter counter = new AtomicBigCounter();

    @Test
    public void canIncrementCounterFromMultipleThreadsSimultaneously() throws InterruptedException {
        MultithreadedStressTester stressTester = new MultithreadedStressTester(25000);
        stressTester.stress(new Runnable() {
            public void run() {
                counter.inc();
            }
        });
        stressTester.shutdown();
        assertThat("final count", counter.count(),
                equalTo(BigInteger.valueOf(stressTester.totalActionCount())));
    }
}

class AtomicBigCounter {
    private BigInteger count = BigInteger.ZERO;

    public synchronized BigInteger count() {
        return count;
    }

    public synchronized void inc() {
        count = count.add(BigInteger.ONE);
    }
}

class MultithreadedStressTester {
    private final int actionCount;
    private final AtomicInteger completedActions = new AtomicInteger(0);

    public MultithreadedStressTester(int actionCount) {
        this.actionCount = actionCount;
    }

    public void stress(Runnable action) throws InterruptedException {
        int numberOfThreads = 10;
        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(new Runnable() {
                public void run() {
                    for (int j = 0; j < actionCount / numberOfThreads; j++) {
                        action.run();
                        completedActions.incrementAndGet();
                    }
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < numberOfThreads; i++)
            threads[i].join();
    }

    public void shutdown() {}

    public int totalActionCount() {
        return completedActions.get();
    }
}
