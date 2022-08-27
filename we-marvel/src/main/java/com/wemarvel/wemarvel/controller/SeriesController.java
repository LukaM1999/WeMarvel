package com.wemarvel.wemarvel.controller;

import com.wemarvel.wemarvel.model.dto.SeriesDTO;
import com.wemarvel.wemarvel.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("series")
public class SeriesController {
    @Autowired
    private SeriesService seriesService;

    @GetMapping("/simple")
    private List<SeriesDTO> getSeriesSimple(){
        return seriesService.getSeriesSimple();
    }
}
