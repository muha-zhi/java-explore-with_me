package ru.practicum.mainservice.controllers.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.events.AdminPatchEventDto;
import ru.practicum.mainservice.dto.events.EventDto;
import ru.practicum.mainservice.enums.EventState;
import ru.practicum.mainservice.service.events.AdminEventService;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AdminEventController {

    private final AdminEventService eventService;


    @GetMapping("/admin/events")
    public List<EventDto> getAllEvents(@RequestParam(required = false) List<Long> users,
                                       @RequestParam(required = false) List<EventState> states,
                                       @RequestParam(required = false) List<Long> categories,
                                       @RequestParam(required = false) LocalDateTime rangeStart,
                                       @RequestParam(required = false) LocalDateTime rangeEnd,
                                       @RequestParam(defaultValue = "0") @Min(0) int from,
                                       @RequestParam(defaultValue = "10") @Positive int size) {

        log.info(" GET USERS: {}, STATES: {}, CATEGORIES: {}, RANGE_START: {}, RANGE_END: {}, FROM: {}, SIZE: {}",
                users, states, categories, rangeStart, rangeEnd, from, size);

        return eventService.getAllEvents(users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                from,
                size);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventDto patchEvent(@PathVariable Long eventId,
                               @RequestBody @Validated AdminPatchEventDto adminPatchEventDto) {

        log.info("PATCH EVENTID: {}", eventId);

        return eventService.patchEvent(eventId,
                adminPatchEventDto);
    }


}
