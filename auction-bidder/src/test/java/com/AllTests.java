package com;

import com.unit.auctionsniper.xmpp.AuctionMessageTranslatorTest;
import com.unit.auctionsniper.ui.SnipersTableModelTest;
import com.integration.auctionsniper.ui.MainWindowTest;
import com.unit.auctionsniper.AuctionSniperTest;
import com.unit.auctionsniper.SniperLauncherTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuctionMessageTranslatorTest.class,
//        AuctionSniperEndToEndTest.class,
        AuctionSniperTest.class,
        MainWindowTest.class,
        SnipersTableModelTest.class,
        SniperLauncherTest.class,
//        XMPPAuctionHouseTest.class,
})

public class AllTests {}
