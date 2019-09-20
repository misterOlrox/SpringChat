package com.olrox.chat.controller;

import com.olrox.chat.controller.util.command.CommandParser;
import com.olrox.chat.controller.util.command.CommandProcessor;
import com.olrox.chat.controller.util.command.ParsedCommand;
import com.olrox.chat.tcp.Connection;
import com.olrox.chat.tcp.TcpController;
import com.olrox.chat.tcp.TcpServer;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.ConnectionService;
import com.olrox.chat.service.sending.ChatSession;
import com.olrox.chat.service.sending.TcpChatSessionAdapter;
import org.springframework.beans.factory.annotation.Autowired;

@TcpController
public class SocketConnectionController {

    @Autowired
    private TcpServer tcpServer;

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private CommandParser commandParser;

    @Autowired
    private CommandProcessor commandProcessor;


    public void receiveData(Connection connection, byte[] data) {
        String messageFromUser = new String(data);

        ParsedCommand command = commandParser.parse(messageFromUser);
        ChatSession chatSession = new TcpChatSessionAdapter(connection);
        commandProcessor.process(chatSession, command);
    }

    public void connect(Connection connection) {
        System.out.println("New connection " + connection.getAddress().getCanonicalHostName());

        ChatSession chatSession = new TcpChatSessionAdapter(connection);
        connectionService.addNewChatSession(chatSession);
    }

    public void disconnect(Connection connection) {
        System.out.println("Disconnect " + connection.getAddress().getCanonicalHostName());
    }
}
