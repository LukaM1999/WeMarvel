package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.MarvelCharacter;
import com.wemarvel.wemarvel.model.dto.CharacterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface CharacterRepository extends PagingAndSortingRepository<MarvelCharacter, Long> {

    Page<MarvelCharacter> findAllByNameContainingIgnoreCase(String name, PageRequest request);

    int countAllByNameContainingIgnoreCase(String name);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.CharacterDTO(c.id, c.name, c.description, c.thumbnail, c.url, " +
            "AVG(r.rating), COUNT(r.rating)) " +
            "FROM MarvelCharacter c " +
            "LEFT JOIN Review r ON c.id = r.marvelEntityId AND r.rating > 0 " +
            "GROUP BY c.id " +
            "ORDER BY COALESCE(AVG(r.rating), 0) DESC, c.name ASC")
    Page<CharacterDTO> findAllByAverageRating(PageRequest pageRequest);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.CharacterDTO(c.id, c.name, c.description, c.thumbnail, " +
            "c.url, AVG(r.rating), COUNT(r.rating)) " +
            "FROM MarvelCharacter c " +
            "LEFT JOIN Review r ON c.id = r.marvelEntityId " +
            "GROUP BY c.id, c.name, c.thumbnail, c.url " +
            "ORDER BY COUNT(r.rating) DESC")
    Page<CharacterDTO> findAllByRatingCount(PageRequest pageRequest);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.CharacterDTO(c.id, c.name, c.thumbnail, " +
            "COUNT(distinct t.id), COUNT(p), MAX(p.createdAt)) " +
            "FROM MarvelCharacter c " +
            "LEFT JOIN Topic t ON c.id = t.marvelEntityId " +
            "LEFT JOIN Post p ON p.topicId = t.id " +
            "GROUP BY c.id, c.name, c.thumbnail " +
            "ORDER BY c.name")
    List<CharacterDTO> findAllWithPostInfo();

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.CharacterDTO(c.id, c.name, c.description, c.thumbnail) " +
            "FROM MarvelCharacter c " +
            "INNER JOIN CharacterInComic cic ON cic.comicId = ?1 " +
            "WHERE cic.comicId = ?1 AND cic.characterId = c.id " +
            "GROUP BY c.id, c.name, c.description, c.thumbnail " +
            "ORDER BY c.name")
    List<CharacterDTO> getCharactersInComic(Long comicId);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.CharacterDTO(c.id, c.name, c.description, c.thumbnail) " +
            "FROM MarvelCharacter c " +
            "INNER JOIN CharacterInSeries cis ON cis.seriesId = ?1 " +
            "WHERE cis.seriesId = ?1 AND cis.characterId = c.id " +
            "GROUP BY c.id, c.name, c.description, c.thumbnail " +
            "ORDER BY c.name")
    List<CharacterDTO> getCharactersInSeries(Long seriesId);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.CharacterDTO(c.id, c.name, c.description, c.thumbnail, c.url, " +
            "AVG(r.rating), COUNT(r.rating)) " +
            "FROM MarvelCharacter c " +
            "LEFT JOIN Review r ON c.id = r.marvelEntityId AND r.rating > 0 " +
            "WHERE c.id = ?1 " +
            "GROUP BY c.id")
    CharacterDTO getCharacterWithRating(Long characterId);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.CharacterDTO(c.id, c.name, c.description, c.thumbnail, c.url, " +
            "AVG(r.rating), COUNT(r)) " +
            "FROM MarvelCharacter c " +
            "LEFT JOIN Review r ON c.id = r.marvelEntityId " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(r) DESC, c.name ASC")
    List<CharacterDTO> getPopularCharacters();
}
