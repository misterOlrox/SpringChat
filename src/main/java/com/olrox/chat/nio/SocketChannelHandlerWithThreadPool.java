package com.olrox.chat.nio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketChannelHandlerWithThreadPool extends SocketChannelHandler {

    private final static Logger LOGGER = LogManager.getLogger(SocketChannelHandlerWithThreadPool.class);

    static ExecutorService pool = Executors.newFixedThreadPool(2);
    static final int PROCESSING = 2;

    public SocketChannelHandlerWithThreadPool(Selector sel, SocketChannel c) throws IOException {
        super(sel, c);
    }

    void read() throws IOException {
        message = messageReader.readMessage();
        state = PROCESSING;
        pool.execute(new Processor());
    }

    private synchronized void processAndHandOff() {
        send();
    }

    class Processor implements Runnable {
        public void run() {
            processAndHandOff();
        }
    }
}