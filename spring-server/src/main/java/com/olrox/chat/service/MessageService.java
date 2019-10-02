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

    public void markMessageAsDelivered(Message message) {
        message.setDelivered(true);
        messageRepository.save(message);
    }

    public Message createUserMessage(User user, String text, MessageType type) {
        Message message = new Message();
        message.setSender(user);
        message.setSendTime(LocalDateTime.now());
        message.setType(type);
        message.setText(text);

        return messageRepository.save(message);
    }

    public Message createGreetingMessage(User user) {
        Message greeting = new Message();
        greeting.setType(MessageType.SERVER_INFO);
        greeting.setSendTime(LocalDateTime.now());
        greeting.setText("Hello ಠ_ಠ");
        greeting.setRecipient(user);

        return messageRepository.save(greeting);
    }

    public Message createRegisterInfoMessage(User user) {
        Message message = new Message();
        message.setType(MessageType.SERVER_INFO);
        message.setSendTime(LocalDateTime.now());
        message.setText("Print \"/register [agent|client] YourName\" to register");
        message.setRecipient(user);

        return messageRepository.save(message);
    }

    public Message createInfoMessage(User user, String text) {
        Message message = new Message();
        message.setType(MessageType.SERVER_INFO);
        message.setSendTime(LocalDateTime.now());
        message.setText(text);
        message.setRecipient(user);

        return messageRepository.save(message);
    }

    public Message createErrorMessage(User user, String errorDetails) {
        Message message = new Message();
        message.setType(MessageType.SERVER_ERROR);
        message.setSendTime(LocalDateTime.now());
        message.setText(errorDetails);
        message.setRecipient(user);

        return messageRepository.save(message);
    }
}
