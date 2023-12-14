package ru.practicum.mainservice.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dao.comment.CommentRepository;
import ru.practicum.mainservice.dto.comment.CommentDto;
import ru.practicum.mainservice.dto.comment.CommentReturnDto;
import ru.practicum.mainservice.exceptions.ConflictRequestException;
import ru.practicum.mainservice.exceptions.DataNotFoundException;
import ru.practicum.mainservice.mapper.comment.CommentMapper;
import ru.practicum.mainservice.models.comment.Comment;
import ru.practicum.mainservice.models.events.Event;
import ru.practicum.mainservice.models.users.User;
import ru.practicum.mainservice.service.events.PrivateEventService;
import ru.practicum.mainservice.service.user.AdminUserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final AdminUserService adminUserService;

    private final PrivateEventService privateEventService;


    @Override
    public List<CommentReturnDto> getEventComments(Long eventsId) {
        Event event = privateEventService.getEventIfExist(eventsId);

        log.info("GET eventsId: {}", eventsId);

        return commentRepository.findAllByEvent(event).stream()
                .map(CommentMapper::toCommentReturnDto)
                .collect(Collectors.toList());

    }

    @Transactional
    @Override
    public CommentReturnDto patchComment(Long userId, Long commentId, CommentDto commentDto) {

        User author = adminUserService.getUserIfExist(userId);

        Comment comment = getCommentIfExist(commentId);

        validCommentator(author, comment);

        comment.setText(commentDto.getText());

        log.info("PATCH userId: {}, commentId: {}, commentDto: {}", userId, commentId, commentDto);

        return CommentMapper.toCommentReturnDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public void deleteComment(Long userId, Long commentId) {

        User author = adminUserService.getUserIfExist(userId);

        Comment comment = getCommentIfExist(commentId);

        validCommentator(author, comment);

        log.info("DELETE userId: {}, commentId: {}", userId, commentId);

        commentRepository.delete(comment);
    }

    @Override
    public Comment getCommentIfExist(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("Comment with id " + commentId + " not found"));
    }

    private void validCommentator(User user, Comment comment) {
        if (!user.getId().equals(comment.getAuthor().getId())) {
            throw new ConflictRequestException("User " + user.getId() + " can't edit comment " + comment.getId());
        }
    }
}
