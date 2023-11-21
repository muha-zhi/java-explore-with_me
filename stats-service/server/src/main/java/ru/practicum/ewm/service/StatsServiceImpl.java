package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.Hit;
import ru.practicum.ewm.StatsRequest;
import ru.practicum.ewm.mapper.StatsMapper;
import ru.practicum.ewm.model.StatsModel;
import ru.practicum.ewm.repository.StatsRepository;

import java.security.InvalidParameterException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statRepository;

    @Override
    public void saveHit(Hit hit) {
        statRepository.save(StatsMapper.toHitModel(hit));
    }

    @Override
    public List<StatsModel> getListStats(StatsRequest request) {

        if (request.getStart() == null || request.getEnd() == null) {
            throw new InvalidParameterException("Start and end dates cannot be null");
        }

        if (request.getStart().isAfter(request.getEnd())) {
            throw new InvalidParameterException("Start date must be before end date");
        }

        if (request.isUnique()) {
            return statRepository.getUniqueStats(request.getStart(), request.getEnd(), request.getUris());
        }

        return statRepository.getStats(request.getStart(), request.getEnd(), request.getUris());
    }
}