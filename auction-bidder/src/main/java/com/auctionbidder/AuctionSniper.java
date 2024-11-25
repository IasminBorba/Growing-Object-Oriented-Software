package com.auctionbidder;

public class AuctionSniper implements AuctionEventListener{
    private final SniperListener sniperListener;
    private final Auction auction;
    private boolean isWinning = false;

    public AuctionSniper(Auction auction, SniperListener sniperListener) {
        this.auction = auction;
        this.sniperListener = sniperListener;
    }

    public void auctionClosed() {
        if (isWinning) {
            sniperListener.sniperWon();
        } else {
            sniperListener.sniperLost();
        }
    }

    //Preço atual do item (preço, incremento, de quem veio (Do sniper ou de outro licitante)
    public void currentPrice(int price, int increment, PriceSource priceSource) {
        isWinning = priceSource == PriceSource.FromSniper;
        if(isWinning)
            sniperListener.sniperWinning();
        else {
            auction.bid(price+increment);
            sniperListener.sniperBidding();
        }
    }
}
