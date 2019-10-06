package com.olrox.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.olrox.chat.entity.*;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageDto {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;
    private Long id;
    private MessageType type;
    private Long chatRoomId;
    private String text;
    private Long senderId;

    public MessageDto(){
    }

    public MessageDto(Message message) {
        this.id = message.getId();
        this.type = message.getType();
        this.text = message.getText();
        this.sendTime = message.getSendTime();

        ChatRoom chatRoom = message.getChatRoom();
        if(chatRoom != null) {
            this.chatRoomId = chatRoom.getId();
        }

        User sender = message.getSender();
        if(sender != null) {
            this.senderId = sender.getId();
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

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
}
