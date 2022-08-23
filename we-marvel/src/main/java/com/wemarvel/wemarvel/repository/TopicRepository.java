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
            "LEFT JOIN Post p ON t.id = p.topicId " +
            "WHERE t.id = ?1 " +
            "GROUP BY t.id, p.ownerId, t.boardId, p.createdAt, t.title " +
            "ORDER BY p.createdAt DESC")
    List<Topic> getRecentBoardTopics(Long id, PageRequest pageRequest);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.TopicDTO(t.id, u.id, u.username, t.boardId, b.title, " +
            "t.marvelEntityId, t.createdAt, t.title, t.sticky, t.locked) " +
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
            "GROUP BY t.id, t.title")
    List<TopicDTO> getBoardTopics(Long id);

    @Query("SELECT wt from WatchedTopic wt WHERE wt.topicId = ?1 AND wt.userId = ?2")
    WatchedTopic getWatchedTopic(Long topicId, Long userId);
}
