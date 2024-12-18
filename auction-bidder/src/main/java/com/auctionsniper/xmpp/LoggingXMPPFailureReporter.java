package com.auctionsniper.xmpp;

import java.util.logging.Logger;

public class LoggingXMPPFailureReporter {
    public LoggingXMPPFailureReporter(Logger logger){}
    public void cannotTranslateMessage(String itemId, String message, Exception exception) {}
}
