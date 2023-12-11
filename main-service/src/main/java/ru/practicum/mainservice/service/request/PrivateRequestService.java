package ru.practicum.mainservice.service.request;

import ru.practicum.mainservice.dto.request.RequestDto;

import java.util.List;

public interface PrivateRequestService {

    List<RequestDto> getUserRequests(Long userId);

    RequestDto postUserRequest(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);
}
