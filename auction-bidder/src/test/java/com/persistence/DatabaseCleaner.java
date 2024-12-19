package com.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.sql.SQLException;

public class DatabaseCleaner {
    private static final Class<?>[] ENTITY_TYPES = {
            Customer.class,
            PaymentMethod.class,
            AuctionSiteCredentials.class,
            AuctionSite.class,
            Address.class
    };
    private final EntityManager entityManager;

    public DatabaseCleaner(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void clean() throws SQLException {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        for (Class<?> entityType : ENTITY_TYPES)
            deleteEntities(entityType);

        transaction.commit();
    }

    private void deleteEntities(Class<?> entityType) {
        entityManager
                .createQuery("delete from " + entityNameOf(entityType))
                .executeUpdate();
    }
}
