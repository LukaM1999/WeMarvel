package com.wemarvel.wemarvel.service.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.wemarvel.wemarvel.model.Post;
import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.model.dto.PostDTO;
import com.wemarvel.wemarvel.repository.PostRepository;
import com.wemarvel.wemarvel.service.PostService;
import org.hibernate.id.GUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.wemarvel.wemarvel.util.SecurityContextUtils.getSignedInUser;

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
        RegisteredUser registeredUser = getSignedInUser();
        if(registeredUser == null) return null;
        post.setOwnerId(registeredUser.getId());
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    @Override
    public String uploadImage(Long boardId, Long topicId, MultipartFile image) {
        Bucket bucket = StorageClient.getInstance().bucket();
        String imageName = UUID.randomUUID().toString().replace("-", "");
        try {
            bucket.create("board/" + boardId + "/topic/" + topicId + "/" + imageName, image.getBytes(), image.getContentType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imageName;
    }

    @Override
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        RegisteredUser registeredUser = getSignedInUser();
        if(registeredUser == null || !registeredUser.getId().equals(post.getOwnerId())
                || !registeredUser.getRole().getAuthority().equals("ADMIN"))
            throw new RuntimeException("You are not authorized to delete this post");
        post.setDeleted(true);
        postRepository.save(post);
    }

    @Override
    public Post updatePost(Long postId, String content) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        RegisteredUser registeredUser = getSignedInUser();
        if(registeredUser == null) throw new UsernameNotFoundException("User not logged in");
        if(!registeredUser.getId().equals(post.getOwnerId())
                && !registeredUser.getRole().getAuthority().equals("ADMIN"))
            throw new RuntimeException("You are not authorized to modify this post");
        post.setContent(content);
        post.setModifiedAt(LocalDateTime.now());
        post.setModifiedById(registeredUser.getId());
        post.setModifications(post.getModifications() + 1);
        return postRepository.save(post);
    }

    @Override
    public void deleteBoardPosts(Long boardId) {
        List<Post> posts = postRepository.findByBoardId(boardId);
        postRepository.deleteAll(posts);
    }

    @Override
    public void deleteTopicPosts(Long topicId) {
        postRepository.deleteAllByTopicId(topicId);
    }
}
