package com.wemarvel.wemarvel.controller;

import com.wemarvel.wemarvel.model.Board;
import com.wemarvel.wemarvel.model.Post;
import com.wemarvel.wemarvel.model.dto.BoardDTO;
import com.wemarvel.wemarvel.model.dto.PostDTO;
import com.wemarvel.wemarvel.model.dto.TopicDTO;
import com.wemarvel.wemarvel.service.BoardService;
import com.wemarvel.wemarvel.service.PostService;
import com.wemarvel.wemarvel.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private PostService postService;

    @GetMapping("/boards")
    public List<BoardDTO> getAllBoards() {
        return boardService.getAllBoards();
    }

    @GetMapping("/topic/{topicId}")
    public TopicDTO getTopicWithPosts(@PathVariable Long topicId) {
        return topicService.getTopicWithPosts(topicId);
    }

    @PostMapping("/post")
    public Post createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @PostMapping("/post/image")
    public String uploadImage(@RequestParam MultipartFile UploadFiles) {
        return postService.uploadImage(UploadFiles);
    }

    @DeleteMapping("/post/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
    }

    @GetMapping("/topic/{topicId}/name")
    public String getTopicName(@PathVariable Long topicId) {
        return topicService.getTopicName(topicId);
    }

    @PatchMapping("/post/{postId}")
    public Post updatePost(@PathVariable Long postId, @RequestBody PostDTO post) {
        return postService.updatePost(postId, post.getContent(), post.getModifiedByUsername());
    }
}
