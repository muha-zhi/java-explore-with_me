package ru.practicum.mainservice.service.compilations;

import ru.practicum.mainservice.dto.compilations.CompilationsDto;
import ru.practicum.mainservice.dto.compilations.NewCompilationsDto;
import ru.practicum.mainservice.dto.compilations.PatchCompilationsDto;

public interface AdminCompilationsService {

    CompilationsDto saveCompilation(NewCompilationsDto newCompilationsDto);


    void deleteCompilation(Long compId);

    CompilationsDto updateCompilation(Long compId, PatchCompilationsDto patchCompilationsDto);

}
