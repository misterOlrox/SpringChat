package com.olrox.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.olrox.chat.entity.*;

import java.time.LocalDateTime;

public class MessageDto {

    private Long id;
    private MessageType type;
    private ChatRoomDto chatRoom;
    private String text;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

    private UserDto sender;
    private MessageDetail.Status status;

    public MessageDto(Message message, MessageDetail.Status status) {
        this.id = message.getId();
        this.type = message.getType();
        this.text = message.getText();
        this.sendTime = message.getSendTime();
        this.status = status;

        ChatRoom chatRoom = message.getChatRoom();
        if(chatRoom != null) {
            this.chatRoom = new ChatRoomDto(chatRoom);
        }

        User sender = message.getSender();
        if(sender != null) {
            this.sender = new UserDto(sender);
        }
    }

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

    public ChatRoomDto getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoomDto chatRoom) {
        this.chatRoom = chatRoom;
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

    public UserDto getSender() {
        return sender;
    }

    public void setSender(UserDto sender) {
        this.sender = sender;
    }

    public MessageDetail.Status getStatus() {
        return status;
    }

    public void setStatus(MessageDetail.Status status) {
        this.status = status;
    }
}
