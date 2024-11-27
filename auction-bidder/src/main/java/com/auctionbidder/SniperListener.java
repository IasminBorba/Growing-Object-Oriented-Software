package com.auctionbidder;

import java.util.EventListener;

public interface SniperListener extends EventListener {
    void sniperLost();
    void sniperStateChanged(final SniperSnapshot state);
    void sniperWon();
}
