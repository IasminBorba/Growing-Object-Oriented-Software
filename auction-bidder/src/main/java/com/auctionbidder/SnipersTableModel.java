package com.auctionbidder;

import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel {
    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);
    private final SniperSnapshot sniperState = STARTING_UP;
    private static String[] STATUS_TEXT = { MainWindow.STATUS_JOINING, MainWindow.STATUS_BIDDING};
    private SniperSnapshot snapshot;
    private String state;

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (Column.at(columnIndex)) {
            case ITEM_IDENTIFIER:
                return sniperState.itemId;
            case LAST_PRICE:
                return sniperState.lastPrice;
            case LAST_BID:
                return sniperState.lastBid;
            case SNIPER_STATUS:
                return state;
            default:
                throw new IllegalArgumentException("No column at " + columnIndex);
        }
    }

    public void setStatusText(String newStatusText) {
        state = newStatusText;
        fireTableRowsUpdated(0, 0);
    }

    public void sniperStatusChanged(SniperSnapshot newSnapshot) {
        this.snapshot = newSnapshot;
        this.state = STATUS_TEXT[newSnapshot.state.ordinal()];

        fireTableRowsUpdated(0, 0);
    }
}
