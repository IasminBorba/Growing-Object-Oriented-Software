package com.auctionsniper;
import com.auctionsniper.UserRequestListener.Item;

public interface AuctionHouse {
    Auction auctionFor(Item itemId);
}
