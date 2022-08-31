package com.wemarvel.wemarvel.service.impl;

import com.wemarvel.wemarvel.model.Post;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.wemarvel.wemarvel.util.SecurityContextUtils.getSignedInUser;

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
            profileDTO.setId(profile.getId());
            profileDTO.setUsername(profile.getUsername());
            profileDTO.setRole(profile.getRole().getAuthority());
            profileDTO.setDeleted(profile.isDeleted());
            post.setOwner(profileDTO);
            topicDTO.getPosts().add(post);
        }
        return topicDTO;
    }

    @Override
    public String getTopicName(Long topicId) {
        return topicRepository.getTopicName(topicId);
    }

    @Override
    public List<TopicDTO> getBoardTopics(Long boardId) {
        List<TopicDTO> topics;
        if(boardId == 1) topics = topicRepository.getCharacterBoardTopics(PageRequest.of(0, 100));
        else if(boardId == 2) topics = topicRepository.getComicBoardTopics(PageRequest.of(0, 100));
        else topics = topicRepository.getBoardTopics(boardId);
        topics.sort((o1, o2) -> o2.getLastPostDate().compareTo(o1.getLastPostDate()));
        RegisteredUser user = getSignedInUser();
        if(user == null) return topics;
        for (TopicDTO topic : topics) {
            boolean watched = topicRepository.getWatchedTopic(topic.getId(), user.getId()) != null;
            topic.setWatched(watched);
        }
        return topics;
    }

    @Override
    public TopicDTO createTopic(TopicDTO topicDTO) {
        RegisteredUser user = getSignedInUser();
        if(user == null) throw new UsernameNotFoundException("User not found");
        Topic topic = new Topic();
        topic.setTitle(topicDTO.getTitle());
        topic.setBoardId(topicDTO.getBoardId());
        topic.setOwnerId(user.getId());
        topic.setCreatedAt(LocalDateTime.now());
        topic.setMarvelEntityId(topicDTO.getMarvelEntityId());
        Topic newTopic = topicRepository.save(topic);
        Post newPost = postService.createPost(new Post(user.getId(), newTopic.getId(),
                topicDTO.getFirstPostContent()));
        topic.setFirstPostId(newPost.getId());
        topicRepository.save(topic);
        topicDTO.setId(newTopic.getId());
        topicDTO.setCreatedAt(newTopic.getCreatedAt());
        return topicDTO;
    }

    @Override
    public List<TopicDTO> getByCharacterId(Long id) {
        List<TopicDTO> topics = topicRepository.getAllByCharacterId(id);
        topics.sort((o1, o2) -> o2.getLastPostDate().compareTo(o1.getLastPostDate()));
        RegisteredUser user = getSignedInUser();
        if(user == null) return topics;
        for (TopicDTO topic : topics) {
            boolean watched = topicRepository.getWatchedTopic(topic.getId(), user.getId()) != null;
            topic.setWatched(watched);
        }
        return topics;
    }

    @Override
    public List<TopicDTO> getByComicId(Long comicId) {
        List<TopicDTO> topics = topicRepository.getAllByComicId(comicId);
        topics.sort((o1, o2) -> o2.getLastPostDate().compareTo(o1.getLastPostDate()));
        RegisteredUser user = getSignedInUser();
        if(user == null) return topics;
        for (TopicDTO topic : topics) {
            boolean watched = topicRepository.getWatchedTopic(topic.getId(), user.getId()) != null;
            topic.setWatched(watched);
        }
        return topics;
    }

    @Override
    public List<TopicDTO> getBySeriesId(Long seriesId) {
        List<TopicDTO> topics = topicRepository.getAllBySeriesId(seriesId);
        topics.sort((o1, o2) -> o2.getLastPostDate().compareTo(o1.getLastPostDate()));
        RegisteredUser user = getSignedInUser();
        if(user == null) return topics;
        for (TopicDTO topic : topics) {
            boolean watched = topicRepository.getWatchedTopic(topic.getId(), user.getId()) != null;
            topic.setWatched(watched);
        }
        return topics;
    }
}
