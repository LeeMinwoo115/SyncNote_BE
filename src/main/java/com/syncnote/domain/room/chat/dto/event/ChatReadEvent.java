package com.syncnote.domain.room.chat.dto.event;

public record ChatReadEvent(
        Long roomId,
        Long userId,
        Long lastReadMessageId
) {
}
