package com.auctionsniper;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;

import org.junit.*;
import org.junit.runner.RunWith;

import com.auctionsniper.AuctionEventListener.PriceSource;

import static com.auctionsniper.SniperState.*;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(JMock.class)
public class AuctionSniperTest {
    protected static final String ITEM_ID = "item id";
    public static final Item ITEM = new Item(ITEM_ID, 1234);
    private final Mockery context = new Mockery();
    private final Auction auction = context.mock(Auction.class);
    private final SniperListener sniperListener = context.mock(SniperListener.class);
    private final AuctionSniper sniper = new AuctionSniper(ITEM, auction);
    private final States sniperState = context.states("sniper");

    @Before
    public void attachListener() {
        sniper.addSniperListener(sniperListener);
    }

    @Test
    public void reportsLostIfAuctionClosesImmediately() {
        context.checking(new Expectations() {{
            atLeast(1).of(sniperListener).sniperStateChanged(
                    new SniperSnapshot(ITEM_ID, 0, 0, LOST));
        }});

        sniper.auctionClosed();
    }

    @Test
    public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;
        final int bid = price + increment;
        context.checking(new Expectations() {{
            one(auction).bid(bid); // lance seja enviado uma unica vez com o valor correto
            atLeast(1).of(sniperListener).sniperStateChanged(
                    new SniperSnapshot(ITEM_ID, price, bid, BIDDING)); //se, pelo menos uma vez, o sniper notifica que o listener que está dando lance
        }});

        sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);
    }

    @Test
    public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
        context.checking(new Expectations() {{
            ignoring(auction);
            allowing(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(BIDDING)));
                        then(sniperState.is("bidding"));

            atLeast(1).of(sniperListener).sniperStateChanged(
                    new SniperSnapshot(ITEM_ID, 135, 135, WINNING));
                        when(sniperState.is("bidding"));
        }});

        sniper.currentPrice(123, 12, PriceSource.FromOtherBidder);
        sniper.currentPrice(135, 45, PriceSource.FromSniper);
    }

    @Test
    public void reportsLostIfAuctionClosesWhenBidding() {
        context.checking(new Expectations() {{
            ignoring(auction); //ignora o leilão
            allowing(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(BIDDING))); //allowing = apenas suporte ao teste, não é o foco principal (o teste não falha se a chamada não for feita)
                        then(sniperState.is("bidding")); //registramos o status bidding como o status atual do sniper

            atLeast(1).of(sniperListener).sniperStateChanged(
                    new SniperSnapshot(ITEM_ID, 123, 123+45, LOST));
                        when(sniperState.is("bidding")); //expectativa principal: sniper está dando lance
        }});

        sniper.currentPrice(123,45,PriceSource.FromOtherBidder);
        sniper.auctionClosed();
    }

    @Test
    public void reportsWonIfAuctionClosesWhenWinning() {
        context.checking(new Expectations() {{
            ignoring(auction);
            allowing(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(BIDDING))); then(sniperState.is("bidding"));

            allowing(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(WINNING))); then(sniperState.is("winning"));

            atLeast(1).of(sniperListener).sniperStateChanged(
                    new SniperSnapshot(ITEM_ID, 135, 135, WON));
                        when(sniperState.is("winning"));
        }});

        sniper.currentPrice(123, 12, PriceSource.FromOtherBidder);
        sniper.currentPrice(135, 45, PriceSource.FromSniper);
        sniper.auctionClosed();
    }

    @Test
    public void doesNotBidAndReportsLosingIfSubsequentPriceIsAboveStopPrice() {
        allowingSniperBidding();
        context.checking(new Expectations() {{
            int bid = 123 + 45;
            allowing(auction).bid(bid);
            atLeast(1).of(sniperListener).sniperStateChanged(
                    new SniperSnapshot(ITEM_ID, 2345, bid, LOSING));
            when(sniperState.is("bidding"));
        }});
        sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
        sniper.currentPrice(2345, 25, PriceSource.FromOtherBidder);
    }

    @Test public void
    reportsFailedIfAuctionFailsWhenBidding() {
        ignoringAuction();
        allowingSniperBidding();
        expectSniperToFailWhenItIs("bidding");
        sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
        sniper.auctionFailed();
    }


    private void ignoringAuction() {
        context.checking(new Expectations() {{
            ignoring(auction);
        }});
    }

    private void allowingSniperBidding() {
        context.checking(new Expectations() {{
            allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(BIDDING)));
            then(sniperState.is("bidding"));
        }});
    }

    private void expectSniperToFailWhenItIs(final String state) {
        context.checking(new Expectations() {{
            atLeast(1).of(sniperListener).sniperStateChanged(
                    new SniperSnapshot(ITEM_ID, 0, 0, SniperState.FAILED));
            when(sniperState.is(state));
        }});
    }

    private Matcher<SniperSnapshot> aSniperThatIs(final SniperState state) {
        return new FeatureMatcher<SniperSnapshot, SniperState>(
                equalTo(state), "sniper that is ", "was") {
            @Override
            protected SniperState featureValueOf(SniperSnapshot actual) {
                return actual.state;
            }
        };
    }
}
