package com.wemarvel.wemarvel.controller;

import com.wemarvel.wemarvel.model.Comic;
import com.wemarvel.wemarvel.model.MarvelCharacter;
import com.wemarvel.wemarvel.model.dto.CharacterDTO;
import com.wemarvel.wemarvel.model.dto.ComicDTO;
import com.wemarvel.wemarvel.service.CharacterService;
import com.wemarvel.wemarvel.service.ComicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comic")
public class ComicController {
    @Autowired
    private ComicService comicService;

    @GetMapping("/{comicId}")
    public Comic getComic(@PathVariable Long comicId) {
        return comicService.getById(comicId);
    }

    @GetMapping("")
    public List<Comic> getComics(@PathParam("title") Optional<String> title, @PathParam("limit") int limit,
                                     @PathParam("offset") int offset,
                                     @PathParam("sortBy") Optional<String> sortBy,
                                     @PathParam("sortOrder") Optional<String> sortOrder) {
        if(limit > 0 && offset >= 0) {
            return comicService.getComics(title.orElse(""), limit, offset, sortBy.orElse("title"),
                    sortOrder.orElse("asc"));
        }
        return comicService.getComics(title.orElse(""), 10, 0, sortBy.orElse("title"),
                sortOrder.orElse("asc"));
    }

    @GetMapping("/series/{seriesId}/simple")
    public List<ComicDTO> getBySeriesIdSimple(@PathVariable Long seriesId){
        return comicService.getBySeriesIdSimple(seriesId);
    }

    @GetMapping("/{comicId}/name")
    public String getComicName(@PathVariable Long comicId) {
        Comic comic = comicService.getById(comicId);
        if(comic == null) return null;
        return comic.getTitle();
    }

    @GetMapping("/withPostInfo")
    public List<ComicDTO> getComicsWithPostInfo(){
        return comicService.getComicsWithPostInfo();
    }

    @GetMapping("/{comicId}/withSeries")
    public ComicDTO getComicWithSeries(@PathVariable Long comicId){
        return comicService.getComicWithSeries(comicId);
    }

    @GetMapping("/withSeries")
    public List<ComicDTO> getComicsWithSeries(){
        return comicService.getComicsWithSeries();
    }

    @GetMapping("/series/{seriesId}")
    public List<ComicDTO> getComicsBySeries(@PathVariable Long seriesId){
        return comicService.getBySeriesId(seriesId);
    }

    @GetMapping("/character/{characterId}")
    public List<ComicDTO> getComicsWithCharacter(@PathVariable Long characterId){
        return comicService.getComicsWithCharacter(characterId);
    }

    @GetMapping("/{comicId}/withRating")
    public ComicDTO getComicWithRating(@PathVariable Long comicId){
        return comicService.getComicWithRating(comicId);
    }

    @GetMapping("/topRated")
    public List<ComicDTO> getTopRatedComics(){
        return comicService.getTopRatedComics();
    }

    @GetMapping("/popular")
    public List<ComicDTO> getPopularComics(){
        return comicService.getPopularComics();
    }

}
