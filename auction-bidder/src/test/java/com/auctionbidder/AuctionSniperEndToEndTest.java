package com.auctionbidder;

import org.junit.jupiter.api.*;

public class AuctionSniperEndToEndTest {
    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
    private final ApplicationRunner application = new ApplicationRunner(); //sniper

    @Test
    public void testSniperJoinsAuctionUntilAuctionCloses() throws Exception {
        auction.startSellingItem(); //Inicia o leilão
        application.startBiddingIn(auction); //Sniper iniciando a participação no leilão
//        auction.hasReceivedJoinRequestFromSniper(); //o leilão recebeu a solicitação de participação do sniper??
//        auction.announceClosed(); //fechamento do leilão
        application.showsSniperHasLostAuction(); //sniper perdeu o leilão
    }

    @AfterEach
    public void stopAuction() {
        auction.stop();
    }
    @AfterEach
    public void stopApplication() {
        application.stop();
    }
}
