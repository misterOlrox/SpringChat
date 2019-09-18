package com.olrox.chat.service;

import com.olrox.chat.controller.custom.tcp.Connection;
import com.olrox.chat.entity.User;
import com.olrox.chat.entity.message.Message;
import com.olrox.chat.entity.message.author.ServerAsAuthor;
import com.olrox.chat.repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;

    public void addNewConnection(Connection connection) {
        IChatSession chatSession = new TcpChatSession(connection);

        User newUser = new User();

        connectionRepository.addChatSession(newUser, chatSession);

        Message greeting1 = new Message();
        greeting1.setSender(ServerAsAuthor.getInstance());
        greeting1.setSendTime(LocalDateTime.now());
        greeting1.setText("Hello ಠ_ಠ");

        chatSession.send(greeting1);
    }
}
