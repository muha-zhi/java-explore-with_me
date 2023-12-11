package ru.practicum.mainservice.service.compilations;

import ru.practicum.mainservice.dto.compilations.CompilationsDto;

import java.util.List;

public interface PublicCompilationService {

    List<CompilationsDto> getAllCompilations(
            int from,
            int size,
            boolean pinned
    );

    CompilationsDto getCompilationInfo(Long compId);

}
