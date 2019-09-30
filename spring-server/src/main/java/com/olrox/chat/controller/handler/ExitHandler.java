package com.olrox.chat.controller.handler;

import com.olrox.chat.controller.util.MessageParser;
import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.SupportChatRoomService;
import com.olrox.chat.service.MessageService;
import com.olrox.chat.service.UserService;
import com.olrox.chat.service.sending.MessageSender;
import com.olrox.chat.service.sending.MessageSenderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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

    @Autowired
    private MessageSenderFactory messageSenderFactory;

    @Autowired
    private MessageParser messageParser;

    @Override
    public void handleCommand(User user, String data) {
        MessageSender messageSender = messageSenderFactory.getMessageSender(user.getConnectionType());

        if (!user.isRegistered()) {
            Message message = messageService.createRegisterInfoMessage(user);
            messageSender.send(message);
        } else if (user.getChatRooms() == null || user.getChatRooms().isEmpty()) {
            Message message = messageService.createInfoMessage(user, "You aren't chatting. Your message will not be delivered.");
            messageSender.send(message);
        }


    }

    @Override
    public boolean checkMatch(String data) {
        return data.matches(regex);
    }
}