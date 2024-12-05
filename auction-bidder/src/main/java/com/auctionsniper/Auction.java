package com.auctionsniper;

public interface  Auction {
    void bid(int amount);
    void join();
    void addAuctionEventListener(AuctionEventListener listener);
}
