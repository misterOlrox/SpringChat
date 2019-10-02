package com.olrox.chat.dto;

import com.olrox.chat.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityToDtoConverter {
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setConnectionType(user.getConnectionType());
        userDto.setCurrentRoleType(user.getCurrentRoleType());

        return userDto;
    }

    public Page<UserDto> toDto(Page<User> page) {
        return page.map(this::toDto);
    }
}
