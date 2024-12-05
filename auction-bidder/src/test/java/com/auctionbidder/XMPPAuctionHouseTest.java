package com.auctionbidder;

import java.util.concurrent.CountDownLatch;
import org.jivesoftware.smack.*;
import org.junit.jupiter.api.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static junit.framework.TestCase.assertTrue;

public class XMPPAuctionHouseTest {
    private final FakeAuctionServer auctionServer = new FakeAuctionServer("item-54321");
    private XMPPAuctionHouse auctionHouse;

    @BeforeEach
    public void openConnection() throws XMPPException {
        auctionHouse = XMPPAuctionHouse.connect(
                FakeAuctionServer.XMPP_HOSTNAME,
                ApplicationRunner.SNIPER_ID,
                ApplicationRunner.SNIPER_PASSWORD);
    }

    @AfterEach
    public void closeConnection() {
        auctionHouse.disconnect();
    }

    @BeforeEach
    public void startAuction() throws XMPPException {
        auctionServer.startSellingItem();
    }

    @AfterEach
    public void stopAuction() {
        auctionServer.stop();
    }

    @Test
    public void receivesEventsFromAuctionServerAfterJoining() throws Exception {
        CountDownLatch auctionWasClosed = new CountDownLatch(1);

        Auction auction = auctionHouse.auctionFor(auctionServer.getItemId());
        auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));
//        auction.join();

        auctionServer.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        auctionServer.announceClosed();

        assertTrue("should have been closed", auctionWasClosed.await(2, SECONDS));
    }
    private AuctionEventListener auctionClosedListener(final CountDownLatch auctionWasClosed) {
        return new AuctionEventListener() {
            @Override
            public void auctionClosed() {
                auctionWasClosed.countDown();
            }

            @Override
            public void currentPrice(int price, int increment, PriceSource priceSource) {}
        };
    }
}
