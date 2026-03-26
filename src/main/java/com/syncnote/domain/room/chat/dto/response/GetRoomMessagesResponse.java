package com.syncnote.domain.room.chat.dto.response;

import java.util.List;

public record GetRoomMessagesResponse(
        List<ChatMessageResponse> messages,
        Long nextCursor,
        boolean hasNext
) {
}
