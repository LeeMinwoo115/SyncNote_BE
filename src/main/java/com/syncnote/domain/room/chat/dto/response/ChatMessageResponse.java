package com.syncnote.domain.room.chat.dto.response;

import com.syncnote.global.enums.ChatMessageType;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        Long id,
        Long roomId,
        Long userId,
        String nickname,
        ChatMessageType messageType,
        String content,
        boolean edited,
        boolean deleted,
        long readCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
