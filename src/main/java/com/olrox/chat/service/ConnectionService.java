package com.olrox.chat.service;

import com.olrox.chat.entity.User;
import com.olrox.chat.repository.ConnectionRepository;
import com.olrox.chat.service.chatsession.IChatSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private MessageFactoryService messageFactoryService;

    public void addNewChatSession(IChatSession chatSession) {
        connectionRepository.addChatSession(chatSession.getUser(), chatSession);

        chatSession.send(messageFactoryService.createGreetingMessage());

        chatSession.send(messageFactoryService.createRegisterInfoMessage());
    }
}
