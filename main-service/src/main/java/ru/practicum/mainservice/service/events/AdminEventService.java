package ru.practicum.mainservice.service.events;

import ru.practicum.mainservice.dto.events.AdminPatchEventDto;
import ru.practicum.mainservice.dto.events.EventDto;
import ru.practicum.mainservice.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {

    List<EventDto> getAllEvents(List<Long> users,
                                List<EventState> states,
                                List<Long> categories,
                                LocalDateTime rangeStart,
                                LocalDateTime rangeEnd,
                                int from, int size);

    EventDto patchEvent(Long eventId,
                        AdminPatchEventDto adminPatchEventDto);
}
