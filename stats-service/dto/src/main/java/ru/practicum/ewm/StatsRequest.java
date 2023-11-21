package ru.practicum.ewm;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsRequest {
    private List<String> uris;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean unique;
    private String app;
}