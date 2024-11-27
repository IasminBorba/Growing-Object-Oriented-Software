package com.auctionbidder;

public class AuctionSniper implements AuctionEventListener{
    private final SniperListener sniperListener;
    private final Auction auction;
    private boolean isWinning = false;
    private final String itemId;

    public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener) {
        this.itemId = itemId;
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

    public void currentPrice(int price, int increment, PriceSource priceSource) {
        isWinning = priceSource == PriceSource.FromSniper;
        if(isWinning)
            sniperListener.sniperWinning();
        else {
            int bid = price + increment;
            auction.bid(bid);
            sniperListener.sniperStateChanged(new SniperSnapshot(itemId, price, bid, SniperState.BIDDING));
        }
    }
}
