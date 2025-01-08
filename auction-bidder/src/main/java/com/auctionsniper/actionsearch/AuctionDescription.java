package com.auctionsniper.actionsearch;

public class AuctionDescription {
    private final String id;
    private final StubAuctionHouse house;

    public AuctionDescription(StubAuctionHouse house, String id) {
        this.house = house;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public StubAuctionHouse getHouse() {
        return house;
    }

    @Override
    public String toString() {
        return "AuctionDescription{" +
                "id='" + id + '\'' +
                ", house=" + house +
                '}';
    }
}
