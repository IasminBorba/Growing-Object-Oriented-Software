package com.unit.auctionsniper.auctionsearch;

import com.auctionsniper.AuctionHouse;
import com.auctionsniper.actionsearch.AuctionDescription;
import com.auctionsniper.actionsearch.AuctionSearch;
import com.auctionsniper.actionsearch.AuctionSearchConsumer;
import com.auctionsniper.actionsearch.StubAuctionHouse;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.concurrent.DeterministicExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static java.util.Arrays.asList;


@RunWith(JMock.class)
public class AuctionSearchTests {
    Mockery context = new JUnit4Mockery();
    final DeterministicExecutor executor = new DeterministicExecutor();
    final StubAuctionHouse houseA = new StubAuctionHouse("houseA");
    final StubAuctionHouse houseB = new StubAuctionHouse("houseB");
    List<AuctionDescription> resultsFromA = List.of(auction(houseA, "1"));
    List<AuctionDescription> resultsFromB = List.of(auction(houseB, "2"));;
    final AuctionSearchConsumer consumer = context.mock(AuctionSearchConsumer.class);
    final AuctionSearch search =
            new AuctionSearch(executor, houses(houseA, houseB), consumer);

    @Test
    public void searchesAllAuctionHouses() throws Exception {
        final Set<String> keywords = set("sheep", "cheese");
        houseA.willReturnSearchResults(keywords, resultsFromA);
        houseB.willReturnSearchResults(keywords, resultsFromB);

        context.checking(new Expectations() {{
            final States searching = context.states("searching");
            oneOf(consumer).auctionSearchFound(resultsFromA); when(searching.isNot("done"));
            oneOf(consumer).auctionSearchFound(resultsFromB); when(searching.isNot("done"));
            oneOf(consumer).auctionSearchFinished();
            then(searching.is("done"));
        }});

        search.search(keywords);
        executor.runUntilIdle();
    }

    public static AuctionDescription auction(StubAuctionHouse house, String id) {
        return new AuctionDescription(house, id);
    }

    public static List<AuctionHouse> houses(StubAuctionHouse... stubAuctionHouses) {
        return new ArrayList<>(asList(stubAuctionHouses));
    }

    @SafeVarargs
    public static <T> Set<T> set(T... elements) {
        Set<T> result = new HashSet<>();
        Collections.addAll(result, elements);
        return result;
    }
}
