package ru.practicum.mainservice.mapper.event;

import ru.practicum.mainservice.dto.events.EventDto;
import ru.practicum.mainservice.dto.events.NewEventDto;
import ru.practicum.mainservice.dto.events.ShortEventDto;
import ru.practicum.mainservice.enums.EventState;
import ru.practicum.mainservice.mapper.category.CategoryMapper;
import ru.practicum.mainservice.mapper.location.LocationMapper;
import ru.practicum.mainservice.mapper.user.UserMapper;
import ru.practicum.mainservice.models.category.Category;
import ru.practicum.mainservice.models.events.Event;
import ru.practicum.mainservice.models.location.Location;
import ru.practicum.mainservice.models.users.User;

import java.time.LocalDateTime;

public class EventMapper {

    public static Event mapNewEventDtoToEvent(NewEventDto newEventDto, User initiator, Location location, Category category) {

        Event event = new Event();

        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setTitle(newEventDto.getTitle());
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        event.setCategory(category);
        event.setConfirmedRequests(0L);
        event.setLocation(location);
        event.setInitiator(initiator);
        event.setViews(0L);

        return event;


    }

    public static EventDto mapEventToEventDto(Event event) {

        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setConfirmedRequests(event.getConfirmedRequests());
        eventDto.setDescription(event.getDescription());
        eventDto.setEventDate(event.getEventDate());
        eventDto.setCreatedOn(event.getCreatedOn());
        eventDto.setPaid(event.getPaid());
        eventDto.setParticipantLimit(event.getParticipantLimit());
        eventDto.setPublishedOn(event.getPublishedOn());
        eventDto.setRequestModeration(event.getRequestModeration());
        eventDto.setState(event.getState());
        eventDto.setTitle(event.getTitle());
        eventDto.setViews(event.getViews());
        eventDto.setLocation(LocationMapper.mapLocationToLocationDto(event.getLocation()));
        eventDto.setInitiator(UserMapper.mapToUserDto(event.getInitiator()));
        eventDto.setCategory(CategoryMapper.categoryToCategoryDto(event.getCategory()));
        eventDto.setViews(event.getViews());

        return eventDto;
    }

    public static ShortEventDto mapEventToPublicEventDto(Event event) {

        ShortEventDto eventDto = new ShortEventDto();
        eventDto.setId(event.getId());
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setConfirmedRequests(event.getConfirmedRequests());
        eventDto.setEventDate(event.getEventDate());
        eventDto.setPaid(event.getPaid());
        eventDto.setTitle(event.getTitle());
        eventDto.setInitiator(UserMapper.mapToUserDto(event.getInitiator()));
        eventDto.setCategory(CategoryMapper.categoryToCategoryDto(event.getCategory()));
        eventDto.setViews(event.getViews());

        return eventDto;
    }
}
