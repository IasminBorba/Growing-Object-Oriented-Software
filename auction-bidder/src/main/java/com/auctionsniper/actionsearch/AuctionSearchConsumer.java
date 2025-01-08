package com.auctionsniper.actionsearch;

import java.util.List;

public interface AuctionSearchConsumer {
    void auctionSearchFound(List<AuctionDescription> results);
    void auctionSearchFinished();
}
