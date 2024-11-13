package com.auctionbidder;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat.*;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import static com.auctionbidder.XMPPConnectionUtil.connectTo;

import javax.swing.*;

public class Main {
    private final MainWindow ui = new MainWindow();
    public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
    public static final String SNIPER_STATUS_NAME = "sniper status";
    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;
    private static final int ARG_ITEM_ID = 3;
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;
    @SuppressWarnings("unused") private Chat notToBeGCd;

    public Main() throws Exception {
//        startUserInterface();
    }

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.joinAuction(connectTo(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]), args[ARG_ITEM_ID]);
    }

    private void joinAuction(AbstractXMPPConnection connection, String itemId) throws SmackException.NotConnectedException, InterruptedException, XmppStringprepException {
        ChatManager chatManager = ChatManager.getInstanceFor(connection);
        Chat chat = chatManager.createChat(auctionId(itemId, connection),
                new ChatMessageListener() {
                    @Override
                    public void processMessage(Chat achat, Message message) {
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

    private static EntityJid auctionId(String itemId, AbstractXMPPConnection connection) throws XmppStringprepException {
        String ts = String.format(AUCTION_ID_FORMAT, itemId, connection.getXMPPServiceDomain());
        return JidCreate.entityBareFrom(ts);
    }
}