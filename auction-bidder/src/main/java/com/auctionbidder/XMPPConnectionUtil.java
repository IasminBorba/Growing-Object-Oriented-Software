package com.auctionbidder;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jxmpp.stringprep.XmppStringprepException;


import java.io.IOException;

public class XMPPConnectionUtil {
    private static final String AUCTION_RESOURCE = "Auction";

    public static AbstractXMPPConnection connectTo(String hostname, String username, String password) {
        XMPPTCPConnectionConfiguration config = null;
        try {
            config = XMPPTCPConnectionConfiguration.builder()
                    .setXmppDomain(hostname)
                    .setUsernameAndPassword(username, password)
                    .setResource(AUCTION_RESOURCE)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new XMPPTCPConnection(config);
    }
}
