package com.wemarvel.wemarvel.service.impl;

import com.wemarvel.wemarvel.model.WatchedTopic;
import com.wemarvel.wemarvel.repository.WatchedTopicRepository;
import com.wemarvel.wemarvel.service.WatchedTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.wemarvel.wemarvel.util.SecurityContextUtils.getSignedInUser;

@Service
public class WatchedTopicServiceImpl implements WatchedTopicService {
    @Autowired
    private WatchedTopicRepository watchedTopicRepository;

    @Override
    public WatchedTopic watchTopic(Long topicId) {
        Long userId = Objects.requireNonNull(getSignedInUser()).getId();
        WatchedTopic watchedTopic = watchedTopicRepository.findByTopicIdAndUserId(topicId, userId);
        if(watchedTopic != null) {
            watchedTopicRepository.deleteById(watchedTopic.getId());
            return null;
        }
        return watchedTopicRepository.save(new WatchedTopic(null, userId, topicId));
    }

    @Override
    public List<WatchedTopic> getWatchedTopics() {
        return watchedTopicRepository.findByUserId(Objects.requireNonNull(getSignedInUser()).getId());
    }

    @Override
    public WatchedTopic getWatchedTopic(Long topicId, Long userId) {
        return watchedTopicRepository.findByTopicIdAndUserId(topicId, userId);
    }
}
