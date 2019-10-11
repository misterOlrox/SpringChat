package com.olrox.chat.service;

import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.MessageDetail;
import com.olrox.chat.entity.User;
import com.olrox.chat.repository.MessageDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageDetailService {

    @Autowired
    private MessageDetailRepository messageDetailRepository;

    public MessageDetail create(Message message, User recipient, MessageDetail.Status status) {
        MessageDetail detail = new MessageDetail();
        detail.setMessage(message);
        detail.setStatus(status);
        detail.setUser(recipient);

        message.addMessageDetail(detail);

        return messageDetailRepository.save(detail);
    }

    public void markAsReceived(MessageDetail detail) {
        detail.setStatus(MessageDetail.Status.RECEIVED);
        messageDetailRepository.save(detail);
    }

    public List<MessageDetail> findAllFor(User user) {
        return messageDetailRepository.findAllFor(user);
    }
}
