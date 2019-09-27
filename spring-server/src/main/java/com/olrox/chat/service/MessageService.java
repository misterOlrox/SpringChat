package com.olrox.chat.service;

import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.MessageType;
import com.olrox.chat.entity.User;
import com.olrox.chat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message createUserMessage(User user, String text) {
        Message message = new Message();
        message.setSender(user);
        message.setSendTime(LocalDateTime.now());

        messageRepository.save(message);

        return message;
    }

    public Message createGreetingMessage(User user) {
        Message greeting = new Message();
        greeting.setType(MessageType.SERVER_TO_USER);
        greeting.setSendTime(LocalDateTime.now());
        greeting.setText("Hello ಠ_ಠ");
        greeting.setRecipient(user);

        messageRepository.save(greeting);

        return greeting;
    }

    public Message createRegisterInfoMessage(User user) {
        Message message = new Message();
        message.setType(MessageType.SERVER_TO_USER);
        message.setSendTime(LocalDateTime.now());
        message.setText("Print \"/register [agent|client] YourName\" to register");
        message.setRecipient(user);

        messageRepository.save(message);

        return message;
    }

    public Message createServerMessage(String text) {
        Message message = new Message();
        message.setType(MessageType.SERVER_TO_USER);
        message.setSendTime(LocalDateTime.now());
        message.setText(text);

        messageRepository.save(message);

        return message;
    }
}
