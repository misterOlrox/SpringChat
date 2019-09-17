package com.olrox.chat.service;

import com.olrox.chat.command.CommandType;
import com.olrox.chat.entity.Message;
import com.olrox.chat.session.ChatSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageHandlingService {

    private final CommandParser commandParser;

    @Autowired
    public MessageHandlingService(CommandParser commandParser) {
        this.commandParser = commandParser;
    }

    public void handleMessage(Message message, ChatSession session) {
        message.setAuthor(session.getUser());

        CommandType command = commandParser.parse(message.getText());

        switch(command) {
            case REGISTER:
                session.register(message);
                break;
            case SEND_MESSAGE:
                session.sendMessage(message);
                break;
            case LEAVE:
                session.leave();
                break;
            case EXIT:
                session.exit();
        }
    }
}
