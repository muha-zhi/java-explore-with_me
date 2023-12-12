package ru.practicum.mainservice.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.dto.location.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @NotNull(message = "annotation cannot be null")
    @NotBlank(message = "annotation cannot be blank")
    @Size(min = 20, max = 2000, message = "annotation must be between 20 and 2000 characters")
    private String annotation;

    @NotNull(message = "category cannot be null")
    private Long category;

    @NotNull(message = "description cannot be null")
    @NotBlank(message = "description cannot be blank")
    @Size(min = 20, max = 7000, message = "description must be between 20 and 7000 characters")
    private String description;

    @NotNull(message = "eventDate cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(message = "location cannot be null")
    private LocationDto location;

    private Boolean paid = false;

    private Long participantLimit = 0L;

    private Boolean requestModeration = true;

    @NotNull(message = "title cannot be null")
    @NotBlank(message = "title cannot be blank")
    @Size(min = 3, max = 120, message = "title must be between 3 and 120 characters")
    private String title;
}
