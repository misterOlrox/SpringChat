package com.olrox.chat.entity.message.author;

public class ServerAsAuthor implements Author {
    private final static String NAME = "Server";
    private final static ServerAsAuthor INSTANCE = new ServerAsAuthor();

    private ServerAsAuthor(){
    }

    public static ServerAsAuthor getInstance(){
        return INSTANCE;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public AuthorType getAuthorType() {
        return AuthorType.SERVER;
    }
}
