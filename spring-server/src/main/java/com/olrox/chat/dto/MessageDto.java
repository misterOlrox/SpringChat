package com.olrox.chat.dto;

import com.olrox.chat.entity.ChatRoom;
import com.olrox.chat.entity.MessageDetail;
import com.olrox.chat.entity.MessageType;
import com.olrox.chat.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDto {

    private Long id;
    private MessageType type;
    private ChatRoom chatRoom;
    private String text;
    private LocalDateTime sendTime;
    private User sender;
    private List<MessageDetail> messageDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public List<MessageDetail> getMessageDetails() {
        return messageDetails;
    }

    public void setMessageDetails(List<MessageDetail> messageDetails) {
        this.messageDetails = messageDetails;
    }

    public void addMessageDetail(MessageDetail messageDetail) {
        if(getMessageDetails() == null) {
            setMessageDetails(new ArrayList<>());
        }
        messageDetails.add(messageDetail);
    }
}
