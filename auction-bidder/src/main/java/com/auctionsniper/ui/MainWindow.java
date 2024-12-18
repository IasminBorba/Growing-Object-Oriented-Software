package com.auctionsniper.ui;

import com.auctionsniper.Item;
import com.auctionsniper.SniperPortfolio;
import com.auctionsniper.UserRequestListener;
import com.auctionsniper.util.Announcer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    private static final String SNIPERS_TABLE_NAME = "Snipers Table";
    public static final String APPLICATION_TITLE = "Auction Sniper";
    public static final String NEW_ITEM_ID_NAME = "item id";
    public static final String NEW_ITEM_STOP_PRICE_NAME = "stop price";
    public static final String JOIN_BUTTON_NAME = "join button";

    private final Announcer<UserRequestListener> userRequests = Announcer.to(UserRequestListener.class);
    private final Map<String, JComponent> fieldsMap = new HashMap<>();

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

    private JPanel makeControls() {
        JPanel controls = new JPanel(new FlowLayout());

        addFieldWithLabel(controls, "Item:", NEW_ITEM_ID_NAME, new JTextField(10));
        addFieldWithLabel(controls, "Stop price:", NEW_ITEM_STOP_PRICE_NAME, new JFormattedTextField(NumberFormat.getIntegerInstance()));

        controls.add(createJoinAuctionButton());

        return controls;
    }

    private void addFieldWithLabel(JPanel panel, String labelText, String fieldName, JTextComponent field) {
        field.setName(fieldName);
        if (field instanceof JFormattedTextField formattedField)
            formattedField.setColumns(10);

        panel.add(new JLabel(labelText));
        panel.add(field);
        fieldsMap.put(fieldName, field);
    }

    private JButton createJoinAuctionButton() {
        JButton joinAuctionButton = new JButton("Join Auction");
        joinAuctionButton.setName(JOIN_BUTTON_NAME);

        add(joinAuctionButton);

        joinAuctionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userRequests.announce().joinAuction(new Item(itemId(), stopPrice()));
                clearFields();
            }
            private String itemId() {
                return ((JTextField) fieldsMap.get(NEW_ITEM_ID_NAME)).getText();
            }
            private int stopPrice() {
                return ((Number)((JFormattedTextField) fieldsMap.get(NEW_ITEM_STOP_PRICE_NAME)).getValue()).intValue();
            }
        });

        return joinAuctionButton;
    }

    public void addUserRequestListener(UserRequestListener userRequestListener) {
        userRequests.addListener(userRequestListener);
    }

    void clearFields() {
        for (String fieldName : fieldsMap.keySet()) {
            JComponent component = fieldsMap.get(fieldName);
            if (component instanceof JTextField) {
                ((JTextField) component).setText("");
            } else if (component instanceof JFormattedTextField) {
                ((JFormattedTextField) component).setText("");
            }
        }
    }
}