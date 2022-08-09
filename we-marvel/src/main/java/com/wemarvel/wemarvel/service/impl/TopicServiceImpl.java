package com.wemarvel.wemarvel.service.impl;

import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.model.Topic;
import com.wemarvel.wemarvel.model.dto.PostDTO;
import com.wemarvel.wemarvel.model.dto.ProfileDTO;
import com.wemarvel.wemarvel.model.dto.TopicDTO;
import com.wemarvel.wemarvel.repository.TopicRepository;
import com.wemarvel.wemarvel.service.PostService;
import com.wemarvel.wemarvel.service.RegisteredUserService;
import com.wemarvel.wemarvel.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private RegisteredUserService registeredUserService;

    @Override
    public List<Topic> getRecentBoardTopics(Long boardId) {
        return topicRepository.getRecentBoardTopics(boardId, PageRequest.of(0, 2));
    }

    @Override
    public TopicDTO getTopicWithPosts(Long topicId) {
        TopicDTO topicDTO = topicRepository.getTopicWithUser(topicId);
        if(topicDTO == null) return null;
        List<PostDTO> posts = postService.getPostsByTopicId(topicId);
        for (PostDTO post : posts) {
            RegisteredUser profile = registeredUserService.getUserByUsername(post.getOwnerUsername());
            if(profile == null) continue;
            ProfileDTO profileDTO = new ProfileDTO();
            profileDTO.setId(profile.getEmail());
            profileDTO.setUsername(profile.getUsername());
            profileDTO.setRole(profile.getRole().getAuthority());
            profileDTO.setDeleted(profile.isDeleted());
            post.setOwner(profileDTO);
            topicDTO.getPosts().add(post);
        }
        return topicDTO;
    }
}
