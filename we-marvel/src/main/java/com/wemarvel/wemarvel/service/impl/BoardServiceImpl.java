package com.wemarvel.wemarvel.service.impl;

import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.wemarvel.wemarvel.model.Board;
import com.wemarvel.wemarvel.model.RegisteredUser;
import com.wemarvel.wemarvel.model.Topic;
import com.wemarvel.wemarvel.model.dto.BoardTopicsDTO;
import com.wemarvel.wemarvel.model.dto.TopicDTO;
import com.wemarvel.wemarvel.repository.BoardRepository;
import com.wemarvel.wemarvel.service.BoardService;
import com.wemarvel.wemarvel.service.RegisteredUserService;
import com.wemarvel.wemarvel.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TopicService topicService;

    @Autowired
    private RegisteredUserService registeredUserService;

    @Override
    public List<BoardTopicsDTO> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        List<BoardTopicsDTO> boardDTOs = new ArrayList<>(boards.size());
        for (Board board : boards) {
            List<TopicDTO> topics = topicService.getRecentBoardTopics(board.getId());
            boardDTOs.add(new BoardTopicsDTO(board.getId(), board.getTitle(), board.getDescription(), topics));
        }
        return boardDTOs;
    }

    @Override
    public BoardTopicsDTO getBoardWithTopics(Long id){
        Board board = boardRepository.findById(id).orElse(null);
        if (board == null) throw new IllegalArgumentException("Board not found");
        List<TopicDTO> topics = topicService.getBoardTopics(id);
        return new BoardTopicsDTO(board.getId(), board.getTitle(), board.getDescription(), topics);
    }

    @Override
    public String getBoardName(Long boardId) {
       return boardRepository.getBoardName(boardId);
    }

    @Override
    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }

    @Override
    @Transactional
    public void deleteBoard(Long id) {
        topicService.deleteBoardTopics(id);
        boardRepository.deleteById(id);
    }
}
