package com.auctionbidder;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;

import java.util.concurrent.ArrayBlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.Is.is;

public class SingleMessageListener implements MessageListener {
    private final ArrayBlockingQueue<Message> messages =
            new ArrayBlockingQueue<Message>(1);
    public void processMessage(Chat chat, Message message) {
        messages.add(message);
    }
    public void receivesAMessage() throws InterruptedException {
        assertThat("Message", messages.poll(5, SECONDS), is(notNullValue()));
    }

    @Override
    public void processMessage(Message message) {

    }
}