package ru.practicum.mainservice.dao.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.models.comment.Comment;
import ru.practicum.mainservice.models.events.Event;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findAllByEventInOrderByCreatedDesc(List<Event> events);

    List<Comment> findAllByEvent(Event event);
}
