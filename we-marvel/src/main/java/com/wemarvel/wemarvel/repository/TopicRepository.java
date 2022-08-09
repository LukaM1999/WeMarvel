package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.Topic;
import com.wemarvel.wemarvel.model.dto.TopicDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TopicRepository extends PagingAndSortingRepository<Topic, Long> {

    @Query("SELECT new com.wemarvel.wemarvel.model.Topic(t.id, p.ownerUsername, t.id, t.boardId, t.id, p.createdAt, " +
            "t.title, false, false) FROM Topic t " +
            "LEFT JOIN Post p ON t.id = p.topicId " +
            "WHERE t.id = ?1 " +
            "GROUP BY t.id, p.ownerUsername, t.boardId, p.createdAt, t.title " +
            "ORDER BY p.createdAt DESC")
    List<Topic> getRecentBoardTopics(Long id, PageRequest pageRequest);

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.TopicDTO(t.id, u.username, t.boardId, b.title, " +
            "t.marvelEntityId, t.createdAt, t.title, t.sticky, t.locked) " +
            "FROM Topic t " +
            "LEFT JOIN RegisteredUser u ON t.ownerUsername = u.username " +
            "LEFT JOIN Board b ON t.boardId = b.id " +
            "WHERE t.id = ?1")
    TopicDTO getTopicWithUser(Long topicId);
}
