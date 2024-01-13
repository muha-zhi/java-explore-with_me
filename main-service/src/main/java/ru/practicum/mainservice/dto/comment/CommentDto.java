package ru.practicum.mainservice.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {


    @NotBlank(message = "комментарий не может быть пустым")
    @NotNull(message = "комментарий не может быть пустым")
    @Size(min = 1, max = 1500, message = "комментарий должен быть от 1 до 2000 символов")
    private String text;
}
