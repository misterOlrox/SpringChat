package com.olrox.chat.service;

import com.olrox.chat.tcp.Connection;
import com.olrox.chat.repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageHandlingService {

    @Autowired
    private ConnectionRepository connectionRepository;

    public void handleMessage(Connection connection, String messageFromUser) {

    }
}
