package com.auctionbidder.fakeconnection;

public class FakeXMPPConnection {
    private final String xmppHostname;
    private boolean connected = false;
    private boolean loggedIn = false;

    public FakeXMPPConnection(String xmppHostname) {
        this.xmppHostname = xmppHostname;
    }

    public void connect() {
        if (!connected) {
            connected = true;
            System.out.println("Connected to " + xmppHostname);
        }
    }

    public void login(String login, String password, String resource) {
        if (connected) {
            loggedIn = true;
            System.out.println("Logged in as " + login + "/" + resource);
        } else {
            throw new IllegalStateException("You must connect before logging in.");
        }
    }

    public FakeChat getChatManager() {
        if (!loggedIn) {
            throw new IllegalStateException("You must log in before accessing the chat manager.");
        }
        return new FakeChat();
    }

    public String getServiceName() {
        return xmppHostname;
    }

    public void disconnect() {
        if (connected) {
            loggedIn = false;
            connected = false;
            System.out.println("Disconnected from " + xmppHostname);
        } else {
            System.out.println("Already disconnected.");
        }
    }
}