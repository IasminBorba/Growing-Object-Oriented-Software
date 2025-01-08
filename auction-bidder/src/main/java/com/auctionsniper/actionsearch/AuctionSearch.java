package com.auctionsniper.actionsearch;

import com.auctionsniper.AuctionHouse;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

public class AuctionSearch {
    private final Executor executor;
    private final List<AuctionHouse> auctionHouses;
    private final AuctionSearchConsumer consumer;
    private int runningSearchCount = 0;

    public AuctionSearch(Executor executor,
                         List<AuctionHouse> auctionHouses,
                         AuctionSearchConsumer consumer) {
        this.executor = executor;
        this.auctionHouses = auctionHouses;
        this.consumer = consumer;
    }

    public void search(Set<String> keywords) {
        for (AuctionHouse auctionHouse : auctionHouses) {
            startSearching(auctionHouse, keywords);
        }
    }

    private void startSearching(final AuctionHouse auctionHouse,
                                final Set<String> keywords) {
        runningSearchCount++;
        executor.execute(new Runnable() {
            public void run() {
                search(auctionHouse, keywords);
            }
        });
    }

    private void search(AuctionHouse auctionHouse, Set<String> keywords) {
        consumer.auctionSearchFound(auctionHouse.findAuctions(keywords));
        runningSearchCount--;
        if (runningSearchCount == 0) {
            consumer.auctionSearchFinished();
        }
    }
}

