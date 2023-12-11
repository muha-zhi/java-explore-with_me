package ru.practicum.mainservice.models.compilations;


import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "compilations")
public class Compilations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "pinned")
    private Boolean pinned;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Compilations)) return false;
        return id != null && id.equals(((Compilations) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
