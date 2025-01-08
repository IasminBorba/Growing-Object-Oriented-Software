package com.unit.auctionsniper.auctionsearch;

import com.auctionsniper.AuctionHouse;
import com.auctionsniper.actionsearch.*;
import org.hamcrest.Matcher;
import org.jmock.*;
import org.jmock.integration.junit4.*;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.*;
import org.junit.runner.RunWith;

import java.util.*;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hibernate.internal.util.collections.CollectionHelper.setOf;
import static org.hamcrest.Matchers.any;

@RunWith(JMock.class)
public class AuctionSearchStressTests {
    private static final int NUMBER_OF_AUCTION_HOUSES = 4;
    private static final int NUMBER_OF_SEARCHES = 8;
    private static final Set<String> KEYWORDS = setOf("sheep", "cheese");

    final Synchroniser synchroniser = new Synchroniser();
    final Mockery context = new JUnit4Mockery() {{
        setThreadingPolicy(synchroniser);
    }};
    final AuctionSearchConsumer consumer = context.mock(AuctionSearchConsumer.class);
    final States searching = context.states("searching");
    final ExecutorService executor = Executors.newCachedThreadPool();
    final AuctionSearch search = new AuctionSearch(executor, auctionHouses(), consumer);

    // [...]

    private List<AuctionHouse> auctionHouses() {
        ArrayList<AuctionHouse> auctionHouses = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_AUCTION_HOUSES; i++) {
            auctionHouses.add(stubbedAuctionHouse(i));
        }
        return auctionHouses;
    }

    private AuctionHouse stubbedAuctionHouse(final int id) {
        StubAuctionHouse house = new StubAuctionHouse("house" + id);
        house.willReturnSearchResults(
                KEYWORDS, List.of(new AuctionDescription(house, "id" + id, "description")));
        return house;
    }

    @Test(timeout = 500)
    public void onlyOneAuctionSearchFinishedNotificationPerSearch() throws Exception {
        context.checking(new Expectations() {{
            ignoring(consumer).auctionSearchFound(with(anyResults()));
        }});
        for (int i = 0; i < NUMBER_OF_SEARCHES; i++) {
            completeASearch();
        }
    }

    private void completeASearch() throws InterruptedException {
        searching.startsAs("in progress");
        context.checking(new Expectations() {{
            exactly(1).of(consumer).auctionSearchFinished(); then(searching.is("done"));
        }});
        search.search(KEYWORDS);
        synchroniser.waitUntil(searching.is("done"));
    }

    @SuppressWarnings("unchecked")
    private Matcher<List<AuctionDescription>> anyResults() {
        return (Matcher<List<AuctionDescription>>) (Matcher<?>) any(List.class);
    }

    @After
    public void cleanUp() throws InterruptedException {
        executor.shutdown();
        executor.awaitTermination(1, SECONDS);
    }
}

