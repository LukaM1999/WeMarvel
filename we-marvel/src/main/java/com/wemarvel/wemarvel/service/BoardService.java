package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.Board;
import com.wemarvel.wemarvel.model.dto.BoardTopicsDTO;

import java.util.List;

public interface BoardService {
    List<BoardTopicsDTO> getAllBoards();
    BoardTopicsDTO getBoardWithTopics(Long id);
    String getBoardName(Long boardId);
    Board createBoard(Board board);
    void deleteBoard(Long id);
}
