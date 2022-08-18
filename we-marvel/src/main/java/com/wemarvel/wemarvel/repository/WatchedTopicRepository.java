package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.WatchedTopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchedTopicRepository extends JpaRepository<WatchedTopic, Long> {

    WatchedTopic findByTopicIdAndUsername(Long topicId, String username);
    List<WatchedTopic> findByUsername(String username);
}
