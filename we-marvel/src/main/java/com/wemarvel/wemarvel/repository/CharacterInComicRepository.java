package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.CharacterInComic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterInComicRepository extends JpaRepository<CharacterInComic, Long> {
    CharacterInComic getByCharacterIdAndComicId(Long characterId, Long comicId);
}
