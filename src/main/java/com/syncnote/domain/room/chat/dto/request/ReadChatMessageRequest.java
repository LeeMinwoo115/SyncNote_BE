package com.syncnote.domain.room.chat.dto.request;

import jakarta.validation.constraints.NotNull;

public record ReadChatMessageRequest(
        @NotNull Long roomId,
        @NotNull Long lastReadMessageId
) {
}
