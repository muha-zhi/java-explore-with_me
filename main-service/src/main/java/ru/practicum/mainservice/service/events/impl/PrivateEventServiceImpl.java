package ru.practicum.mainservice.service.events.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dao.category.CategoryRepository;
import ru.practicum.mainservice.dao.event.EventRepository;
import ru.practicum.mainservice.dao.location.LocationRepository;
import ru.practicum.mainservice.dao.request.RequestRepository;
import ru.practicum.mainservice.dto.events.EventDto;
import ru.practicum.mainservice.dto.events.NewEventDto;
import ru.practicum.mainservice.dto.events.PrivatePatchEventDto;
import ru.practicum.mainservice.dto.request.ChangeStatusRequestDto;
import ru.practicum.mainservice.dto.request.ConfirmedRejectedRequests;
import ru.practicum.mainservice.dto.request.RequestDto;
import ru.practicum.mainservice.enums.EventState;
import ru.practicum.mainservice.enums.PrivateStateAction;
import ru.practicum.mainservice.enums.RequestStatus;
import ru.practicum.mainservice.exceptions.ConflictRequestException;
import ru.practicum.mainservice.exceptions.DataNotFoundException;
import ru.practicum.mainservice.mapper.event.EventMapper;
import ru.practicum.mainservice.mapper.location.LocationMapper;
import ru.practicum.mainservice.mapper.request.RequestMapper;
import ru.practicum.mainservice.models.category.Category;
import ru.practicum.mainservice.models.events.Event;
import ru.practicum.mainservice.models.location.Location;
import ru.practicum.mainservice.models.requests.Request;
import ru.practicum.mainservice.models.users.User;
import ru.practicum.mainservice.service.category.PublicCategoryService;
import ru.practicum.mainservice.service.events.PrivateEventService;
import ru.practicum.mainservice.service.user.AdminUserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PrivateEventServiceImpl implements PrivateEventService {

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final LocationRepository locationRepository;

    private final AdminUserService adminUserService;

    private final CategoryRepository categoryRepository;

    private final PublicCategoryService publicCategoryService;


    @Override
    public EventDto getEventInfo(Long userId, Long eventId) {

        Event eventFromDb = getEventIfExist(eventId);

        log.info("GET userId: {}, eventId: {}", userId, eventId);

        return EventMapper.mapEventToEventDto(eventFromDb);
    }

    @Transactional
    @Override
    public EventDto patchEvent(Long userId, Long eventId, PrivatePatchEventDto privatePatchEventDto) {

        Event patchingEvent = getEventIfExist(eventId);

        adminUserService.getUserIfExist(userId);

        validatePatchingEvent(patchingEvent);

        if (privatePatchEventDto.getStateAction() != null) {

            if (privatePatchEventDto.getStateAction().equals(PrivateStateAction.CANCEL_REVIEW)) {
                patchingEvent.setState(EventState.CANCELED);
            }

            if (privatePatchEventDto.getStateAction().equals(PrivateStateAction.SEND_TO_REVIEW)) {
                patchingEvent.setState(EventState.PENDING);
            }
        }

        if (privatePatchEventDto.getAnnotation() != null) {
            patchingEvent.setAnnotation(privatePatchEventDto.getAnnotation());
        }

        if (privatePatchEventDto.getCategory() != null) {
            patchingEvent.setCategory(
                    publicCategoryService.getCategoryIfExist(privatePatchEventDto.getCategory())
            );
        }

        if (privatePatchEventDto.getDescription() != null) {
            patchingEvent.setDescription(privatePatchEventDto.getDescription());
        }

        if (privatePatchEventDto.getEventDate() != null) {
            validatePatchDate(privatePatchEventDto.getEventDate());
            patchingEvent.setEventDate(patchingEvent.getEventDate());
        }

        if (privatePatchEventDto.getLocation() != null) {
            Location location = patchingEvent.getLocation();

            location.setLon(privatePatchEventDto.getLocation().getLon());
            location.setLat(privatePatchEventDto.getLocation().getLat());

            locationRepository.save(location);

            patchingEvent.setLocation(location);
        }

        if (privatePatchEventDto.getPaid() != null) {
            patchingEvent.setPaid(privatePatchEventDto.getPaid());
        }

        if (privatePatchEventDto.getParticipantLimit() != null) {
            patchingEvent.setParticipantLimit(privatePatchEventDto.getParticipantLimit());
        }

        if (privatePatchEventDto.getRequestModeration() != null) {
            patchingEvent.setRequestModeration(privatePatchEventDto.getRequestModeration());
        }

        if (privatePatchEventDto.getTitle() != null) {
            patchingEvent.setTitle(privatePatchEventDto.getTitle());
        }

        return EventMapper.mapEventToEventDto(eventRepository.save(patchingEvent));
    }

    @Override
    public List<EventDto> getUserEvents(Long userId, int from, int size) {

        User initiator = adminUserService.getUserIfExist(userId);

        List<Event> events = eventRepository.findAllByInitiator(initiator, PageRequest.of(from / size, size));

        return events.stream().map(EventMapper::mapEventToEventDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventDto postEvent(Long userId, NewEventDto newEventDto) {

        validateEventDate(newEventDto.getEventDate());

        User user = adminUserService.getUserIfExist(userId);

        Location location = locationRepository.save(LocationMapper.mapLocationDtoToLocation(newEventDto.getLocation()));

        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(
                () -> new DataNotFoundException("Category not found"));

        Event event = eventRepository.save(EventMapper.mapNewEventDtoToEvent(newEventDto, user, location, category));

        log.info("Пользователь {} создал событие {}", user.getName(), newEventDto.getTitle());

        return EventMapper.mapEventToEventDto(event);
    }

    @Override
    public List<RequestDto> getUserRequests(Long userId, Long eventId) {

        List<Request> requests = requestRepository.findAllByEventAndInitiator(
                adminUserService.getUserIfExist(userId),
                getEventIfExist(eventId));

        log.info("GET userId: {}, eventId: {}", userId, eventId);

        return requests.stream()
                .map(RequestMapper::mapRequestToRequestDto)
                .collect(Collectors.toList());

    }

    @Transactional
    @Override
    public ConfirmedRejectedRequests patchRequestsStatus(Long userId, Long eventId, ChangeStatusRequestDto changeStatusRequestDto) {

        ConfirmedRejectedRequests forReturn = new ConfirmedRejectedRequests();

        Event event = getEventIfExist(eventId);

        List<Request> requests = requestRepository.findByInitiatorAndEventAndIdIn(
                adminUserService.getUserIfExist(userId),
                event, changeStatusRequestDto.getRequestIds());

        List<RequestDto> confirmedRequests = new ArrayList<>();
        List<RequestDto> rejectedRequests = new ArrayList<>();

        switch (changeStatusRequestDto.getStatus()) {
            case CONFIRMED:
                validateEvent(event);
                if ((!event.getRequestModeration()) || event.getParticipantLimit() == 0) {
                    return confirmAllRequests(requests, event);
                }

                for (Request request : requests) {

                    if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
                        rejectedRequests.add(rejectRequest(request));

                    } else {
                        confirmedRequests.add(confirmRequest(request));
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    }

                }

                break;

            case REJECTED:

                for (Request request : requests) {
                    validateRequest(request);
                    rejectedRequests.add(rejectRequest(request));
                }

            default:
                break;
        }

        eventRepository.save(event);
        forReturn.setConfirmedRequests(confirmedRequests);
        forReturn.setRejectedRequests(rejectedRequests);

        return forReturn;
    }

    @Override
    public Event getEventIfExist(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new DataNotFoundException("Event not found"));
    }


    private void validateRequest(Request request) {
        if (!request.getStatus().equals(RequestStatus.PENDING)) {
            throw new ConflictRequestException("Нельзя изменить статус другого запроса");
        }
    }

    private void validateEvent(Event event) {
        if (event.getConfirmedRequests().longValue() == event.getParticipantLimit().longValue()) {
            throw new ConflictRequestException("Событие заполнено");
        }
    }

    private RequestDto confirmRequest(Request request) {

        validateRequest(request);
        request.setStatus(RequestStatus.CONFIRMED);
        requestRepository.save(request);

        return RequestMapper.mapRequestToRequestDto(request);
    }

    private RequestDto rejectRequest(Request request) {
        validateRequest(request);
        request.setStatus(RequestStatus.REJECTED);

        requestRepository.save(request);
        return RequestMapper.mapRequestToRequestDto(request);

    }

    private ConfirmedRejectedRequests confirmAllRequests(List<Request> requests, Event event) {
        ConfirmedRejectedRequests confirmedRejectedRequests = new ConfirmedRejectedRequests();
        List<RequestDto> confirmedRequests = new ArrayList<>();

        for (Request request : requests) {
            confirmRequest(request);
            confirmedRequests.add(RequestMapper.mapRequestToRequestDto(request));
        }
        confirmedRejectedRequests.setConfirmedRequests(confirmedRequests);
        confirmedRejectedRequests.setRejectedRequests(new ArrayList<>());

        event.setConfirmedRequests(event.getConfirmedRequests() + confirmedRequests.size());
        eventRepository.save(event);

        return confirmedRejectedRequests;
    }

    private void validatePatchingEvent(Event event) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictRequestException("The event has already been published");
        }
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictRequestException("The start date of the modified event should not be earlier than an hour from the publication date");
        }
    }

    private void validateEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new IllegalArgumentException("The start date of the modified event should not be earlier than an hour from the publication date");
        }
    }

    private void validatePatchDate(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    " the start date of the modified event should not be earlier than now");

        }
    }
}
