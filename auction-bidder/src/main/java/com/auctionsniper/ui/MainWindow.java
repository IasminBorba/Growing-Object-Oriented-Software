package com.auctionsniper.ui;

import com.auctionsniper.UserRequestListener.Item;
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

        contentPane.add(controls, BorderLayout.NORTH);
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
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

        controls.add(new JLabel("Item:"));
        controls.add(createTextField());

        controls.add(new JLabel("Stop price:"));
        controls.add(createFormattedField());

        controls.add(createJoinAuctionButton());

        return controls;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField(10);
        textField.setName(NEW_ITEM_ID_NAME);

        fieldsMap.put(NEW_ITEM_ID_NAME, textField);

        return textField;
    }

    private JFormattedTextField createFormattedField() {
        JFormattedTextField formattedField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        formattedField.setColumns(10);
        formattedField.setName(NEW_ITEM_STOP_PRICE_NAME);

        fieldsMap.put(NEW_ITEM_STOP_PRICE_NAME, formattedField);

        return formattedField;
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

    public void addUserRequestListener(UserRequestListener userRequestListener) {
        userRequests.addListener(userRequestListener);
    }
}