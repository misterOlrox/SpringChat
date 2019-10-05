package com.olrox.chat.dto;

import com.olrox.chat.entity.ChatRoom;
import com.olrox.chat.entity.SupportChatRoom;
import com.olrox.chat.entity.User;

public class DetailedSupportChatRoomDto extends SupportChatRoomDto {

    private UserDto client;
    private UserDto agent;

    public DetailedSupportChatRoomDto(ChatRoom chatRoom) {
        super(chatRoom);
    }

    public DetailedSupportChatRoomDto(SupportChatRoom supportChatRoom) {
        super(supportChatRoom);

        User agent = supportChatRoom.getAgent();
        if(agent != null)
            this.agent = new UserDto(agent);

        User client = supportChatRoom.getClient();
        if(client != null)
            this.client = new UserDto(client);
    }

    public UserDto getClient() {
        return client;
    }

    public void setClient(UserDto client) {
        this.client = client;
    }

    public UserDto getAgent() {
        return agent;
    }

    public void setAgent(UserDto agent) {
        this.agent = agent;
    }
}
