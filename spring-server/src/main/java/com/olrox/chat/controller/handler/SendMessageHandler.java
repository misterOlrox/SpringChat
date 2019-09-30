package com.olrox.chat.controller.handler;

import com.olrox.chat.controller.util.MessageParser;
import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.MessageType;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.ChatRoomService;
import com.olrox.chat.service.MessageService;
import com.olrox.chat.service.UserService;
import com.olrox.chat.service.sending.MessageSender;
import com.olrox.chat.service.sending.MessageSenderFactory;
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
    private ChatRoomService chatRoomService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageSenderFactory messageSenderFactory;

    @Autowired
    private MessageParser messageParser;

    @Override
    public void handleCommand(User user, String data) {
        messageService.createUserMessage(user, data, MessageType.USER_TO_CHAT);

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