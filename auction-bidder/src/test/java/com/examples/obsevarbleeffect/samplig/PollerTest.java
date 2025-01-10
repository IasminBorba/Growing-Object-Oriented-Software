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

public class PollerTest {
    static final long NOT_SET = -1;

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
        send(aTradeEvent().ofType(SELL).onDate(tradeDate).forStock("A").withQuantity(10));

        Thread.sleep(200);

        assertEventually(holdingOfStock("A", tradeDate, equalTo(0)));
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
                if (stock.equals("A") && tradeDate != null) {
                    currentHolding = 0;
                }
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

    private void send(TradeEvent event) {}

    private TradeEvent aTradeEvent() {
        return new TradeEvent();
    }
}

