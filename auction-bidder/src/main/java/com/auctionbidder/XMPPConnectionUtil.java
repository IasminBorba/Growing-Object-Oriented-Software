package com.auctionbidder;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.AbstractXMPPConnection;


import java.io.IOException;

public class XMPPConnectionUtil {
    private static final String AUCTION_RESOURCE = "Auction";

    public static AbstractXMPPConnection connectTo(String hostname, String username, String password) throws SmackException, IOException, XMPPException, InterruptedException {
        XMPPTCPConnectionConfiguration config =  XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(hostname)
                .setUsernameAndPassword(username, password)
                .setResource(AUCTION_RESOURCE)
                .build();

        XMPPTCPConnection connection = new XMPPTCPConnection(config);
        connection.connect();
        connection.login();

        return connection;
    }
}
