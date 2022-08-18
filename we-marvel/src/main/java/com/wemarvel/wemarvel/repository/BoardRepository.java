package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT b.title from Board b WHERE b.id = ?1")
    String getBoardName(Long boardId);
}
