package com.auctionsniper.actionsearch;

public class AuctionDescription {
    private final String id;
    private final StubAuctionHouse house;
    private final String description;

    public AuctionDescription(StubAuctionHouse house, String id) {
        this.house = house;
        this.id = id;
        this.description = "";
    }

    public AuctionDescription(StubAuctionHouse house, String id, String description) {
        this.house = house;
        this.id = id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public StubAuctionHouse getHouse() {
        return house;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "AuctionDescription{" +
                "id='" + id + '\'' +
                ", house=" + house +
                ", description='" + description + "'" +
                '}';
    }
}
