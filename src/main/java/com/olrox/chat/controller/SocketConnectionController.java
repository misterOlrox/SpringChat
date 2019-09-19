package com.olrox.chat.controller;

import com.olrox.chat.tcp.Connection;
import com.olrox.chat.tcp.TcpController;
import com.olrox.chat.tcp.TcpServer;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.ConnectionService;
import com.olrox.chat.service.MessageHandlingService;
import com.olrox.chat.service.chatsession.IChatSession;
import com.olrox.chat.service.chatsession.TcpChatSession;
import org.springframework.beans.factory.annotation.Autowired;

@TcpController
public class SocketConnectionController {

    @Autowired
    private TcpServer tcpServer;

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private MessageHandlingService messageHandlingService;

    public void receiveData(Connection connection, byte[] data) {
        String messageFromUser = new String(data);

        messageHandlingService.handleMessage(connection, messageFromUser);
    }

    public void connect(Connection connection) {
        System.out.println("New connection " + connection.getAddress().getCanonicalHostName());

        IChatSession chatSession = new TcpChatSession(connection, new User());
        connectionService.addNewChatSession(chatSession);
    }

    public void disconnect(Connection connection) {
        System.out.println("Disconnect " + connection.getAddress().getCanonicalHostName());
    }
}
