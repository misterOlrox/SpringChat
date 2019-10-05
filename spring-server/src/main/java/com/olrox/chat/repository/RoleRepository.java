package com.olrox.chat.repository;

import com.olrox.chat.entity.ChatRoom;
import com.olrox.chat.entity.Role;
import com.olrox.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select role.type " +
            "from Role as role " +
            "where role.user=:user and role.chatRoom=:chatRoom")
    Role.Type findRoleType(@Param(value = "user") User user,
                           @Param(value = "chatRoom") ChatRoom chatRoom);
}
