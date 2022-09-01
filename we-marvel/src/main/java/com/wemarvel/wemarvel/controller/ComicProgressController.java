package com.wemarvel.wemarvel.controller;

import com.wemarvel.wemarvel.model.ComicProgress;
import com.wemarvel.wemarvel.model.dto.CharacterDTO;
import com.wemarvel.wemarvel.model.dto.ComicDTO;
import com.wemarvel.wemarvel.model.dto.ComicProgressDTO;
import com.wemarvel.wemarvel.model.dto.SeriesDTO;
import com.wemarvel.wemarvel.service.ComicProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/comicProgress")
public class ComicProgressController {

    @Autowired
    private ComicProgressService comicProgressService;

    @GetMapping("/shared")
    public List<ComicProgressDTO> getSharedComics(@PathParam("u1") String u1,
                                               @PathParam("u2") String u2) {
        return comicProgressService.getSharedComics(u1, u2);
    }

    @GetMapping("/unique")
    public List<ComicProgressDTO> getUniqueComics(@PathParam("u1") String u1,
                                                  @PathParam("u2") String u2) {
        return comicProgressService.getUniqueComics(u1, u2);
    }

    @GetMapping("/user/{username}")
    public List<ComicProgressDTO> getUserComics(@PathVariable String username) {
        return comicProgressService.getUserComics(username);
    }

    @GetMapping("/user/{username}/comic/{comicId}")
    public ComicProgressDTO getUserComic(@PathVariable String username,
                                         @PathVariable Long comicId) {
        return comicProgressService.getUserComic(username, comicId);
    }

    @PostMapping("")
    public ComicProgress create(@RequestBody ComicProgressDTO comicProgress){
        return comicProgressService.create(comicProgress);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        comicProgressService.delete(id);
    }

    @GetMapping("/comic")
    public List<ComicProgressDTO> getUnreviewedComics() {
        return comicProgressService.getUnreviewedComics();
    }

    @GetMapping("/series")
    public List<SeriesDTO> getUnreviewedSeries() {
        return comicProgressService.getUnreviewedSeries();
    }

    @GetMapping("/character")
    public List<CharacterDTO> getUnreviewedCharacters() {
        return comicProgressService.getUnreviewedCharacters();
    }
}
