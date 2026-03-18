package com.syncnote.domain.room.board.repository;

import com.syncnote.domain.room.board.entity.Board;
import com.syncnote.domain.room.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByRoom(Room room);
}
