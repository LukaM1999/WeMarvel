package com.wemarvel.wemarvel.controller;

import com.wemarvel.wemarvel.model.MarvelCharacter;
import com.wemarvel.wemarvel.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/characters")
public class CharacterController {

    @Autowired
    private CharacterService characterService;

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
}
