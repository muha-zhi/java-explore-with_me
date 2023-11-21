package ru.practicum.ewm.mapper;

import ru.practicum.ewm.Hit;
import ru.practicum.ewm.model.HitModel;


public class StatsMapper {


    public static HitModel toHitModel(Hit hit) {
        return HitModel.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .created_date(hit.getTimestamp())
                .build();

    }
}


