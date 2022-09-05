package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.Post;
import com.wemarvel.wemarvel.model.dto.PostDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.PostDTO(p.id, u.id, u.username, u.imageUrl, p.topicId, t.title, " +
            "p.quotedPostId, p.createdAt, p.content, p.modifiedAt, p.modifiedById, modified.username, " +
            "p.modifications, p.deleted) " +
            "FROM Post p " +
            "LEFT JOIN RegisteredUser u ON p.ownerId = u.id " +
            "LEFT JOIN Topic t ON p.topicId = t.id " +
            "LEFT JOIN RegisteredUser modified ON p.modifiedById = modified.id " +
            "WHERE p.topicId = ?1 " +
            "ORDER BY p.createdAt ASC")
    List<PostDTO> getPostsByTopicId(Long topicId);

    @Query("SELECT p FROM Post p " +
            "INNER JOIN Topic t ON t.boardId = ?1 " +
            "AND p.topicId = t.id")
    List<Post> findByBoardId(Long boardId);
}
