package ru.practicum.mainservice.mapper.request;

import ru.practicum.mainservice.dto.request.RequestDto;
import ru.practicum.mainservice.enums.RequestStatus;
import ru.practicum.mainservice.models.events.Event;
import ru.practicum.mainservice.models.requests.Request;
import ru.practicum.mainservice.models.users.User;

import java.time.LocalDateTime;

public class RequestMapper {

    public static Request mapNewRequest(User requester, Event event) {

        Request request = new Request();

        request.setEvent(event);
        request.setRequester(requester);
        request.setCreated(LocalDateTime.now());

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }

        return request;
    }

    public static RequestDto mapRequestToRequestDto(Request request) {

        RequestDto requestDto = new RequestDto();

        requestDto.setId(request.getId());
        requestDto.setCreated(request.getCreated());
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setStatus(request.getStatus());

        return requestDto;
    }
}
