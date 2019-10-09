package com.olrox.chat.user;

import com.olrox.chat.user.thread.ReadThread;
import com.olrox.chat.user.thread.WriteThread;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Connection {
    private String hostname;
    private int port;

    public Connection(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void start() {
        try {
            SocketFactory socketFactory = SSLSocketFactory.getDefault();
            //Socket socket = new Socket(hostname, port);
            SSLSocket socket = (SSLSocket) socketFactory.createSocket(hostname, port);

            System.out.println("Connected to the chat server");

            new ReadThread(socket).start();
            new WriteThread(socket).start();
            socket.startHandshake();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
