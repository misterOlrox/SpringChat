package com.olrox.chat.session.state;

import com.olrox.chat.entity.Message;
import com.olrox.chat.service.UserService;
import com.olrox.chat.session.ChatSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusyClientService implements ChatService {

    private final static Logger LOGGER = LogManager.getLogger(BusyClientService.class);

    private ChatSession chatSession;
    private BusyAgentService companion;

    private UserService userService;

    public BusyClientService() {
    }

    public BusyClientService(ChatSession chatSession, BusyAgentService companion) {
        this.chatSession = chatSession;
        this.companion = companion;
    }

    public BusyClientService(FreeClientService client) {
        this.chatSession = client.getChatSession();
    }

    @Autowired
    public BusyClientService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void register(Message message) {
        chatSession.receiveFromServer("You are client " + this.chatSession.getUserName() + " chatting with agent " +
                companion.getChatSession().getUserName() + ". You needn't to register.");
    }

    @Override
    public void sendMessage(Message message) {
        companion.receiveFromClient(message);
    }

    public void receiveFromAgent(Message message) {
        chatSession.receiveFromUser(message);
    }

    @Override
    public void leave() {
        LOGGER.info("Client " + this.chatSession.getUserName() +
                " leave chat with agent " + companion.getChatSession().getUserName());

        this.chatSession.receiveFromServer("You left agent " + companion.getChatSession().getUserName());
        companion.getChatSession().receiveFromServer("Client " + this.chatSession.getUserName() + " left.");

        this.setFree();
        companion.setFree();
    }

    @Override
    public void exit() {
        LOGGER.info("Client " + this.chatSession.getUserName() +
                " exit from chat with agent " + companion.getChatSession().getUserName());

        companion.getChatSession().receiveFromServer("Client " + this.chatSession.getUserName() + " exited.");
        userService.removeOnlineUser(this.chatSession.getUserName());
        companion.setFree();
    }

    public synchronized void setCompanion(BusyAgentService companion) {
        this.companion = companion;
    }

    public synchronized void setFree(){
        FreeClientService freeClient = new FreeClientService(this);
        this.chatSession.setState(freeClient);
    }

    public ChatSession getChatSession() {
        return chatSession;
    }
}
