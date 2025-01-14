package com.examples.obsevarbleeffect.samplig;

import com.examples.observableeffect.sampling.*;
import com.examples.obsevarbleeffect.TradeEvent;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import static com.examples.obsevarbleeffect.TradeEvent.TradeType.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PollerTest {
    static final long NOT_SET = -1;
    static final String OTHER_REGION = "Other region";
    static final String SAME_REGION = "Same region";
    private static final Map<String, Integer> stockHoldings = new HashMap<>();

    @Test
    public void testFileLength() throws InterruptedException, IOException {
        File tempFile = File.createTempFile("data", ".txt");
        tempFile.deleteOnExit();

        try (var writer = new java.io.FileWriter(tempFile)) {
            for (int i = 0; i < 2500; i++)
                writer.write("A");
        }

        assertEventually(fileLength(tempFile.getAbsolutePath(), is(greaterThan(2000L))));
    }

    @Test
    public void buyAndSellOfSameStockOnSameDayCancelsOutOurHolding() throws InterruptedException {
        Date tradeDate = new Date();
        send(aTradeEvent().ofType(BUY).onDate(tradeDate).forStock("A").withQuantity(10));
        assertEventually(holdingOfStock("A", tradeDate, equalTo(10)));


        send(aTradeEvent().ofType(SELL).onDate(tradeDate).forStock("A").withQuantity(10));
        assertEventually(holdingOfStock("A", tradeDate, equalTo(0)));
    }

    @Test
    public void doesNotShowTradesInOtherRegions() throws InterruptedException {
        Date tradeDate = new Date();
        send(aTradeEvent().ofType(BUY).forStock("A").withQuantity(10).inTradingRegion(OTHER_REGION));
        send(aTradeEvent().ofType(BUY).forStock("A").withQuantity(66).inTradingRegion(SAME_REGION));
        assertEventually(holdingOfStock("A", tradeDate, equalTo(66)));
    }

    public static void assertEventually(Probe probe) throws InterruptedException {
        new Poller(1000L, 100L).check(probe);
    }

    public static Probe fileLength(String path, final Matcher<Long> matcher) {
        final File file = new File(path);
        return new Probe() {
            private long lastFileLength = NOT_SET;
            public void sample() { lastFileLength = file.length(); }
            public boolean isSatisfied() {
                return lastFileLength != NOT_SET && matcher.matches(lastFileLength);
            }
            public void describeFailureTo(Description d) {
                d.appendText("length was ").appendValue(lastFileLength);
            }
        };
    }


    public static Probe holdingOfStock(String stock, Date tradeDate, Matcher<Integer> matcher) {
        return new Probe() {
            private int currentHolding = (int) NOT_SET;

            @Override
            public void sample() {
                currentHolding = stockHoldings.getOrDefault(stock, 0);
            }

            @Override
            public boolean isSatisfied() {
                return currentHolding != NOT_SET && matcher.matches(currentHolding);
            }

            @Override
            public void describeFailureTo(Description d) {
                d.appendText("holding was ").appendValue(currentHolding);
            }
        };
    }

    private void send(TradeEvent event) {
        String stock = event.getStock();
        int quantity = event.getQuantity();

        switch (event.getType()) {
            case BUY:
                stockHoldings.put(stock, stockHoldings.getOrDefault(stock, 0) + quantity);
                break;
            case SELL:
                stockHoldings.put(stock, stockHoldings.getOrDefault(stock, 0) - quantity);
                break;
        }
    }

    private TradeEvent aTradeEvent() {
        return new TradeEvent();
    }
}

