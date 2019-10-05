package com.olrox.chat.dto;

import com.olrox.chat.entity.ChatRoom;
import com.olrox.chat.entity.SupportChatRoom;

public class SupportChatRoomDto extends ChatRoomDto {

    private SupportChatRoom.State state;

    public SupportChatRoomDto(ChatRoom chatRoom) {
        super(chatRoom);
    }

    public SupportChatRoomDto(SupportChatRoom supportChatRoom) {
        super(supportChatRoom);
        this.state = supportChatRoom.getState();
    }

    public SupportChatRoom.State getState() {
        return state;
    }

    public void setState(SupportChatRoom.State state) {
        this.state = state;
    }
}
