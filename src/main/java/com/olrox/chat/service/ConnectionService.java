package com.olrox.chat.service;

import com.olrox.chat.entity.User;
import com.olrox.chat.repository.ConnectionRepository;
import com.olrox.chat.service.sending.ChatSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private MessageFactoryService messageFactoryService;

    public void addNewChatSession(ChatSession chatSession) {
        User newUser = new User();

        connectionRepository.addChatSession(newUser, chatSession);

        chatSession.send(messageFactoryService.createGreetingMessage());

        chatSession.send(messageFactoryService.createRegisterInfoMessage());
    }

    public User findUserBy(ChatSession chatSession) {
        return connectionRepository.findUserBy(chatSession);
    }
}
