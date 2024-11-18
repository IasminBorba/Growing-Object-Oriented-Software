package com.auctionbidder.fakeconnection;

import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;
import java.util.List;

public class FakeChat {
    private final List<ChatManagerListener> chatListeners = new ArrayList<>();
    private final List<FakeChatInstance> chats = new ArrayList<>();

    public FakeChatInstance createChat(String auctionId, MessageListener messageListener) {
        FakeChatInstance chat = new FakeChatInstance(auctionId, messageListener);
        chats.add(chat);
//        notifyChatListeners(chat);
        return chat;
    }

    public void addChatListener(ChatManagerListener listener) {
        chatListeners.add(listener);
    }

//    private void notifyChatListeners(FakeChatInstance chat) {
//        for (ChatManagerListener listener : chatListeners) {
//            listener.chatCreated(chat, false); // `false` porque não foi criado localmente
//        }
//    }

    public void sendMessageToChat(String auctionId, String messageBody) {
        for (FakeChatInstance chat : chats) {
            if (chat.getAuctionId().equals(auctionId)) {
                Message message = new Message();
                message.setBody(messageBody);
                chat.receiveMessage(message);
            }
        }
    }
}
