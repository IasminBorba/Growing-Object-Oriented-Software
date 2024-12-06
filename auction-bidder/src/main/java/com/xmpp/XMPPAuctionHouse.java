package com.xmpp;

import com.auctionsniper.Auction;
import com.auctionsniper.AuctionHouse;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XMPPAuctionHouse implements AuctionHouse {
    private final XMPPConnection connection;

    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    public XMPPAuctionHouse(XMPPConnection connection) {
        this.connection = connection;
    }

    public Auction auctionFor(String itemId) {
        return new XMPPAuction(connection, auctionId(itemId, connection));
    }

    public void disconnect() {
        connection.disconnect();
    }

    public static XMPPAuctionHouse connect(String hostname, String username, String password) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(hostname);
//        connection.connect();
//        connection.login(username, password, Main.AUCTION_RESOURCE);
        return new XMPPAuctionHouse(connection);
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }
}
