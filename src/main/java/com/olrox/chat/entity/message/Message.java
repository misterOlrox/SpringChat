package com.olrox.chat.entity.message;

import com.olrox.chat.entity.ChatRoom;
import com.olrox.chat.entity.User;
import com.olrox.chat.entity.message.author.Author;

import java.time.LocalDateTime;

public class Message {
    private Author sender;
    private ChatRoom chatRoom;
    private User recipient;
    private String text;
    private LocalDateTime sendTime;

    public Message() {
    }

    public Author getSender() {
        return sender;
    }

    public void setSender(Author sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
}
