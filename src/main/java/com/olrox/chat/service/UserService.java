package com.olrox.chat.service;

import com.olrox.chat.session.ChatSession;

public interface UserService {

    ChatSession pollFreeClient();

    ChatSession pollFreeAgent();

    void addFreeClient(ChatSession client);

    void addFreeAgent(ChatSession agent);

    boolean addOnlineUser(String username);

    void removeOnlineUser(String username);
}
