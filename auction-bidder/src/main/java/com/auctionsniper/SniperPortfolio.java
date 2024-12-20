package com.auctionsniper;

import com.auctionsniper.util.Announcer;

import java.util.ArrayList;
import java.util.EventListener;

public class SniperPortfolio implements SniperCollector {
    public interface PortfolioListener extends EventListener {
        void sniperAdded(AuctionSniper sniper);
    }

    private final Announcer<PortfolioListener> announcer = Announcer.to(PortfolioListener.class);
    private final ArrayList<AuctionSniper> snipers = new ArrayList<>();

    @Override
    public void addSniper(AuctionSniper sniper) {
        snipers.add(sniper);
        announcer.announce().sniperAdded(sniper);
    }

    public void addPortfolioListener(PortfolioListener listener) {
        announcer.addListener(listener);
    }
}

