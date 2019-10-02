package com.olrox.chat.repository;

import com.olrox.chat.entity.MessageDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDetailRepository extends JpaRepository<MessageDetail, Long> {
}
