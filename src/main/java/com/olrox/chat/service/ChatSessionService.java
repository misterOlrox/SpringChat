package com.olrox.chat.service;

import com.olrox.chat.entity.User;
import com.olrox.chat.repository.ChatSessionRepository;
import com.olrox.chat.service.sending.ChatSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatSessionService {

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private MessageFactoryService messageFactoryService;

    public void addNewChatSession(ChatSession chatSession) {
        User newUser = new User();

        chatSessionRepository.addChatSession(newUser, chatSession);

        chatSession.send(messageFactoryService.createGreetingMessage());

        chatSession.send(messageFactoryService.createRegisterInfoMessage());
    }

//    public User findUserBy(ChatSession chatSession) {
//        return chatSessionRepository.findByUserId(chatSession);
//    }
}
