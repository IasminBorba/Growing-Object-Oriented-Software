package com.auctionsniper;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

import java.io.File;
import java.io.IOException;
import java.util.logging.LogManager;
import org.apache.commons.io.FileUtils;


public class AuctionLogDriver {
    public static final String LOG_FILE_NAME = "auction-sniper.log";
    private final File logFile = new File(LOG_FILE_NAME);

    public void hasEntry(Matcher<String> matcher) throws IOException {
        MatcherAssert.assertThat(FileUtils.readFileToString(logFile), matcher);
    }

    public void clearLog() {
        logFile.delete();
        LogManager.getLogManager().reset();
    }
}