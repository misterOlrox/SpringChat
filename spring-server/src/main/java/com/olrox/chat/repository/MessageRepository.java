package com.olrox.chat.repository;

import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select detail.message " +
            "from MessageDetail as detail " +
            "where detail.user.id=:recipientId " +
            "and detail.status='NOT_RECEIVED' ")
    Page<Message> findUnreceivedMessages(@Param(value = "recipientId") Long recipientId, Pageable pageable);
}
