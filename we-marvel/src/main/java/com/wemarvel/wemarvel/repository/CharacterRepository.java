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
            "c.averageRating, COUNT(r.rating)) " +
            "FROM MarvelCharacter c " +
            "LEFT JOIN Review r ON c.id = r.marvelEntityId " +
            "GROUP BY c.id, c.name, c.description," +
            " c.thumbnail, c.url, c.averageRating " +
            "ORDER BY c.averageRating DESC, c.name ASC")
    Page<CharacterDTO> findAllByAverageRating(PageRequest pageRequest);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.CharacterDTO(c.id, c.name, c.description, c.thumbnail, " +
            "c.url, AVG(r.rating), COUNT(r.rating)) " +
            "FROM MarvelCharacter c " +
            "LEFT JOIN Review r ON c.id = r.marvelEntityId " +
            "GROUP BY c.id, c.name, c.thumbnail, c.url " +
            "ORDER BY COUNT(r.rating) DESC")
    Page<CharacterDTO> findAllByRatingCount(PageRequest pageRequest);

}
