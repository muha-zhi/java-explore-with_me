package ru.practicum.mainservice.controllers.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.request.RequestDto;
import ru.practicum.mainservice.service.request.PrivateRequestService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PrivateRequestController {

    private final PrivateRequestService privateRequestService;

    @GetMapping("/users/{userId}/requests")
    public List<RequestDto> getUserRequests(@PathVariable Long userId) {

        log.info("GET USERID: {}", userId);

        return privateRequestService.getUserRequests(userId);
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(value = HttpStatus.CREATED)
    public RequestDto postUserRequests(@PathVariable Long userId,
                                       @RequestParam Long eventId) {
        log.info("POST USERID: {}, EVENTID: {}", userId, eventId);
        return privateRequestService.postUserRequest(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long userId,
                                    @PathVariable Long requestId) {
        log.info("PATCH USERID: {}, REQUESTID: {}", userId, requestId);
        return privateRequestService.cancelRequest(userId, requestId);
    }
}
