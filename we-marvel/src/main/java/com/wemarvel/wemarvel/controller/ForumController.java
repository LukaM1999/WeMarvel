package com.wemarvel.wemarvel.controller;

import com.wemarvel.wemarvel.model.Board;
import com.wemarvel.wemarvel.model.Post;
import com.wemarvel.wemarvel.model.WatchedTopic;
import com.wemarvel.wemarvel.model.dto.*;
import com.wemarvel.wemarvel.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Autowired
    private WatchedTopicService watchedTopicService;


    @GetMapping("/boards")
    public List<BoardTopicsDTO> getAllBoards() {
        return boardService.getAllBoards();
    }

    @PostMapping("/board")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Board createBoard(@RequestBody Board board) {
        return boardService.createBoard(board);
    }

    @DeleteMapping("/board/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
    }

    @GetMapping("/topic/{topicId}")
    public TopicDTO getTopicWithPosts(@PathVariable Long topicId) {
        return topicService.getTopicWithPosts(topicId);
    }

    @PatchMapping("/topic/{topicId}/sticky")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void toggleSticky(@PathVariable Long topicId) {
        topicService.toggleSticky(topicId);
    }

    @PostMapping("/post")
    public Post createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @PostMapping("/board/{boardId}/topic/{topicId}/post/image")
    public String uploadImage(@PathVariable Long boardId, @PathVariable Long topicId,
                              @RequestParam MultipartFile UploadFiles) {
        return postService.uploadImage(boardId, topicId, UploadFiles);
    }

    @DeleteMapping("/post/{postId}")
    public void deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
    }

    @GetMapping("/topic/{topicId}/name")
    public String getTopicName(@PathVariable Long topicId) {
        return topicService.getTopicName(topicId);
    }

    @GetMapping("/board/{boardId}/name")
    public String getBoardName(@PathVariable Long boardId) {
        return boardService.getBoardName(boardId);
    }

    @PatchMapping("/post/{postId}")
    public Post updatePost(@PathVariable Long postId, @RequestBody PostDTO post) {
        return postService.updatePost(postId, post.getContent());
    }

    @GetMapping("/board/{boardId}")
    public BoardTopicsDTO getBoardWithTopics(@PathVariable Long boardId) {
        return boardService.getBoardWithTopics(boardId);
    }

    @PostMapping("/topic/{topicId}/watch")
    public WatchedTopic watchTopic(@PathVariable Long topicId) {
        return watchedTopicService.watchTopic(topicId);
    }

    @DeleteMapping("/topic/{topicId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteTopic(@PathVariable Long topicId) {
        topicService.deleteTopic(topicId);
    }

    @PostMapping("/board/{boardId}/topic")
    public TopicDTO createTopic(@RequestBody TopicDTO topicDTO) {
        return topicService.createTopic(topicDTO);
    }

    @GetMapping("/watchedTopic")
    public List<WatchedTopic> getWatchedTopics() {
        return watchedTopicService.getWatchedTopics();
    }

    @GetMapping("/character/{characterId}/topic")
    public List<TopicDTO> getTopicsByCharacterId(@PathVariable Long characterId) {
        return topicService.getByCharacterId(characterId);
    }

    @GetMapping("/comic/{comicId}/topic")
    public List<TopicDTO> getTopicsByComicId(@PathVariable Long comicId) {
        return topicService.getByComicId(comicId);
    }

    @GetMapping("/series/{seriesId}/topic")
    public List<TopicDTO> getTopicsBySeriesId(@PathVariable Long seriesId) {
        return topicService.getBySeriesId(seriesId);
    }
}
