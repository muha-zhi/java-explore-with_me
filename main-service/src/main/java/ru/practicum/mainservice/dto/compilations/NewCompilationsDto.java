package ru.practicum.mainservice.dto.compilations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationsDto {

    List<Long> events;

    Boolean pinned = false;

    @NotNull(message = "title must not be null")
    @NotBlank(message = "title must not be blank")
    @Size(min = 1, max = 50, message = "title must be between 1 and 50 characters")
    String title;
}
