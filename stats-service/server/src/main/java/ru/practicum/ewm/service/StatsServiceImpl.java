package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.Hit;
import ru.practicum.ewm.StatsRequest;
import ru.practicum.ewm.ViewStats;
import ru.practicum.ewm.mapper.StatsMapper;
import ru.practicum.ewm.repository.StatsRepository;

import java.security.InvalidParameterException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statRepository;

    @Override
    public void saveHit(Hit hit) {
        statRepository.save(StatsMapper.toHitModel(hit));
    }

    @Override
    public List<ViewStats> getListStats(StatsRequest request) {

        if (request.getStart() == null || request.getEnd() == null) {
            log.info("START AND END ARE NULL");
            throw new InvalidParameterException("Start and end dates cannot be null");
        }

        if (request.getStart().isAfter(request.getEnd())) {
            log.info("START AFTER END");
            throw new InvalidParameterException("Start date must be before end date");
        }

        if (request.getUris().isEmpty()) {
            if (request.isUnique()) {
                return statRepository.getUniqueStatsWithoutUri(request.getStart(), request.getEnd());
            } else {
                return statRepository.getStatsWithoutUri(request.getStart(), request.getEnd());
            }
        }

        if (request.isUnique()) {
            return statRepository.getUniqueStats(request.getStart(), request.getEnd(), request.getUris());
        }

        return statRepository.getStats(request.getStart(), request.getEnd(), request.getUris());
    }

}