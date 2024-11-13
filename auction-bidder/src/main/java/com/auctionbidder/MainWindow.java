package com.auctionbidder;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;

import static com.auctionbidder.Main.MAIN_WINDOW_NAME;

public class MainWindow extends JFrame {
    protected static final String STATUS_JOINING = "Joining";
    protected static final String STATUS_LOST = "Lost";
    public static final String SNIPER_STATUS_NAME = "sniper status";
    private final JLabel sniperStatus = createLabel(STATUS_JOINING);

    public MainWindow() {
        super("Auction Sniper");
        setName(MAIN_WINDOW_NAME);
        add(sniperStatus);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(200,100);
        setVisible(true);
    }

    private static JLabel createLabel(String initialText) {
        JLabel result = new JLabel(initialText);
        result.setName(SNIPER_STATUS_NAME);
        result.setBorder(new LineBorder(Color.BLACK));
        return result;
    }

    protected void showStatus(String status) {
        sniperStatus.setText(status);
    }
}

