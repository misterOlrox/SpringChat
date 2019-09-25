package com.olrox.chat.repository;

import com.olrox.chat.service.sending.ChatSession;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ChatSessionRepository {
    private final Map<Long, List<ChatSession>> userMap = new ConcurrentHashMap<>();
    private final Map<ChatSession, Long> chatSessionMap = new ConcurrentHashMap<>();

    public void addChatSession(long userId, ChatSession chatSession) {
        if ((userMap.containsKey(userId))) {
            userMap.get(userId).add(chatSession);
        } else {
            List<ChatSession> usersSessions = new ArrayList<>();
            usersSessions.add(chatSession);
            userMap.put(userId, usersSessions);
        }

        chatSessionMap.put(chatSession, userId);
    }
}
