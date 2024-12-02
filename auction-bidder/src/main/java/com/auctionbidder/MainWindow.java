package com.auctionbidder;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;

public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String SNIPERS_TABLE_NAME = "Snipers Table";
    public static final String APPLICATION_TITLE = "Auction Sniper";
    public static final String NEW_ITEM_ID_NAME = "item id";
    public static final String JOIN_BUTTON_NAME = "Join button";

    private final SnipersTableModel snipers;

    public MainWindow(SnipersTableModel snipersTableModel) {
        super(APPLICATION_TITLE);
        this.snipers = snipersTableModel;

        setName(MAIN_WINDOW_NAME);
        fillContentPane(makeSnipersTable(snipers), makeControls());
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fillContentPane(JTable snipersTable, JPanel controls) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
        contentPane.add(new JScrollPane(controls), BorderLayout.NORTH);
    }

    private JTable makeSnipersTable(TableModel snipersTables) {
        final JTable snipersTable = new JTable(snipersTables);
        snipersTable.setName(SNIPERS_TABLE_NAME);

        configuresTableDesign(snipersTable);

        return snipersTable;
    }

    public void configuresTableDesign(JTable snipersTable) {
        JTableHeader header = snipersTable.getTableHeader();
        Font headerFont = new Font("Arial", Font.BOLD, 18);
        header.setFont(headerFont);

        Font cellFont = new Font("Arial", Font.PLAIN, 16);
        snipersTable.setFont(cellFont);
        snipersTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(snipersTable);
        scrollPane.setBorder(new LineBorder(Color.BLACK, 18));
        this.add(scrollPane);
    }

    private JPanel makeControls() {
        JPanel controls = new JPanel(new FlowLayout());
        final JTextField itemIdField = new JTextField();
        itemIdField.setColumns(25);
        itemIdField.setName(NEW_ITEM_ID_NAME);

        controls.add(itemIdField);
        controls.add(createJoinAuctionButton(itemIdField));

        return controls;
    }

    private JButton createJoinAuctionButton(JTextField textField) {
        JButton joinAuctionButton = new JButton("Join Auction");

        joinAuctionButton.setName(JOIN_BUTTON_NAME);

        joinAuctionButton.addActionListener(e -> {
            snipers.addSniper(new SniperSnapshot(textField.getText(), 0, 0,SniperState.JOINING));
        });

        return joinAuctionButton;
    }
}