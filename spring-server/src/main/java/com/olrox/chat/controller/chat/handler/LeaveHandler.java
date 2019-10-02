package com.olrox.chat.controller.chat.handler;

import com.olrox.chat.controller.chat.util.MessageParser;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.MessageService;
import com.olrox.chat.service.SupportChatRoomService;
import com.olrox.chat.service.UserService;
import com.olrox.chat.service.sending.GeneralSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 2)
public class LeaveHandler implements CommandHandler {

    private final static String regex = "/leave";

    @Autowired
    private UserService userService;

    @Autowired
    private SupportChatRoomService supportChatRoomService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private GeneralSender generalSender;

    @Autowired
    private MessageParser messageParser;

    @Override
    public void handleCommand(User user, String data) {

        generalSender.send(messageService.createInfoMessage(user, "You are trying to leave"));


    }

    @Override
    public boolean checkMatch(String data) {
        return data.matches(regex);
    }
}
