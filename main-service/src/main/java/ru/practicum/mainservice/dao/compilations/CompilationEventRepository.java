package ru.practicum.mainservice.dao.compilations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.models.compilations.CompilationEvent;
import ru.practicum.mainservice.models.compilations.Compilations;

import java.util.List;

@Repository
public interface CompilationEventRepository extends JpaRepository<CompilationEvent, Long> {


    List<CompilationEvent> findAllByCompletions(Compilations compilations);

    void deleteAllByCompletionsId(Long compId);
}
