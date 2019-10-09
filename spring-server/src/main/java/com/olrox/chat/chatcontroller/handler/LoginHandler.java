package com.olrox.chat.chatcontroller.handler;

import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.MessageType;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.MessageService;
import com.olrox.chat.service.SupportChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;

public class LoginHandler implements CommandHandler{
    private final static String regex = "/login .+";

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
