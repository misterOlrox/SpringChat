package com.olrox.chat.service.sending;

import com.olrox.chat.entity.ConnectionType;

public interface MessageSenderFactory {

    MessageSender getMessageSender(ConnectionType connectionType);
}
