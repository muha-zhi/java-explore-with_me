package ru.practicum.mainservice.controllers.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.users.UserDto;
import ru.practicum.mainservice.service.user.AdminUserService;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Slf4j
@RequiredArgsConstructor
public class AdminUsersController {

    private final AdminUserService adminUserService;


    @GetMapping()
    public List<UserDto> getUserInfoList(@RequestParam(value = "ids", required = false) List<Long> ids,
                                         @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                         @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.info("GET IDS: {}, FROM: {}, SIZE: {}", ids, from, size);
        return adminUserService.getUserListByIdsPagination(ids, from, size);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(@RequestBody @Validated UserDto userDto) {
        log.info("POST USER: {}", userDto);
        return adminUserService.saveUser(userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        adminUserService.deleteUserById(userId);
        log.info("DELETE USER: {}", userId);
    }
}
