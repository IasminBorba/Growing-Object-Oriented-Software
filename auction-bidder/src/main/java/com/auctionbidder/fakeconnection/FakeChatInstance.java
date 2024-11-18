package com.auctionbidder.fakeconnection;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;

public class FakeChatInstance {
    private final String auctionId;
    private final List<MessageListener> messageListeners = new ArrayList<>();

    public FakeChatInstance(String auctionId, MessageListener messageListener) {
        this.auctionId = auctionId;
        addMessageListener(messageListener);
    }

    public String getAuctionId() {
        return auctionId;
    }

    public void addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }

    public void receiveMessage(Message message) {
        for (MessageListener listener : messageListeners) {
            listener.processMessage(null, message); // Simula a recepção de uma mensagem
        }
    }

    public void sendMessage(String messageBody) {
        Message message = new Message();
        message.setBody(messageBody);
        receiveMessage(message);
    }
}