package com.persistence;

import com.auctionsniper.persistence.*;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.persistence.CustomerBuilder.aCustomer;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PersistentCustomerBaseTest {
    final EntityManagerFactory factory = Persistence.createEntityManagerFactory("test-persistence-unit");
    final EntityManager entityManager = factory.createEntityManager();
    final JPATransactor transactor = new JPATransactor(entityManager);
    final PersistentCustomerBase customerBase = new PersistentCustomerBase(entityManager);

    @Before
    public void cleanDatabase() throws Exception {
        new DatabaseCleaner(entityManager).clean();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findsCustomersWithCreditCardsThatAreAboutToExpire() throws Exception {
        final String deadline = "6 Jun 2009";
        addCustomers(
                aCustomer().withName("Alice (Expired)")
                        .withPaymentMethods(aCreditCard().withExpiryDate(date("1 Jan 2009"))),
                aCustomer().withName("Bob (Expired)")
                        .withPaymentMethods(aCreditCard().withExpiryDate(date("5 Jun 2009"))),
                aCustomer().withName("Carol (Valid)")
                        .withPaymentMethods(aCreditCard().withExpiryDate(date(deadline))),
                aCustomer().withName("Dave (Valid)")
                        .withPaymentMethods(aCreditCard().withExpiryDate(date("7 Jun 2009")))
        );

        assertCustomersExpiringOn(
                date(deadline),
                containsInAnyOrder(
                        new Matcher[]{customerNamed("Alice (Expired)"), customerNamed("Bob (Expired)")})
        );
    }

    private void addCustomers(final CustomerBuilder... customers) throws Exception {
        transactor.perform(new UnitOfWork() {
            public void work() throws Exception {
                for (CustomerBuilder customer : customers) {
                    customerBase.addCustomer(customer.build());
                }
            }
        });
    }

    private void assertCustomersExpiringOn(final Date date, final Matcher<Iterable<Customer>> matcher) throws Exception {
        transactor.perform(new UnitOfWork() {
            public void work() throws Exception {
                assertThat(customerBase.customersWithExpiredCreditCardsAsOf(date), matcher);
            }
        });
    }

    public static Date date(String dateString) {
        try {
            return new SimpleDateFormat("d MMM yyyy").parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format: " + dateString, e);
        }
    }

    public static CreditCardDetails aCreditCard() {
        return new CreditCardDetails(new Date());
    }

    public static CreditCardDetails aCreditCardWithExpiryDate(Date expiryDate) {
        return new CreditCardDetails(expiryDate);
    }

    public Matcher<Iterable<? super Object>> customerNamed(String name) {
        return Matchers.hasItem(allOf(
                hasProperty("name", is(name))
        ));
    }
}
