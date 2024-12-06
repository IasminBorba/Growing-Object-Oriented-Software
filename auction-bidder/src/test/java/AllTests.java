import com.auctionsniper.*;
import com.ui.MainWindowTest;
import com.ui.SnipersTableModelTest;
import com.xmpp.AuctionMessageTranslatorTest;
import com.xmpp.XMPPAuctionHouseTest;
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
