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

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.ComicDTO(c.id, c.seriesId, s.title, c.title, " +
            "c.description, c.thumbnail, c.url, c.pageCount, " +
            "c.variantDescription, c.format, c.issueNumber) " +
            "FROM Comic c " +
            "LEFT JOIN Series s ON s.id = c.seriesId " +
            "WHERE c.seriesId = ?1")
    List<ComicDTO> getBySeriesId(Long seriesId);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.ComicDTO(c.id, c.seriesId, s.title, c.title, c.thumbnail, " +
            "COUNT(distinct t.id), COUNT(p), MAX(p.createdAt)) " +
            "FROM Comic c " +
            "LEFT JOIN Topic t ON c.id = t.marvelEntityId " +
            "LEFT JOIN Post p ON p.topicId = t.id " +
            "LEFT JOIN Series s ON s.id = c.seriesId " +
            "GROUP BY c.id, c.seriesId, s.title, c.title, c.thumbnail " +
            "ORDER BY s.title")
    List<ComicDTO> findAllWithPostInfo();

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.ComicDTO(c.id, c.seriesId, s.title, c.title, " +
            "c.description, c.thumbnail, " +
            "c.url, c.pageCount, c.variantDescription, c.format, c.issueNumber) " +
            "FROM Comic c " +
            "INNER JOIN Series s ON s.id = c.seriesId " +
            "WHERE c.id = ?1")
    ComicDTO getComicWithSeries(Long comicId);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.ComicDTO(c.id, c.seriesId, s.title, c.title, " +
            "c.description, c.thumbnail, c.url, c.pageCount, " +
            "c.variantDescription, c.format, c.issueNumber) " +
            "FROM Comic c " +
            "LEFT JOIN Series s ON s.id = c.seriesId")
    List<ComicDTO> getComicsWithSeries();

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.ComicDTO(c.id, c.seriesId, s.title, c.title, " +
            "c.description, c.thumbnail, c.url, c.pageCount, " +
            "c.variantDescription, c.format, c.issueNumber) " +
            "FROM Comic c " +
            "INNER JOIN CharacterInComic cic ON cic.characterId = ?1 " +
            "INNER JOIN Series s ON s.id = c.seriesId " +
            "WHERE cic.characterId = ?1 AND cic.comicId = c.id " +
            "GROUP BY c.id, c.seriesId, s.title, c.title, c.description, c.thumbnail, c.url, c.pageCount, " +
            "c.variantDescription, c.format, c.issueNumber " +
            "ORDER BY c.title")
    List<ComicDTO> getComicsWithCharacter(Long characterId);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.ComicDTO(c.id, c.seriesId, s.title, c.title, " +
            "c.description, c.thumbnail, " +
            "c.url, c.pageCount, c.variantDescription, c.format, c.issueNumber, AVG(cp.rating), COUNT(cp.rating)) " +
            "FROM Comic c " +
            "LEFT JOIN Series s ON s.id = c.seriesId " +
            "LEFT JOIN ComicProgress cp ON cp.comicId = c.id AND cp.rating > 0 " +
            "WHERE c.id = ?1 " +
            "GROUP BY c.id, s.title")
    ComicDTO getComicWithRating(Long comicId);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.ComicDTO(c.id, c.seriesId, s.title, c.title, " +
            "c.description, c.thumbnail, " +
            "c.url, c.pageCount, c.variantDescription, c.format, c.issueNumber, AVG(cp.rating), COUNT(cp.rating)) " +
            "FROM Comic c " +
            "LEFT JOIN Series s ON s.id = c.seriesId " +
            "LEFT JOIN ComicProgress cp ON cp.comicId = c.id AND cp.rating > 0 " +
            "GROUP BY c.id, s.title " +
            "ORDER BY COALESCE(AVG(cp.rating), 0) DESC, c.title ASC")
    List<ComicDTO> getTopRatedComics();

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.ComicDTO(c.id, c.seriesId, s.title, c.title, " +
            "c.description, c.thumbnail, " +
            "c.url, c.pageCount, c.variantDescription, c.format, c.issueNumber, AVG(cp.rating), COUNT(cp)) " +
            "FROM Comic c " +
            "LEFT JOIN Series s ON s.id = c.seriesId " +
            "LEFT JOIN ComicProgress cp ON cp.comicId = c.id " +
            "GROUP BY c.id, s.title " +
            "ORDER BY COUNT(cp) DESC, c.title ASC")
    List<ComicDTO> getPopularComics();
}
