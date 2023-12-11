package ru.practicum.mainservice.controllers.events;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.events.EventDto;
import ru.practicum.mainservice.dto.events.NewEventDto;
import ru.practicum.mainservice.dto.events.PrivatePatchEventDto;
import ru.practicum.mainservice.dto.request.ChangeStatusRequestDto;
import ru.practicum.mainservice.dto.request.ConfirmedRejectedRequests;
import ru.practicum.mainservice.dto.request.RequestDto;
import ru.practicum.mainservice.service.events.PrivateEventService;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PrivateEventsController {

    private final PrivateEventService privateEventService;


    @GetMapping("users/{userId}/events/{eventId}")
    public EventDto getEventInfo(@PathVariable Long userId,
                                 @PathVariable Long eventId) {
        log.info("GET USERID: {}, EVENTID: {}", userId, eventId);
        return privateEventService.getEventInfo(userId, eventId);
    }

    @PatchMapping("users/{userId}/events/{eventId}")
    public EventDto patchEvent(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @RequestBody @Validated PrivatePatchEventDto patchEventDto) {

        log.info("PATCH USERID: {}, EVENTID: {}", userId, eventId);
        return privateEventService.patchEvent(userId, eventId, patchEventDto);
    }

    @GetMapping("users/{userId}/events")
    public List<EventDto> getUserEvents(@PathVariable Long userId,
                                        @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                        @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        log.info("GET USERID: {}, FROM: {}, SIZE: {}", userId, from, size);
        return privateEventService.getUserEvents(userId, from, size);
    }

    @PostMapping("users/{userId}/events")
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventDto postEvent(@PathVariable Long userId, @RequestBody @Validated NewEventDto newEventDto) {
        log.info("POST USERID: {}, NEWEVENTDTO: {}", userId, newEventDto);
        return privateEventService.postEvent(userId, newEventDto);
    }

    @GetMapping("users/{userId}/events/{eventId}/requests")
    public List<RequestDto> getUserRequests(@PathVariable Long userId,
                                            @PathVariable Long eventId) {
        log.info("GET USERID: {}, EVENTID: {}", userId, eventId);
        return privateEventService.getUserRequests(userId, eventId);
    }

    @PatchMapping("users/{userId}/events/{eventId}/requests")
    public ConfirmedRejectedRequests patchRequestsStatus(@PathVariable Long userId,
                                                         @PathVariable Long eventId,
                                                         @RequestBody @Validated ChangeStatusRequestDto changeStatusRequestDto) {
        log.info("PATCH USERID: {}, EVENTID: {}, CHANGESTATUSREQUESTDTO: {}", userId, eventId, changeStatusRequestDto);


        return privateEventService.patchRequestsStatus(userId, eventId, changeStatusRequestDto);
    }
}