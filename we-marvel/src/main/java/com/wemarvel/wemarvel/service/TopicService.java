package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.Topic;
import com.wemarvel.wemarvel.model.dto.TopicDTO;

import java.util.List;

public interface TopicService {
    List<Topic> getRecentBoardTopics(Long boardId);
    TopicDTO getTopicWithPosts(Long topicId);
    String getTopicName(Long topicId);
    List<TopicDTO> getBoardTopics(Long id);
    TopicDTO createTopic(TopicDTO topicDTO);
    List<TopicDTO> getByCharacterId(Long id);
    List<TopicDTO> getByComicId(Long comicId);
    List<TopicDTO> getBySeriesId(Long seriesId);
    void deleteBoardTopics(Long boardId);
}
