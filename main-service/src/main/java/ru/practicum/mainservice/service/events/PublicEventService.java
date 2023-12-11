package ru.practicum.mainservice.service.events;

import ru.practicum.mainservice.dto.events.EventDto;
import ru.practicum.mainservice.dto.events.ShortEventDto;
import ru.practicum.mainservice.enums.EventRequestSort;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {

    List<ShortEventDto> getAllEvents(String text,
                                     List<Long> categories,
                                     Boolean paid,
                                     LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd,
                                     Boolean onlyAvailable,
                                     EventRequestSort sort,
                                     Integer from,
                                     Integer size);


    EventDto getEventInfo(Long id, String ip, String uri);

    Long getViews(String uri);


}
