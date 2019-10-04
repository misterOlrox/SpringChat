package com.olrox.chat.repository;

import com.olrox.chat.entity.SupportChatRoom;
import com.olrox.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SupportChatRoomRepository extends JpaRepository<SupportChatRoom, Long> {

    SupportChatRoom findFirstByStateOrderByCreationDateAsc(SupportChatRoom.State state);

    SupportChatRoom findFirstByAgentEqualsOrderByCreationDateDesc(User agent);

    SupportChatRoom findFirstByClientEqualsOrderByCreationDateDesc(User client);

}
