package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.ViewStats;
import ru.practicum.ewm.model.HitModel;
import ru.practicum.ewm.model.StatsModel;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<HitModel, Long> {

    @Query("SELECT NEW ru.practicum.ewm.ViewStats(v.app, v.uri, count(v.ip)) " +
            "FROM stats as v " +
            "WHERE v.createdDate BETWEEN :start AND :end " +
            "AND v.uri IN :uris " +
            "GROUP BY v.app, v.uri" +
            " ORDER BY count(v.ip) DESC")
    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT NEW ru.practicum.ewm.ViewStats(v.app, v.uri, count(v.ip)) " +
            "FROM stats as v " +
            "WHERE v.createdDate BETWEEN :start AND :end " +
            "GROUP BY v.app, v.uri" +
            " ORDER BY count(v.ip) DESC")
    List<ViewStats> getStatsWithoutUri(LocalDateTime start, LocalDateTime end);

    @Query("SELECT NEW ru.practicum.ewm.ViewStats(v.app, v.uri, count(distinct v.ip)) " +
            "FROM stats as v " +
            "WHERE v.createdDate BETWEEN :start AND :end " +
            "AND v.uri IN :uris " +
            "GROUP BY v.app, v.uri" +
            " ORDER BY count(DISTINCT v.ip) DESC")
    List<ViewStats> getUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT NEW ru.practicum.ewm.ViewStats(v.app, v.uri, count(distinct v.ip)) " +
            "FROM stats as v " +
            "WHERE v.createdDate BETWEEN :start AND :end " +
            "GROUP BY v.app, v.uri" +
            " ORDER BY count(DISTINCT v.ip) DESC")
    List<ViewStats> getUniqueStatsWithoutUri(LocalDateTime start, LocalDateTime end);
}
