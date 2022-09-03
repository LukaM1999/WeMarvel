package com.wemarvel.wemarvel.controller;

import com.wemarvel.wemarvel.model.MarvelCharacter;
import com.wemarvel.wemarvel.model.dto.CharacterDTO;
import com.wemarvel.wemarvel.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/character")
public class CharacterController {

    @Autowired
    private CharacterService characterService;


    @GetMapping("/{characterId}")
    public MarvelCharacter getCharacter(@PathVariable Long characterId) {
        return characterService.getCharacter(characterId);
    }

    @GetMapping("/{characterId}/withRating")
    public CharacterDTO getCharacterWithRating(@PathVariable Long characterId) {
        return characterService.getCharacterWithRating(characterId);
    }

    @GetMapping("")
    public List<MarvelCharacter> getCharacters(@PathParam("name") Optional<String> name, @PathParam("limit") int limit,
                                               @PathParam("offset") int offset,
                                               @PathParam("sortBy") Optional<String> sortBy,
                                               @PathParam("sortOrder") Optional<String> sortOrder) {
        if(limit > 0 && offset >= 0) {
            return characterService.getCharacters(name.orElse(""), limit, offset, sortBy.orElse("name"),
                    sortOrder.orElse("asc"));
        }
        return characterService.getCharacters(name.orElse(""), 10, 0, sortBy.orElse("name"),
                sortOrder.orElse("asc"));
    }

    @GetMapping("/count")
    public int getCharactersCount(@PathParam("name") Optional<String> name) {
        return characterService.getCharactersCount(name.orElse(""));
    }

    @GetMapping("/topRated")
    public List<CharacterDTO> getCharactersByAverageRating(@PathParam("limit") Optional<Integer> limit,
                                                           @PathParam("offset") Optional<Integer> offset) {
        if(limit.orElse(0) > 0 && offset.orElse(0) >= 0) {
            return characterService.getCharactersByAverageRating(limit.orElse(100000), offset.orElse(0));
        }
        return characterService.getCharactersByAverageRating(100000, 0);
    }

    @GetMapping("/popular")
    public List<CharacterDTO> getPopularCharacters() {
        return characterService.getPopularCharacters();
    }

    @GetMapping("/withPostInfo")
    public List<CharacterDTO> getCharactersWithPostInfo() {
        return characterService.getCharactersWithPostInfo();
    }

    @GetMapping("/{characterId}/name")
    public String getCharacterName(@PathVariable Long characterId) {
        MarvelCharacter character = characterService.getCharacter(characterId);
        if(character == null) return null;
        return character.getName();
    }

    @GetMapping("/comic/{comicId}")
    public List<CharacterDTO> getCharactersInComic(@PathVariable Long comicId) {
        return characterService.getCharactersInComic(comicId);
    }

    @GetMapping("/series/{seriesId}")
    public List<CharacterDTO> getCharactersInSeries(@PathVariable Long seriesId) {
        return characterService.getCharactersInSeries(seriesId);
    }
}
