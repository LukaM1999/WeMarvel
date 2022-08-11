package com.wemarvel.wemarvel.service.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.wemarvel.wemarvel.model.Post;
import com.wemarvel.wemarvel.model.dto.PostDTO;
import com.wemarvel.wemarvel.repository.PostRepository;
import com.wemarvel.wemarvel.service.PostService;
import org.hibernate.id.GUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<PostDTO> getPostsByTopicId(Long topicId) {
        return postRepository.getPostsByTopicId(topicId);
    }

    @Override
    public Post createPost(Post post) {
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    @Override
    public String uploadImage(MultipartFile image) {
        Bucket bucket = StorageClient.getInstance().bucket();
        String imageName = UUID.randomUUID().toString().replace("-", "");
        try {
            bucket.create(imageName, image.getBytes(), image.getContentType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imageName;
    }

    @Override
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setDeleted(true);
        postRepository.save(post);
    }
}
