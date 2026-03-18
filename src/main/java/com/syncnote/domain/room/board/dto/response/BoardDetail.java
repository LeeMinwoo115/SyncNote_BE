package com.syncnote.domain.room.board.dto.response;

import java.util.List;

public record BoardDetail(
        Long id,
        List<BoardElementDetail> boardElements
) {
}
