package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.WatchedTopic;

import java.util.List;

public interface WatchedTopicService {
    WatchedTopic watchTopic(Long topicId);
    List<WatchedTopic> getWatchedTopics();
    WatchedTopic getWatchedTopic(Long topicId, Long userId);
}
