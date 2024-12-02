import com.auctionbidder.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AuctionMessageTranslatorTest.class,
        AuctionSniperEndToEndTest.class,
        AuctionSniperTest.class,
        SnipersTableModelTest.class,
        MainWindowTest.class,
})

public class AllTests {}
