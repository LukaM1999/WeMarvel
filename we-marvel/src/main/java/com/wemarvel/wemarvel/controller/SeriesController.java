package com.wemarvel.wemarvel.controller;

import com.wemarvel.wemarvel.model.Comic;
import com.wemarvel.wemarvel.model.Series;
import com.wemarvel.wemarvel.model.dto.ComicDTO;
import com.wemarvel.wemarvel.model.dto.SeriesDTO;
import com.wemarvel.wemarvel.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("series")
public class SeriesController {
    @Autowired
    private SeriesService seriesService;

    @GetMapping("/{seriesId}")
    public Series getSeries(@PathVariable Long seriesId) {
        return seriesService.getById(seriesId);
    }

    @GetMapping("/simple")
    private List<SeriesDTO> getSeriesSimple(){
        return seriesService.getSeriesSimple();
    }

    @GetMapping("/{seriesId}/name")
    public String getSeriesName(@PathVariable Long seriesId) {
        Series series = seriesService.getById(seriesId);
        if(series == null) return null;
        return series.getTitle();
    }

    @GetMapping("/withPostInfo")
    public List<SeriesDTO> getSeriesWithPostInfo(){
        return seriesService.getSeriesWithPostInfo();
    }
}
