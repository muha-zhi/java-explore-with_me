package ru.practicum.mainservice.service.request.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dao.event.EventRepository;
import ru.practicum.mainservice.dao.request.RequestRepository;
import ru.practicum.mainservice.dto.request.RequestDto;
import ru.practicum.mainservice.enums.EventState;
import ru.practicum.mainservice.enums.RequestStatus;
import ru.practicum.mainservice.exceptions.ConflictRequestException;
import ru.practicum.mainservice.exceptions.DataNotFoundException;
import ru.practicum.mainservice.mapper.request.RequestMapper;
import ru.practicum.mainservice.models.events.Event;
import ru.practicum.mainservice.models.requests.Request;
import ru.practicum.mainservice.models.users.User;
import ru.practicum.mainservice.service.events.PrivateEventService;
import ru.practicum.mainservice.service.request.PrivateRequestService;
import ru.practicum.mainservice.service.user.AdminUserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivateRequestServiceImpl implements PrivateRequestService {

    private final RequestRepository requestRepository;

    private final PrivateEventService privateEventService;

    private final AdminUserService adminUserService;

    private final EventRepository eventRepository;


    @Override
    public List<RequestDto> getUserRequests(Long userId) {
        User requester = adminUserService.getUserIfExist(userId);

        List<Request> requests = requestRepository.findAllByRequesterOrderByCreatedDesc(requester);

        return requests.stream()
                .map(RequestMapper::mapRequestToRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public RequestDto postUserRequest(Long userId, Long eventId) {

        Event event = privateEventService.getEventIfExist(eventId);
        User requester = adminUserService.getUserIfExist(userId);


        validateEvent(event, requester);

        validateRequestExist(event, requester);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        Request newRequest = RequestMapper.mapNewRequest(requester, event);

        log.info("Request created: {}", newRequest);

        return RequestMapper.mapRequestToRequestDto(requestRepository.save(newRequest));
    }

    @Transactional
    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {

        Request request = getRequestIfExist(requestId);

        adminUserService.getUserIfExist(userId);

        request.setStatus(RequestStatus.CANCELED);

        log.info("Request canceled: {}", request);

        return RequestMapper.mapRequestToRequestDto(requestRepository.save(request));

    }

    private void validateRequestExist(Event event, User requester) {
        Optional<Request> validRequest = requestRepository.findByRequesterAndEvent(requester, event);
        if (validRequest.isPresent()) throw new ConflictRequestException("Request already exists");
    }

    private void validateEvent(Event event, User requester) {
        if (event.getInitiator().getId().equals(requester.getId()))
            throw new ConflictRequestException("You can't request your own event");

        if (!event.getState().equals(EventState.PUBLISHED))
            throw new ConflictRequestException("Event is not published");

        if ((event.getParticipantLimit() != 0) && (event.getParticipantLimit() == event.getConfirmedRequests()))
            throw new ConflictRequestException("Event is full");
    }

    private Request getRequestIfExist(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(
                () -> new DataNotFoundException("Request not found")
        );
    }
}
