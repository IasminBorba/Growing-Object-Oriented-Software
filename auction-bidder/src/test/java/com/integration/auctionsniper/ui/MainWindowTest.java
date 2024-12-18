package com.integration.auctionsniper.ui;

import com.endtoend.auctionsniper.AuctionSniperDriver;
import com.auctionsniper.Item;
import com.auctionsniper.SniperPortfolio;
import com.auctionsniper.UserRequestListener;
import com.auctionsniper.ui.MainWindow;
import com.objogate.wl.swing.probe.ValueMatcherProbe;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class MainWindowTest {
    private final SniperPortfolio portfolio = new SniperPortfolio();
    private final MainWindow mainWindow = new MainWindow(portfolio);
    private final AuctionSniperDriver driver = new AuctionSniperDriver(100);

    @Test
    public void makesUserRequestWhenJoinButtonClicked() {
        final ValueMatcherProbe<Item> itemProbe = new ValueMatcherProbe<>(equalTo(new Item("an item id", 789)), "item request");

        mainWindow.addUserRequestListener(
                new UserRequestListener() {
                    public void joinAuction(Item item) {
                        itemProbe.setReceivedValue(item);
                    }
                });
        driver.startBiddingFor("an item id", 789);
        driver.check(itemProbe);
    }

    @After
    public void disposeDriver() {
        driver.dispose();
    }
}
