package com.auctionbidder;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat.*;
import org.jivesoftware.smack.packet.Message;

import java.io.IOException;

import static com.auctionbidder.XMPPConnectionUtil.connectTo;
import static java.lang.String.format;

public class FakeAuctionServer {
    private final SingleMessageListener messageListener = new SingleMessageListener();
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String XMPP_HOSTNAME = "localhost";
    private static final String AUCTION_PASSWORD = "auction";
    private final String itemId;
//    private final AbstractXMPPConnection connection;
    private Chat currentChat;

    public FakeAuctionServer(String itemId) {
        this.itemId = itemId;
//        this.connection = connectTo(XMPP_HOSTNAME, format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD);
    }

    public void startSellingItem() throws XMPPException, SmackException, IOException, InterruptedException {
//        connection.connect();
//        connection.login();
//        connection.getChatManager().addChatListener(
//                new ChatManagerListener() {
//                    public void chatCreated(Chat chat, boolean createdLocally) {
//                        currentChat = chat;
//                        chat.addMessageListener((ChatMessageListener) messageListener);
//                    }
//                });
    }

    public String getItemId() {
        return itemId;
    }

    public void hasReceivedJoinRequestFromSniper() throws InterruptedException {
        messageListener.receivesAMessage();
    }

    public void announceClosed() throws SmackException.NotConnectedException, InterruptedException {
        currentChat.sendMessage(new Message());
    }

    public void stop() {
//        connection.disconnect();
    }
}

