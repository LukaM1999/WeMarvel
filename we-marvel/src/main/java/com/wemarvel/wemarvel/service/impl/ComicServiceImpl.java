package com.wemarvel.wemarvel.service.impl;

import com.wemarvel.wemarvel.model.Comic;
import com.wemarvel.wemarvel.model.MarvelCharacter;
import com.wemarvel.wemarvel.model.dto.ComicDTO;
import com.wemarvel.wemarvel.repository.ComicRepository;
import com.wemarvel.wemarvel.service.ComicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComicServiceImpl implements ComicService {

    @Autowired
    private ComicRepository comicRepository;

    @Override
    public List<Comic> getComics(String title, int limit, int offset, String sortBy, String sortOrder) {
        Sort.Direction direction = Sort.Direction.ASC;
        if(sortOrder != null && sortOrder.equals("desc")) {
            direction = Sort.Direction.DESC;
        }
        if(sortBy != null && sortBy.equals("")) {
            sortBy = "title";
        }
        Page<Comic> comics = comicRepository.
                findAllByTitleContainingIgnoreCase(title, PageRequest.of(offset, limit, direction, sortBy));
        return comics.getContent();
    }

    @Override
    public List<ComicDTO> getBySeriesIdSimple(Long seriesId) {
        return comicRepository.getBySeriesIdSimple(seriesId);
    }

    @Override
    public Comic getById(Long id){
        return comicRepository.findById(id).orElse(null);
    }

    @Override
    public List<ComicDTO> getComicsWithPostInfo() {
        return comicRepository.findAllWithPostInfo();
    }

    @Override
    public ComicDTO getComicWithSeries(Long comicId) {
        return comicRepository.getComicWithSeries(comicId);
    }

    @Override
    public List<ComicDTO> getComicsWithSeries() {
        return comicRepository.getComicsWithSeries();
    }

    @Override
    public List<ComicDTO> getBySeriesId(Long seriesId) {
        return comicRepository.getBySeriesId(seriesId);
    }

    @Override
    public List<ComicDTO> getComicsWithCharacter(Long characterId) {
        return comicRepository.getComicsWithCharacter(characterId);
    }
}
