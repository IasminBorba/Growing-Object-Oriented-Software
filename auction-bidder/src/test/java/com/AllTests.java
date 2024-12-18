package com;

import com.endtoend.auctionsniper.AuctionSniperEndToEndTest;
import com.integration.auctionsniper.xmpp.XMPPAuctionHouseTest;
import com.unit.auctionsniper.*;
import com.unit.auctionsniper.xmpp.AuctionMessageTranslatorTest;
import com.unit.auctionsniper.ui.SnipersTableModelTest;
import com.integration.auctionsniper.ui.MainWindowTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuctionMessageTranslatorTest.class,
//        AuctionSniperEndToEndTest.class,
        AuctionSniperTest.class,
        MainWindowTest.class,
        SniperLauncherTest.class,
        SniperPortfolioTest.class,
        SniperSnapshotTest.class,
        SnipersTableModelTest.class,
        SniperStateTests.class,
//        XMPPAuctionHouseTest.class,
})

public class AllTests {}
