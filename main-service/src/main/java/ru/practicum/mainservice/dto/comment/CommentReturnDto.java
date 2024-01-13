package ru.practicum.mainservice.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReturnDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
