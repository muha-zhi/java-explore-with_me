package ru.practicum.ewm.mapper;

import ru.practicum.ewm.Hit;
import ru.practicum.ewm.model.HitModel;


public class StatsMapper {


    public static HitModel toHitModel(Hit hit) {
        HitModel hitModel = new HitModel();
        hitModel.setApp(hit.getApp());
        hitModel.setIp(hit.getIp());
        hitModel.setUri(hit.getUri());
        hitModel.setCreatedDate(hit.getCreatedDate());
        return hitModel;

    }
}


