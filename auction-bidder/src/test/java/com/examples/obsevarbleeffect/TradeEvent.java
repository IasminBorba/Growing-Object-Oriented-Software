package com.examples.obsevarbleeffect;

import java.util.Date;

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

    public TradeType getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public String getStock() {
        return stock;
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
