package com.ui;

import com.auctionsniper.SniperListener;
import com.auctionsniper.SniperSnapshot;
import com.auctionsniper.SniperState;
import com.util.Defect;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class SnipersTableModel extends AbstractTableModel implements SniperListener {
    private static final String[] STATUS_TEXT  = {
            "Joining", "Bidding", "Winning", "Lost", "Won"
    };

    private final ArrayList<SniperSnapshot> snapshots = new ArrayList<>();

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public int getRowCount() {
        return snapshots.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(snapshots.get(rowIndex));
    }

    @Override
    public String getColumnName(int column) {
        return Column.at(column).name;
    }

    public void sniperStateChanged(final SniperSnapshot newSnapshot) {
        int row = rowMatching(newSnapshot);
        snapshots.set(row, newSnapshot);
        fireTableRowsUpdated(row, row);
    }

    private int rowMatching(SniperSnapshot snapshot) {
        for (int i = 0; i < snapshots.size(); i++) {
            if (snapshot.isForSameItemAs(snapshots.get(i))) {
                return i;
            }
        }
        throw new Defect("Cannot find match for " + snapshot);
    }

    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }

    public void addSniper(SniperSnapshot joining) {
        snapshots.add(joining);
        int row = snapshots.size() - 1;
        fireTableRowsInserted(row, row);
    }
}
