package ru.practicum.mainservice.mapper.hit;

import ru.practicum.ewm.Hit;

public class HitMapper {


    public static Hit mapEventtoHit(String ip, String uri) {

        Hit hit = new Hit();
        hit.setUri(uri);
        hit.setApp("ewm_main_service");
        hit.setIp(ip);

        return hit;
    }
}
