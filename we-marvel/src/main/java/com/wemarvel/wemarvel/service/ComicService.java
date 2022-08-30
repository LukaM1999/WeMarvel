package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.Comic;
import com.wemarvel.wemarvel.model.MarvelCharacter;
import com.wemarvel.wemarvel.model.dto.ComicDTO;

import java.util.List;

public interface ComicService {
    List<Comic> getComics(String title, int limit, int offset, String sortBy, String sortOrder);
    List<ComicDTO> getBySeriesIdSimple(Long seriesId);
    Comic getById(Long comicId);
    List<ComicDTO> getComicsWithPostInfo();
}
