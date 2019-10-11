package com.olrox.chat.chatcontroller.handler;

import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.MessageType;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.MessageService;
import com.olrox.chat.service.SupportChatRoomService;
import com.olrox.chat.service.sending.GeneralSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 255)
public class SendMessageHandler implements CommandHandler {

    private final static String regex = ".+";

    @Autowired
    private SupportChatRoomService supportChatRoomService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private GeneralSender generalSender;

    @Override
    public void handleCommand(User user, String data) {
        Message message = messageService.createUserMessage(user, data, MessageType.USER_TO_CHAT);

        if (!user.isRegistered()) {
            Message infoMessage = messageService.createRegisterInfoMessage(user);
            generalSender.send(infoMessage);
            return;
        }

        supportChatRoomService.broadcast(message);
    }

    @Override
    public boolean checkMatch(String data) {
        return data.matches(regex);
    }
}