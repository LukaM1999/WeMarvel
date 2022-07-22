package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.MarvelCharacter;

import java.util.List;

public interface CharacterService {
    MarvelCharacter getCharacter(Long id);
    List<MarvelCharacter> getCharacters(String name, int limit, int offset, String sortBy, String sortOrder);
    int getCharactersCount(String name);
}
