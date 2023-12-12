package ru.practicum.mainservice.service.events;

import ru.practicum.mainservice.dto.events.EventDto;
import ru.practicum.mainservice.dto.events.NewEventDto;
import ru.practicum.mainservice.dto.events.PrivatePatchEventDto;
import ru.practicum.mainservice.dto.request.ChangeStatusRequestDto;
import ru.practicum.mainservice.dto.request.ConfirmedRejectedRequests;
import ru.practicum.mainservice.dto.request.RequestDto;
import ru.practicum.mainservice.models.events.Event;

import java.util.List;

public interface PrivateEventService {
    EventDto getEventInfo(Long userId,
                          Long eventId);

    EventDto patchEvent(Long userId,
                        Long eventId,
                        PrivatePatchEventDto patchEventDto);

    List<EventDto> getUserEvents(Long userId,
                                 int from,
                                 int size);

    EventDto postEvent(Long userId,
                       NewEventDto newEventDto);


    List<RequestDto> getUserRequests(Long userId, Long eventId);

    ConfirmedRejectedRequests patchRequestsStatus(Long userId,
                                                  Long eventId,
                                                  ChangeStatusRequestDto changeStatusRequestDto);

    Event getEventIfExist(Long eventId);
}
