package com.olrox.chat.controller;

import com.olrox.chat.entity.ConnectionType;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.ConnectionService;
import com.olrox.chat.service.MessageService;
import com.olrox.chat.service.UserService;
import com.olrox.chat.service.sending.MessageSender;
import com.olrox.chat.service.sending.MessageSenderFactory;
import com.olrox.chat.tcp.Connection;
import com.olrox.chat.tcp.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@TcpController
public class SocketController {

    private final Map<Connection, Long> connections = new ConcurrentHashMap<>();

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSenderFactory messageSenderFactory;

    @Autowired
    private MessageService messageService;

    @OnTcpConnect
    public void connect(Connection connection) {
        System.out.println("New connection " + connection.getAddress().getCanonicalHostName());

        User user = userService.addUnauthorizedUser(ConnectionType.SOCKET);
        Long userId = user.getId();
        connections.put(connection, userId);
        connectionService.addSocketConnection(userId, connection);

        MessageSender messageSender = messageSenderFactory.getMessageSender(ConnectionType.SOCKET);
        messageSender.send(messageService.createGreetingMessage(user));
        messageSender.send(messageService.createRegisterInfoMessage(user));
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
