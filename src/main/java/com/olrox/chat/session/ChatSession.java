package com.olrox.chat.session;

import com.olrox.chat.controller.ChatHandler;
import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.User;
import com.olrox.chat.message.author.ServerAsAuthor;
import com.olrox.chat.session.state.ChatService;
import com.olrox.chat.session.state.UnauthorizedService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ChatSession {

    private final static Logger LOGGER = LogManager.getLogger(ChatSession.class);

    private User user;
    private ChatService state;
    private ChatHandler chatHandler;

    public ChatSession(ChatHandler ChatHandler) {
        this.chatHandler = ChatHandler;
        this.state = new UnauthorizedService(this);
    }

    public ChatSession(User user, ChatHandler ChatHandler, ChatService state) {
        this.user = user;
        this.chatHandler = ChatHandler;
        this.state = state;
    }

    public ChatService getState() {
        return state;
    }

    public void setState(ChatService state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserName(){
        return user.getName();
    }

    public void register(Message message){
        this.state.register(message);
    }

    public void sendMessage(Message message){
        this.state.sendMessage(message);
    }

    public void leave() {
        this.state.leave();
    }

    public void exit() {
        this.state.exit();
        if(user != null) LOGGER.info("User " + user.getName() + " exited");
        chatHandler.disconnect();
    }

    public void receiveFromUser(Message message) {
        chatHandler.getMessageWriter().write(message);
    }

    public void receiveFromServer(String text) {
        chatHandler.getMessageWriter().write(new Message(text, ServerAsAuthor.getInstance()));
    }
}
