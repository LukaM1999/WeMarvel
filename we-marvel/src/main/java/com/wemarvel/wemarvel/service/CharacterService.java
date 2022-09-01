package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.MarvelCharacter;
import com.wemarvel.wemarvel.model.dto.CharacterDTO;

import java.util.List;

public interface CharacterService {
    MarvelCharacter getCharacter(Long id);
    List<MarvelCharacter> getCharacters(String name, int limit, int offset, String sortBy, String sortOrder);
    int getCharactersCount(String name);
    List<CharacterDTO> getCharactersByAverageRating(int limit, int offset);
    List<CharacterDTO> getCharactersByRatingCount(int limit, int offset);
    List<CharacterDTO> getCharactersWithPostInfo();
    List<CharacterDTO> getCharactersInComic(Long comicId);
    List<CharacterDTO> getCharactersInSeries(Long seriesId);
}
