package com.olrox.chat.repository;

import com.olrox.chat.entity.MessageDetail;
import com.olrox.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageDetailRepository extends JpaRepository<MessageDetail, Long> {

    @Query("select detail " +
            "from MessageDetail as detail " +
            "join Message as message " +
            "on message.id=detail.message.id " +
            "where detail.user=:user " +
            "order by message.sendTime asc")
    List<MessageDetail> findAllFor(@Param(value = "user") User user);
}
