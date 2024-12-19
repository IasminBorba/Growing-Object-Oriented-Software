package com.auctionsniper.persistence;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditCardDetails> paymentMethods = new ArrayList<>();

    protected Customer() {}

    public Customer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<CreditCardDetails> getPaymentMethods() {
        return paymentMethods;
    }

    public void addPaymentMethod(CreditCardDetails creditCardDetails) {
        this.paymentMethods.add(creditCardDetails);
    }
}
