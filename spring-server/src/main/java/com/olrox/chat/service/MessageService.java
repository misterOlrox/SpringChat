package com.olrox.chat.service;

import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.MessageType;
import com.olrox.chat.entity.User;
import com.olrox.chat.entity.MessageDetail;
import com.olrox.chat.repository.MessageDetailRepository;
import com.olrox.chat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageDetailsService messageDetailsService;

    @Transactional
    public Message createUserMessage(User sender, String text, MessageType type) {
        Message message = new Message();
        message.setSender(sender);
        message.setSendTime(LocalDateTime.now());
        message.setType(type);
        message.setText(text);

        return messageRepository.save(message);
    }

    @Transactional
    public Message createGreetingMessage(User recipient) {
        Message greeting = new Message();
        greeting.setType(MessageType.SERVER_INFO);
        greeting.setSendTime(LocalDateTime.now());
        greeting.setText("Hello ಠ_ಠ");

        MessageDetail detail = messageDetailsService.create(greeting, recipient, MessageDetail.Status.NOT_RECEIVED);
        List<MessageDetail> messageDetails = new ArrayList<>();
        messageDetails.add(detail);
        greeting.setMessageDetails(messageDetails);

        return messageRepository.save(greeting);
    }

    @Transactional
    public Message createRegisterInfoMessage(User recipient) {
        Message message = new Message();
        message.setType(MessageType.SERVER_INFO);
        message.setSendTime(LocalDateTime.now());
        message.setText("Print \"/register [agent|client] YourName\" to register");

        MessageDetail detail = messageDetailsService.create(message, recipient, MessageDetail.Status.NOT_RECEIVED);
        List<MessageDetail> messageDetails = new ArrayList<>();
        messageDetails.add(detail);
        message.setMessageDetails(messageDetails);

        message.setMessageDetails(messageDetails);

        return messageRepository.save(message);
    }

    @Transactional
    public Message createInfoMessage(User recipient, String text) {
        Message message = new Message();
        message.setType(MessageType.SERVER_INFO);
        message.setSendTime(LocalDateTime.now());
        message.setText(text);

        MessageDetail detail = messageDetailsService.create(message, recipient, MessageDetail.Status.NOT_RECEIVED);
        List<MessageDetail> messageDetails = new ArrayList<>();
        messageDetails.add(detail);
        message.setMessageDetails(messageDetails);

        message.setMessageDetails(messageDetails);

        return messageRepository.save(message);
    }

    @Transactional
    public Message createErrorMessage(User recipient, String errorDetails) {
        Message message = new Message();
        message.setType(MessageType.SERVER_ERROR);
        message.setSendTime(LocalDateTime.now());
        message.setText(errorDetails);

        MessageDetail detail = messageDetailsService.create(message, recipient, MessageDetail.Status.NOT_RECEIVED);
        List<MessageDetail> messageDetails = new ArrayList<>();
        messageDetails.add(detail);
        message.setMessageDetails(messageDetails);

        message.setMessageDetails(messageDetails);

        return messageRepository.save(message);
    }
}
