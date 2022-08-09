package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.Post;
import com.wemarvel.wemarvel.model.dto.PostDTO;

import java.util.List;

public interface PostService {
    List<PostDTO> getPostsByTopicId(Long topicId);
}
