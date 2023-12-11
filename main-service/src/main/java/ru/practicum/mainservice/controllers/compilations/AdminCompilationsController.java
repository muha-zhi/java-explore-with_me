package ru.practicum.mainservice.controllers.compilations;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.compilations.CompilationsDto;
import ru.practicum.mainservice.dto.compilations.NewCompilationsDto;
import ru.practicum.mainservice.dto.compilations.PatchCompilationsDto;
import ru.practicum.mainservice.service.compilations.AdminCompilationsService;

@RestController
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminCompilationsController {

    private final AdminCompilationsService adminCompilationsService;


    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationsDto saveCompilation(@RequestBody @Validated NewCompilationsDto newCompilationsDto) {
        log.info(" Save compilation {}", newCompilationsDto);
        return adminCompilationsService.saveCompilation(newCompilationsDto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Delete compilation {}", compId);
        adminCompilationsService.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationsDto updateCompilation(@PathVariable Long compId,
                                             @RequestBody @Validated PatchCompilationsDto patchCompilationsDto) {

        log.info("Update compilation {}", compId);
        return adminCompilationsService.updateCompilation(compId, patchCompilationsDto);
    }

}
