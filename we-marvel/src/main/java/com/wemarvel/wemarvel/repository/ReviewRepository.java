package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.Review;
import com.wemarvel.wemarvel.model.dto.ReviewDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ReviewRepository extends PagingAndSortingRepository<Review, Long> {

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.ReviewDTO(r.id, r.marvelEntityId, u.id, u.username, u.enabled, " +
            "u.imageUrl, r.type, r.recommendation, r.rating, r.text, r.createdAt) " +
            "FROM Review r " +
            "INNER JOIN RegisteredUser u ON r.ownerId = u.id " +
            "WHERE u.id = ?1")
    List<ReviewDTO> findByOwnerId(Long id);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.ReviewDTO(r.id, r.marvelEntityId, u.id, u.username, u.enabled, " +
            "u.imageUrl, r.type, r.recommendation, r.rating, r.text, r.createdAt) " +
            "FROM Review r " +
            "INNER JOIN RegisteredUser u ON r.ownerId = u.id")
    List<ReviewDTO> getAll();

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.ReviewDTO(r.id, r.marvelEntityId, c.name, c.thumbnail, u.id, u.username, " +
            "u.imageUrl, u.enabled, r.type, r.recommendation, r.rating, r.text, r.createdAt) " +
            "FROM Review r " +
            "INNER JOIN RegisteredUser u ON r.ownerId = u.id " +
            "INNER JOIN MarvelCharacter c ON r.marvelEntityId = c.id " +
            "WHERE r.marvelEntityId = ?1")
    List<ReviewDTO> findByCharacterId(Long id);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.ReviewDTO(r.id, r.marvelEntityId, c.title, c.thumbnail, u.id, u.username, " +
            "u.imageUrl, u.enabled, r.type, r.recommendation, r.rating, r.text, r.createdAt) " +
            "FROM Review r " +
            "INNER JOIN RegisteredUser u ON r.ownerId = u.id " +
            "INNER JOIN Comic c ON r.marvelEntityId = c.id " +
            "WHERE r.marvelEntityId = ?1")
    List<ReviewDTO> findByComicId(Long id);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.ReviewDTO(r.id, r.marvelEntityId, s.title, s.thumbnail, u.id, u.username, " +
            "u.imageUrl, u.enabled, r.type, r.recommendation, r.rating, r.text, r.createdAt) " +
            "FROM Review r " +
            "INNER JOIN RegisteredUser u ON r.ownerId = u.id " +
            "INNER JOIN Series s ON r.marvelEntityId = s.id " +
            "WHERE r.marvelEntityId = ?1")
    List<ReviewDTO> findBySeriesId(Long id);


    @Query("SELECT new com.wemarvel.wemarvel.model.dto.ReviewDTO(r.id, r.marvelEntityId, u.id, u.username, u.enabled," +
            "u.imageUrl, r.type, r.recommendation, r.rating, r.text, r.createdAt) " +
            "FROM Review r " +
            "INNER JOIN RegisteredUser u ON r.ownerId = u.id " +
            "WHERE r.marvelEntityId = ?1 AND r.ownerId = ?2")
    ReviewDTO findByEntityAndUser(Long entityId, Long id);
}
