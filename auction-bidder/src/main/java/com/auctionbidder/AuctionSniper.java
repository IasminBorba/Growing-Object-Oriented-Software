package com.auctionbidder;

public class AuctionSniper implements AuctionEventListener{
    private final SniperListener sniperListener;
    private final Auction auction;

    public AuctionSniper(Auction auction, SniperListener sniperListener) {
        this.auction = auction;
        this.sniperListener = sniperListener;
    }

    public void auctionClosed() {
        sniperListener.sniperLost();
    }

    //Preço atual do item (preço, incremento, de quem veio (Do sniper ou de outro licitante)
    public void currentPrice(int price, int increment, PriceSource priceSource) {
        switch (priceSource) {
            case FromSniper -> sniperListener.sniperWinning();
            case FromOtherBidder -> {
                auction.bid(price+increment);
                sniperListener.sniperBidding();
            }
        }
    }
}
