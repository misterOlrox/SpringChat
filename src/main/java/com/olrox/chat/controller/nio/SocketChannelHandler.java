package com.olrox.chat.controller.nio;

import com.olrox.chat.controller.ChatHandler;
import com.olrox.chat.entity.Message;
import com.olrox.chat.message.MessageReader;
import com.olrox.chat.message.MessageReaderNio;
import com.olrox.chat.message.MessageWriter;
import com.olrox.chat.message.MessageWriterNio;
import com.olrox.chat.service.CommandParser;
import com.olrox.chat.service.MessageHandlingService;
import com.olrox.chat.session.ChatSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class SocketChannelHandler implements Runnable, ChatHandler {

    private final static Logger LOGGER = LogManager.getLogger(SocketChannelHandler.class);

    private final MessageHandlingService messageHandlingService;

    final SocketChannel socketChannel;
    final SelectionKey selectionKey;
    static final int READING = 0, SENDING = 1;
    int state = READING;
    final MessageReader messageReader;
    final MessageWriter messageWriter;
    private ChatSession chatSession;

    SocketChannelHandler(Selector selector, SocketChannel c) throws IOException {

        LOGGER.info("Handler was created.");

        socketChannel = c;
        c.configureBlocking(false);
        selectionKey = socketChannel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
        messageReader = new MessageReaderNio(socketChannel);
        messageWriter = new MessageWriterNio(socketChannel);
        chatSession = new ChatSession(this);
        this.messageHandlingService = new MessageHandlingService(new CommandParser());
    }


    public void run() {
        try {
            if (state == READING) {
                read();
            } else if (state == SENDING) {
                send();
            }
        } catch (IOException ex) {
            LOGGER.error("Error in Handler: ", ex);
        }
    }

    Message message;

    void read() throws IOException {
        message = messageReader.readMessage();

        state = SENDING;
        // Interested in writing
        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

    void send(){
        messageHandlingService.handleMessage(message, chatSession);

        selectionKey.interestOps(SelectionKey.OP_READ);
        state = READING;
    }

    @Override
    public MessageWriter getMessageWriter() {
        return messageWriter;
    }

    public void disconnect() {
        try {
            socketChannel.close();
            messageReader.close();
            messageWriter.close();
        } catch (IOException ex) {
            LOGGER.error("Error in Handler: ", ex);
        }
    }
}