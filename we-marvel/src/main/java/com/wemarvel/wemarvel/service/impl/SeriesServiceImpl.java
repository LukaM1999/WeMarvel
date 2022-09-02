package com.wemarvel.wemarvel.service.impl;

import com.wemarvel.wemarvel.model.Series;
import com.wemarvel.wemarvel.model.dto.ComicDTO;
import com.wemarvel.wemarvel.model.dto.SeriesDTO;
import com.wemarvel.wemarvel.repository.SeriesRepository;
import com.wemarvel.wemarvel.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeriesServiceImpl implements SeriesService {

    @Autowired
    private SeriesRepository seriesRepository;

    @Override
    public List<SeriesDTO> getSeriesSimple() {
        return seriesRepository.getSeriesSimple();
    }

    @Override
    public Series getById(Long seriesId) {
        return seriesRepository.findById(seriesId).orElse(null);
    }

    @Override
    public List<SeriesDTO> getSeriesWithPostInfo() {
        return seriesRepository.findAllWithPostInfo();
    }

    @Override
    public List<SeriesDTO> getSeriesWithCharacter(Long characterId) {
        return seriesRepository.getSeriesWithCharacter(characterId);
    }

    @Override
    public List<SeriesDTO> getAllSeries() {
        return seriesRepository.getAllSeries();
    }

    @Override
    public SeriesDTO getSeriesWithRating(Long seriesId) {
        return seriesRepository.getSeriesWithRating(seriesId);
    }

    @Override
    public List<SeriesDTO> getTopRatedSeries() {
        return seriesRepository.getTopRatedSeries();
    }

    @Override
    public List<SeriesDTO> getPopularSeries() {
        return seriesRepository.getPopularSeries();
    }
}
