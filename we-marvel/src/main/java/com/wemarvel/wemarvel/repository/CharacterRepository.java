package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.MarvelCharacter;
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

//    @Query(value="SELECT c.id, c.name, c.description, c.thumbnail, c.resourceURI, c.modified " +
//            "FROM marvel_character c ORDER BY c.modified DESC limit ?1 offset ?2", nativeQuery = true)
    //Page<MarvelCharacter> findAll(PageRequest pageRequest);

    Page<MarvelCharacter> findAllByNameContainingIgnoreCase(String name, PageRequest request);

    int countAllByNameContainingIgnoreCase(String name);
}
