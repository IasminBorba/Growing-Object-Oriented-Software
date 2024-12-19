package com.auctionsniper.persistence;

import javax.persistence.*;
import java.util.Date;

@Entity
public class CreditCardDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date expiryDate;

    protected CreditCardDetails() {}

    public CreditCardDetails(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public CreditCardDetails withExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }
}
