package ru.practicum.mainservice.dao.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.models.events.Event;
import ru.practicum.mainservice.models.requests.Request;
import ru.practicum.mainservice.models.users.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {


    @Query("SELECT r FROM Request r " +
            " JOIN r.event e " +
            " WHERE e.initiator = :initiator " +
            " AND r.id IN :requestIds" +
            " AND r.event = :event " +
            " ORDER BY r.created ASC ")
    List<Request> findByInitiatorAndEventAndIdIn(User initiator, Event event, List<Long> requestIds);


    @Query("SELECT r FROM Request r " +
            " JOIN r.event e " +
            " WHERE e.initiator = :initiator " +
            " AND r.event = :event " +
            " ORDER BY r.created DESC ")
    List<Request> findAllByEventAndInitiator(User initiator, Event event);

    Optional<Request> findByRequesterAndEvent(User requester, Event event);

    List<Request> findAllByRequesterOrderByCreatedDesc(User requester);
}
