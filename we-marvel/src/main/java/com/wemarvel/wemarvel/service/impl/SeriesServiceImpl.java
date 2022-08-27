package com.wemarvel.wemarvel.service.impl;

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
}
