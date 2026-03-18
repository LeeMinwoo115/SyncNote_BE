package com.syncnote.domain.room.board.service;

import com.syncnote.domain.room.board.dto.response.BoardDetail;
import com.syncnote.domain.room.board.dto.response.BoardElementDetail;
import com.syncnote.domain.room.board.entity.Board;
import com.syncnote.domain.room.board.entity.BoardElement;
import com.syncnote.domain.room.board.repository.BoardElementRepository;
import com.syncnote.domain.room.board.repository.BoardRepository;
import com.syncnote.domain.room.room.entity.Room;
import com.syncnote.global.error.code.BoardErrorCode;
import com.syncnote.global.error.exception.ErrorException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardElementRepository boardElementRepository;

    @Transactional
    public Board createBoard(Room room) {
        Board board = Board.builder().room(room).build();

        return boardRepository.save(board);
    }

    @Transactional
    public BoardDetail getBoard(Room room) {
        Board board = boardRepository.findByRoom(room).orElseThrow(
                () -> new ErrorException(BoardErrorCode.NOT_FOUND));

        List<BoardElement> boardElements = boardElementRepository.findAllByBoard(board);

        List<BoardElementDetail> boardElementDetails = boardElements.stream()
                .map(element -> new BoardElementDetail(
                        element.getId(),
                        element.getElementType(),
                        element.getCreatedBy() != null ? element.getCreatedBy().getId() : null,
                        element.getUpdatedBy() != null ? element.getUpdatedBy().getId() : null,
                        element.isDeleted(),
                        element.getData(),
                        element.getUpdatedAt()
                ))
                .toList();

        return new BoardDetail(
                board.getId(),
                boardElementDetails
        );
    }
}
