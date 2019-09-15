package com.olrox.chat.message.author;

import com.olrox.chat.manager.UsersManager;
import com.olrox.chat.manager.UsersManagerFactory;

public class ServerAsAuthor implements Author {
    private final static String NAME = "Server";
    private final static ServerAsAuthor INSTANCE = new ServerAsAuthor();

    private ServerAsAuthor(){
        UsersManager usersManager = UsersManagerFactory.createUsersManager();
        usersManager.addOnlineUser(this.getUsername());
    }

    public static ServerAsAuthor getInstance(){
        return INSTANCE;
    }

    @Override
    public String getUsername() {
        return NAME;
    }

    @Override
    public AuthorType getAuthorType() {
        return AuthorType.SERVER;
    }
}
