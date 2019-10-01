package com.olrox.chat.controller.chat.handler;

import com.olrox.chat.controller.util.MessageParser;
import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.MessageType;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.SupportChatRoomService;
import com.olrox.chat.service.MessageService;
import com.olrox.chat.service.UserService;
import com.olrox.chat.service.sending.GeneralSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 4)
public class SendMessageHandler implements CommandHandler {

    private final static String regex = ".+";

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
        messageService.createUserMessage(user, data, MessageType.USER_TO_CHAT);

        if (!user.isRegistered()) {
            Message message = messageService.createRegisterInfoMessage(user);
            generalSender.send(message);
        } else if (user.getChatRooms() == null || user.getChatRooms().isEmpty()) {
            Message message = messageService.createInfoMessage(user, "You aren't chatting. Your message will not be delivered.");
            generalSender.send(message);
        }


    }

    @Override
    public boolean checkMatch(String data) {
        return data.matches(regex);
    }
}