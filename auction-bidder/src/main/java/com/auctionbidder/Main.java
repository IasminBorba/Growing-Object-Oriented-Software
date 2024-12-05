package com.auctionbidder;

import javax.swing.SwingUtilities;
import java.awt.event.*;
import java.util.ArrayList;

public class Main {
    private MainWindow ui;
    @SuppressWarnings("unused") private ArrayList<Auction> notToBeGCd = new ArrayList<>();
    private final SnipersTableModel snipers = new SnipersTableModel();

    private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
    private static final int ARG_PASSWORD = 2;

    public static final String AUCTION_RESOURCE = "Auction";
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
    public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";

    public Main() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ui = new MainWindow(snipers);
            }
        });
    }

    public static void main(String... args) throws Exception {
        Main main = new Main();

        XMPPAuctionHouse auctionHouse = XMPPAuctionHouse.connect(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
        main.disconnectWhenUICloses(auctionHouse);
        main.addUserRequestListenerFor(auctionHouse);
    }

    private void disconnectWhenUICloses(final XMPPAuctionHouse auctionHouse) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                auctionHouse.disconnect();
            }
        });
    }

    private void addUserRequestListenerFor(final XMPPAuctionHouse auctionHouse) {
        ui.addUserRequestListener(new UserRequestListener() {
            public void joinAuction(String itemId) {
                snipers.addSniper(SniperSnapshot.joining(itemId));
                Auction auction = auctionHouse.auctionFor(itemId);
                notToBeGCd.add(auction);
                auction.addAuctionEventListener(
                                new AuctionSniper(itemId, auction,
                                        new SwingThreadSniperListener(snipers))); //Conecta o listener a um AuctionSniper, que monitora o estado do leilão.
//                auction.join(); //Envia comando de entrada
            }
        });
    }

    public class SwingThreadSniperListener implements SniperListener {
        private final SniperListener delegate;

        public SwingThreadSniperListener(SniperListener delegate) {
            this.delegate = delegate;
        }

        @Override
        public void sniperStateChanged(final SniperSnapshot snapshot) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    delegate.sniperStateChanged(snapshot);
                }
            });
        }
    }
}