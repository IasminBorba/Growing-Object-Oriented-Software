package com.auctionsniper.persistence;

public interface UnitOfWork {
    void work() throws Exception;
}
