package com.auctionsniper.persistence;

import java.util.Date;
import java.util.List;

public interface CustomerBase {
    // [...]
    void addCustomer(Customer customer);
    List<Customer> customersWithExpiredCreditCardsAsOf(Date deadline);
}
