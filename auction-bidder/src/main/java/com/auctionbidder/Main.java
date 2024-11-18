package com.auctionbidder;

import com.auctionbidder.fakeconnection.FakeChatInstance;
import com.auctionbidder.fakeconnection.FakeXMPPConnection;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import javax.swing.SwingUtilities;

public class Main {
    @SuppressWarnings("unused") private FakeChatInstance notToBeGCd;
    private MainWindow ui;

    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private static final int ARG_ITEM_ID = 3;

    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;


    public Main() throws Exception {
        startUserInterface();
    }

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.joinAuction(connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]), args[ARG_ITEM_ID]);
    }

    private void joinAuction(FakeXMPPConnection connection, String itemId) throws XMPPException {
        final FakeChatInstance chat = connection.getChatManager().createChat(
                auctionId(itemId, connection),
                new MessageListener() {
                    @Override
                    public void processMessage(Chat aChat, Message message) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                ui.showStatus(MainWindow.STATUS_LOST);
                            }
                        });
                    }
                });
        this.notToBeGCd = chat;
        chat.sendMessage("");
    }

    private static FakeXMPPConnection connection(String hostname, String username, String password) throws XMPPException {
        FakeXMPPConnection connection = new FakeXMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);
        return connection;
    }

    private static String auctionId(String itemId, FakeXMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }

    private void startUserInterface() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ui = new MainWindow();
            }
        });
    }
}