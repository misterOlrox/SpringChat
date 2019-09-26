package com.olrox.chat.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "chat_room_table")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<User> userList;

    @OneToMany
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
