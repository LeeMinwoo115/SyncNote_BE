package com.syncnote.domain.room.board.service;

import com.syncnote.domain.room.board.entity.Board;
import com.syncnote.domain.room.board.repository.BoardRepository;
import com.syncnote.domain.room.room.entity.Room;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Board createBoard(Room room) {
        Board board = Board.builder().room(room).build();

        return boardRepository.save(board);
    }
}
