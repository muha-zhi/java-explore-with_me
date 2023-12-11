package ru.practicum.mainservice.dao.compilations;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.mainservice.models.compilations.Compilations;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilations, Long> {

    @Query("SELECT c FROM Compilations c WHERE (:pinned IS NULL OR c.pinned = :pinned)")
    List<Compilations> findAllByPinned(Boolean pinned, PageRequest pageRequest);
}
