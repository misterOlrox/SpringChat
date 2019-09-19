package com.olrox.chat.repository;

import com.olrox.chat.tcp.Connection;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.chatsession.IChatSession;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ConnectionRepository {
    private final Map<Connection, IChatSession> tcpSessions = new ConcurrentHashMap<>();
    private final Map<User, IChatSession> userToSession = new ConcurrentHashMap<>();

    public IChatSession addChatSession(User user, IChatSession chatSession){
        return userToSession.put(user, chatSession);
    }

    //public IChatSession getSession()

}
