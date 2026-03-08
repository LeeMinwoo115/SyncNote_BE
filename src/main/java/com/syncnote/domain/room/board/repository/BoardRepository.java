package com.syncnote.domain.room.board.repository;

import com.syncnote.domain.room.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
