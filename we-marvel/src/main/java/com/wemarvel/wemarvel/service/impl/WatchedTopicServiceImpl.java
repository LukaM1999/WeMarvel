package com.wemarvel.wemarvel.service.impl;

import com.wemarvel.wemarvel.model.WatchedTopic;
import com.wemarvel.wemarvel.repository.WatchedTopicRepository;
import com.wemarvel.wemarvel.service.WatchedTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.wemarvel.wemarvel.util.SecurityContextUtils.getSignedInUsername;

@Service
public class WatchedTopicServiceImpl implements WatchedTopicService {
    @Autowired
    private WatchedTopicRepository watchedTopicRepository;

    @Override
    public WatchedTopic watchTopic(Long topicId) {
        String username = getSignedInUsername();
        if(username == null) throw new IllegalStateException("User is not signed in");
        WatchedTopic watchedTopic = watchedTopicRepository.findByTopicIdAndUsername(topicId, username);
        if(watchedTopic != null) {
            watchedTopicRepository.deleteById(watchedTopic.getId());
            return null;
        }
        return watchedTopicRepository.save(new WatchedTopic(null, username, topicId));
    }

    @Override
    public List<WatchedTopic> getWatchedTopics() {
        String username = getSignedInUsername();
        if(username == null) throw new IllegalStateException("User is not signed in");
        return watchedTopicRepository.findByUsername(username);
    }

    @Override
    public WatchedTopic getWatchedTopic(Long topicId, String username) {
        return watchedTopicRepository.findByTopicIdAndUsername(topicId, username);
    }
}
