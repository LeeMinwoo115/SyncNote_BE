package com.syncnote.domain.room.board.dto.response;

import com.syncnote.domain.room.chat.ElementType;

import java.time.LocalDateTime;

public record BoardElementDetail(
        Long id,
        ElementType elementType,
        Long createdByUserId,
        Long updatedByUserId,
        boolean isDeleted,
        String data,
        LocalDateTime updatedAt
) {
}
