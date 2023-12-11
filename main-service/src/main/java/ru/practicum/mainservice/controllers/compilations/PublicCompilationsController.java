package ru.practicum.mainservice.controllers.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainservice.dto.compilations.CompilationsDto;
import ru.practicum.mainservice.service.compilations.PublicCompilationService;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PublicCompilationsController {

    private final PublicCompilationService publicCompilationService;

    @GetMapping("/compilations")
    public List<CompilationsDto> getAllCompilations(@RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                                    @RequestParam(value = "size", defaultValue = "10") @Positive int size,
                                                    @RequestParam(required = false, value = "pinned") boolean pinned) {
        log.info("GET from: {}, size: {}, pinned: {}", from, size, pinned);
        return publicCompilationService.getAllCompilations(from, size, pinned);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationsDto getCompilationInfo(@PathVariable Long compId) {
        log.info("GET compId: {}", compId);
        return publicCompilationService.getCompilationInfo(compId);
    }
}
