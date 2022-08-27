package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.ComicProgress;
import com.wemarvel.wemarvel.model.dto.ComicProgressDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComicProgressRepository extends JpaRepository<ComicProgress, Long> {
    @Query("SELECT DISTINCT NEW com.wemarvel.wemarvel.model.dto.ComicProgressDTO(c1.comicId, comic.title, comic.url, " +
            "comic.thumbnail, c1.userId, c1.status, c1.pagesRead, c1.rating, c1.startDate, c1.endDate, c2.userId, " +
            "c2.status, c2.pagesRead, c2.rating, c2.startDate, c2.endDate) " +
            "FROM ComicProgress c " +
            "LEFT JOIN ComicProgress c1 ON c1.userId = ?1 " +
            "LEFT JOIN ComicProgress c2 ON c2.userId = ?2 " +
            "LEFT JOIN Comic comic ON comic.id = c1.comicId " +
            "WHERE c1.comicId = c2.comicId")
    List<ComicProgressDTO> getSharedComics(Long firstUserId, Long secondUserId);

    @Query("SELECT NEW com.wemarvel.wemarvel.model.dto.ComicProgressDTO(c.comicId, comic.title, comic.url, " +
            "comic.thumbnail, comic.seriesId, s.title, s.thumbnail, c.userId, c.status, c.pagesRead, c.rating, c.startDate, c.endDate) " +
            "FROM ComicProgress c " +
            "LEFT JOIN Comic comic ON c.comicId = c.id " +
            "LEFT JOIN Series s ON s.id = comic.seriesId " +
            "WHERE c.userId = ?1 AND c.comicId NOT IN (SELECT c1.comicId from ComicProgress c1 WHERE c1.userId = ?2)")
    List<ComicProgressDTO> getUniqueComics(Long firstUserId, Long secondUserId);

    @Query("SELECT NEW com.wemarvel.wemarvel.model.dto.ComicProgressDTO(cp.comicId, comic.title, comic.url, " +
            "comic.thumbnail, comic.seriesId, s.title, s.thumbnail, cp.userId, cp.status, cp.pagesRead, cp.rating, cp.startDate, cp.endDate) " +
            "FROM ComicProgress cp " +
            "LEFT JOIN Comic comic ON comic.id = cp.comicId " +
            "LEFT JOIN Series s ON s.id = comic.seriesId " +
            "WHERE cp.userId = ?1")
    List<ComicProgressDTO> getUserComics(Long id);

    @Query("SELECT cp FROM ComicProgress cp WHERE cp.userId = ?1 AND cp.comicId = ?2")
    ComicProgress getByUserAndComic(Long id, Long comicId);
}
