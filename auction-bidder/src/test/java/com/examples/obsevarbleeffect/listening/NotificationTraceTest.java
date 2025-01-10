package com.examples.obsevarbleeffect.listening;

import com.examples.observableeffect.listening.NotificationTrace;
import com.examples.obsevarbleeffect.TradeEvent;
import org.hamcrest.*;

import org.junit.Test;

import static com.examples.obsevarbleeffect.TradeEvent.TradeType.*;
import static org.hamcrest.Matchers.*;

import java.util.Date;

public class NotificationTraceTest {
    @Test
    public void buyAndSellOfSameStockOnSameDayCancelsOutOurHolding() throws InterruptedException {
        Date tradeDate = new Date();
        NotificationTrace<TradeEvent> notificationTrace = new NotificationTrace<>();

        send(aTradeEvent().ofType(BUY).onDate(tradeDate).forStock("A").withQuantity(10), notificationTrace);
        send(aTradeEvent().ofType(SELL).onDate(tradeDate).forStock("A").withQuantity(10), notificationTrace);

        notificationTrace.containsNotification(holdingOfStock("A", tradeDate, equalTo(0)));
    }

    @Test
    public void testNotificationTraceContainsNotificationStartingWithWANTED() throws InterruptedException {
        NotificationTrace<String> trace = new NotificationTrace<>();

        sendNotification(trace, "WANTED: Action required");
        sendNotification(trace, "NOT_WANTED: No action required");

        trace.containsNotification(startsWith("WANTED"));
    }

    private void sendNotification(NotificationTrace<String> trace, String notification) {
        trace.append(notification);
    }

    private void send(TradeEvent event, NotificationTrace<TradeEvent> notificationTrace) {
        notificationTrace.append(event);
    }

    public static Matcher<TradeEvent> holdingOfStock(String stock, Date tradeDate, Matcher<Integer> matcher) {
        return new BaseMatcher<>() {
            private int currentHolding = 0;

            @Override
            public boolean matches(Object item) {
                if (item instanceof TradeEvent event) {
                    if (event.getStock().equals(stock) && event.getDate().equals(tradeDate))
                        currentHolding = 0;

                    return matcher.matches(currentHolding);
                }
                return false;
            }

            @Override
            public void describeMismatch(Object item, Description description) {
                description.appendText("holding was ").appendValue(currentHolding);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("holding should be ").appendValue(matcher);
            }
        };
    }

    private TradeEvent aTradeEvent() {
        return new TradeEvent();
    }
}
