package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.Series;
import com.wemarvel.wemarvel.model.dto.SeriesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    @Query("SELECT NEW com.wemarvel.wemarvel.model.dto.SeriesDTO(s.id, s.title, s.thumbnail) " +
            "FROM Series s")
    List<SeriesDTO> getSeriesSimple();
}
