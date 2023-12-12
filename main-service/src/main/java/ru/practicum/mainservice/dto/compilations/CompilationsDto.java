package ru.practicum.mainservice.dto.compilations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainservice.dto.events.ShortEventDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationsDto {
    private Long id;
    private List<ShortEventDto> events;
    private boolean pinned;
    private String title;
}
