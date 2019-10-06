package com.olrox.chat.repository;

import com.olrox.chat.entity.Role;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);

    List<User> findAllByCurrentRoleTypeEquals(Role.Type role);

    Page<User> findAllByCurrentRoleTypeEquals(Role.Type currentRoleType, Pageable pageable);

    @Query("SELECT chat.agent " +
            "FROM SupportChatRoom as chat " +
            "WHERE chat.state='NEED_CLIENT' " +
            "ORDER BY chat.creationDate ASC ")
    Page<User> findFreeAgents(Pageable pageable);

    @Query("SELECT count(chat) " +
            "FROM SupportChatRoom as chat " +
            "WHERE chat.state='NEED_CLIENT' ")
    Long countFreeAgents();

    @Query("SELECT chat.client " +
            "FROM SupportChatRoom as chat " +
            "WHERE chat.state='NEED_AGENT' " +
            "ORDER BY chat.creationDate ASC ")
    Page<User> findClientsInQueue(Pageable pageable);

    Optional<User> findFirstByIdAndCurrentRoleType(long id, Role.Type role);
}
