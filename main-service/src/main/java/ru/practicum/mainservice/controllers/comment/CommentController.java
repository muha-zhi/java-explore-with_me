package ru.practicum.mainservice.controllers.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.CommentReturnDto;
import ru.practicum.mainservice.service.comment.CommentService;
import ru.practicum.mainservice.service.events.PrivateEventService;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final PrivateEventService privateEventService;

    private final CommentService commentService;


    @PostMapping("/users/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentReturnDto createComment(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @RequestBody @Validated CommentDto commentDto) {
        log.info("POST USERID: {}, EVENTID: {}", userId, eventId);
        return privateEventService.createComment(userId, eventId, commentDto);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentReturnDto> getEventComments(
            @PathVariable Long eventId) {
        log.info("GET  EVENTID: {}", eventId);
        return commentService.getEventComments(eventId);
    }

    @PatchMapping("/users/{userId}/comments/{commentId}")
    public CommentReturnDto patchComment(@PathVariable Long userId,
                                         @PathVariable Long commentId,
                                         @RequestBody @Validated CommentDto commentDto) {
        log.info("PATCH USERID: {}, COMMENTID: {}", userId, commentId);
        return commentService.patchComment(userId, commentId, commentDto);
    }

    @DeleteMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {

        log.info("DELETE USERID: {}, COMMENTID: {}", userId, commentId);

        commentService.deleteComment(userId, commentId);
    }
}
