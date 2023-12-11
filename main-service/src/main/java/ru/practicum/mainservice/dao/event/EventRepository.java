package ru.practicum.mainservice.dao.event;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.enums.EventState;
import ru.practicum.mainservice.models.events.Event;
import ru.practicum.mainservice.models.users.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {


    @Query("select e from Event e " +
            "where ((lower(e.description) like lower(concat('%', :text, '%'))) or (lower(e.annotation) like lower(concat('%', :text, '%'))))" +
            " and e.eventDate between :rangeStart and :rangeEnd " +
            " and e.category.id in :categories " +
            " and e.participantLimit < e.confirmedRequests " +
            " and (:paid is null or e.paid = :paid) " +
            " and e.state = :state " +
            " order by e.eventDate desc")
    List<Event> getAllEventsByTitleOnlyAvailableIsTrue(String text,
                                                       LocalDateTime rangeStart,
                                                       LocalDateTime rangeEnd,
                                                       List<Long> categories,
                                                       Boolean paid,
                                                       EventState state,
                                                       PageRequest pageRequest);

    @Query("select e from Event e " +
            " where ((lower(e.description) like lower(concat('%', :text, '%'))) or (lower(e.annotation) like lower(concat('%', :text, '%'))))" +
            " and e.eventDate between :rangeStart and :rangeEnd " +
            " and e.category.id in :categories " +
            " and (:paid is null or e.paid = :paid) " +
            " and e.state = :state " +
            " order by e.eventDate desc")
    List<Event> getAllEventsByTitleOnlyAvailableIsFalse(String text,
                                                        LocalDateTime rangeStart,
                                                        LocalDateTime rangeEnd,
                                                        List<Long> categories,
                                                        Boolean paid,
                                                        EventState state,
                                                        PageRequest pageRequest);


    @Query("select e from Event e " +
            " where ((lower(e.description) like lower(concat('%', :text, '%'))) or (lower(e.annotation) like lower(concat('%', :text, '%'))))" +
            " and e.eventDate > :date " +
            " and e.category.id in :categories " +
            " and (:paid is null or e.paid = :paid) " +
            " and e.state = :state " +
            " order by e.eventDate desc")
    List<Event> getAllEventsByTitleOnlyAvailableIsFalseWithoutDates(String text,
                                                                    LocalDateTime date,
                                                                    List<Long> categories,
                                                                    Boolean paid,
                                                                    EventState state,
                                                                    Pageable pageable);

    @Query("select e from Event e " +
            " where ((lower(e.description) like lower(concat('%', :text, '%'))) or (lower(e.annotation) like lower(concat('%', :text, '%'))))" +
            " and e.eventDate >= :date " +
            " and e.category.id in :categories " +
            " and e.participantLimit < e.confirmedRequests " +
            " and e.state = :state " +
            " and (:paid is null or e.paid = :paid) " +
            " order by e.eventDate desc")
    List<Event> getAllEventsByTitleOnlyAvailableIsTrueWithoutDates(String text,
                                                                   LocalDateTime date,
                                                                   List<Long> categories,
                                                                   Boolean paid,
                                                                   EventState state,
                                                                   Pageable pageRequest);

    List<Event> findAllByInitiator(User initiator, PageRequest pageRequest);


    @Query("SELECT e FROM Event e " +
            " WHERE (:users IS NULL OR EXISTS (SELECT 1 FROM User u WHERE u.id = e.initiator.id AND u.id IN :users)) " +
            " AND (:categories IS NULL OR e.category.id IN :categories) " +
            " AND (:states IS NULL OR e.state IN :states) " +
            " AND e.eventDate BETWEEN :rangeStart AND :rangeEnd " +
            " ORDER BY e.eventDate DESC")
    List<Event> findAllForAdminWithDates(List<Long> users,
                                         List<EventState> states,
                                         List<Long> categories,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         PageRequest pageRequest);

    Optional<Event> findByIdAndState(Long id, EventState state);

    @Query("SELECT e FROM Event e " +
            " WHERE (:users IS NULL OR EXISTS (SELECT 1 FROM User u WHERE u.id = e.initiator.id AND u.id IN :users)) " +
            " AND (:categories IS NULL OR e.category.id IN :categories) " +
            " AND (:states IS NULL OR e.state IN :states) " +
            " ORDER BY e.eventDate DESC")
    List<Event> findAllForAdmin(List<Long> users,
                                List<EventState> states,
                                List<Long> categories,
                                PageRequest pageRequest);


}
