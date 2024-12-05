package com.auctionsniper;

import com.ui.SnipersTableModel;
import com.ui.SwingThreadSniperListener;

public class SniperPortfolio implements PortfolioListener, SniperCollector {
    SnipersTableModel tableModel;

    public void sniperAdded(AuctionSniper sniper) {

    }

    public void addPortfolioListener(SnipersTableModel tableModel) {
        this.tableModel = tableModel;
    }

    @Override
    public void addSniper(AuctionSniper sniper) {

    }
}
