package com.integration.auctionsniper.xmpp;

import java.util.concurrent.CountDownLatch;

import com.auctionsniper.UserRequestListener.Item;
import com.endtoend.auctionsniper.ApplicationRunner;
import com.auctionsniper.Auction;
import com.auctionsniper.AuctionEventListener;
import com.endtoend.auctionsniper.FakeAuctionServer;
import com.auctionsniper.xmpp.XMPPAuctionException;
import com.auctionsniper.xmpp.XMPPAuctionHouse;
import org.jivesoftware.smack.*;
import org.junit.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static junit.framework.TestCase.assertTrue;

public class XMPPAuctionHouseTest {
    private final FakeAuctionServer auctionServer = new FakeAuctionServer("item-54321");
    private XMPPAuctionHouse auctionHouse;

    @Before
    public void openConnection() throws XMPPException, XMPPAuctionException {
        auctionHouse = XMPPAuctionHouse.connect(
                FakeAuctionServer.XMPP_HOSTNAME,
                ApplicationRunner.SNIPER_ID,
                ApplicationRunner.SNIPER_PASSWORD);
    }

    @After
    public void closeConnection() {
        auctionHouse.disconnect();
    }

    @Before
    public void startAuction() throws XMPPException {
        auctionServer.startSellingItem();
    }

    @After
    public void stopAuction() {
        auctionServer.stop();
    }

    @Test
    public void receivesEventsFromAuctionServerAfterJoining() throws Exception {
        CountDownLatch auctionWasClosed = new CountDownLatch(1);

        Auction auction = auctionHouse.auctionFor(new Item(auctionServer.getItemId(), 567));
        auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));
        auction.join();

        auctionServer.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        auctionServer.announceClosed();

        assertTrue("should have been closed", auctionWasClosed.await(4, SECONDS));
    }

    private AuctionEventListener auctionClosedListener(final CountDownLatch auctionWasClosed) {
        return new AuctionEventListener() {
            @Override
            public void auctionClosed() {
                auctionWasClosed.countDown();
            }

            @Override
            public void currentPrice(int price, int increment, PriceSource priceSource) {}

            @Override
            public void auctionFailed() {}
        };
    }
}
