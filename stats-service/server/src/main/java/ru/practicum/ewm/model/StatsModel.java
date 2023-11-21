package ru.practicum.ewm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class StatsModel {
    private String app;
    private String uri;
    private Long hits;
}
