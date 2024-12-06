package com.ui;

import com.auctionsniper.SniperPortfolio;
import com.auctionsniper.UserRequestListener;
import com.util.Announcer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        JLabel label = new JLabel(NEW_ITEM_ID_NAME);
        final JTextField itemIdField = new JTextField(15);
        itemIdField.setName(NEW_ITEM_ID_NAME);

        controls.add(label);
        controls.add(itemIdField);

        JLabel label2 = new JLabel(NEW_ITEM_STOP_PRICE_NAME);
        final JTextField priceStopField = new JTextField(15);
        priceStopField.setName(NEW_ITEM_STOP_PRICE_NAME);

        controls.add(label2);
        controls.add(priceStopField);

        controls.add(createJoinAuctionButton(itemIdField));

        return controls;
    }

    private JButton createJoinAuctionButton(JTextField textField) {
        JButton joinAuctionButton = new JButton("Join Auction");
        joinAuctionButton.setName(JOIN_BUTTON_NAME);

        joinAuctionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userRequests.announce().joinAuction(textField.getText());
            }
        });

        return joinAuctionButton;
    }

    public void addUserRequestListener(UserRequestListener userRequestListener) {
        userRequests.addListener(userRequestListener);
    }
}