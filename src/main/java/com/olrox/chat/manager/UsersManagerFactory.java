package com.olrox.chat.manager;

public class UsersManagerFactory {
    public static UsersManager createUsersManager(){
        return new UsersManagerImpl();
    }
}
