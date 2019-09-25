package com.olrox.chat.controller;

import com.olrox.chat.service.ChatSessionService;
import com.olrox.chat.service.sending.ChatSession;
import com.olrox.chat.service.sending.TcpChatSessionAdapter;
import com.olrox.chat.tcp.Connection;
import com.olrox.chat.tcp.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@TcpController
public class SocketController {

//    @Autowired
//    private TcpServer tcpServer;

    @Autowired
    private ChatSessionService chatSessionService;


    @OnTcpConnect
    public void connect(Connection connection) {
        System.out.println("New connection " + connection.getAddress().getCanonicalHostName());

        ChatSession chatSession = new TcpChatSessionAdapter(connection);
        chatSessionService.addNewChatSession(chatSession);
    }

    @OnTcpDisconnect
    public void disconnect(Connection connection) {
        System.out.println("Disconnect " + connection.getAddress().getCanonicalHostName());
    }

    @OnTcpMessage
    public void receiveMessage(Connection connection, String text) {


        connection.send("That was just a message\n".getBytes());
    }

    @OnTcpCommand(regex = "\\/register .+", priority = 1)
    public void register(Connection connection, String text) {

        connection.send("That was /register\n".getBytes());
    }

    @OnTcpCommand(regex = "\\/leave", priority = 2)
    public void leave(Connection connection, String text) {

        connection.send("That was /leave\n".getBytes());
    }

    @OnTcpCommand(regex = "\\/exit", priority = 3)
    public void exit(Connection connection, String text) {

    }
}
