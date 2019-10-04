package com.olrox.chat.repository;

import com.olrox.chat.entity.ChatRoom;
import com.olrox.chat.entity.Role;
import com.olrox.chat.entity.SupportChatRoom;
import com.olrox.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {


    // FIXME replace joins with ids?
    @Query("select role.type " +
            "from Role as role " +
            "join User as user " +
            "join ChatRoom as chatroom " +
            "where user=:user and chatroom=:chatRoom")
    Role.Type findRoleTypeForUserInChatRoom(User user, ChatRoom chatRoom);
}
