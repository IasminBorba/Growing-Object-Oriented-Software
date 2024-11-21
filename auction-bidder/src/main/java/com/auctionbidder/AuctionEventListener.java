package com.auctionbidder;

public interface AuctionEventListener {
    void auctionClosed();

    void currentPrice(int prince, int increment);
}
