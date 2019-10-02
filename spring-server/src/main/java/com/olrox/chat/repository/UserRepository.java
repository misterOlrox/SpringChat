package com.olrox.chat.repository;

import com.olrox.chat.entity.Role;
import com.olrox.chat.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);

    List<User> findAllByCurrentRoleTypeEquals(Role.Type role);

    Page<User> findAllByCurrentRoleTypeEquals(Role.Type currentRoleType, Pageable pageable);

    @Query("SELECT chat.userList\n" +
            "FROM SupportChatRoom as chat\n" +
            "WHERE chat.state='NEED_CLIENT'")
    Page<User> findFreeAgents(Pageable pageable);
//
//    @Query("SELECT user.id FROM\n" +
//            "user_table user\n" +
//            "JOIN chat_room_user_table join_table\n" +
//            "ON user.id=join_table.user_id\n" +
//            "JOIN chat_room_table chat\n" +
//            "ON join_table.chat_room_id=chat.id\n" +
//            "WHERE chat.state='NEED_CLIENT'")
//    List<User> findFreeAgents();
}
