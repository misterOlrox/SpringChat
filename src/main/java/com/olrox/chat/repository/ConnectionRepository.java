package com.olrox.chat.repository;

import com.olrox.chat.entity.User;
import com.olrox.chat.service.sending.ChatSession;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ConnectionRepository {
    private final Map<User, ChatSession> userMap = new ConcurrentHashMap<>();
    private final Map<ChatSession, User> chatSessionMap = new ConcurrentHashMap<>();

    public void addChatSession(User user, ChatSession chatSession){
        chatSessionMap.put(chatSession, user);
        userMap.put(user, chatSession);
    }

    public User findUserBy(ChatSession chatSession){
        return chatSessionMap.get(chatSession);
    }

    //public IChatSession getSession()

}
