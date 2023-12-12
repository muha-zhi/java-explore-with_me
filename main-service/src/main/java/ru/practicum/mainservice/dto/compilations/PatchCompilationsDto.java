package ru.practicum.mainservice.dto.compilations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchCompilationsDto {

    List<Long> events;

    Boolean pinned;

    @Size(min = 1, max = 50, message = "title must be between 1 and 50 characters")
    String title;
}
