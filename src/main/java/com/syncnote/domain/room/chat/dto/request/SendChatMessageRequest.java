package com.syncnote.domain.room.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SendChatMessageRequest(
        @NotNull Long roomId,
        @NotBlank String content
) {
}
