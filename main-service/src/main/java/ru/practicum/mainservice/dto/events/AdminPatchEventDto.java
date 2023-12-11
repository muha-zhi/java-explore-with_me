package ru.practicum.mainservice.dto.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.dto.location.LocationDto;
import ru.practicum.mainservice.enums.AdminStateAction;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminPatchEventDto {

    @Size(min = 20, max = 2000, message = "annotation must be between 20 and 2000 characters")
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000, message = "description must be between 20 and 7000 characters")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDto location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;

    @Size(min = 3, max = 120, message = "title must be between 3 and 120 characters")
    private String title;
    private AdminStateAction stateAction;
}
