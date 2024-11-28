package com.auctionbidder;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String SNIPERS_TABLE_NAME = "Snipers Table";
    public static final String APPLICATION_TITLE = "Auction Sniper";

    private final SnipersTableModel snipers;

    public MainWindow(SnipersTableModel snipersTableModel) {
        super(APPLICATION_TITLE);
        this.snipers = snipersTableModel;

        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fillContentPane(JTable snipersTable) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable() {
        final JTable snipersTable = new JTable(snipers);
        snipersTable.setName(SNIPERS_TABLE_NAME);

        configuresTableDesign(snipersTable);

        return snipersTable;
    }

    public void configuresTableDesign(JTable snipersTable) {
        Font cellFont = new Font("Arial", Font.PLAIN, 16);
        snipersTable.setFont(cellFont);
        snipersTable.setRowHeight(30);

        JTableHeader header = snipersTable.getTableHeader();
        Font headerFont = new Font("Arial", Font.BOLD, 18);
        header.setFont(headerFont);

        DefaultTableCellRenderer centralizeRenderer = new DefaultTableCellRenderer();
        centralizeRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < snipersTable.getColumnCount(); i++)
            snipersTable.getColumnModel().getColumn(i).setCellRenderer(centralizeRenderer);

        JScrollPane scrollPane = new JScrollPane(snipersTable);
        scrollPane.setBorder(new LineBorder(Color.BLACK, 500));
        this.add(scrollPane);
    }
}