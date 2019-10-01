package com.olrox.chat.repository;

import com.olrox.chat.entity.Role;
import com.olrox.chat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByName(String name);

    List<User> findAllByCurrentRoleTypeEquals(Role.Type role);
}
