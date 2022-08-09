package com.wemarvel.wemarvel.repository;

import com.wemarvel.wemarvel.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
