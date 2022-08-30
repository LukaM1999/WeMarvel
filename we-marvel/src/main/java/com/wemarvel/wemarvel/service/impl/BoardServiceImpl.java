package com.wemarvel.wemarvel.service.impl;

import com.wemarvel.wemarvel.model.Board;
import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.model.Topic;
import com.wemarvel.wemarvel.model.dto.BoardDTO;
import com.wemarvel.wemarvel.model.dto.BoardTopicsDTO;
import com.wemarvel.wemarvel.repository.BoardRepository;
import com.wemarvel.wemarvel.service.BoardService;
import com.wemarvel.wemarvel.service.RegisteredUserService;
import com.wemarvel.wemarvel.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TopicService topicService;

    @Autowired
    private RegisteredUserService registeredUserService;

    @Override
    public List<BoardDTO> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        List<BoardDTO> boardDTOs = new ArrayList<>(boards.size());
        for (Board board : boards) {
            List<Topic> topics = topicService.getRecentBoardTopics(board.getId());
            BoardDTO boardDTO = new BoardDTO();
            boardDTO.setId(board.getId());
            boardDTO.setTitle(board.getTitle());
            boardDTO.setDescription(board.getDescription());
            if(topics.size() > 0) {
                boardDTO.setFirstTopicId(topics.get(0).getId());
                boardDTO.setFirstTopicTitle(topics.get(0).getTitle());
                boardDTO.setFirstTopicDate(topics.get(0).getCreatedAt());
                RegisteredUser user = registeredUserService.getUserById(topics.get(0).getOwnerId());
                boardDTO.setFirstTopicUsername(user.getUsername());
                boardDTO.setFirstTopicUserId(user.getEmail());
                boardDTO.setFirstTopicUserImageUrl(user.getImageUrl());
            }
            if(topics.size() > 1) {
                boardDTO.setSecondTopicId(topics.get(1).getId());
                boardDTO.setSecondTopicTitle(topics.get(1).getTitle());
                boardDTO.setSecondTopicDate(topics.get(1).getCreatedAt());
                RegisteredUser user = registeredUserService.getUserById(topics.get(1).getOwnerId());
                boardDTO.setSecondTopicUsername(user.getUsername());
                boardDTO.setSecondTopicUserId(user.getEmail());
                boardDTO.setSecondTopicUserImageUrl(user.getImageUrl());
            }
            boardDTOs.add(boardDTO);
        }
        return boardDTOs;
    }

    @Override
    public BoardTopicsDTO getBoardWithTopics(Long id){
        BoardTopicsDTO boardTopicsDTO = new BoardTopicsDTO();
        Board board = boardRepository.findById(id).orElse(null);
        if (board == null) throw new IllegalArgumentException("Board not found");
        boardTopicsDTO.setId(board.getId());
        boardTopicsDTO.setTitle(board.getTitle());
        boardTopicsDTO.setDescription(board.getDescription());
        boardTopicsDTO.setTopics(topicService.getBoardTopics(id));
        return boardTopicsDTO;
    }

    @Override
    public String getBoardName(Long boardId) {
       return boardRepository.getBoardName(boardId);
    }
}
