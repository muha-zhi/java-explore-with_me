package ru.practicum.mainservice.service.compilations.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.dao.compilations.CompilationEventRepository;
import ru.practicum.mainservice.dao.compilations.CompilationRepository;
import ru.practicum.mainservice.dto.compilations.CompilationsDto;
import ru.practicum.mainservice.exceptions.DataNotFoundException;
import ru.practicum.mainservice.mapper.compilations.CompilationMapper;
import ru.practicum.mainservice.models.compilations.Compilations;
import ru.practicum.mainservice.service.compilations.PublicCompilationService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicCompilationServiceImpl implements PublicCompilationService {

    private final CompilationEventRepository compilationEventRepository;

    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationsDto> getAllCompilations(int from, int size, boolean pinned) {

        List<CompilationsDto> forReturn = new ArrayList<>();

        List<Compilations> compilations = compilationRepository.findAllByPinned(pinned, PageRequest.of(from / size, size));

        for (Compilations compilation : compilations) {
            CompilationsDto compilationsDto = CompilationMapper.mapExpiriment(compilation,
                    compilationEventRepository.findAllByCompletions(compilation));

            forReturn.add(compilationsDto);
        }

        log.info("GET from: {}, size: {}, pinned: {}", from, size, pinned);

        return forReturn;
    }

    @Override
    public CompilationsDto getCompilationInfo(Long compId) {

        log.info("GET compId: {}", compId);

        Compilations compilations = compilationRepository.findById(compId).orElseThrow(
                () -> new DataNotFoundException("Compilation not found")
        );

        return CompilationMapper.mapExpiriment(compilations, compilationEventRepository.findAllByCompletions(compilations));
    }
}
