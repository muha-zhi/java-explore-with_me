package ru.practicum.ewm;

import lombok.*;


@Getter
@AllArgsConstructor
@ToString
@Data
@NoArgsConstructor
public class ViewStats {
    private String app;
    private String uri;
    private Long hits;
}