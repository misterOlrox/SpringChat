package com.olrox.chat.repository;

import com.olrox.chat.entity.SupportChatRoom;
import com.olrox.chat.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportChatRoomRepository extends JpaRepository<SupportChatRoom, Long> {

    SupportChatRoom findFirstByStateOrderByCreationDateAsc(SupportChatRoom.State state);

    SupportChatRoom findFirstByAgentEqualsOrderByCreationDateDesc(User agent);

    SupportChatRoom findFirstByClientEqualsOrderByCreationDateDesc(User client);

    Page<SupportChatRoom> findAllByStateIsNot(SupportChatRoom.State state, Pageable pageable);
}
