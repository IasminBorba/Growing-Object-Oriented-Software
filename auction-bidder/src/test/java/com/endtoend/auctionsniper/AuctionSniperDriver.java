package com.endtoend.auctionsniper;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.*;
import com.objogate.wl.swing.gesture.GesturePerformer;

import javax.swing.*;
import javax.swing.table.JTableHeader;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static com.auctionsniper.ui.MainWindow.*;
import static java.lang.String.valueOf;

@SuppressWarnings("unchecked")
public class AuctionSniperDriver extends JFrameDriver {
    public AuctionSniperDriver(int timeoutMillis) {
        super(new GesturePerformer(),
                JFrameDriver.topLevelFrame(
                        named(MAIN_WINDOW_NAME),
                        showingOnScreen()),
                new AWTEventQueueProber(timeoutMillis, 100));
    }

    public void startBiddingWithStopPrice(String itemId, int stopPrice) {
        textField(NEW_ITEM_ID_NAME).typeText(itemId);
        textField(NEW_ITEM_STOP_PRICE_NAME).typeText(String.valueOf(stopPrice));

        bidButton().click();
    }

    private JTextFieldDriver textField(String fieldName) {
        JTextFieldDriver newTextField = new JTextFieldDriver(this, JTextField.class, named(fieldName));
        newTextField.focusWithMouse();
        return newTextField;
    }


    private JButtonDriver bidButton() {
        return new JButtonDriver(this, JButton.class, named(JOIN_BUTTON_NAME));
    }

    public void showsSniperStatus(String itemID, int lastPrice, int lastBid, String statusText) {
        JTableDriver table = new JTableDriver(this);
        table.hasRow(matching(withLabelText(itemID),
                withLabelText(valueOf(lastPrice)),
                withLabelText(valueOf(lastBid)),
                withLabelText(statusText)));
    }

    public void hasColumnTitles() {
        JTableHeaderDriver headers = new JTableHeaderDriver(this, JTableHeader.class);
        headers.hasHeaders(matching(
                withLabelText("Item"),
                withLabelText("Last Price"),
                withLabelText("Last Bid"),
                withLabelText("State")));
    }
}
