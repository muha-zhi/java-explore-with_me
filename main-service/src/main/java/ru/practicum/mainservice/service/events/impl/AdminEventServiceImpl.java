package ru.practicum.mainservice.service.events.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dao.comment.CommentRepository;
import ru.practicum.mainservice.dao.event.EventRepository;
import ru.practicum.mainservice.dao.location.LocationRepository;
import ru.practicum.mainservice.dto.events.AdminPatchEventDto;
import ru.practicum.mainservice.dto.events.EventDto;
import ru.practicum.mainservice.enums.EventState;
import ru.practicum.mainservice.exceptions.ConflictRequestException;
import ru.practicum.mainservice.mapper.comment.CommentMapper;
import ru.practicum.mainservice.mapper.event.EventMapper;
import ru.practicum.mainservice.models.category.Category;
import ru.practicum.mainservice.models.comment.Comment;
import ru.practicum.mainservice.models.events.Event;
import ru.practicum.mainservice.models.location.Location;
import ru.practicum.mainservice.service.category.PublicCategoryService;
import ru.practicum.mainservice.service.comment.CommentService;
import ru.practicum.mainservice.service.events.AdminEventService;
import ru.practicum.mainservice.service.events.PrivateEventService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;

    private final PrivateEventService privateEventService;

    private final PublicCategoryService publicCategoryService;

    private final LocationRepository locationRepository;

    private final CommentRepository commentRepository;

    private final CommentService commentService;


    @Override
    public List<EventDto> getAllEvents(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {

        List<Event> forReturn;

        if (rangeStart == null || rangeEnd == null) {
            forReturn = eventRepository.findAllForAdmin(users, states, categories, PageRequest.of(from / size, size));
        } else {
            forReturn = eventRepository.findAllForAdminWithDates(users, states, categories, rangeStart, rangeEnd, PageRequest.of(from / size, size));
        }

        log.info("GET users: {}, states: {}, categories: {}, rangeStart: {}, rangeEnd: {}, from: {}, size: {}",
                users, states, categories, rangeStart, rangeEnd, from, size);

        Map<Long, List<Comment>> comments = commentRepository.findAllByEventInOrderByCreatedDesc(forReturn).stream()
                .collect(groupingBy(commentsDto -> commentsDto.getEvent().getId(), toList()));

        return forReturn.stream()
                .map(event -> EventMapper.mapEventToEventDto(event, CommentMapper.toCommentReturnDtoList(comments.getOrDefault(event.getId(), new ArrayList<>()))))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventDto patchEvent(Long eventId, AdminPatchEventDto adminPatchEventDto) {
        Event patchingEvent = privateEventService.getEventIfExist(eventId);
        validateEventDate(patchingEvent);
        if (adminPatchEventDto.getStateAction() != null) {
            switch (adminPatchEventDto.getStateAction()) {
                case PUBLISH_EVENT:

                    validateEventState(patchingEvent);
                    patchingEvent.setState(EventState.PUBLISHED);
                    patchingEvent.setPublishedOn(LocalDateTime.now());

                    break;
                case REJECT_EVENT:

                    validatePublishEventState(patchingEvent);
                    patchingEvent.setState(EventState.CANCELED);
                    break;
            }
        }

        if (adminPatchEventDto.getAnnotation() != null) {
            patchingEvent.setAnnotation(adminPatchEventDto.getAnnotation());
        }

        if (adminPatchEventDto.getCategory() != null) {
            Category category = publicCategoryService.getCategoryIfExist(adminPatchEventDto.getCategory());
            patchingEvent.setCategory(category);
        }

        if (adminPatchEventDto.getDescription() != null) {
            patchingEvent.setDescription(adminPatchEventDto.getDescription());
        }

        if (adminPatchEventDto.getEventDate() != null) {
            validatePatchDate(adminPatchEventDto.getEventDate());
            patchingEvent.setEventDate(patchingEvent.getEventDate());
        }

        if (adminPatchEventDto.getLocation() != null) {
            Location location = patchingEvent.getLocation();

            location.setLon(adminPatchEventDto.getLocation().getLon());
            location.setLat(adminPatchEventDto.getLocation().getLat());

            locationRepository.save(location);

            patchingEvent.setLocation(location);
        }

        if (adminPatchEventDto.getPaid() != null) {
            patchingEvent.setPaid(adminPatchEventDto.getPaid());
        }

        if (adminPatchEventDto.getParticipantLimit() != null) {
            patchingEvent.setParticipantLimit(adminPatchEventDto.getParticipantLimit());
        }

        if (adminPatchEventDto.getRequestModeration() != null) {
            patchingEvent.setRequestModeration(adminPatchEventDto.getRequestModeration());
        }

        if (adminPatchEventDto.getTitle() != null) {
            patchingEvent.setTitle(adminPatchEventDto.getTitle());
        }

        List<Comment> comments = commentRepository.findAllByEvent(patchingEvent);

        return EventMapper.mapEventToEventDto(eventRepository.save(patchingEvent), CommentMapper.toCommentReturnDtoList(comments));

    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentService.getCommentIfExist(commentId);
        commentRepository.delete(comment);
    }


    private void validateEventDate(Event event) {
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ConflictRequestException(
                    " the start date of the modified event should not be earlier than an hour from the publication date");

        }
    }

    private void validateEventState(Event event) {

        if (!(event.getState().equals(EventState.PENDING))) {
            throw new ConflictRequestException(
                    " the state of the modified event must be PENDING");
        }
    }

    private void validatePublishEventState(Event event) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictRequestException(
                    " the state of the modified event must be PENDING");
        }
    }

    private void validatePatchDate(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    " the start date of the modified event should not be earlier than now");

        }
    }

}
