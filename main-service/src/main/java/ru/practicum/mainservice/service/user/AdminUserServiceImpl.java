package ru.practicum.mainservice.service.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dao.user.AdminUserRepository;
import ru.practicum.mainservice.dto.users.UserDto;
import ru.practicum.mainservice.exceptions.DataNotFoundException;
import ru.practicum.mainservice.mapper.user.UserMapper;
import ru.practicum.mainservice.models.users.User;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class AdminUserServiceImpl implements AdminUserService {


    private final AdminUserRepository userRepository;


    @Override
    public List<UserDto> getUserListByIdsPagination(List<Long> ids, int from, int size) {
        List<User> usersForRet = userRepository.getAllUsersBIds(ids, PageRequest.of(from / size, size));

        log.info("GET ALL USERS BY IDS {}", ids);

        return usersForRet.stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto saveUser(UserDto userForSave) {
        User savedUser = userRepository.save(UserMapper.mapToUser(userForSave));

        log.info("Saved User {}", savedUser);

        return UserMapper.mapToUserDto(savedUser);
    }

    @Transactional
    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
        log.info("DELETE BY ID {}", userId);
    }

    public User getUserIfExist(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found" + userId));
    }
}
