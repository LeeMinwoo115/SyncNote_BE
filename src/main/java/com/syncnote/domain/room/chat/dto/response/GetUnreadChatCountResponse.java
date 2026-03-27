package com.syncnote.domain.room.chat.dto.response;

import java.util.Optional;

public record GetUnreadChatCountResponse(
        Optional<Long> lastReadMessageId,
        long count
) {
}
