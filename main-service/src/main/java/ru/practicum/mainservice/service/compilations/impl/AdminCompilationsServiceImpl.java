package ru.practicum.mainservice.service.compilations.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dao.compilations.CompilationEventRepository;
import ru.practicum.mainservice.dao.compilations.CompilationRepository;
import ru.practicum.mainservice.dto.compilations.CompilationsDto;
import ru.practicum.mainservice.dto.compilations.NewCompilationsDto;
import ru.practicum.mainservice.dto.compilations.PatchCompilationsDto;
import ru.practicum.mainservice.exceptions.DataNotFoundException;
import ru.practicum.mainservice.mapper.compilations.CompilationMapper;
import ru.practicum.mainservice.models.compilations.CompilationEvent;
import ru.practicum.mainservice.models.compilations.Compilations;
import ru.practicum.mainservice.models.events.Event;
import ru.practicum.mainservice.service.compilations.AdminCompilationsService;
import ru.practicum.mainservice.service.events.PrivateEventService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminCompilationsServiceImpl implements AdminCompilationsService {

    private final PrivateEventService privateEventService;

    private final CompilationRepository compilationRepository;

    private final CompilationEventRepository compilationEventRepository;


    @Transactional
    @Override
    public CompilationsDto saveCompilation(NewCompilationsDto newCompilationsDto) {

        Compilations compilations = compilationRepository.save(CompilationMapper.mapNewCompilationToCompilation(newCompilationsDto));
        log.info("POST newCompilationsDto: {}", newCompilationsDto);
        if (newCompilationsDto.getEvents() != null && !newCompilationsDto.getEvents().isEmpty()) {
            return saveCompilationEvent(compilations, getEventsByIds(newCompilationsDto.getEvents()));
        }
        return CompilationMapper.mapCompilationToCompilationDtoWithoutEvents(compilations);
    }

    @Transactional
    @Override
    public void deleteCompilation(Long compId) {
        compilationEventRepository.deleteAllByCompletionsId(compId);
        compilationRepository.deleteById(compId);
        log.info("DELETE compilationId: {}", compId);
    }

    @Transactional
    @Override
    public CompilationsDto updateCompilation(Long compId, PatchCompilationsDto patchCompilationsDto) {
        Compilations updated = compilationRepository.findById(compId).orElseThrow(
                () -> new DataNotFoundException("Compilation not found"));

        if (patchCompilationsDto.getTitle() != null) {
            updated.setTitle(patchCompilationsDto.getTitle());
        }
        if (patchCompilationsDto.getPinned() != null) {
            updated.setPinned(patchCompilationsDto.getPinned());
        }

        if (patchCompilationsDto.getEvents() != null && !patchCompilationsDto.getEvents().isEmpty()) {
            compilationEventRepository.deleteAllByCompletionsId(compId);
            return saveCompilationEvent(updated, getEventsByIds(patchCompilationsDto.getEvents()));
        }

        return CompilationMapper.mapCompilationToCompilationDtoWithoutEvents(updated);

    }

    private CompilationsDto saveCompilationEvent(Compilations compilations, List<Event> events) {
        List<CompilationEvent> saved = new ArrayList<>();
        for (Event event : events) {
            CompilationEvent forAdd = compilationEventRepository.save(
                    CompilationMapper.mapNewCompilationEvent(event, compilations)
            );

            saved.add(forAdd);
        }
        return CompilationMapper.mapCompilationEventsToCompilationDtoWithEvents(saved);
    }

    private List<Event> getEventsByIds(List<Long> eventIds) {
        List<Event> forReturn = new ArrayList<>();

        for (Long eventId : eventIds) {
            forReturn.add(privateEventService.getEventIfExist(eventId));
        }

        return forReturn;
    }
}
