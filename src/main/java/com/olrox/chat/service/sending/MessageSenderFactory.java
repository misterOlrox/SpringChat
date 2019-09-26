package com.olrox.chat.service.sending;

import com.olrox.chat.entity.ConnectionType;
import org.springframework.stereotype.Component;

@Component
public interface MessageSenderFactory {

    MessageSender getMessageSender(ConnectionType connectionType);
}
