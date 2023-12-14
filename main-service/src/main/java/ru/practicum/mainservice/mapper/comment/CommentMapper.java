package ru.practicum.mainservice.mapper.comment;

import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.CommentReturnDto;
import ru.practicum.mainservice.models.comment.Comment;
import ru.practicum.mainservice.models.events.Event;
import ru.practicum.mainservice.models.users.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static CommentReturnDto toCommentReturnDto(Comment comment) {
        return new CommentReturnDto(comment.getId(), comment.getText(), comment.getAuthor().getName(), comment.getCreated());
    }

    public static Comment toComment(CommentDto commentDto, Event event, User author) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setEvent(event);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

    public static List<CommentReturnDto> toCommentReturnDtoList(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toCommentReturnDto)
                .collect(Collectors.toList());
    }
}
