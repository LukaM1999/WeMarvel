package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.Series;
import com.wemarvel.wemarvel.model.dto.ComicDTO;
import com.wemarvel.wemarvel.model.dto.SeriesDTO;

import java.util.List;

public interface SeriesService {
    List<SeriesDTO> getSeriesSimple();
    Series getById(Long seriesId);
    List<SeriesDTO> getSeriesWithPostInfo();
    List<SeriesDTO> getSeriesWithCharacter(Long characterId);
    List<SeriesDTO> getAllSeries();
    SeriesDTO getSeriesWithRating(Long seriesId);
    List<SeriesDTO> getTopRatedSeries();
    List<SeriesDTO> getPopularSeries();
}
