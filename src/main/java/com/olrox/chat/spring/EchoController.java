package com.olrox.chat.spring;

import com.olrox.chat.spring.tcp.Connection;
import com.olrox.chat.spring.tcp.TcpController;

@TcpController
public class EchoController {

    public void receiveData(Connection connection, byte[] data) {
        String s = new String(data);
        connection.send(s.toUpperCase().getBytes());
    }

    public void connect(Connection connection) {
        System.out.println("New connection " + connection.getAddress().getCanonicalHostName());
    }

    public void disconnect(Connection connection) {
        System.out.println("Disconnect " + connection.getAddress().getCanonicalHostName());
    }
}
