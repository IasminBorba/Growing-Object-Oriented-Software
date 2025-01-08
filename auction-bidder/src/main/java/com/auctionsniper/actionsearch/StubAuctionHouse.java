package com.auctionsniper.actionsearch;

import com.auctionsniper.Auction;
import com.auctionsniper.AuctionHouse;
import com.auctionsniper.UserRequestListener;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

public class StubAuctionHouse implements AuctionHouse {
    private final String name;
    private final Map<Set<String>, List<AuctionDescription>> searchResults = new HashMap<>();

    public StubAuctionHouse(String name) {
        this.name = name;
    }

    public void willReturnSearchResults(Set<String> keywords, List<AuctionDescription> results) {
        searchResults.put(keywords, results);
    }

    public List<AuctionDescription> search(Set<String> keywords) {
        return searchResults.getOrDefault(keywords, List.of());
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Auction auctionFor(UserRequestListener.Item item) {
        return null;
    }

    @Override
    public List<AuctionDescription> findAuctions(Set<String> keywords) {
        return search(keywords);
    }
}

