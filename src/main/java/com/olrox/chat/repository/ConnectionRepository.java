package com.olrox.chat.repository;

import com.olrox.chat.entity.User;
import com.olrox.chat.service.IChatSession;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ConnectionRepository {
    private final Map<User, IChatSession> usersChatSessions = new ConcurrentHashMap<>();

    public IChatSession addChatSession(User user, IChatSession chatSession){
        return usersChatSessions.put(user, chatSession);
    }
}
