package com.ui;

import com.auctionsniper.Item;
import com.auctionsniper.SniperPortfolio;
import com.auctionsniper.UserRequestListener;
import com.util.Announcer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    private static final String SNIPERS_TABLE_NAME = "Snipers Table";
    public static final String APPLICATION_TITLE = "Auction Sniper";
    public static final String NEW_ITEM_ID_NAME = "item id";
    public static final String NEW_ITEM_STOP_PRICE_NAME = "stop price";
    public static final String JOIN_BUTTON_NAME = "join button";

    private final Announcer<UserRequestListener> userRequests = Announcer.to(UserRequestListener.class);

    public MainWindow(SniperPortfolio portfolio) {
        super(APPLICATION_TITLE);
        setName(MAIN_WINDOW_NAME);

        fillContentPane(makeSnipersTable(portfolio), makeControls());

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

    private JTable makeSnipersTable(SniperPortfolio portfolio) {
        SnipersTableModel model = new SnipersTableModel();
        portfolio.addPortfolioListener(model);

        JTable snipersTable = new JTable(model);
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

    private JPanel makeControls () {
        JPanel controls = new JPanel(new FlowLayout());

        final JTextField itemIdField = new JTextField(10);
        itemIdField.setName(NEW_ITEM_ID_NAME);
        controls.add(new JLabel("Item:"));
        controls.add(itemIdField);

        final JFormattedTextField stopPriceField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        stopPriceField.setColumns(7);
        stopPriceField.setName(NEW_ITEM_STOP_PRICE_NAME);
        controls.add(new JLabel("Stop price:"));
        controls.add(stopPriceField);

        controls.add(createJoinAuctionButton(itemIdField, stopPriceField));

        return controls;
    }

    private JButton createJoinAuctionButton(JTextField itemIdField, JFormattedTextField stopPriceField) {
        JButton joinAuctionButton = new JButton("Join Auction");
        joinAuctionButton.setName(JOIN_BUTTON_NAME);

        add(joinAuctionButton);

        joinAuctionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userRequests.announce().joinAuction(new Item(itemId(), stopPrice()));
            }
            private String itemId() {
                return itemIdField.getText();
            }
            private int stopPrice() {
                return ((Number)stopPriceField.getValue()).intValue();
            }
        });

        return joinAuctionButton;
    }

    public void addUserRequestListener(UserRequestListener userRequestListener) {
        userRequests.addListener(userRequestListener);
    }
}