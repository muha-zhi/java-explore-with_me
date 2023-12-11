package ru.practicum.mainservice.controllers.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.dto.events.EventDto;
import ru.practicum.mainservice.dto.events.ShortEventDto;
import ru.practicum.mainservice.enums.EventRequestSort;
import ru.practicum.mainservice.service.events.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PublicEventController {

    private final PublicEventService publicEventService;


    @GetMapping("/events")
    public List<ShortEventDto> getAllEvents(@RequestParam(defaultValue = "") String text,
                                            @RequestParam(required = false) List<Long> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) LocalDateTime rangeStart,
                                            @RequestParam(required = false) LocalDateTime rangeEnd,
                                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(defaultValue = "EVENT_DATE") EventRequestSort sort,
                                            @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                            @RequestParam(value = "size", defaultValue = "10") @Positive int size,
                                            HttpServletRequest request) {
        log.info("GET TEXT: {}, CATEGORIES: {}, PAID: {}, RANGE_START: {}, RANGE_END: {}, ONLY_AVAILABLE: {}, SORT: {}, FROM: {}, SIZE: {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        return publicEventService.getAllEvents(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size,
                request.getRemoteAddr(),
                request.getRequestURI());
    }

    @GetMapping("/events/{id}")
    public EventDto getEventInfo(@PathVariable Long id, HttpServletRequest request) {

        log.info("GET ID: {}, IP: {}, URI: {}", id, request.getRemoteAddr(), request.getRequestURI());
        return publicEventService.getEventInfo(id, request.getRemoteAddr(), request.getRequestURI());
    }


}
