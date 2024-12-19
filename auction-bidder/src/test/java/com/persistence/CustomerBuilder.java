package com.persistence;

import com.auctionsniper.persistence.CreditCardDetails;
import com.auctionsniper.persistence.Customer;

public class CustomerBuilder {
    private String name;
    private Customer customer;

    public static CustomerBuilder aCustomer() {
        return new CustomerBuilder();
    }

    public CustomerBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CustomerBuilder withPaymentMethods(CreditCardDetails... paymentMethods) {
        if (this.customer == null) {
            this.customer = new Customer(name);
        }
        for (CreditCardDetails paymentMethod : paymentMethods) {
            this.customer.addPaymentMethod(paymentMethod);
        }
        return this;
    }

    public Customer build() {
        if (this.customer == null) {
            this.customer = new Customer(name);
        }
        return this.customer;
    }
}


