package com.olrox.chat.controller.chat.handler;

import com.olrox.chat.entity.User;
import com.olrox.chat.service.MessageService;
import com.olrox.chat.service.SupportChatRoomService;
import com.olrox.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
@Order(value = 3)
public class ExitHandler implements CommandHandler {

    private final static String regex = "/exit";

    @Autowired
    private UserService userService;

    @Autowired
    private SupportChatRoomService supportChatRoomService;

    @Autowired
    private MessageService messageService;

    @Override
    public void handleCommand(User user, String data) {



    }

    @Override
    public boolean checkMatch(String data) {
        return data.matches(regex);
    }
}