package com.olrox.chat.manager;

import com.olrox.chat.user.User;

public interface UsersManager {

    User pollFreeClient();

    User pollFreeAgent();

    void addFreeClient(User client);

    void addFreeAgent(User agent);

    boolean addOnlineUser(String username);

    void removeOnlineUser(String username);
}
