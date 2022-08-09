package com.wemarvel.wemarvel.controller;

import com.wemarvel.wemarvel.model.Board;
import com.wemarvel.wemarvel.model.dto.BoardDTO;
import com.wemarvel.wemarvel.model.dto.TopicDTO;
import com.wemarvel.wemarvel.service.BoardService;
import com.wemarvel.wemarvel.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private TopicService topicService;

    @GetMapping("/boards")
    public List<BoardDTO> getAllBoards() {
        return boardService.getAllBoards();
    }

    @GetMapping("/topic/{topicId}")
    public TopicDTO getTopicWithPosts(@PathVariable Long topicId) {
        return topicService.getTopicWithPosts(topicId);
    }
}
