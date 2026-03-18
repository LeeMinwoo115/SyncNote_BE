package com.syncnote.domain.room.board.repository;

import com.syncnote.domain.room.board.entity.Board;
import com.syncnote.domain.room.board.entity.BoardElement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardElementRepository extends JpaRepository<BoardElement, Long> {
    List<BoardElement> findAllByBoard(Board board);
}
