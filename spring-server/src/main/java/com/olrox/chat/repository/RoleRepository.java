package com.olrox.chat.repository;

import com.olrox.chat.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {


    // replace joins with ids?
//    @Query("select role.type " +
//            "from Role as role " +
//            "join User as user " +
//            "join ChatRoom as chatroom " +
//            "where user=:user and chatroom=:chatRoom")
//    Role.Type findRoleTypeForUserInChatRoom(User user, ChatRoom chatRoom);
}
