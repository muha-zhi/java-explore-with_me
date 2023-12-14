package ru.practicum.mainservice.service.comment;

import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.CommentReturnDto;
import ru.practicum.mainservice.models.comment.Comment;

import java.util.List;

public interface CommentService {


    List<CommentReturnDto> getEventComments(Long eventsId);

    CommentReturnDto patchComment(Long userId,
                                  Long commentId,
                                  CommentDto commentDto);

    void deleteComment(Long userId,
                       Long commentId);

    Comment getCommentIfExist(Long commentId);
}
