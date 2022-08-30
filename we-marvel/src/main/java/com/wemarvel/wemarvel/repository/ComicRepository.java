package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.Comic;
import com.wemarvel.wemarvel.model.MarvelCharacter;
import com.wemarvel.wemarvel.model.dto.CharacterDTO;
import com.wemarvel.wemarvel.model.dto.ComicDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComicRepository extends PagingAndSortingRepository<Comic, Long> {

    Page<Comic> findAllByTitleContainingIgnoreCase(String title, PageRequest request);


    @Query("SELECT new com.wemarvel.wemarvel.model.dto.ComicDTO(c.id, c.seriesId, c.title, c.description, c.thumbnail, " +
            "c.url, c.pageCount, c.averageRating, COUNT(r.rating)) " +
            "FROM Comic c " +
            "LEFT JOIN Review r ON c.id = r.marvelEntityId " +
            "GROUP BY c.id, c.seriesId, c.title, c.description, c.thumbnail, c.url " +
            "ORDER BY c.averageRating DESC, c.title ASC")
    Page<ComicDTO> findAllByAverageRating(PageRequest pageRequest);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.ComicDTO(c.id, c.seriesId, c.title, c.description, c.thumbnail, " +
            "c.url, c.pageCount, COUNT(p)) " +
            "FROM Comic c " +
            "LEFT JOIN ComicProgress p ON c.id = p.comicId " +
            "WHERE p.pagesRead > 0 " +
            "GROUP BY c.id, c.seriesId, c.title, c.description, c.thumbnail, c.url, c.pageCount " +
            "ORDER BY COUNT(p) DESC, c.title ASC")
    Page<ComicDTO> findAllByReadingCount(PageRequest pageRequest);

    @Query("SELECT NEW com.wemarvel.wemarvel.model.dto.ComicDTO(c.id, c.seriesId, c.title, c.thumbnail) " +
            "FROM Comic c WHERE c.seriesId = ?1")
    List<ComicDTO> getBySeriesIdSimple(Long seriesId);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.ComicDTO(c.id, c.seriesId, c.title, c.thumbnail, " +
            "COUNT(p), MAX(p.createdAt)) " +
            "FROM Comic c " +
            "LEFT JOIN Topic t ON c.id = t.marvelEntityId " +
            "LEFT JOIN Post p ON p.topicId = t.id " +
            "GROUP BY c.id, c.seriesId, c.title, c.thumbnail " +
            "ORDER BY c.title")
    List<ComicDTO> findAllWithPostInfo();
}
