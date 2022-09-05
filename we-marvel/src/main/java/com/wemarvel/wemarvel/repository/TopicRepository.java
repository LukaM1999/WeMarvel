package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.Topic;
import com.wemarvel.wemarvel.model.WatchedTopic;
import com.wemarvel.wemarvel.model.dto.TopicDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.persistence.QueryHint;
import java.util.List;

public interface TopicRepository extends PagingAndSortingRepository<Topic, Long> {

    @Query("SELECT new com.wemarvel.wemarvel.model.Topic(t.id, p.ownerId, t.id, t.boardId, t.id, " +
            "p.createdAt, t.title, false, false) " +
            "FROM Topic t " +
            "INNER JOIN Post p ON t.id = p.topicId " +
            "WHERE t.boardId = ?1 " +
            "GROUP BY t.id, p.ownerId, t.boardId, p.createdAt, t.title " +
            "ORDER BY p.createdAt DESC")
    List<Topic> getRecentBoardTopics(Long id, PageRequest pageRequest);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.TopicDTO(t.id, u.id, u.username, t.boardId, b.title, " +
            "t.createdAt, t.title, t.sticky, t.locked) " +
            "FROM Topic t " +
            "LEFT JOIN RegisteredUser u ON t.ownerId = u.id " +
            "LEFT JOIN Board b ON t.boardId = b.id " +
            "WHERE t.id = ?1")
    TopicDTO getTopicWithUser(Long topicId);

    @Query("SELECT t.title from Topic t WHERE t.id = ?1")
    String getTopicName(Long topicId);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.TopicDTO(t.id, t.title, COUNT(p), max(p.createdAt), " +
            "t.createdAt, t.ownerId, u.username) " +
            "FROM Topic t " +
            "LEFT JOIN Board b ON t.boardId = b.id " +
            "LEFT JOIN Post p ON t.id = p.topicId " +
            "LEFT JOIN RegisteredUser u ON t.ownerId = u.id " +
            "WHERE t.boardId = ?1 " +
            "GROUP BY t.id, t.title, u.username")
    List<TopicDTO> getBoardTopics(Long id);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.TopicDTO(t.id, t.title, COUNT(p), max(p.createdAt), " +
            "t.createdAt, t.ownerId, u.username, c.id, c.name) " +
            "FROM Topic t " +
            "LEFT JOIN Board b ON t.boardId = b.id " +
            "LEFT JOIN Post p ON t.id = p.topicId " +
            "LEFT JOIN RegisteredUser u ON t.ownerId = u.id " +
            "LEFT JOIN MarvelCharacter c ON c.id = t.marvelEntityId " +
            "WHERE t.boardId = 1 AND c IS NOT NULL " +
            "GROUP BY t.id, t.title, u.username, c.id, c.name")
    List<TopicDTO> getCharacterBoardTopics(PageRequest pageRequest);

    @Query("SELECT wt from WatchedTopic wt WHERE wt.topicId = ?1 AND wt.userId = ?2")
    WatchedTopic getWatchedTopic(Long topicId, Long userId);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.TopicDTO(t.id, t.title, COUNT(p), max(p.createdAt), " +
            "t.createdAt, t.ownerId, u.username, c.id, c.title) " +
            "FROM Topic t " +
            "LEFT JOIN Board b ON t.boardId = b.id " +
            "LEFT JOIN Post p ON t.id = p.topicId " +
            "LEFT JOIN RegisteredUser u ON t.ownerId = u.id " +
            "LEFT JOIN Comic c ON c.id = t.marvelEntityId " +
            "WHERE t.boardId = 1 AND c IS NOT NULL " +
            "GROUP BY t.id, t.title, u.username, c.id, c.title")
    List<TopicDTO> getComicBoardTopics(PageRequest pageRequest);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.TopicDTO(t.id, t.title, COUNT(p), max(p.createdAt), " +
            "t.createdAt, t.ownerId, u.username, c.id, c.name) " +
            "FROM Topic t " +
            "INNER JOIN Board b ON t.boardId = b.id " +
            "INNER JOIN Post p ON t.id = p.topicId " +
            "INNER JOIN RegisteredUser u ON t.ownerId = u.id " +
            "INNER JOIN MarvelCharacter c ON c.id = ?1 " +
            "WHERE t.boardId = 1 AND t.marvelEntityId = c.id " +
            "GROUP BY t.id, t.title, u.username, c.id, c.name")
    List<TopicDTO> getAllByCharacterId(Long characterId);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.TopicDTO(t.id, t.title, COUNT(p), max(p.createdAt), " +
            "t.createdAt, t.ownerId, u.username, c.id, c.title) " +
            "FROM Topic t " +
            "INNER JOIN Board b ON t.boardId = b.id " +
            "INNER JOIN Post p ON t.id = p.topicId " +
            "INNER JOIN RegisteredUser u ON t.ownerId = u.id " +
            "INNER JOIN Comic c ON c.id = ?1 " +
            "WHERE t.boardId = 2 AND t.marvelEntityId = c.id " +
            "GROUP BY t.id, t.title, u.username, c.id, c.title")
    List<TopicDTO> getAllByComicId(Long comicId);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.TopicDTO(t.id, t.title, COUNT(p), max(p.createdAt), " +
            "t.createdAt, t.ownerId, u.username, s.id, s.title) " +
            "FROM Topic t " +
            "INNER JOIN Board b ON t.boardId = b.id " +
            "INNER JOIN Post p ON t.id = p.topicId " +
            "INNER JOIN RegisteredUser u ON t.ownerId = u.id " +
            "INNER JOIN Series s ON s.id = ?1 " +
            "WHERE t.boardId = 3 AND t.marvelEntityId = s.id " +
            "GROUP BY t.id, t.title, u.username, s.id, s.title")
    List<TopicDTO> getAllBySeriesId(Long seriesId);

    void deleteAllByBoardId(Long boardId);
}
