package com.olrox.chat.repository;

import com.olrox.chat.entity.SupportChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportChatRoomRepository extends JpaRepository<SupportChatRoom, Long> {

    SupportChatRoom findFirstByStateOrderByCreationDateAsc(SupportChatRoom.State state);
}
