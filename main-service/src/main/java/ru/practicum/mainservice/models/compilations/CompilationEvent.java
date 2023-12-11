package ru.practicum.mainservice.models.compilations;

import lombok.*;
import ru.practicum.mainservice.models.events.Event;

import javax.persistence.*;

@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "compilations_events")
public class CompilationEvent {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Event event;

    private C

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompilationEvent)) return false;
        return id != null && id.equals(((CompilationEvent) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
