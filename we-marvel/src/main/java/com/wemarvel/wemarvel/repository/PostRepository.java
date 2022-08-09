package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.Post;
import com.wemarvel.wemarvel.model.dto.PostDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT new com.wemarvel.wemarvel.model.dto.PostDTO(p.id, u.username, p.topicId, t.title, " +
            "p.createdAt, p.content, p.modifiedAt, p.modifiedByUsername, p.modifications, p.deleted) " +
            "FROM Post p " +
            "LEFT JOIN RegisteredUser u ON p.ownerUsername = u.username " +
            "LEFT JOIN Topic t ON p.topicId = t.id " +
            "WHERE p.topicId = ?1 " +
            "ORDER BY p.createdAt DESC")
    List<PostDTO> getPostsByTopicId(Long topicId);
}
