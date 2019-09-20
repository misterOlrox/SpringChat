package com.olrox.chat.service;

import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.MessageType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageFactoryService {
    public Message createGreetingMessage(){
        Message greeting = new Message();
        greeting.setType(MessageType.SERVER_INFO);
        greeting.setSendTime(LocalDateTime.now());
        greeting.setText("Hello ಠ_ಠ");

        return greeting;
    }

    public Message createRegisterInfoMessage() {
        Message message = new Message();
        message.setType(MessageType.SERVER_INFO);
        message.setSendTime(LocalDateTime.now());
        message.setText("Print \"/register [agent|client] YourName\" to register");

        return message;
    }

    public Message createServerMessage(String text) {
        Message message = new Message();
        message.setType(MessageType.SERVER_INFO);
        message.setSendTime(LocalDateTime.now());
        message.setText(text);

        return message;
    }
}
