package com.wemarvel.wemarvel.service.impl;

import com.wemarvel.wemarvel.model.MarvelCharacter;
import com.wemarvel.wemarvel.model.dto.CharacterDTO;
import com.wemarvel.wemarvel.repository.CharacterRepository;
import com.wemarvel.wemarvel.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.awt.print.Pageable;
import java.util.List;

@Service
public class CharacterServiceImpl implements CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    @Override
    public MarvelCharacter getCharacter(Long id) {
        return characterRepository.findById(id).orElse(null);
    }

    @Override
    public List<MarvelCharacter> getCharacters(String name, int limit, int offset, String sortBy, String sortOrder) {
        Sort.Direction direction = Sort.Direction.ASC;
        if(sortOrder != null && sortOrder.equals("desc")) {
            direction = Sort.Direction.DESC;
        }
        if(sortBy != null && sortBy.equals("")) {
            sortBy = "name";
        }
        Page<MarvelCharacter> characters = characterRepository.
                findAllByNameContainingIgnoreCase(name, PageRequest.of(offset, limit, direction, sortBy));
        return characters.getContent();
    }

    @Override
    public int getCharactersCount(String name) {
        return characterRepository.countAllByNameContainingIgnoreCase(name);
    }

    @Override
    public List<CharacterDTO> getCharactersByAverageRating(int limit, int offset) {
        Page<CharacterDTO> characters = characterRepository.findAllByAverageRating(PageRequest.of(offset, limit));
        return characters.getContent();
    }

    @Override
    public List<CharacterDTO> getCharactersByRatingCount(int limit, int offset) {
        Page<CharacterDTO> characters = characterRepository.findAllByRatingCount(PageRequest.of(offset, limit));
        return characters.getContent();
    }
}
