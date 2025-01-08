package com.auctionsniper;
import com.auctionsniper.UserRequestListener.Item;
import com.auctionsniper.actionsearch.AuctionDescription;

import java.util.List;
import java.util.Set;

public interface AuctionHouse {
    Auction auctionFor(Item itemId);
    List<AuctionDescription> findAuctions(Set<String> keywords);
}
