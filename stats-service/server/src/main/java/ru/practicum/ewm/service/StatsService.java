package ru.practicum.ewm.service;

import ru.practicum.ewm.Hit;
import ru.practicum.ewm.StatsRequest;
import ru.practicum.ewm.ViewStats;

import java.util.List;

public interface StatsService {
    void saveHit(Hit hit);

    List<ViewStats> getListStats(StatsRequest request);
}