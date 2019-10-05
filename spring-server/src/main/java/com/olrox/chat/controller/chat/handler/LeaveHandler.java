package com.olrox.chat.controller.chat.handler;

import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.MessageType;
import com.olrox.chat.entity.SupportChatRoom;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.MessageService;
import com.olrox.chat.service.SupportChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 2)
public class LeaveHandler implements CommandHandler {

    private final static String regex = "/leave";

    @Autowired
    private SupportChatRoomService supportChatRoomService;

    @Autowired
    private MessageService messageService;

    @Override
    public void handleCommand(User user, String data) {
        Message message = messageService.createUserMessage(user, data, MessageType.USER_TO_SERVER);

        supportChatRoomService.leaveChat(user, message);
    }

    @Override
    public boolean checkMatch(String data) {
        return data.matches(regex);
    }
}
