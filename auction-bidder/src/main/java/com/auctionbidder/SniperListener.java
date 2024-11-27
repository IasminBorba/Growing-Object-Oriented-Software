package com.auctionbidder;

import java.util.EventListener;

public interface SniperListener extends EventListener {
    void sniperLost();
    void sniperStateChanged(SniperSnapshot state);
    void sniperWinning();
    void sniperWon();
}
