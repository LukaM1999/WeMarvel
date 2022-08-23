package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.CharacterInSeries;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterInSeriesRepository extends JpaRepository<CharacterInSeries, Long> {
    CharacterInSeries getByCharacterIdAndSeriesId(Long characterId, Long seriesId);
}
