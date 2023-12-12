package ru.practicum.mainservice.mapper.user;

import ru.practicum.mainservice.dto.users.UserDto;
import ru.practicum.mainservice.models.users.User;

public class UserMapper {

    public static User mapToUser(UserDto forMapUser) {
        User newUser = new User();
        newUser.setName(forMapUser.getName());
        newUser.setEmail(forMapUser.getEmail());
        return newUser;
    }

    public static UserDto mapToUserDto(User user) {
        UserDto newUserDto = new UserDto();
        newUserDto.setId(user.getId());
        newUserDto.setName(user.getName());
        newUserDto.setEmail(user.getEmail());
        return newUserDto;
    }
}
