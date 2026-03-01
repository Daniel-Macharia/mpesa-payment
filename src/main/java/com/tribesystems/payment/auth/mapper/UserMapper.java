package com.tribesystems.payment.auth.mapper;

import com.tribesystems.payment.auth.dto.UserDto;
import com.tribesystems.payment.auth.enums.UserRole;
import com.tribesystems.payment.auth.model.User;

public class UserMapper {

    public static User userDtoToUserMapper(UserDto userDto)
    {
        return User.builder()
                .username(userDto.username())
                .password(userDto.password())
                .role(UserRole.valueOf(userDto.role()))
                .build();
    }

    public static UserDto userToUserDtoMapper(User user)
    {
        return new UserDto(
                user.getUsername(),
                user.getPassword(),
                user.getRole().name()
        );
    }
}
