package com.auctionbidder;

import java.util.EventListener;

public interface SniperListener extends EventListener {
    void sniperStateChanged(final SniperSnapshot state);
}
