package ru.practicum.mainservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.dto.events.EventDto;
import ru.practicum.mainservice.dto.users.UserDto;
import ru.practicum.mainservice.enums.RequestStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private Long id;
    private LocalDateTime created;
    private EventDto event;
    private UserDto requester;
    private RequestStatus status;
}
