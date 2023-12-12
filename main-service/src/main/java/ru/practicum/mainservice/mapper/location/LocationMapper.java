package ru.practicum.mainservice.mapper.location;

import ru.practicum.mainservice.dto.location.LocationDto;
import ru.practicum.mainservice.models.location.Location;

public class LocationMapper {

    public static Location mapLocationDtoToLocation(LocationDto locationDto) {
        Location location = new Location();
        location.setLat(locationDto.getLat());
        location.setLon(locationDto.getLon());
        return location;
    }

    public static LocationDto mapLocationToLocationDto(Location location) {
        LocationDto locationDto = new LocationDto();
        locationDto.setLat(location.getLat());
        locationDto.setLon(location.getLon());
        return locationDto;
    }
}
