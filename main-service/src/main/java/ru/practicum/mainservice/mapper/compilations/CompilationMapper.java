package ru.practicum.mainservice.mapper.compilations;

import ru.practicum.mainservice.dto.compilations.CompilationsDto;
import ru.practicum.mainservice.dto.compilations.NewCompilationsDto;
import ru.practicum.mainservice.dto.events.ShortEventDto;
import ru.practicum.mainservice.mapper.event.EventMapper;
import ru.practicum.mainservice.models.compilations.CompilationEvent;
import ru.practicum.mainservice.models.compilations.Compilations;
import ru.practicum.mainservice.models.events.Event;

import java.util.ArrayList;
import java.util.List;

public class CompilationMapper {

    public static CompilationsDto mapCompilationEventsToCompilationDtoWithEvents(List<CompilationEvent> events) {

        if ((events != null) && !events.isEmpty()) {


            CompilationsDto forReturn = new CompilationsDto();

            Compilations compilations = events.get(0).getCompletions();

            List<ShortEventDto> eventsForAdd = new ArrayList<>();

            for (CompilationEvent event : events) {
                ShortEventDto shortEventDto = EventMapper.mapEventToPublicEventDto(event.getEvent());
                eventsForAdd.add(shortEventDto);
            }

            forReturn.setEvents(eventsForAdd);


            forReturn.setId(compilations.getId());
            forReturn.setPinned(compilations.getPinned());
            forReturn.setTitle(compilations.getTitle());

            return forReturn;
        }

        return new CompilationsDto();

    }

    public static Compilations mapNewCompilationToCompilation(NewCompilationsDto newCompilationsDto) {
        Compilations compilations = new Compilations();
        compilations.setPinned(newCompilationsDto.getPinned());
        compilations.setTitle(newCompilationsDto.getTitle());
        return compilations;
    }

    public static CompilationEvent mapNewCompilationEvent(Event event, Compilations compilations) {
        CompilationEvent compilationEvent = new CompilationEvent();
        compilationEvent.setCompletions(compilations);
        compilationEvent.setEvent(event);
        return compilationEvent;

    }

    public static CompilationsDto mapCompilationToCompilationDtoWithoutEvents(Compilations compilations) {

        CompilationsDto forReturn = new CompilationsDto();
        forReturn.setEvents(new ArrayList<>());
        forReturn.setId(compilations.getId());
        forReturn.setPinned(compilations.getPinned());
        forReturn.setTitle(compilations.getTitle());

        return forReturn;

    }

    public static CompilationsDto mapExpiriment(Compilations compilations, List<CompilationEvent> events) {


        CompilationsDto forReturn = new CompilationsDto();

        if (events != null && !events.isEmpty()) {


            List<ShortEventDto> eventsForAdd = new ArrayList<>();

            for (CompilationEvent event : events) {
                ShortEventDto shortEventDto = EventMapper.mapEventToPublicEventDto(event.getEvent());
                eventsForAdd.add(shortEventDto);
            }

            forReturn.setEvents(eventsForAdd);
        } else {
            forReturn.setEvents(new ArrayList<>());
        }

        forReturn.setId(compilations.getId());
        forReturn.setPinned(compilations.getPinned());
        forReturn.setTitle(compilations.getTitle());

        return forReturn;
    }
}
