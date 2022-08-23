package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.ComicProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComicProgressRepository extends JpaRepository<Long, ComicProgress> {
    @Query("SELECT c from ComicProgress c " +
            "LEFT JOIN ComicProgress c1 on c1.userId = ?1 " +
            "LEFT JOIN ComicProgress c2 on c2.userId = ?2 " +
            "WHERE c1.comicId = c2.comicId")
    List<ComicProgress> getSharedComics(Long firstUserId, Long secondUserId);
}
