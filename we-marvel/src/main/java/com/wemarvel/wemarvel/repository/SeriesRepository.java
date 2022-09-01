package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.Series;
import com.wemarvel.wemarvel.model.dto.ComicDTO;
import com.wemarvel.wemarvel.model.dto.SeriesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    @Query("SELECT NEW com.wemarvel.wemarvel.model.dto.SeriesDTO(s.id, s.title, s.thumbnail) " +
            "FROM Series s")
    List<SeriesDTO> getSeriesSimple();

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.SeriesDTO(s.id, s.title, s.thumbnail, " +
            "COUNT(distinct t.id), COUNT(p), MAX(p.createdAt)) " +
            "FROM Series s " +
            "LEFT JOIN Topic t ON s.id = t.marvelEntityId " +
            "LEFT JOIN Post p ON p.topicId = t.id " +
            "GROUP BY s.id, s.title, s.thumbnail " +
            "ORDER BY s.title")
    List<SeriesDTO> findAllWithPostInfo();

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.SeriesDTO(s.id, s.title, s.description, " +
            "s.thumbnail, s.type, s.startYear, s.endYear) " +
            "FROM Series s " +
            "INNER JOIN CharacterInSeries cis ON cis.characterId = ?1 " +
            "WHERE cis.characterId = ?1 AND cis.seriesId = s.id " +
            "GROUP BY s.id, s.title, s.description, s.thumbnail, s.type, s.startYear, s.endYear " +
            "ORDER BY s.title")
    List<SeriesDTO> getSeriesWithCharacter(Long characterId);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.SeriesDTO(s.id, s.title, s.description, " +
            "s.thumbnail, s.type, s.startYear, s.endYear) " +
            "FROM Series s " +
            "GROUP BY s.id, s.title, s.description, s.thumbnail, s.type, s.startYear, s.endYear " +
            "ORDER BY s.title")
    List<SeriesDTO> getAllSeries();
}
