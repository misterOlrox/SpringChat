package com.olrox.chat.entity;

import com.olrox.chat.entity.message.Message;

import java.util.List;

public class ChatRoom {
    private List<User> userList;
    private List<Message> messageHistory;

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<Message> getMessageHistory() {
        return messageHistory;
    }

    public void setMessageHistory(List<Message> messageHistory) {
        this.messageHistory = messageHistory;
    }
}
