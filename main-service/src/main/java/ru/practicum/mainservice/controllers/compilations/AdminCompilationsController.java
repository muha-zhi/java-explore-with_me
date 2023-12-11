package ru.practicum.mainservice.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainservice.dto.compilations.NewCompilationsDto;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminCompilationsController {

    @PostMapping("/compilations")
    public void saveCompilation(@RequestBody NewCompilationsDto newCompilationsDto) {
        log.info("Save compilation {}", newCompilationsDto);
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Delete compilation {}", compId);
    }

    @PatchMapping("/compilations/{compId}")
    

}
