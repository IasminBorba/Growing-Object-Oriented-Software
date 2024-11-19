package com.auctionbidder;

import org.junit.jupiter.api.*;

public class AuctionSniperEndToEndTest {
    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
    private final ApplicationRunner application = new ApplicationRunner(); //sniper

    @Test
    public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
        auction.startSellingItem(); //Inicia o leilão

        application.startBiddingIn(auction); //Sniper iniciando a participação no leilão
        auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID); //aguardando o leilão receber a solicitação de participação do sniper

        auction.announceClosed(); //fechamento do leilão
        application.showsSniperHasLostAuction(); //sniper perdeu o leilão
    }

    @Test
    public void sniperMakesAHigherBidButLoses() throws Exception {
        auction.startSellingItem();

        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

        auction.reportPrice(1000,98, "other bidder"); //leilão informa as sniper o preço atual do item e qual o incremento minimo para o proximo lance
        application.hasShownSniperIsBidding(); //verifica se o sniper recebeu a informação e deu lance

        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID); //verifica se o leilão recebeu um lance do sniper (igual o ultimo preço + incremento minimo)

        auction.announceClosed();
        application.showsSniperHasLostAuction();
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
