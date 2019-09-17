package com.olrox.chat.message;

import com.olrox.chat.entity.Message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class MessageReaderNio implements MessageReader{

    private final static int MESSAGE_MAX_LENGTH = 1024 * 4;

    private SocketChannel socketChannel;
    private ByteBuffer input = ByteBuffer.allocate(MESSAGE_MAX_LENGTH);
    private String data;

    public MessageReaderNio(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public Message readMessage() throws IOException {
        int readCount = socketChannel.read(input);
        if (readCount > 0) {
            readProcess(readCount);
        } else {
            data = null;
        }

        Message message = new Message(data);

        return message;
    }

    private synchronized void readProcess(int readCount) {
        StringBuilder sb = new StringBuilder();
        input.flip();
        data = StandardCharsets.UTF_8.decode(input).toString().trim();
        input.clear();
    }

    @Override
    public void close() {

    }
}
