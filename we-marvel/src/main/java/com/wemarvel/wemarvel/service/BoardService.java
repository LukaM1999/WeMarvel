package com.wemarvel.wemarvel.service;

import com.wemarvel.wemarvel.model.Board;
import com.wemarvel.wemarvel.model.dto.BoardDTO;

import java.util.List;

public interface BoardService {
    List<BoardDTO> getAllBoards();
}
