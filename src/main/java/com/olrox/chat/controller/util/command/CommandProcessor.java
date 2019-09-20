package com.olrox.chat.controller.util.command;

import com.olrox.chat.entity.User;
import com.olrox.chat.service.ConnectionService;
import com.olrox.chat.service.MessageFactoryService;
import com.olrox.chat.service.UserService;
import com.olrox.chat.service.sending.ChatSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommandProcessor {

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageFactoryService messageFactoryService;

    public void process(ChatSession chatSession, ParsedCommand parsedCommand){
        switch (parsedCommand.getType()){
            case REGISTER:
                register(chatSession, parsedCommand.getParams());
        }
    }

    private void register(ChatSession chatSession, List<String> params){
        User user = connectionService.findUserBy(chatSession);
        if(user.getName() != null && !user.getName().isEmpty()){
            chatSession.send(messageFactoryService.createServerMessage(
                    "You are already registered as " + user.getName()));

            return;
        }



    }
}
