package com.olrox.chat.dto;

import com.olrox.chat.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class EntityToDtoConverter {
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());

        return userDto;
    }

    public Page<UserDto> toDto(Page<User> page) {
        return page.map(this::toDto);
    }

    public UserDetailsDto toDetailsDto(User user) {
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setId(user.getId());
        userDetailsDto.setName(user.getName());
        userDetailsDto.setConnectionType(user.getConnectionType());
        userDetailsDto.setCurrentRoleType(user.getCurrentRoleType());

        return userDetailsDto;
    }
}
