package com.olrox.chat.controller;

import com.olrox.chat.custom.controller.tcp.Connection;
import com.olrox.chat.custom.controller.tcp.TcpController;
import com.olrox.chat.custom.controller.tcp.TcpServer;
import com.olrox.chat.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;

@TcpController
public class SocketConnectionController {

    @Autowired
    private TcpServer tcpServer;

    @Autowired
    private ConnectionService connectionService;

    public void receiveData(Connection connection, byte[] data) {
        String s = new String(data);
        connection.send(s.toUpperCase().getBytes());
    }

    public void connect(Connection connection) {
        System.out.println("New connection " + connection.getAddress().getCanonicalHostName());

        connectionService.addNewConnection(connection);
    }

    public void disconnect(Connection connection) {
        System.out.println("Disconnect " + connection.getAddress().getCanonicalHostName());
    }
}
