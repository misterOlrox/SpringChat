package com.olrox.chat.entity;

import com.olrox.chat.entity.message.author.Author;
import com.olrox.chat.entity.message.author.AuthorType;

public class User implements Author {
    private AuthorType authorType;
    private String name;

    public User(){}

    public User(String name, AuthorType authorType) {
        this.name = name;
        this.authorType = authorType;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public AuthorType getAuthorType() {
        return authorType;
    }

    public void setAuthorType(AuthorType authorType) {
        this.authorType = authorType;
    }
}
