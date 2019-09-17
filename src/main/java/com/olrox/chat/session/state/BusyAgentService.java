package com.olrox.chat.session.state;

import com.olrox.chat.entity.Message;
import com.olrox.chat.service.UserService;
import com.olrox.chat.session.ChatSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusyAgentService implements ChatService {

    private final static Logger LOGGER = LogManager.getLogger(BusyAgentService.class);

    private ChatSession chatSession;
    private BusyClientService companion;

    private UserService userService;

    public BusyAgentService() {
    }

    // for testing
    public BusyAgentService(ChatSession chatSession, BusyClientService companion) {
        this.chatSession = chatSession;
        this.companion = companion;
    }

    public BusyAgentService(FreeAgentService agent) {
        this.chatSession = agent.getChatSession();
    }

    @Autowired
    public BusyAgentService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void register(Message message) {
        chatSession.receiveFromServer("You are agent " + this.chatSession.getUserName() + " chatting with client " +
                companion.getChatSession().getUserName() + ". You needn't to register.");
    }

    @Override
    public void sendMessage(Message message) {
        companion.receiveFromAgent(message);
    }

    public void receiveFromClient(Message message) {
        chatSession.receiveFromUser(message);
    }

    @Override
    public void leave() {
        LOGGER.info("Agent " + this.chatSession.getUserName() +
                " leave chat with client " + companion.getChatSession().getUserName());

        this.chatSession.receiveFromServer("You left client " + companion.getChatSession().getUserName());
        companion.getChatSession().receiveFromServer("Agent " + this.chatSession.getUserName() + " left.");

        this.setFree();
        companion.setFree();
    }

    @Override
    public void exit() {
        LOGGER.info("Agent " + this.chatSession.getUserName() +
                " exit from chat with client " + companion.getChatSession().getUserName());

        companion.getChatSession().receiveFromServer("Agent " + this.chatSession.getUserName() + " exited.");
        userService.removeOnlineUser(this.chatSession.getUserName());
        companion.setFree();
    }

    public synchronized void setCompanion(BusyClientService companion) {
        this.companion = companion;
    }

    public synchronized void setFree(){
        FreeAgentService freeAgent = new FreeAgentService(this);
        this.chatSession.setState(freeAgent);
        freeAgent.findCompanion();
    }

    public ChatSession getChatSession() {
        return chatSession;
    }
}
