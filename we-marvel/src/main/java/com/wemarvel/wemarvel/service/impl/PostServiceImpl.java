package com.wemarvel.wemarvel.service.impl;

import com.wemarvel.wemarvel.model.Post;
import com.wemarvel.wemarvel.model.dto.PostDTO;
import com.wemarvel.wemarvel.repository.PostRepository;
import com.wemarvel.wemarvel.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<PostDTO> getPostsByTopicId(Long topicId) {
        return postRepository.getPostsByTopicId(topicId);
    }
}
