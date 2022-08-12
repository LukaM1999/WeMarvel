package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.Post;
import com.wemarvel.wemarvel.model.dto.PostDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    List<PostDTO> getPostsByTopicId(Long topicId);
    Post createPost(Post post);
    String uploadImage(MultipartFile image);
    void deletePost(Long postId);
    Post updatePost(Long postId, String content, String modifiedBy);
}
