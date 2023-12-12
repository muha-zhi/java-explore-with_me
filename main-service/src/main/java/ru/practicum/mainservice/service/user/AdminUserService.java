package ru.practicum.mainservice.service.user;


import org.springframework.stereotype.Service;
import ru.practicum.mainservice.dto.users.UserDto;
import ru.practicum.mainservice.models.users.User;

import java.util.List;

@Service
public interface AdminUserService {

    List<UserDto> getUserListByIdsPagination(List<Long> ids,
                                             int from,
                                             int size);

    UserDto saveUser(UserDto userForSave);

    void deleteUserById(Long userId);

    User getUserIfExist(Long userId);
}
