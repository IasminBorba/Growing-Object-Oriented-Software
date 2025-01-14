package com.examples.obsevarbleeffect;

import java.util.Date;
import java.util.Objects;

public class TradeEvent {
    public enum TradeType {
        BUY, SELL
    }

    private TradeType type;
    private Date date;
    private String stock;
    private int quantity;

    public TradeEvent ofType(TradeType type) {
        this.type = type;
        return this;
    }

    public TradeEvent onDate(Date date) {
        this.date = date;
        return this;
    }

    public TradeEvent forStock(String stock) {
        this.stock = stock;
        return this;
    }

    public TradeEvent withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public TradeEvent inTradingRegion(String region) {
        if (Objects.equals(region, "Other region"))
            quantity = 0;
        return this;
    }

    public TradeType getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public String getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "TradeEvent{" +
                "type=" + type +
                ", date=" + date +
                ", stock='" + stock + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
