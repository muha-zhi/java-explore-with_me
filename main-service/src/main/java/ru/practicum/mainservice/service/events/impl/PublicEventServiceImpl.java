package ru.practicum.mainservice.service.events.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.StatsClient;
import ru.practicum.ewm.ViewStats;
import ru.practicum.mainservice.dao.category.CategoryRepository;
import ru.practicum.mainservice.dao.event.EventRepository;
import ru.practicum.mainservice.dto.events.EventDto;
import ru.practicum.mainservice.dto.events.ShortEventDto;
import ru.practicum.mainservice.enums.EventRequestSort;
import ru.practicum.mainservice.enums.EventState;
import ru.practicum.mainservice.exceptions.DataNotFoundException;
import ru.practicum.mainservice.mapper.event.EventMapper;
import ru.practicum.mainservice.mapper.hit.HitMapper;
import ru.practicum.mainservice.models.category.Category;
import ru.practicum.mainservice.models.events.Event;
import ru.practicum.mainservice.service.events.PublicEventService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventRepository;

    private final StatsClient statsClient;

    private final CategoryRepository categoryRepository;


    @Override
    public List<ShortEventDto> getAllEvents(String text,
                                            List<Long> categories,
                                            Boolean paid,
                                            LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd,
                                            Boolean onlyAvailable,
                                            EventRequestSort sort,
                                            Integer from,
                                            Integer size) {

        validateRangeDates(rangeStart, rangeEnd);

        log.info("GET text: {}, categories: {}, paid: {}, rangeStart: {}, rangeEnd: {}, onlyAvailable: {}, sort: {}, from: {}, size: {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        return getEventsFromDb(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size);
    }

    @Override
    public EventDto getEventInfo(Long id, String ip, String uri) {

        Event event = eventRepository.findByIdAndState(id, EventState.PUBLISHED).orElseThrow(() -> new DataNotFoundException("Event not found"));

        statsClient.postStats(HitMapper.mapEventtoHit(ip, uri));

        event.setViews(getViews(uri));

        log.info("GET id: {}, ip: {}, uri: {}", id, ip, uri);

        return EventMapper.mapEventToEventDto(event);

    }


    private List<ShortEventDto> getEventsFromDb(String text,
                                                List<Long> categories,
                                                Boolean paid,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd,
                                                Boolean onlyAvailable,
                                                EventRequestSort sort,
                                                Integer from,
                                                Integer size) {

        List<Event> events;

        if (categories == null) {

            categories = categoryRepository.findAll()
                    .stream().map(Category::getId)
                    .collect(Collectors.toList());
        }


        if (rangeStart != null && rangeEnd != null) {

            events = getEventsWithDates(text,
                    categories,
                    paid,
                    rangeStart,
                    rangeEnd,
                    onlyAvailable,
                    from,
                    size);
        } else {
            events = getEventsWithoutDates(text,
                    categories,
                    paid,
                    onlyAvailable,
                    from,
                    size);
        }


        return getSortedEvents(events, sort);

    }


    public List<ShortEventDto> getSortedEvents(List<Event> events, EventRequestSort sort) {
        List<ShortEventDto> result;

        if (sort == EventRequestSort.VIEWS) {

            result = events.stream()
                    .map(EventMapper::mapEventToPublicEventDto)
                    .sorted(Comparator.comparing(ShortEventDto::getViews).reversed())
                    .collect(Collectors.toList());
        } else {
            result = events.stream()
                    .map(EventMapper::mapEventToPublicEventDto)
                    .collect(Collectors.toList());
        }

        return result;
    }

    private void validateRangeDates(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new IllegalArgumentException("Start date should be before end date");
        }

    }

    private List<Event> getEventsWithDates(String text,
                                           List<Long> categories,
                                           Boolean paid,
                                           LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd,
                                           Boolean onlyAvailable,
                                           Integer from,
                                           Integer size) {

        if (onlyAvailable) {
            return eventRepository
                    .getAllEventsByTitleOnlyAvailableIsTrue(text,
                            rangeStart,
                            rangeEnd,
                            categories,
                            paid,
                            EventState.PUBLISHED,
                            PageRequest.of(from / size, size));
        } else {
            return eventRepository
                    .getAllEventsByTitleOnlyAvailableIsFalse(text,
                            rangeStart,
                            rangeEnd,
                            categories,
                            paid,
                            EventState.PUBLISHED,
                            PageRequest.of(from / size, size));
        }
    }

    private List<Event> getEventsWithoutDates(String text,
                                              List<Long> categories,
                                              Boolean paid,
                                              Boolean onlyAvailable,
                                              Integer from,
                                              Integer size) {


        if (onlyAvailable) {
            return eventRepository
                    .getAllEventsByTitleOnlyAvailableIsTrueWithoutDates(text,
                            LocalDateTime.now(),
                            categories,
                            paid,
                            EventState.PUBLISHED,
                            PageRequest.of(from / size, size));
        } else {
            return eventRepository
                    .getAllEventsByTitleOnlyAvailableIsFalseWithoutDates(text,
                            LocalDateTime.now(),
                            categories,
                            paid,
                            EventState.PUBLISHED,
                            PageRequest.of(from / size, size));
        }
    }

    public Long getViews(String uri) {

        List<String> uriList = List.of(uri);

        List<ViewStats> viewStats = statsClient.getStats(LocalDateTime.of(2023, 1, 1, 0, 0), LocalDateTime.of(2024, 12, 1, 0, 0), uriList, true);
        if (!viewStats.isEmpty()) {
            return viewStats.get(0).getHits();
        }


        return 0L;
    }
}